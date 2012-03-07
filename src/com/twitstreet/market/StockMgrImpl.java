package com.twitstreet.market;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import twitter4j.Trend;

import com.google.inject.Inject;
import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;
import com.twitstreet.db.base.DBConstants;
import com.twitstreet.db.base.DBMgr;
import com.twitstreet.db.base.DBMgrImpl;
import com.twitstreet.db.data.Stock;
import com.twitstreet.db.data.StockHistoryData;
import com.twitstreet.db.data.User;
import com.twitstreet.session.UserMgr;
import com.twitstreet.task.StockUpdateTask;
import com.twitstreet.twitter.TwitstreetAnnouncer;
import com.twitstreet.twitter.TwitterProxy;
import com.twitstreet.twitter.TwitterProxyFactory;

public class StockMgrImpl implements StockMgr {

	private static int TWITTER_TRENDS_CLEANUP_PERIOD = 24 * 60; // minutes

	private static String TRENDY_STOCK_AVAILABLE_THRESHOLD = "5";

	private static String TRENDY_STOCK_TOTAL_THRESHOLD = "500";
	private static String TRENDY_STOCK_AVAILABLE_PERCENTAGE_THRESHOLD = "0.99";

	private static String SELECT_FROM_STOCK = " select id, name, longName, total, stock_sold(id) as sold, pictureUrl, lastUpdate, changePerHour, verified from stock ";
	private static String SELECT_DISTINCT_FROM_STOCK = " select distinct id, name,longName, total, stock_sold(id) as sold, pictureUrl, lastUpdate, changePerHour, verified from stock ";
	private static int MAX_TRENDS = 6;

	private static StockMgrImpl instance = new StockMgrImpl();
	
	public static StockMgr getInstance(){
		
		return instance;
	}
	@Inject
	private UserMgr userMgr;

	@Inject
	private TwitterProxyFactory twitterProxyFactory = null;
	@Inject
	private DBMgr dbMgr;
	private static Logger logger = Logger.getLogger(StockMgrImpl.class);

	public static int STOCK_TREND_IN_MINUTES = 60;

	public Stock notifyBuy(String stock, double amount) {
		return null;
	}

	public Stock getStock(String name) {
		Connection connection = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		Stock stockDO = null;
		try {
			connection = dbMgr.getConnection();

			ps = connection.prepareStatement(SELECT_FROM_STOCK + "  where name = ?");
			ps.setString(1, name);
			rs = ps.executeQuery();
			if (rs.next()) {
				stockDO = new Stock();
				stockDO.getDataFromResultSet(rs);

				if (stockDO.isUpdateRequired()) {
					updateStockData(name);

					// stockdo shall not require an update due to the update
					// above
					// so getStockById should go with the else block this time
					stockDO = getStockById(stockDO.getId());

				}
				logger.debug(DBConstants.QUERY_EXECUTION_SUCC + ps.toString());

			} else {

				twitter4j.User twUser = getTwitterProxy().getTwUser(name);

				if (twUser != null) {
					stockDO = new Stock();
					stockDO.setId(twUser.getId());
					stockDO.setLongName(twUser.getName());
					stockDO.setName(twUser.getScreenName());
					stockDO.setTotal(twUser.getFollowersCount());
					stockDO.setPictureUrl(twUser.getProfileImageURL().toExternalForm());
					stockDO.setSold(0.0D);
					stockDO.setVerified(twUser.isVerified());
					saveStock(stockDO);

					// stockdo shall not require an update due to the update
					// above
					// so getStockById should go with the else block this time
					stockDO = getStockById(stockDO.getId());

				} else {

					logger.error("Invalid name: " + name);
				}

			}
		} catch (SQLException ex) {
			logger.error(DBConstants.QUERY_EXECUTION_FAIL + ps.toString(), ex);

		} finally {
			dbMgr.closeResources(connection, ps, rs);
		}
		return stockDO;
	}

	

