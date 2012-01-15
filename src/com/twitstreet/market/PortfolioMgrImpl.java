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
	private static Logger logger = Logger.getLogger(PortfolioMgrImpl.class);
	@Inject
	DBMgr dbMgr;
	@Inject
	private StockMgr stockMgr;
	@Inject
	private UserMgr userMgr;
	@Inject
	private TransactionMgr transactionMgr;

	@Override
	public BuySellResponse buy(long buyer, long stock, int amount) {
		User user = userMgr.getUserById(buyer);
		int amount2Buy = user.getCash() < amount ? user.getCash() : amount;
		Stock stockObj = stockMgr.getStockById(stock);
		double sold = (double) amount2Buy / (double) stockObj.getTotal();
		stockObj.setSold(stockObj.getSold() + sold);
		UserStock userStock = getStockInPortfolio(buyer, stock);

		if (userStock == null) {
			addStock2Portfolio(buyer, stock, sold);

		} else {
			updateStockInPortfolio(buyer, stock, sold);
		}
		stockMgr.updateSold(stock, sold);
		userMgr.updateCashAndPortfolio(buyer, amount2Buy);
		transactionMgr.recordTransaction(user, stockObj, amount2Buy,
				TransactionMgr.BUY);
		user.setCash(user.getCash() - amount2Buy);
		user.setPortfolio(user.getPortfolio() + amount2Buy);
		UserStock updateUserStock = getStockInPortfolio(buyer, stock);
		int userStockValue = (int) (updateUserStock.getPercent() * stockObj
				.getTotal());
		return new BuySellResponse(user, stockObj, userStockValue);

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
				if (!ps.isClosed()) {
					ps.close();
				}
				if (!connection.isClosed()) {
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
			if (!ps.isClosed()) {
				ps.close();
			}
			if (!connection.isClosed()) {
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
			if (!rs.isClosed()) {
				rs.close();
			}
			if (!ps.isClosed()) {
				ps.close();
			}
			if (!connection.isClosed()) {
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
				if (!rs.isClosed()) {
					rs.close();
				}
				if (!ps.isClosed()) {
					ps.close();
				}
				if (!connection.isClosed()) {
					connection.close();
				}
			} catch (SQLException e) {
				logger.error("DB: Resources could not be closed properly", e);
			}
		}
		return 0.0;
	}

	@Override
	public BuySellResponse sell(long seller, long stock, int amount) {
		User user = userMgr.getUserById(seller);
		int amount2Buy = user.getCash() < amount ? user.getCash() : amount;
		Stock stockObj = stockMgr.getStockById(stock);
		double sold = (double) amount2Buy / (double) stockObj.getTotal();
		stockObj.setSold(stockObj.getSold() - sold);
		UserStock userStock = getStockInPortfolio(seller, stock);

		if (userStock != null) {
			int soldAmount = (int) (userStock.getPercent() * stockObj
					.getTotal());
			if (amount >= soldAmount) {
				deleteStockInPortfolio(seller, stock);
			} else {
				updateStockInPortfolio(seller, stock, -sold);
			}
		}

		stockMgr.updateSold(stock, -sold);
		userMgr.updateCashAndPortfolio(seller, -amount2Buy);
		transactionMgr.recordTransaction(user, stockObj, amount2Buy,
				TransactionMgr.SELL);
		user.setCash(user.getCash() + amount2Buy);
		user.setPortfolio(user.getPortfolio() - amount2Buy);
		UserStock updateUserStock = getStockInPortfolio(seller, stock);
		int userStockValue = updateUserStock == null ? 0
				: (int) (updateUserStock.getPercent() * stockObj.getTotal());
		return new BuySellResponse(user, stockObj, userStockValue);

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
				if (!ps.isClosed()) {
					ps.close();
				}
				if (!connection.isClosed()) {
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
				if (!cs.isClosed()) {
					cs.close();
				}
				if (!connection.isClosed()) {
					connection.close();
				}
			} catch (SQLException e) {
				logger.error("DB: Resources could not be closed properly", e);
			}
		}
	}

	@Override
	public Portfolio getUserPortfolio(long userId) {
		User user = null;
		Portfolio portfolio = null;
		user = userMgr.getUserById(userId);

		if (user != null) {
			portfolio = new Portfolio(user);
			Connection connection = null;
			PreparedStatement ps = null;
			ResultSet rs = null;
			try {
				connection = dbMgr.getConnection();
				ps = connection
						.prepareStatement("select stock.name as stockName, stock.id as stockId, (stock.total * portfolio.percentage) as amount, stock.pictureUrl as pictureUrl from portfolio, stock where portfolio.stock = stock.id and portfolio.user_id = ?");
				ps.setLong(1, userId);
				rs = ps.executeQuery();

				while (rs.next()) {
					StockInPortfolio stockInPortfolio = new StockInPortfolio(
							rs.getLong("stockId"), rs.getString("stockName"),
							(int) Math.rint(rs.getDouble("amount")),
							rs.getString("pictureUrl"));
					portfolio.add(stockInPortfolio);
				}

				logger.debug("DB: Query executed successfully - "
						+ ps.toString());
			} catch (SQLException ex) {
				logger.error("DB: Query failed - " + ps.toString(), ex);
			} finally {
				try {
					if (!rs.isClosed()) {
						rs.close();
					}
					if (!ps.isClosed()) {
						ps.close();
					}
					if (!connection.isClosed()) {
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
			if (!rs.isClosed()) {
				rs.close();
			}
			if (!ps.isClosed()) {
				ps.close();
			}
			if (!connection.isClosed()) {
				connection.close();
			}
		} catch (SQLException e) {
			logger.error("DB: Releasing resources failed.", e);
		}
		return userStockList;
	}
}
