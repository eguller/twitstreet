package com.twitstreet.market;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.google.inject.Inject;
import com.twitstreet.db.base.DBMgr;
import com.twitstreet.db.data.Portfolio;
import com.twitstreet.db.data.StockInPortfolio;
import com.twitstreet.db.data.User;
import com.twitstreet.db.data.UserStock;
import com.twitstreet.db.data.Stock;
import com.twitstreet.db.data.UserStockDetail;
import com.twitstreet.servlet.BuySellResponse;
import com.twitstreet.session.UserMgr;

public class PortfolioMgrImpl implements PortfolioMgr {
	
	// commission rate 1%
	private static final double COMMISSION_RATE = 0.01;
	
	private static Logger logger = Logger.getLogger(PortfolioMgrImpl.class);
	@Inject
	DBMgr dbMgr;
	@Inject
	private UserMgr userMgr;
	@Inject
	private TransactionMgr transactionMgr;

	@Override
	public BuySellResponse buy(User buyer, Stock stock, int amount) {
		int amount2Buy = buyer.getCash() < amount ? (int)buyer.getCash() : amount;
		if (stock.getAvailable() > 0) {
			amount2Buy = amount2Buy < stock.getAvailable() ? amount2Buy : stock.getAvailable();
			double sold = (double) amount2Buy / (double) stock.getTotal();
			stock.setSold(stock.getSold() + sold);
			UserStock userStock = getStockInPortfolio(buyer.getId(),
					stock.getId());

			if (userStock == null) {
				addStock2Portfolio(buyer.getId(), stock.getId(), sold);

			} else {
				updateStockInPortfolio(buyer.getId(), stock.getId(), sold);
			}
			userMgr.updateCash(buyer.getId(), amount2Buy);
			transactionMgr.recordTransaction(buyer, stock, amount2Buy,
					TransactionMgr.BUY);
			buyer.setCash(buyer.getCash() - amount2Buy);
			buyer.setPortfolio(buyer.getPortfolio() + amount2Buy);
			
		}
		
		UserStock updateUserStock = getStockInPortfolio(buyer.getId(),
				stock.getId());
		int userStockValue = (int) (updateUserStock.getPercent() * stock
				.getTotal());
		return new BuySellResponse(buyer, stock, userStockValue);

	}

	@Override
	public BuySellResponse sell(User seller, Stock stock, int amount) {
		
		UserStock userStock = getStockInPortfolio(seller.getId(), stock.getId());

		if (userStock != null) {
			int stockValueInPortfolio = (int) (userStock.getPercent() * stock
					.getTotal());

			// if someone is trying to sell more than he has, let him sell what
			// he has.
			int amount2Sell = amount > stockValueInPortfolio ? stockValueInPortfolio
					: amount;

			double sold = (double) amount2Sell / (double) stock.getTotal();
			stock.setSold(stock.getSold() - sold);
            
			if (amount2Sell >= stockValueInPortfolio && Math.abs(amount2Sell - stockValueInPortfolio) < 1) {
				// if user sold all he has, delete stock from his portfolio.
				// we do not want to show $0 value stock in portfolio.
				//if remainin portfolio value is less than 1 again delete 
				//portfolio
				deleteStockInPortfolio(seller.getId(), stock.getId());
			} else {
				// if user did not sell all he has, just update stock in
				// portfolio.
				updateStockInPortfolio(seller.getId(), stock.getId(), -sold);
			}
			
			// calculate commission
			double commission = (amount2Sell * COMMISSION_RATE);

			// subtract commission
			double cash = amount2Sell - commission;
			
			userMgr.updateCash(seller.getId(), -cash);
			transactionMgr.recordTransaction(seller, stock, amount2Sell,
					TransactionMgr.SELL);
			seller.setCash(seller.getCash() + cash);
			seller.setPortfolio(seller.getPortfolio() - amount2Sell);
			UserStock updateUserStock = getStockInPortfolio(seller.getId(),
					stock.getId());
			int userStockValue = updateUserStock == null ? 0
					: (int) (updateUserStock.getPercent() * stock.getTotal());

			return new BuySellResponse(seller, stock, userStockValue);
		} else {
			return new BuySellResponse(seller, stock, 0);
		}

	}

