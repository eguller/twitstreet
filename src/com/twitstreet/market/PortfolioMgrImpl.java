package com.twitstreet.market;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import com.google.inject.Inject;
import com.twitstreet.config.ConfigMgr;
import com.twitstreet.db.base.DBConstants;
import com.twitstreet.db.base.DBMgr;
import com.twitstreet.db.data.Portfolio;
import com.twitstreet.db.data.Stock;
import com.twitstreet.db.data.StockInPortfolio;
import com.twitstreet.db.data.User;
import com.twitstreet.db.data.UserStock;
import com.twitstreet.db.data.UserStockDetail;
import com.twitstreet.servlet.BuySellResponse;
import com.twitstreet.session.UserMgr;

public class PortfolioMgrImpl implements PortfolioMgr {
	public static String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
	SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
	// commission rate 1%
	private static final double COMMISSION_RATE = 0.01;
	
	private static Logger logger = Logger.getLogger(PortfolioMgrImpl.class);
	@Inject
	DBMgr dbMgr;
	@Inject
	private UserMgr userMgr;
	@Inject
	private TransactionMgr transactionMgr;
	@Inject private ConfigMgr configMgr;

	@Override
	public boolean buy(User buyer, Stock stock, int amount) {
		if(stock.getTotal()<=0){
			logger.info("Buy operation cancelled. " + stock.getName()+" has no followers");
			return false;	
		}
		int day = 1000 * 60 * 60 * 24;
		Date thresholdDate = new Date((new Date()).getTime() - day * Stock.STOCK_OLDER_THAN_DAYS_AVAILABLE);		
		if(!stock.isOldEnough()){
			logger.info("Buy operation cancelled. " + stock.getName()+" is not older than "+sdf.format(thresholdDate)+" ("+Stock.STOCK_OLDER_THAN_DAYS_AVAILABLE+" days)");
			return false;
			
		}
		if (stock.getAvailable()<= 0) {
			logger.info("Buy operation cancelled. " + stock.getName()+" is sold out");
			return false;
		}
		if(buyer.getCash() <= 0){
			logger.info("Buy operation cancelled. " + buyer.getUserName() +" has no cash");
			return false;
		}
		int amount2Buy = buyer.getCash() < amount ? (int)buyer.getCash() : amount;
		amount2Buy = amount2Buy < stock.getAvailable() ? amount2Buy : stock.getAvailable();
		
		double sold = (double) amount2Buy / (double) stock.getTotal();
		stock.setSold(stock.getSold() + sold);
		UserStock userStock = getStockInPortfolio(buyer.getId(),
				stock.getId());

		if (userStock == null) {
			addStock2Portfolio(buyer.getId(), stock.getId(), sold);

		} else {
			updateStockInPortfolio(buyer.getId(), stock.getId(), sold, userStock.getCapital()+amount2Buy);
		}
		userMgr.updateCash(buyer.getId(), amount2Buy);
		transactionMgr.recordTransaction(buyer, stock, amount2Buy,
				TransactionMgr.BUY);
		buyer.setCash(buyer.getCash() - amount2Buy);
		buyer.setPortfolio(buyer.getPortfolio() + amount2Buy);

		return true;	
	}
	
	@Override
	public boolean sell(User seller, Stock stock, int amount) {

		UserStock userStock = getStockInPortfolio(seller.getId(), stock.getId());

		if (userStock == null) {
			return false;
		}
		double stockPercentInPortfolio = userStock.getPercent();
		double stockCapitalInPortfolio = userStock.getCapital();
		int stockValueInPortfolio = (int) (userStock.getPercent() * stock.getTotal());

		// if someone is trying to sell more than he has, let him sell what
		// he has.
		int amount2Sell = amount > stockValueInPortfolio ? stockValueInPortfolio : amount;

		double sold = (double) amount2Sell / (double) stock.getTotal();
		stock.setSold(stock.getSold() - sold);

		if (amount2Sell >= stockValueInPortfolio && Math.abs(amount2Sell - stockValueInPortfolio) < 1) {
			// if user sold all he has, delete stock from his portfolio.
			// we do not want to show $0 value stock in portfolio.
			// if remainin portfolio value is less than 1 again delete
			// portfolio
			deleteStockInPortfolio(seller.getId(), stock.getId());
		} else {
			// if user did not sell all he has, just update stock in
			// portfolio.
			double newCapital = ((stockCapitalInPortfolio / stockPercentInPortfolio) * (stockPercentInPortfolio - sold));
			updateStockInPortfolio(seller.getId(), stock.getId(), -sold, newCapital);
		}

		// calculate commission

		double commission = (seller.getCash() + seller.getPortfolio()) < configMgr.getComissionTreshold() ? 0 : (amount2Sell * COMMISSION_RATE);

		// subtract commission
		double cash = amount2Sell - commission;

		userMgr.updateCash(seller.getId(), -cash);
		transactionMgr.recordTransaction(seller, stock, amount2Sell, TransactionMgr.SELL);
		seller.setCash(seller.getCash() + cash);
		seller.setPortfolio(seller.getPortfolio() - amount2Sell);
		UserStock updateUserStock = getStockInPortfolio(seller.getId(), stock.getId());
		int userStockValue = updateUserStock == null ? 0 : (int) (updateUserStock.getPercent() * stock.getTotal());

		return true;

	}

