/**
	TwitStreet - Twitter Stock Market Game
    Copyright (C) 2012  Engin Guller (bisanth@gmail.com), Cagdas Ozek (cagdasozek@gmail.com)

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
 **/

package com.twitstreet.market;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;

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

	private static int MAX_SUGGESTED_STOCKS = 180;
	private static int TOP_GROSSING_STOCK_TOTAL_THRESHOLD = 1000;

	private static int TWITTER_TRENDS_CLEANUP_PERIOD = 24 * 60; // minutes

	private static String TRENDY_STOCK_AVAILABLE_THRESHOLD = "5";

	private static String TRENDY_STOCK_TOTAL_THRESHOLD = "500";
	private static String TRENDY_STOCK_AVAILABLE_PERCENTAGE_THRESHOLD = "0.99";

	public static String SELECT_FROM_STOCK = " select id, name, longName, "
			+ " total, stock_sold(id) as sold, pictureUrl, "
			+ " lastUpdate, createdAt, changePerHour, verified, language, description, location  from stock ";
	public static String SELECT_DISTINCT_FROM_STOCK = " select distinct id, name,longName, total, stock_sold(id) as sold, pictureUrl, lastUpdate, createdAt, changePerHour, verified, language, description, location from stock ";

	private static String STOCK_IN_PORTFOLIO = " stock.id in (select distinct stock from portfolio) ";
	private static String STOCK_IN_WATCHLIST = " stock.id in (select distinct stock_id from user_stock_watch ) ";
	private static String STOCK_IN_TWITTER_TRENDS = " stock.id in (select stock_id from twitter_trends) ";

	private static String FIND_SUGGESTED_STOCKS = SELECT_FROM_STOCK
			+ " where changePerHour is not null " + " and stock_sold(id)< "
			+ TRENDY_STOCK_AVAILABLE_PERCENTAGE_THRESHOLD + " "
			+ " and total-(total*stock_sold(id))> "
			+ TRENDY_STOCK_AVAILABLE_THRESHOLD + " and total >= "
			+ TRENDY_STOCK_TOTAL_THRESHOLD
			+ " and (TIMESTAMPDIFF(minute, lastUpdate, now())  < "
			+ StockUpdateTask.LAST_UPDATE_DIFF_MINUTES + ") "
			+ " and createdAt  < TIMESTAMPADD(DAY,-"
			+ Stock.STOCK_OLDER_THAN_DAYS_AVAILABLE + ", NOW())  " + " and "
			+ " (" + STOCK_IN_PORTFOLIO + " or " + STOCK_IN_TWITTER_TRENDS
			+ ")" + " order by (changePerHour/total) desc ";

	private static String DROP_SUGGESTED_STOCKS = " drop table  if exists suggested_stocks ";
	private static String CREATE_SUGGESTED_STOCKS = " create table suggested_stocks like twitter_trends ";
	private static String FILL_SUGGESTED_STOCKS = " insert ignore into suggested_stocks(stock_id) select distinct id from ("
			+ FIND_SUGGESTED_STOCKS
			+ " limit "
			+ MAX_SUGGESTED_STOCKS
			+ " ) as suggestedstocks ";
	private static String GET_SUGGESTED_STOCKS = SELECT_FROM_STOCK
			+ " where id in (select stock_id from suggested_stocks) and stock_sold(id)< "
			+ TRENDY_STOCK_AVAILABLE_PERCENTAGE_THRESHOLD
			+ "  order by changePerHour/total desc ";

	@Inject
	ConfigMgr configMgr;

	private static StockMgrImpl instance = new StockMgrImpl();

	public static StockMgr getInstance() {

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

			ps = connection.prepareStatement(SELECT_FROM_STOCK
					+ "  where name = ?");
			ps.setString(1, name);
			rs = ps.executeQuery();
			if (rs.next()) {
				stockDO = new Stock();
				stockDO.getDataFromResultSet(rs);

				if (stockDO.isUpdateRequired()) {
					if (updateStockData(stockDO.getId())) {
						stockDO = getStockById(stockDO.getId());
					} else {
						return null;
					}
				}
				logger.debug(DBConstants.QUERY_EXECUTION_SUCC + ps.toString());

			} else {

				twitter4j.User twUser = getTwitterProxy().getTwUser(name);

				if (twUser != null) {
					stockDO = new Stock(twUser);
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
			ps = connection.prepareStatement(SELECT_FROM_STOCK
					+ " where id = ?");
			ps.setLong(1, id);

			rs = ps.executeQuery();
			if (rs.next()) {
				stockDO = new Stock();
				stockDO.getDataFromResultSet(rs);

				if (stockDO.isUpdateRequired()) {
					if (updateStockData(id)) {
						stockDO = getStockById(id);
					} else {
						stockDO.setSuspended(true);
						return stockDO;
					}
				}
				logger.debug(DBConstants.QUERY_EXECUTION_SUCC + ps.toString());
			} else {

				twitter4j.User twUser = getTwitterProxy().getTwUser(id);

				if (twUser != null) {
					stockDO = new Stock(twUser);
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

	private TwitterProxy getTwitterProxy() {
		User user = userMgr.random();

		TwitterProxy twitterProxy = user == null ? null : twitterProxyFactory
				.create(user.getOauthToken(), user.getOauthTokenSecret());
		return twitterProxy;

	}

	@Override
	public boolean updateStockData(long id) {

		twitter4j.User twUser = getTwitterProxy().getTwUser(id);
		if (twUser != null) {
			updateTwitterData(twUser);
			return true;
		}
		return false;

	}

	private void setStockListUpdating(ArrayList<Long> idList, boolean updating) {

		for (Long id : idList) {
			setStockUpdating(id, updating);
		}

	}

	private void setStockUpdating(long id, boolean updating) {

		Connection connection = null;
		PreparedStatement ps = null;

		try {
			connection = dbMgr.getConnection();
			ps = connection
					.prepareStatement("update stock set updating = ?  where id = ?");

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
	public void updateStockListData(ArrayList<Long> idList) {

		try {
			setStockListUpdating(idList, true);

			ArrayList<twitter4j.User> twUserList = getTwitterProxy()
					.getTwUsers(idList);

			for (twitter4j.User twUser : twUserList) {

				updateTwitterData(twUser);
				setStockUpdating(twUser.getId(), false);
			}
		} finally {
			setStockListUpdating(idList, false);
		}

	}

	@Override
	public StockHistoryData getStockHistory(long id) {
		return getStockHistory(id, -1);
	}

	@Override
	public StockHistoryData getStockHistory(long id, java.util.Date since) {

		String sinceStr = " ";
		if (since != null) {
			sinceStr = " and stock_history.lastUpdate >   TIMESTAMP('" + since
					+ "') ";
		}

		Connection connection = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		StockHistoryData stockHistoryData = null;
		try {
			connection = dbMgr.getConnection();
			ps = connection
					.prepareStatement("("
							+ " select distinct stock_history.lastUpdate as lastUpdate, stock.id, stock.name, stock_history.total  "
							+ " from stock_history,stock where stock_history.stock = ? and stock.id =  stock_history.stock  "
							+ sinceStr
							+ " ) "
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
		String forMinutesStr = "  and timestampdiff(minute,stock_history.lastUpdate ,now()) <  "
				+ forMinutes;
		if (forMinutes <= 0) {
			forMinutesStr = "";
		}

		Connection connection = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		StockHistoryData stockHistoryData = null;
		try {
			connection = dbMgr.getConnection();

			ps = connection
					.prepareStatement("(select distinct stock_history.lastUpdate as lastUpdate, stock.id, stock.name, stock_history.total  "
							+ " from stock_history,stock where stock_history.stock = ? and stock.id =  stock_history.stock "
							+ forMinutesStr
							+ ") "
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

	public void updateStockHistory() {
		Connection connection = null;
		PreparedStatement ps = null;

		try {
			connection = dbMgr.getConnection();
			ps = connection
					.prepareStatement("insert ignore into stock_history(stock, total, date, hour, lastUpdate) "
							+ " select id, total, DATE(lastUpdate), HOUR(lastUpdate), lastUpdate from stock ");

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

	private void updateTwitterData(twitter4j.User twUser) {
		Connection connection = null;
		PreparedStatement ps = null;
		try {
			connection = dbMgr.getConnection();
			ps = connection
					.prepareStatement("update stock set total = ?, pictureUrl = ?, lastUpdate = now(), name = ?,longName = ?, verified = ?,language = ?,description = ?, createdAt=?, location = ?  where id = ?");

			ps.setInt(1, twUser.getFollowersCount());
			ps.setString(2, twUser.getProfileImageURL().toExternalForm());
			ps.setString(3, twUser.getScreenName());
			ps.setString(4, twUser.getName());
			ps.setBoolean(5, twUser.isVerified());
			ps.setString(6, twUser.getLang());
			ps.setString(7, twUser.getDescription());
			ps.setDate(8, new Date(twUser.getCreatedAt().getTime()));
			ps.setString(9, twUser.getLocation());
			ps.setLong(10, twUser.getId());
			ps.executeUpdate();

			// This query should be called right after the stock update,
			// since the get_stock_trend_for_x_minutes requires an up to date
			// stock table
			ps = connection
					.prepareStatement("update stock set changePerHour = get_stock_trend_for_x_minutes(?,?) where id = ?");

			ps.setLong(1, twUser.getId());
			ps.setInt(2, STOCK_TREND_IN_MINUTES);
			ps.setLong(3, twUser.getId());
			ps.executeUpdate();

			ps = connection
					.prepareStatement("insert ignore into stock_history(stock, total, date, hour, lastUpdate) "
							+ " select id, total, DATE(NOW()), HOUR(NOW()), lastUpdate from stock where id = ?");
			ps.setLong(1, twUser.getId());
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
			ps = connection
					.prepareStatement("insert ignore into stock(id, name, longName, description, total, pictureUrl, lastUpdate, verified,language,createdAt, location) values(?, ?,?,?, ?, ?, now(), ?, ?,?, ?)");
			ps.setLong(1, stock.getId());
			ps.setString(2, stock.getName());
			ps.setString(3, stock.getLongName());
			ps.setString(4, stock.getDescription());
			ps.setInt(5, stock.getTotal());
			ps.setString(6, stock.getPictureUrl());

			ps.setBoolean(7, stock.isVerified());
			ps.setString(8, stock.getLanguage());
			ps.setDate(9, new Date(stock.getCreatedAt().getTime()));
			ps.setString(10, stock.getLocation());

			ps.executeUpdate();

			ps = connection
					.prepareStatement("insert ignore into stock_history(stock, total, date, hour, lastUpdate) "
							+ " select id, total, DATE(NOW()), HOUR(NOW()), lastUpdate from stock where id = ?");
			ps.setLong(1, stock.getId());
			ps.executeUpdate();

			// java.util.Date date =
			// getTwitterProxy().getFirstTweetDate(stock.getId());

			logger.debug(DBConstants.QUERY_EXECUTION_SUCC + ps.toString());
		} catch (MySQLIntegrityConstraintViolationException e) {
			logger.warn("DB: Stock already exist - Stock Id:" + stock.getId()
					+ " User Name: " + stock.getName() + " - " + e.getMessage());
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

		if (limit <= 0) {

			limit = Integer.MAX_VALUE;

		}
		ArrayList<Stock> stockList = new ArrayList<Stock>();
		Connection connection = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			connection = dbMgr.getConnection();
			ps = connection
					.prepareStatement(SELECT_DISTINCT_FROM_STOCK
							+ " where (TIMESTAMPDIFF(minute, lastUpdate, now())  > ?  or lastUpdate is null) "
							+ " and (" + STOCK_IN_PORTFOLIO + " or "
							+ STOCK_IN_WATCHLIST + " or "
							+ STOCK_IN_TWITTER_TRENDS + " )"
							+ " and updating != b'1' "
							+ " order by lastUpdate asc " + " limit ?");

			ps.setLong(1, StockUpdateTask.LAST_UPDATE_DIFF_MINUTES);
			ps.setInt(2, TwitterProxyImpl.IDS_SIZE);
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
	public List<Stock> getUpdateRequiredStocksByServer() {
		ArrayList<Stock> stockList = new ArrayList<Stock>();
		Connection connection = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			connection = dbMgr.getConnection();
			ps = connection
					.prepareStatement(SELECT_DISTINCT_FROM_STOCK
							+ " where (TIMESTAMPDIFF(minute, lastUpdate, now())  > ?  or lastUpdate is null) "
							+ " and (" + STOCK_IN_PORTFOLIO + " or "
							+ STOCK_IN_WATCHLIST + " or "
							+ STOCK_IN_TWITTER_TRENDS + " )"
							+ " and mod(id, ?) = ?");

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
	public List<TrendyStock> getTopGrossedStocks(int forhours) {

		ArrayList<TrendyStock> stockList = new ArrayList<TrendyStock>();
		Connection connection = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			connection = dbMgr.getConnection();

			// TODO optimize query
			ps = connection
					.prepareStatement(" select s.*, sh.total as oldValue, sh.lastUpdate as oldUpdate from stock_history sh,stock s "
							+ "  where "
							+

							"   TIMESTAMPDIFF(minute,s.lastUpdate,now()) <= ? "
							+ "   and TIMESTAMPDIFF(minute,sh.lastUpdate,s.lastUpdate)  >= ? "
							+ "   and TIMESTAMPDIFF(minute,sh.lastUpdate,s.lastUpdate)  <= ? "
							+ "   and sh.stock = s.id "
							+ "   and s.total > ? "
							+ "  order by (s.total-sh.total)/sh.total desc limit 1; ");

			ps.setInt(1, 35);
			ps.setInt(2, (forhours * 60) - 35);
			ps.setInt(3, (forhours * 60) + 35);
			ps.setInt(4, TOP_GROSSING_STOCK_TOTAL_THRESHOLD);
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
			ps = connection
					.prepareStatement("update stock set changePerHour = NULL"
							+ " where TIMESTAMPDIFF(minute, lastUpdate,now()) > ?");
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
			ps = connection
					.prepareStatement("update stock set changePerHour = NULL"
							+ " where TIMESTAMPDIFF(minute, lastUpdate,now()) > ? and mod(id, ?) = ?");
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
	public ArrayList<Stock> getSuggestedStocks() {
		return getSuggestedStocks(0, MAX_TRENDS_PER_PAGE);
	}

	@Override
	public void loadSuggestedStocks() {

		Connection connection = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			connection = dbMgr.getConnection();

			ps = connection.prepareStatement(DROP_SUGGESTED_STOCKS);
			ps.execute();
			ps = connection.prepareStatement(CREATE_SUGGESTED_STOCKS);
			ps.execute();
			ps = connection.prepareStatement(FILL_SUGGESTED_STOCKS);

			ps.execute();

			logger.debug(DBConstants.QUERY_EXECUTION_SUCC + ps.toString());
		} catch (SQLException ex) {
			logger.error(DBConstants.QUERY_EXECUTION_FAIL + ps.toString(), ex);
		} finally {
			dbMgr.closeResources(connection, ps, rs);
		}
	}

	@Override
	public int getSuggestedStockCount() {

		Connection connection = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			connection = dbMgr.getConnection();
			ps = connection
					.prepareStatement("select count(*) from suggested_stocks ");

			rs = ps.executeQuery();
			while (rs.next()) {
				return rs.getInt(1);
			}
			logger.debug(DBConstants.QUERY_EXECUTION_SUCC + ps.toString());
		} catch (SQLException ex) {
			logger.error(DBConstants.QUERY_EXECUTION_FAIL + ps.toString(), ex);
		} finally {
			dbMgr.closeResources(connection, ps, rs);
		}
		return -1;
	}

	@Override
	public ArrayList<Stock> getSuggestedStocks(int offset, int count) {
		ArrayList<Stock> stockList = new ArrayList<Stock>();
		Connection connection = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			connection = dbMgr.getConnection();
			ps = connection.prepareStatement(GET_SUGGESTED_STOCKS
					+ " limit ?,? ");

			ps.setInt(1, offset);
			ps.setInt(2, count);
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
	public ArrayList<Stock> getTopGrossingStocks() {
		return getTopGrossingStocks(0, MAX_TRENDS_PER_PAGE);
	}

	@Override
	public ArrayList<Stock> getTopGrossingStocks(int offset, int count) {
		ArrayList<Stock> stockList = new ArrayList<Stock>();
		Connection connection = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			connection = dbMgr.getConnection();
			ps = connection.prepareStatement(SELECT_FROM_STOCK
					+ " where changePerHour is not null "
					+ " and (TIMESTAMPDIFF(minute, lastUpdate, now())  < ?) "
					+ " and total >= " + TRENDY_STOCK_TOTAL_THRESHOLD + " and "
					+ " (" + STOCK_IN_PORTFOLIO + " or "
					+ STOCK_IN_TWITTER_TRENDS + ")"
					+ " order by (changePerHour/total) desc limit ?,?;");

			ps.setInt(1, StockUpdateTask.LAST_UPDATE_DIFF_MINUTES);
			ps.setInt(2, offset);
			ps.setInt(3, count);
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

	private java.util.Date getFirstTweetDate(long stockId) {
		return getTwitterProxy().getFirstTweetDate(stockId);

	}

	private ArrayList<Long> getTwitterTrendsAndSaveAsStock() {

		ArrayList<Long> idList = new ArrayList<Long>();

		TwitterProxy twitterProxy = getTwitterProxy();
		if (twitterProxy != null) {
			Set<String> trends = twitterProxy.getTrends();
			if (trends != null) {
				logger.info("Convert trend to stock started. Trend Size: "
						+ trends.size());
				int converted2Stock = 0;
				Iterator<String> trendsIterator = trends.iterator();
				for (; trendsIterator.hasNext();) {
					String name = trendsIterator.next();
					logger.debug("\n\nTwitter trend: " + name);
					Stock stock = getStockById(getTwitterProxy()
							.searchAndGetFirstResult(name));
					if (stock != null) {
						idList.add(stock.getId());
						logger.debug("Stock: " + stock.getName());
						converted2Stock++;
					} else {
						logger.debug("Twitter trend is not related to any twitter user. Trend: "
								+ name);
					}
				}
				logger.info("Convert trend to stock completed. "
						+ converted2Stock + " trend converted to stock");
			}
		}
		return idList;
	}

	@Override
	public void updateTwitterTrends() {

		// stock id list
		ArrayList<Long> idList = getTwitterTrendsAndSaveAsStock();

		if (idList.size() > 0) {

			Connection connection = null;
			PreparedStatement ps = null;

			ResultSet rs = null;

			String idListStr = DBMgrImpl
					.getIdListAsCommaSeparatedString(idList);
			try {
				connection = dbMgr.getConnection();
				ps = connection
						.prepareStatement("insert into twitter_trends (stock_id)  values "
								+ idListStr
								+ " on duplicate key update lastUpdate = now() ");
				ps.executeUpdate();

				ps = connection
						.prepareStatement("delete from twitter_trends where TIMESTAMPDIFF(minute, lastUpdate, now()) > ? ");
				ps.setInt(1, TWITTER_TRENDS_CLEANUP_PERIOD);
				ps.executeUpdate();

				logger.debug(DBConstants.QUERY_EXECUTION_SUCC + ps.toString());
			} catch (SQLException ex) {
				logger.error(DBConstants.QUERY_EXECUTION_FAIL + ps.toString(),
						ex);
			} finally {
				dbMgr.closeResources(connection, ps, rs);
			}
		}
	}

	@Override
	public boolean addStockIntoAnnouncement(long stockid) {
		Connection connection = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			connection = dbMgr.getConnection();
			ps = connection
					.prepareStatement("insert into announcement(stock_id) VALUES  (?) ");
			ps.setLong(1, stockid);
			ps.executeUpdate();

			logger.debug(DBConstants.QUERY_EXECUTION_SUCC + ps.toString());

			return true;
		} catch (SQLException e) {

			return false;
		} finally {
			dbMgr.closeResources(connection, ps, rs);
		}
	}

	@Override
	public void removeOldRecords(int olderThanMinutesOld) {
		Connection connection = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			connection = dbMgr.getConnection();
			ps = connection
					.prepareStatement(" delete from announcement where timestampdiff(minute,timeSent,now()) > ? ");
			ps.setInt(1, olderThanMinutesOld);
			ps.executeUpdate();

			logger.debug(DBConstants.QUERY_EXECUTION_SUCC + ps.toString());

		} catch (SQLException e) {
			logger.error(DBConstants.QUERY_EXECUTION_FAIL + ps.toString(), e);

		} finally {
			dbMgr.closeResources(connection, ps, rs);
		}
	}

	@Override
	public void removeOldRecordsByServer(int removeOlderThanMinutes) {
		Connection connection = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			connection = dbMgr.getConnection();
			ps = connection
					.prepareStatement(" delete from announcement where timestampdiff(minute,timeSent,now()) > ? and mod(stock_id, ?) = ?");
			ps.setInt(1, removeOlderThanMinutes);
			ps.setInt(2, configMgr.getServerCount());
			ps.setInt(3, configMgr.getServerId());
			ps.executeUpdate();

			logger.debug(DBConstants.QUERY_EXECUTION_SUCC + ps.toString());

		} catch (SQLException e) {
			logger.error(DBConstants.QUERY_EXECUTION_FAIL + ps.toString(), e);

		} finally {
			dbMgr.closeResources(connection, ps, rs);
		}
	}

	@Override
	public void saveTrend(long stockId) {
		Connection connection = null;
		PreparedStatement ps = null;
		try {
			connection = dbMgr.getConnection();
			ps = connection
					.prepareStatement("insert into twitter_trends (stock_id)  values (?) on duplicate key update lastUpdate = now() ");
			ps.setLong(1, stockId);
			ps.executeUpdate();

		} catch (SQLException ex) {
			logger.error(DBConstants.QUERY_EXECUTION_FAIL + ps.toString(), ex);
		} finally {
			dbMgr.closeResources(connection, ps, null);
		}
	}

	@Override
	public void truncateStockHistory() {
		ArrayList<Long> idList = new ArrayList<Long>();
		Connection connection = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			connection = dbMgr.getConnection();
			ps = connection
					.prepareStatement("select max(id) as id from stock_history where date < DATE(now()) group by stock, date");
			rs = ps.executeQuery();
			while(rs.next()){
				idList.add(rs.getLong("id"));
			}
			String idListStr = DBMgrImpl.getIdListAsCommaSeparatedString4In(idList);
			rs.close();
			ps.close();
			ps = connection.prepareStatement("delete from stock_history where id not in (" + idListStr + ")" );
		} catch (SQLException ex) {
			logger.error(DBConstants.QUERY_EXECUTION_FAIL + ps.toString(), ex);
		} finally {
			dbMgr.closeResources(connection, ps, null);
		}
	}
}