	private void updateStockInPortfolio(long buyer, long stock, double sold) {
		Connection connection = null;
		PreparedStatement ps = null;
		try {
			connection = dbMgr.getConnection();
			ps = connection
					.prepareStatement("update portfolio set percentage = (percentage + ?) where user_id = ? and stock = ?");
			ps.setDouble(1, sold);
			ps.setLong(2, buyer);
			ps.setLong(3, stock);
			ps.execute();
			logger.debug("DB: Query executed successfully - " + ps.toString());
		} catch (SQLException ex) {
			logger.error("DB: Query failed - " + ps.toString(), ex);
		} finally {
			try {
				if (ps != null && !ps.isClosed()) {
					ps.close();
				}
				if (connection != null && !connection.isClosed()) {
					connection.close();
				}
			} catch (SQLException e) {
				logger.error("DB: Resources could not be closed properly", e);
			}
		}
	}

	private void addStock2Portfolio(long buyer, long stock, double sold) {
		Connection connection = null;
		PreparedStatement ps = null;
		try {
			connection = dbMgr.getConnection();
			ps = connection
					.prepareStatement("insert into portfolio(user_id, stock, percentage) values(?, ?, ?)");
			ps.setLong(1, buyer);
			ps.setLong(2, stock);
			ps.setDouble(3, sold);
			ps.execute();
		} catch (SQLException ex) {
			logger.error("DB: Adding stock to portfolio failed", ex);
		}
		try {
			if (ps != null && !ps.isClosed()) {
				ps.close();
			}
			if (connection != null && !connection.isClosed()) {
				connection.close();
			}
		} catch (Exception e) {
			logger.error("DB: Resources could not be closed properly", e);
		}
	}