	public Stock getStockById(long id) {
		Connection connection = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		Stock stockDO = null;
		try {
			connection = dbMgr.getConnection();
			ps = connection.prepareStatement(SELECT_FROM_STOCK + " where id = ?");
			ps.setLong(1, id);

			rs = ps.executeQuery();
			if (rs.next()) {
				stockDO = new Stock();
				stockDO.getDataFromResultSet(rs);

				if (stockDO.isUpdateRequired()) {
					updateStockData(id);
					stockDO = getStockById(id);
				}
				logger.debug(DBConstants.QUERY_EXECUTION_SUCC + ps.toString());
			} else {
		
				twitter4j.User twUser = getTwitterProxy().getTwUser(id);

				if (twUser != null) {
					stockDO = new Stock();
					stockDO.setId(twUser.getId());
					stockDO.setName(twUser.getScreenName());
					stockDO.setLongName(twUser.getName());
					stockDO.setTotal(twUser.getFollowersCount());
					stockDO.setPictureUrl(twUser.getProfileImageURL().toExternalForm());
					stockDO.setSold(0.0D);
					stockDO.setVerified(twUser.isVerified());
					saveStock(stockDO);

					// stockdo shall not require an update due to the update
					// above
					// so getStockById should go with the else block this time
					stockDO = getStockById(id);

				} else {

					logger.debug("Invalid ID: " + id);
				}

			}

		} catch (SQLException ex) {
			logger.error(DBConstants.QUERY_EXECUTION_FAIL + ps.toString(), ex);
		} finally {
			dbMgr.closeResources(connection, ps, rs);
		}
		return stockDO;
	}

	
	private TwitterProxy getTwitterProxy(){
		User user = userMgr.random();
		TwitterProxy twitterProxy = user == null ? null : twitterProxyFactory.create(user.getOauthToken(), user.getOauthTokenSecret());
		return twitterProxy;
		
		
	}
	
	@Override
	public void updateStockData(long id) {
		
		twitter4j.User twUser = getTwitterProxy().getTwUser(id);
		if (twUser != null) {
			updateTwitterData(twUser.getId(), twUser.getFollowersCount(), twUser.getProfileImageURL().toExternalForm(), twUser.getScreenName(),twUser.getName(), twUser.isVerified());
		}

	}

	@Override
	public void updateStockData(String stockName) {
	
		twitter4j.User twUser = getTwitterProxy().getTwUser(stockName);

		updateTwitterData(twUser.getId(), twUser.getFollowersCount(), twUser.getProfileImageURL().toExternalForm(), twUser.getScreenName(),twUser.getName(), twUser.isVerified());

	}
	
	@Override
	public StockHistoryData getStockHistory(long id){
		return getStockHistory(id, -1);
	}
	
	@Override
	public StockHistoryData getStockHistory(long id, String since) {
		
		String sinceStr =" " ;
		if(since!=null){	
			sinceStr =" and stock_history.lastUpdate >   TIMESTAMP('" + since+"') " ;
		}

		Connection connection = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		StockHistoryData stockHistoryData = null;
		try {
			connection = dbMgr.getConnection();
			ps = connection.prepareStatement("(" + " select distinct stock_history.lastUpdate as lastUpdate, stock.id, stock.name, stock_history.total  " +
					  " from stock_history,stock where stock_history.stock = ? and stock.id =  stock_history.stock  "+ sinceStr+" ) "
					+ " union (select lastUpdate as lastUpdate, id, name, total from stock where  stock.id =  ? ) order by lastUpdate asc ");
			ps.setLong(1, id);
			ps.setLong(2, id);
			rs = ps.executeQuery();
			stockHistoryData = new StockHistoryData();

			stockHistoryData.getDataFromResultSet(rs);

			logger.debug(DBConstants.QUERY_EXECUTION_SUCC + ps.toString());
		} catch (SQLException ex) {
			logger.error(DBConstants.QUERY_EXECUTION_FAIL + ps.toString(), ex);
		} finally {
			dbMgr.closeResources(connection, ps, rs);
		}
		return stockHistoryData;

	}
	