	private void updateStockInPortfolio(long buyer, long stock, double sold, double newCapital) {
		
		Connection connection = null;
		PreparedStatement ps = null;
		try {
			connection = dbMgr.getConnection();
			ps = connection
					.prepareStatement("update portfolio set percentage = (percentage + ?), capital = ? " +
							" where user_id = ? and stock = ?");
			ps.setDouble(1, sold);
			ps.setDouble(2, newCapital);
			ps.setLong(3, buyer);
			ps.setLong(4, stock);
			ps.execute();
			logger.debug(DBConstants.QUERY_EXECUTION_SUCC + ps.toString());
		} catch (SQLException ex) {
			logger.error(DBConstants.QUERY_EXECUTION_FAIL + ps.toString(), ex);
		} finally {
			dbMgr.closeResources(connection, ps, null);
		}
	}

	private void addStock2Portfolio(long buyer, long stock, double sold) {
		Connection connection = null;
		PreparedStatement ps = null;
		try {
			connection = dbMgr.getConnection();
			ps = connection
					.prepareStatement("insert into portfolio(user_id, stock, percentage, capital) values(?, ?, ?,?*(select total from stock where id = ?))");
			ps.setLong(1, buyer);
			ps.setLong(2, stock);
			ps.setDouble(3, sold);
			ps.setDouble(4, sold);
     		ps.setLong(5, stock);
			ps.execute();
		} catch (SQLException ex) {
			logger.error("DB: Adding stock to portfolio failed", ex);
		}
		finally{			
			dbMgr.closeResources(connection, ps, null);
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
					.prepareStatement("select id, percentage, capital from portfolio where user_id = ? and stock = ?");
			ps.setLong(1, userId);
			ps.setLong(2, stockId);
			rs = ps.executeQuery();

			if (rs.next()) {
				userStock = new UserStock();
				userStock.setId(rs.getLong("id"));
				userStock.setPercent(rs.getDouble("percentage"));
				userStock.setCapital(rs.getDouble("capital"));
			}
		} catch (SQLException ex) {
			logger.error("DB: Retrieving stock from portfolio failed. User: "
					+ userId + " ,Stock: " + stockId, ex);
		}finally{
			
			dbMgr.closeResources(connection, ps, rs);
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
			logger.debug(DBConstants.QUERY_EXECUTION_SUCC + ps.toString());
		} catch (SQLException ex) {
			logger.debug(DBConstants.QUERY_EXECUTION_FAIL + ps.toString(), ex);
		} finally {
			dbMgr.closeResources(connection, ps, rs);
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
			logger.debug(DBConstants.QUERY_EXECUTION_SUCC + ps.toString());
		} catch (SQLException ex) {
			logger.error(DBConstants.QUERY_EXECUTION_FAIL + ps.toString(), ex);
		} finally {
			dbMgr.closeResources(connection, ps, null);
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
						.prepareStatement("select user_stock_profit(portfolio.user_id, portfolio.stock) as changePerHour, " +
								" portfolio.capital as capital, stock.name as stockName, " +
								" stock.id as stockId, " +
								" stock.longName as stockLongName, " +
								" (stock.total * portfolio.percentage) as amount, " +
								" stock.pictureUrl as pictureUrl, " +
								" percentage, " +
								" stock.verified as verified,  " +
								" stock.total as total, " +
								" stock.changePerHour as totalChangePerHour " +
								"from portfolio, stock where portfolio.stock = stock.id and portfolio.user_id = ? order by changePerHour desc, stockName asc ");
				ps.setLong(1, user.getId());
				rs = ps.executeQuery();

				while (rs.next()) {
					
					
					
					
					StockInPortfolio stockInPortfolio = new StockInPortfolio();
					stockInPortfolio.getDataFromResultSet(rs);
					portfolio.add(stockInPortfolio);
				}

				logger.debug(DBConstants.QUERY_EXECUTION_SUCC
						+ ps.toString());
			} catch (SQLException ex) {
				logger.error(DBConstants.QUERY_EXECUTION_FAIL + ps.toString(), ex);
			} finally {
				dbMgr.closeResources(connection, ps, rs);

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
							+ "portfolio.stock = stock.id and stock = ? order by portfolio_percentage desc ");
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
			logger.debug(DBConstants.QUERY_EXECUTION_SUCC + ps.toString());
		} catch (SQLException e) {
			logger.error(DBConstants.QUERY_EXECUTION_FAIL + ps.toString(), e);
		}finally{
			dbMgr.closeResources(connection, ps, rs);
		}
		
		return userStockList;
	}
	
}