	@Override
	public UserStock getStockInPortfolio(long userId, long stockId) {
		UserStock userStock = null;
		Connection connection = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			connection = dbMgr.getConnection();
			ps = connection
					.prepareStatement("select id, percentage from portfolio where user_id = ? and stock = ?");
			ps.setLong(1, userId);
			ps.setLong(2, stockId);
			rs = ps.executeQuery();

			if (rs.next()) {
				userStock = new UserStock();
				userStock.setId(rs.getLong("id"));
				userStock.setPercent(rs.getDouble("percentage"));
			}
		} catch (SQLException ex) {
			logger.error("DB: Retrieving stock from portfolio failed. User: "
					+ userId + " ,Stock: " + stockId, ex);
		}
		try {
			if (rs != null && !rs.isClosed()) {
				rs.close();
			}
			if (ps != null && !ps.isClosed()) {
				ps.close();
			}
			if (connection != null && !connection.isClosed()) {
				connection.close();
			}
		} catch (SQLException ex) {
			logger.error("DB: Resources could not be closed properly", ex);
		}
		return userStock;
	}

	@Override
	public double getStockSoldPercentage(long userId, long stockId) {
		Connection connection = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			connection = dbMgr.getConnection();
			ps = connection
					.prepareStatement("select percentage from portfolio where user_id = ? and stock = ?");
			ps.setLong(1, userId);
			ps.setLong(2, stockId);

			rs = ps.executeQuery();
			if (rs.next()) {
				return rs.getDouble("percentage");

			}
			logger.debug("DB: Query executed successfully - " + ps.toString());
		} catch (SQLException ex) {
			logger.debug("DB: Query failed - " + ps.toString(), ex);
		} finally {
			try {
				if (rs != null && !rs.isClosed()) {
					rs.close();
				}
				if (ps != null && !ps.isClosed()) {
					ps.close();
				}
				if (connection != null && !connection.isClosed()) {
					connection.close();
				}
			} catch (SQLException e) {
				logger.error("DB: Resources could not be closed properly", e);
			}
		}
		return 0.0;
	}

	public void deleteStockInPortfolio(long userId, long stockId) {
		Connection connection = null;
		PreparedStatement ps = null;
		try {
			connection = dbMgr.getConnection();
			ps = connection
					.prepareStatement("delete from portfolio where user_id = ? and stock = ?");
			ps.setLong(1, userId);
			ps.setLong(2, stockId);
			ps.executeUpdate();
			logger.debug("DB: Query executed successfully - " + ps.toString());
		} catch (SQLException ex) {
			logger.error("DB: Query failed - " + ps.toString(), ex);
		} finally {
			try {
				if (ps != null && !ps.isClosed()) {
					ps.close();
				}
				if (connection != null && !connection.isClosed()) {
					connection.close();
				}
			} catch (SQLException e) {
				logger.error("DB: Resources could not be closed properly", e);
			}
		}
	}

	@Override
	public void rerank() {
		Connection connection = null;
		CallableStatement cs = null;
		try {
			connection = dbMgr.getConnection();
			cs = connection.prepareCall("{call rerank()}");
			cs.execute();
			logger.debug("DB: Query executed successfully - " + cs.toString());
		} catch (SQLException ex) {
			logger.error("DB: Query failed - " + cs.toString(), ex);
		} finally {
			try {
				if (cs != null && !cs.isClosed()) {
					cs.close();
				}
				if (connection != null && !connection.isClosed()) {
					connection.close();
				}
			} catch (SQLException e) {
				logger.error("DB: Resources could not be closed properly", e);
			}
		}
	}

	@Override
	public Portfolio getUserPortfolio(User user) {
		Portfolio portfolio = null;

		if (user != null) {
			portfolio = new Portfolio(user);
			Connection connection = null;
			PreparedStatement ps = null;
			ResultSet rs = null;
			try {
				connection = dbMgr.getConnection();
				ps = connection
						.prepareStatement("select stock.name as stockName, stock.id as stockId, (stock.total * portfolio.percentage) as amount, stock.pictureUrl as pictureUrl from portfolio, stock where portfolio.stock = stock.id and portfolio.user_id = ?");
				ps.setLong(1, user.getId());
				rs = ps.executeQuery();

				while (rs.next()) {
					StockInPortfolio stockInPortfolio = new StockInPortfolio(
							rs.getLong("stockId"), rs.getString("stockName"),
							rs.getDouble("amount"),
							rs.getString("pictureUrl"));
					portfolio.add(stockInPortfolio);
				}

				logger.debug("DB: Query executed successfully - "
						+ ps.toString());
			} catch (SQLException ex) {
				logger.error("DB: Query failed - " + ps.toString(), ex);
			} finally {
				try {
					if (rs != null && !rs.isClosed()) {
						rs.close();
					}
					if (ps != null && !ps.isClosed()) {
						ps.close();
					}
					if (connection != null && !connection.isClosed()) {
						connection.close();
					}
				} catch (SQLException e) {
					logger.error("DB: Releasing resources failed.", e);
				}

			}
		}
		return portfolio;
	}

	@Override
	public List<UserStockDetail> getStockDistribution(long stock) {
		ArrayList<UserStockDetail> userStockList = new ArrayList<UserStockDetail>();
		Connection connection = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			connection = dbMgr.getConnection();
			ps = connection
					.prepareStatement("select portfolio.id as portfolio_id, users.id as user_id, "
							+ "users.pictureUrl as userPictureUrl, users.userName as user_name, "
							+ "portfolio.percentage as portfolio_percentage, "
							+ "stock.total as stock_total from portfolio, stock, "
							+ "users where portfolio.user_id = users.id and "
							+ "portfolio.stock = stock.id and stock = ?");
			ps.setLong(1, stock);
			rs = ps.executeQuery();

			while (rs.next()) {
				UserStockDetail userStockDetail = new UserStockDetail();
				userStockDetail.setId(rs.getLong("portfolio_id"));
				userStockDetail.setUserId(rs.getLong("user_id"));
				userStockDetail.setUserPictureUrl(rs
						.getString("userPictureUrl"));
				userStockDetail
						.setPercent(rs.getDouble("portfolio_percentage"));
				userStockDetail.setStockTotal(rs.getInt("stock_total"));
				userStockDetail.setUserName(rs.getString("user_name"));
				userStockList.add(userStockDetail);
			}
			logger.debug("DB: Query executed successfully - " + ps.toString());
		} catch (SQLException e) {
			logger.error("DB: Query failed - " + ps.toString(), e);
		}
		try {
			if (rs != null && !rs.isClosed()) {
				rs.close();
			}
			if (ps != null && !ps.isClosed()) {
				ps.close();
			}
			if (connection != null && !connection.isClosed()) {
				connection.close();
			}
		} catch (SQLException e) {
			logger.error("DB: Releasing resources failed.", e);
		}
		return userStockList;
	}
}