	@Override
	public StockHistoryData getStockHistory(long id, int forMinutes) {
		String forMinutesStr = "  and timestampdiff(minute,stock_history.lastUpdate ,now()) <  "+forMinutes;
		if(forMinutes<=0){			
			forMinutesStr ="";	
		}
	
		Connection connection = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		StockHistoryData stockHistoryData = null;
		try {
			connection = dbMgr.getConnection();
			
			ps = connection.prepareStatement("(select distinct stock_history.lastUpdate as lastUpdate, stock.id, stock.name, stock_history.total  " +
					  " from stock_history,stock where stock_history.stock = ? and stock.id =  stock_history.stock "+forMinutesStr +") "+ 
					  " union (select lastUpdate as lastUpdate, id, name, total from stock where  stock.id =  ? ) order by lastUpdate asc ");
			ps.setLong(1, id);
			ps.setLong(2, id);
			rs = ps.executeQuery();
			stockHistoryData = new StockHistoryData();

			stockHistoryData.getDataFromResultSet(rs);

			logger.debug(DBConstants.QUERY_EXECUTION_SUCC + ps.toString());
		} catch (SQLException ex) {
			logger.error(DBConstants.QUERY_EXECUTION_FAIL + ps.toString(), ex);
		} finally {
			dbMgr.closeResources(connection, ps, rs);
		}
		return stockHistoryData;

	}

	
	public void updateStockHistory() {
		Connection connection = null;
		PreparedStatement ps = null;

		try {
			connection = dbMgr.getConnection();
			ps = connection.prepareStatement("insert ignore into stock_history(stock, total, date, hour, lastUpdate) " + " select id, total, DATE(lastUpdate), HOUR(lastUpdate), lastUpdate from stock ");

			ps.executeUpdate();

			logger.debug(DBConstants.QUERY_EXECUTION_SUCC + ps.toString());
		} catch (MySQLIntegrityConstraintViolationException ex) {

			logger.debug(DBConstants.RECORD_ALREADY_EXISTS + ps.toString());

		} catch (SQLException ex) {
			logger.error(DBConstants.QUERY_EXECUTION_FAIL + ps.toString(), ex);
		} finally {
			dbMgr.closeResources(connection, ps, null);
		}

	}

	public void updateTwitterData(long id, int total, String pictureUrl, String screenName,String longName, boolean verified) {
		Connection connection = null;
		PreparedStatement ps = null;

		try {
			connection = dbMgr.getConnection();
			ps = connection.prepareStatement("update stock set total = ?, pictureUrl = ?, lastUpdate = now(), name = ?,longName = ?, verified = ? where id = ?");

			ps.setInt(1, total);
			ps.setString(2, pictureUrl);
			ps.setString(3, screenName);
			ps.setString(4, longName);
			ps.setBoolean(5, verified);
			ps.setLong(6, id);
			ps.executeUpdate();

			// This query should be called right after the stock update,
			// since the get_stock_trend_for_x_minutes requires an up to date
			// stock table
			ps = connection.prepareStatement("update stock set changePerHour = get_stock_trend_for_x_minutes(?,?) where id = ?");

			ps.setLong(1, id);
			ps.setInt(2, STOCK_TREND_IN_MINUTES);
			ps.setLong(3, id);
			ps.executeUpdate();

			ps = connection.prepareStatement("insert ignore into stock_history(stock, total, date, hour, lastUpdate) " + " select id, total, DATE(NOW()), HOUR(NOW()), lastUpdate from stock where id = ?");
			ps.setLong(1, id);
			ps.executeUpdate();

			logger.debug(DBConstants.QUERY_EXECUTION_SUCC + ps.toString());
		} catch (SQLException ex) {
			logger.error(DBConstants.QUERY_EXECUTION_FAIL + ps.toString(), ex);
		} finally {
			dbMgr.closeResources(connection, ps, null);
		}
	}

