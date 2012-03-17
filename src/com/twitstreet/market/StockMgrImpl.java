package com.twitstreet.market;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import twitter4j.Trend;

import com.google.inject.Inject;
import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;
import com.twitstreet.config.ConfigMgr;
import com.twitstreet.db.base.DBConstants;
import com.twitstreet.db.base.DBMgr;
import com.twitstreet.db.base.DBMgrImpl;
import com.twitstreet.db.data.Stock;
import com.twitstreet.db.data.StockHistoryData;
import com.twitstreet.db.data.TrendyStock;
import com.twitstreet.db.data.User;
import com.twitstreet.session.UserMgr;
import com.twitstreet.task.StockUpdateTask;
import com.twitstreet.twitter.TwitterProxy;
import com.twitstreet.twitter.TwitterProxyFactory;
import com.twitstreet.twitter.TwitterProxyImpl;

public class StockMgrImpl implements StockMgr {

	private static int TOP_GROSSING_STOCK_TOTAL_THRESHOLD = 1000;

	private static int TWITTER_TRENDS_CLEANUP_PERIOD = 24 * 60; // minutes

	private static String TRENDY_STOCK_AVAILABLE_THRESHOLD = "5";

	private static String TRENDY_STOCK_TOTAL_THRESHOLD = "500";
	private static String TRENDY_STOCK_AVAILABLE_PERCENTAGE_THRESHOLD = "0.99";

	private static String SELECT_FROM_STOCK = " select id, name, longName, " +
												" total, stock_sold(id) as sold, pictureUrl, " +
												" lastUpdate, createdAt, changePerHour, verified, language, description  from stock ";
	private static String SELECT_DISTINCT_FROM_STOCK = " select distinct id, name,longName, total, stock_sold(id) as sold, pictureUrl, lastUpdate, createdAt, changePerHour, verified, language, description from stock ";
	
	private static String STOCK_IN_PORTFOLIO =" stock.id in (select distinct stock from portfolio) ";
	private static String STOCK_IN_WATCHLIST =" stock.id in (select distinct stock_id from user_stock_watch ) ";
	private static String STOCK_IN_TWITTER_TRENDS =" stock.id in (select stock_id from twitter_trends) ";
	
	private static int MAX_TRENDS = 6;
	