	@Override
	public void saveStock(Stock stock) {
		Connection connection = null;
		PreparedStatement ps = null;
		try {
			connection = dbMgr.getConnection();
			ps = connection.prepareStatement("insert into stock(id, name, longName, total, pictureUrl, lastUpdate, verified) values(?, ?,?, ?, ?, now(), ?)");
			ps.setLong(1, stock.getId());
			ps.setString(2, stock.getName());
			ps.setString(3, stock.getLongName());
			ps.setInt(4, stock.getTotal());
			ps.setString(5, stock.getPictureUrl());
			
			ps.setBoolean(6, stock.isVerified());

			ps.executeUpdate();
			logger.debug(DBConstants.QUERY_EXECUTION_SUCC + ps.toString());
		} catch (MySQLIntegrityConstraintViolationException e) {
			logger.warn("DB: Stock already exist - Stock Id:" + stock.getId() + " User Name: " + stock.getName() + " - " + e.getMessage());
		} catch (SQLException ex) {
			logger.error(DBConstants.QUERY_EXECUTION_FAIL + ps.toString(), ex);
		} finally {
			dbMgr.closeResources(connection, ps, null);
		}
	}

	
	public boolean sendMessage(String toUser, String message){
		User user = userMgr.getUserById(273572038);
		TwitterProxy twitterProxy = user == null ? null : twitterProxyFactory.create(user.getOauthToken(), user.getOauthTokenSecret());
		twitterProxy.sendMessage(toUser, message);
		
		
		return true;
	}
	
	
	@Override
	public List<Stock> getUpdateRequiredStocks() {
	
		ArrayList<Stock> stockList = new ArrayList<Stock>();
		Connection connection = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			connection = dbMgr.getConnection();
			ps = connection.prepareStatement(SELECT_DISTINCT_FROM_STOCK + " " + " where (TIMESTAMPDIFF(minute, lastUpdate, now())  > ?  or lastUpdate is null) " + " and (" + "		stock.id in (select distinct stock from portfolio) or " + "  		stock.id in (select distinct stock_id from user_stock_watch ) or " + "		stock.id in (select stock_id from twitter_trends )" + "		)");

			ps.setLong(1, StockUpdateTask.LAST_UPDATE_DIFF_MINUTES);
			rs = ps.executeQuery();
			while (rs.next()) {
				Stock stockDO = new Stock();
				stockDO.getDataFromResultSet(rs);
				stockList.add(stockDO);
			}
			logger.debug(DBConstants.QUERY_EXECUTION_SUCC + ps.toString());
		} catch (SQLException ex) {
			logger.error(DBConstants.QUERY_EXECUTION_FAIL + ps.toString(), ex);
		} finally {
			dbMgr.closeResources(connection, ps, rs);
		}
		return stockList;
	}

	@Override
	public void resetSpeedOfOldStocks() {

		Connection connection = null;
		PreparedStatement ps = null;

		try {
			connection = dbMgr.getConnection();
			ps = connection.prepareStatement("update stock set changePerHour = NULL" + " where TIMESTAMPDIFF(minute, lastUpdate,now()) > ?");
			ps.setInt(1, StockUpdateTask.LAST_UPDATE_DIFF_MINUTES * 3);
			ps.executeUpdate();

			logger.debug(DBConstants.QUERY_EXECUTION_SUCC + ps.toString());
		} catch (SQLException ex) {
			logger.error(DBConstants.QUERY_EXECUTION_FAIL + ps.toString(), ex);
		} finally {
			dbMgr.closeResources(connection, ps, null);
		}

	}

	@Override
	public ArrayList<Stock> getTrendyStocks() {
		ArrayList<Stock> stockList = new ArrayList<Stock>();
		Connection connection = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			connection = dbMgr.getConnection();
			ps = connection.prepareStatement(SELECT_FROM_STOCK + " where changePerHour is not null " +
												" and stock_sold(id)< " + TRENDY_STOCK_AVAILABLE_PERCENTAGE_THRESHOLD + " " +
												" and total-(total*stock_sold(id))> " + TRENDY_STOCK_AVAILABLE_THRESHOLD +
												" and total >= " + TRENDY_STOCK_TOTAL_THRESHOLD +
												" and (TIMESTAMPDIFF(minute, lastUpdate, now())  < ?) "  +												
												//" and stock_sold(id) > 0 "  +		
												" order by (changePerHour/total) desc limit ?;");

			ps.setInt(1, StockUpdateTask.LAST_UPDATE_DIFF_MINUTES);
			ps.setInt(2, MAX_TRENDS);
			rs = ps.executeQuery();
			while (rs.next()) {
				Stock stockDO = new Stock();
				stockDO.getDataFromResultSet(rs);
				stockList.add(stockDO);
			}
			logger.debug(DBConstants.QUERY_EXECUTION_SUCC + ps.toString());
		} catch (SQLException ex) {
			logger.error(DBConstants.QUERY_EXECUTION_FAIL + ps.toString(), ex);
		} finally {
			dbMgr.closeResources(connection, ps, rs);
		}
		return stockList;
	}

	@Override
	public ArrayList<Stock> getUserWatchList(long userid) {
		ArrayList<Stock> stockList = new ArrayList<Stock>();
		Connection connection = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			connection = dbMgr.getConnection();
			ps = connection.prepareStatement(SELECT_FROM_STOCK + " where id in (select stock_id from user_stock_watch where user_id=?)");

			ps.setLong(1, userid);
			rs = ps.executeQuery();
			while (rs.next()) {
				Stock stockDO = new Stock();
				stockDO.getDataFromResultSet(rs);
				stockList.add(stockDO);
			}
			logger.debug(DBConstants.QUERY_EXECUTION_SUCC + ps.toString());
		} catch (SQLException ex) {
			logger.error(DBConstants.QUERY_EXECUTION_FAIL + ps.toString(), ex);
		} finally {
			dbMgr.closeResources(connection, ps, rs);
		}
		return stockList;
	}

	@Override
	public void addStockIntoUserWatchList(long stockid, long userid) {
		Connection connection = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			connection = dbMgr.getConnection();
			ps = connection.prepareStatement("insert ignore into user_stock_watch(user_id,stock_id) VALUES  (?,?) ");

			ps.setLong(1, userid);

			ps.setLong(2, stockid);
			ps.executeUpdate();

			logger.debug(DBConstants.QUERY_EXECUTION_SUCC + ps.toString());
		} catch (SQLException ex) {
			logger.error(DBConstants.QUERY_EXECUTION_FAIL + ps.toString(), ex);
		} finally {
			dbMgr.closeResources(connection, ps, rs);
		}
	}

	@Override
	public void removeStockFromUserWatchList(long stockid, long userid) {
		Connection connection = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			connection = dbMgr.getConnection();
			ps = connection.prepareStatement("delete from user_stock_watch where user_id=? and stock_id=? ");

			ps.setLong(1, userid);

			ps.setLong(2, stockid);
			ps.executeUpdate();

			logger.debug(DBConstants.QUERY_EXECUTION_SUCC + ps.toString());
		} catch (SQLException ex) {
			logger.error(DBConstants.QUERY_EXECUTION_FAIL + ps.toString(), ex);
		} finally {
			dbMgr.closeResources(connection, ps, rs);
		}
	}

	
	private ArrayList<Long> getTwitterTrendsAndSaveAsStock(){
		
		ArrayList<Long> idList = new ArrayList<Long>();

		ArrayList<Trend> trends = getTwitterProxy().getTrends();

		if (trends != null) {

			for (Trend trend : trends) {

				String name = trend.getName();
				logger.info("Twitter trend: " + name);
				Stock stock = getStockById(getTwitterProxy().searchAndGetFirstResult(name));

				if (stock != null) {
					idList.add(stock.getId());
					logger.info("Stock: "+stock.getName());
				}
				else{
					logger.info("Twitter trend is not related to any twitter user. Trend: " + name);
					
				}
			}
		}
		return idList;
	}
	
	@Override
	public void updateTwitterTrends() {
	
		
		//stock id list
		ArrayList<Long> idList = getTwitterTrendsAndSaveAsStock();

		if (idList.size() > 0) {
			
			Connection connection = null;
			PreparedStatement ps = null;

			ResultSet rs = null;
			
			String idListStr = DBMgrImpl.getIdListAsCommaSeparatedString(idList);
			try {
				connection = dbMgr.getConnection();
				ps = connection.prepareStatement("insert into twitter_trends (stock_id)  values " + idListStr + " on duplicate key update lastUpdate = now() ");
				ps.executeUpdate();

				ps = connection.prepareStatement("delete from twitter_trends where TIMESTAMPDIFF(minute, lastUpdate, now()) > ? ");
				ps.setInt(1, TWITTER_TRENDS_CLEANUP_PERIOD);
				ps.executeUpdate();

				logger.debug(DBConstants.QUERY_EXECUTION_SUCC + ps.toString());
			} catch (SQLException ex) {
				logger.error(DBConstants.QUERY_EXECUTION_FAIL + ps.toString(), ex);
			} finally {
				dbMgr.closeResources(connection, ps, rs);
			}
		}
	}
}