	@Inject
	ConfigMgr configMgr;

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
					stockDO.setLanguage(twUser.getLang());
					stockDO.setCreatedAt(twUser.getCreatedAt());
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
					stockDO.setLanguage(twUser.getLang());
					stockDO.setCreatedAt(twUser.getCreatedAt());
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
			updateTwitterData(twUser.getId(), twUser.getFollowersCount(), twUser.getProfileImageURL().toExternalForm(), twUser.getScreenName(),twUser.getName(), twUser.isVerified(),twUser.getLang(),twUser.getDescription(), twUser.getCreatedAt());
		}

	}
	
	private void setStockListUpdating(ArrayList<Long> idList, boolean updating){
		
		for(Long id : idList){
			setStockUpdating(id, updating);
		}
		
		
	}
	
	private void setStockUpdating(long id, boolean updating){
		
		Connection connection = null;
		PreparedStatement ps = null;

		try {
			connection = dbMgr.getConnection();
			ps = connection.prepareStatement("update stock set updating = ?  where id = ?");

			ps.setBoolean(1, updating);
			ps.setLong(2, id);
			ps.executeUpdate();

			logger.debug(DBConstants.QUERY_EXECUTION_SUCC + ps.toString());
		} catch (SQLException ex) {
			logger.error(DBConstants.QUERY_EXECUTION_FAIL + ps.toString(), ex);
		} finally {
			dbMgr.closeResources(connection, ps, null);
		}
		
		
	}
	@Override
	public void updateStockListData(ArrayList<Long>idList) {
		
		try{
			setStockListUpdating(idList, true);
			
			ArrayList<twitter4j.User> twUserList = getTwitterProxy().getTwUsers(idList);
			
			for(twitter4j.User twUser : twUserList){
			
				updateTwitterData(twUser.getId(), twUser.getFollowersCount(), twUser.getProfileImageURL().toExternalForm(), twUser.getScreenName(),twUser.getName(), twUser.isVerified(),twUser.getLang(),twUser.getDescription(), twUser.getCreatedAt());
				setStockUpdating(twUser.getId(), false);
			}
		}
		finally{
			setStockListUpdating(idList, false);
		}
		
	}
	@Override
	public void updateStockData(String stockName) {
	
		
		twitter4j.User twUser = getTwitterProxy().getTwUser(stockName);

		updateTwitterData(twUser.getId(), twUser.getFollowersCount(), twUser.getProfileImageURL().toExternalForm(), twUser.getScreenName(),twUser.getName(), twUser.isVerified(),twUser.getLang(),twUser.getDescription(), twUser.getCreatedAt());

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

	private void updateTwitterData(long id, int total, String pictureUrl, String screenName,String longName, boolean verified,String language, String description, java.util.Date createdAt) {
		Connection connection = null;
		PreparedStatement ps = null;

		try {
			connection = dbMgr.getConnection();
			ps = connection.prepareStatement("update stock set total = ?, pictureUrl = ?, lastUpdate = now(), name = ?,longName = ?, verified = ?,language = ?,description = ?, createdAt=?  where id = ?");

			ps.setInt(1, total);
			ps.setString(2, pictureUrl);
			ps.setString(3, screenName);
			ps.setString(4, longName);
			ps.setBoolean(5, verified);
			ps.setString(6, language);
			ps.setString(7, description);
			ps.setDate(8, new Date(createdAt.getTime()));
			ps.setLong(9, id);
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
			ps = connection.prepareStatement("insert into stock(id, name, longName, description, total, pictureUrl, lastUpdate, verified,language,createdAt) values(?, ?,?,?, ?, ?, now(), ?, ?,?)");
			ps.setLong(1, stock.getId());
			ps.setString(2, stock.getName());
			ps.setString(3, stock.getLongName());
			ps.setString(4, stock.getDescription());
			ps.setInt(5, stock.getTotal());
			ps.setString(6, stock.getPictureUrl());
			
			ps.setBoolean(7, stock.isVerified());
			ps.setString(8, stock.getLanguage());
			ps.setDate(9, new Date(stock.getCreatedAt().getTime()));

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

	
	@Override
	public List<Stock> getUpdateRequiredStocks() {

		return getUpdateRequiredStocks(-1);
	}
	@Override
	public List<Stock> getUpdateRequiredStocks(int limit) {

		if(limit<=0){
			
			limit = Integer.MAX_VALUE;
			
		}
		ArrayList<Stock> stockList = new ArrayList<Stock>();
		Connection connection = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			connection = dbMgr.getConnection();
			ps = connection.prepareStatement(SELECT_DISTINCT_FROM_STOCK +  
					" where (TIMESTAMPDIFF(minute, lastUpdate, now())  > ?  or lastUpdate is null) " 
					+ " and (" + STOCK_IN_PORTFOLIO
						+" or " +STOCK_IN_WATCHLIST 
						+" or " + STOCK_IN_TWITTER_TRENDS 
						+ " )" +
					  " and updating != b'1' " +
					" order by lastUpdate asc " +
					" limit ?");
			
			ps.setLong(1, StockUpdateTask.LAST_UPDATE_DIFF_MINUTES);
			ps.setInt(2, TwitterProxyImpl.USER_COUNT_FOR_UPDATE);
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
	public ArrayList<Long> getUpdateRequiredStockIds() {

		ArrayList<Long> idList = new ArrayList<Long>();
		Connection connection = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			connection = dbMgr.getConnection();
			ps = connection.prepareStatement(SELECT_DISTINCT_FROM_STOCK +  
					" where (TIMESTAMPDIFF(minute, lastUpdate, now())  > ?  or lastUpdate is null) " 
					+ " and (" + STOCK_IN_PORTFOLIO
						+" or " +STOCK_IN_WATCHLIST 
						+" or " + STOCK_IN_TWITTER_TRENDS 
						+ " )");

			ps.setLong(1, StockUpdateTask.LAST_UPDATE_DIFF_MINUTES);
			rs = ps.executeQuery();
			while (rs.next()) {
				Long id = rs.getLong("id");
				idList.add(id);
			}
			logger.debug(DBConstants.QUERY_EXECUTION_SUCC + ps.toString());
		} catch (SQLException ex) {
			logger.error(DBConstants.QUERY_EXECUTION_FAIL + ps.toString(), ex);
		} finally {
			dbMgr.closeResources(connection, ps, rs);
		}
		return idList;
	}
	
	@Override
	public List<Stock> getUpdateRequiredStocksByServer() {
		ArrayList<Stock> stockList = new ArrayList<Stock>();
		Connection connection = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			connection = dbMgr.getConnection();
			ps = connection.prepareStatement(SELECT_DISTINCT_FROM_STOCK 
					+" where (TIMESTAMPDIFF(minute, lastUpdate, now())  > ?  or lastUpdate is null) " 
					+ " and (" + STOCK_IN_PORTFOLIO
						+" or " +STOCK_IN_WATCHLIST 
						+" or " + STOCK_IN_TWITTER_TRENDS 
					+ " )"
					+" and mod(id, ?) = ?");

			ps.setLong(1, StockUpdateTask.LAST_UPDATE_DIFF_MINUTES);
			ps.setInt(2, configMgr.getServerCount());
			ps.setInt(3, configMgr.getServerId());
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
	public List<TrendyStock> getTopGrossingStocks(int forhours){
		
		ArrayList<TrendyStock> stockList = new ArrayList<TrendyStock>();
		Connection connection = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			connection = dbMgr.getConnection();
			ps = connection.prepareStatement(
			" select s.*, sh.total as oldValue, sh.lastUpdate as oldUpdate from stock_history sh,stock s "+
			"  where "+
			
			"   TIMESTAMPDIFF(minute,s.lastUpdate,now()) <= ? " +
			"   and TIMESTAMPDIFF(minute,sh.lastUpdate,s.lastUpdate)  >= ? " +
			"   and TIMESTAMPDIFF(minute,sh.lastUpdate,s.lastUpdate)  <= ? " +
			"   and sh.stock = s.id "+
			"   and s.total > ? " +
			"  order by (s.total-sh.total)/sh.total desc limit 1; ");
			
			ps.setInt(1, 35);
			ps.setInt(2, (forhours * 60) -35 );
			ps.setInt(3, (forhours * 60) +35 );
			//TODO create constant
			ps.setInt(4, TOP_GROSSING_STOCK_TOTAL_THRESHOLD );
			rs = ps.executeQuery();
			
			while (rs.next()) {
				TrendyStock stockDO = new TrendyStock();
				stockDO.getDataFromResultSet(rs);
				stockDO.setTrendDuration(forhours);
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
	public List<TrendyStock> getTopGrossingStocksByServer(int forhours){
		
		ArrayList<TrendyStock> stockList = new ArrayList<TrendyStock>();
		Connection connection = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			connection = dbMgr.getConnection();
			ps = connection.prepareStatement(
			" select s.*, sh.total as oldValue, sh.lastUpdate as oldUpdate from stock_history sh,stock s "+
			"  where "+
			
			"   TIMESTAMPDIFF(minute,s.lastUpdate,now()) <= ? " +
			"   and TIMESTAMPDIFF(minute,sh.lastUpdate,s.lastUpdate)  >= ? " +
			"   and TIMESTAMPDIFF(minute,sh.lastUpdate,s.lastUpdate)  <= ? " +
			"   and sh.stock = s.id "+
			"   and s.total > ? and mod(s.id, ?) = ?" +
			"  order by (s.total-sh.total)/sh.total desc limit 1; ");
			
			ps.setInt(1, 2 * StockUpdateTask.LAST_UPDATE_DIFF_MINUTES);
			ps.setInt(2, (forhours * 60) - (2 * StockUpdateTask.LAST_UPDATE_DIFF_MINUTES) );
			ps.setInt(3, (forhours * 60) + (2 * StockUpdateTask.LAST_UPDATE_DIFF_MINUTES) );
			ps.setInt(4, 500 );
			ps.setInt(5, configMgr.getServerCount());
			ps.setInt(6, configMgr.getServerId());
			rs = ps.executeQuery();
			
			while (rs.next()) {
				TrendyStock stockDO = new TrendyStock();
				stockDO.getDataFromResultSet(rs);
				stockDO.setTrendDuration(forhours);
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
	public void resetSpeedOfOldStocksByServer() {
		Connection connection = null;
		PreparedStatement ps = null;

		try {
			connection = dbMgr.getConnection();
			ps = connection.prepareStatement("update stock set changePerHour = NULL" + " where TIMESTAMPDIFF(minute, lastUpdate,now()) > ? and mod(id, ?) = ?");
			ps.setInt(1, StockUpdateTask.LAST_UPDATE_DIFF_MINUTES * 3);
			ps.setInt(2, configMgr.getServerCount());
			ps.setInt(3, configMgr.getServerId());
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
												" and createdAt  < TIMESTAMPADD(DAY,?, NOW())  "  +												
												" and " +
												" ("+STOCK_IN_PORTFOLIO+ " or "
													+STOCK_IN_TWITTER_TRENDS+
												  ")" +
												" order by (changePerHour/total) desc limit ?;");

			ps.setInt(1, StockUpdateTask.LAST_UPDATE_DIFF_MINUTES);
			ps.setInt(2, Stock.STOCK_OLDER_THAN_DAYS_AVAILABLE);
			ps.setInt(3, MAX_TRENDS);
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
				logger.info("\n\nTwitter trend: " + name);
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
