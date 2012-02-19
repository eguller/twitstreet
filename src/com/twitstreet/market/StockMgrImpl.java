package com.twitstreet.market;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.google.inject.Inject;
import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;
import com.twitstreet.db.base.DBConstants;
import com.twitstreet.db.base.DBMgr;
import com.twitstreet.db.data.Stock;
import com.twitstreet.db.data.StockHistoryData;
import com.twitstreet.task.StockUpdateTask;

public class StockMgrImpl implements StockMgr {
	private static String SELECT_FROM_STOCK = " select id, name, total, stock_sold(id) as sold, pictureUrl, lastUpdate, changePerHour, verified from stock ";
	private static int MAX_TRENDS = 10;
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

			ps = connection
					.prepareStatement(SELECT_FROM_STOCK +"  where name = ?");
			ps.setString(1, name);
			rs = ps.executeQuery();
			while (rs.next()) {
				stockDO = new Stock();
				stockDO.getDataFromResultSet(rs);
				break;
			}
			logger.debug(DBConstants.QUERY_EXECUTION_SUCC + ps.toString());
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
			ps = connection
					.prepareStatement(SELECT_FROM_STOCK +" where id = ?");
			ps.setLong(1, id);

			rs = ps.executeQuery();
			if (rs.next()) {
				stockDO = new Stock();
				stockDO.getDataFromResultSet(rs);
			}
			logger.debug(DBConstants.QUERY_EXECUTION_SUCC + ps.toString());
		} catch (SQLException ex) {
			logger.error(DBConstants.QUERY_EXECUTION_FAIL + ps.toString(), ex);
		} finally {
			dbMgr.closeResources(connection, ps, rs);
		}
		return stockDO;
	}

	public StockHistoryData getStockHistory(long id){
		
		Connection connection = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		StockHistoryData stockHistoryData = null;
		try {
			connection = dbMgr.getConnection();
			ps = connection
					.prepareStatement("select distinct stock_history.lastUpdate, stock.id, stock.name, stock_history.total " +
							" from stock_history,stock " +
							" where stock_history.stock = ? and stock.id =  stock_history.stock " +
							" order by stock_history.lastUpdate asc ");
			ps.setLong(1, id);

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

	public void updateStockHistory(){
		Connection connection = null;
		PreparedStatement ps = null;

		try {
			connection = dbMgr.getConnection();
			ps = connection
					.prepareStatement("insert ignore into stock_history(stock, total, date, hour, lastUpdate) " +
											" select id, total, DATE(lastUpdate), HOUR(lastUpdate), lastUpdate from stock ");
		
			ps.executeUpdate();
				
			logger.debug(DBConstants.QUERY_EXECUTION_SUCC + ps.toString());
		}catch(MySQLIntegrityConstraintViolationException ex){
			
			logger.debug(DBConstants.RECORD_ALREADY_EXISTS + ps.toString());
			
		}
		catch (SQLException ex) {
			logger.error(DBConstants.QUERY_EXECUTION_FAIL + ps.toString(), ex);
		} finally {
			dbMgr.closeResources(connection, ps, null);
		}
		
		
	}

	
	public void updateTwitterData(long id, int total, String pictureUrl,
			String screenName, boolean verified) {
		Connection connection = null;
		PreparedStatement ps = null;

		try {
			connection = dbMgr.getConnection();
			ps = connection
					.prepareStatement("update stock set total = ?, pictureUrl = ?, lastUpdate = now(), name = ?, verified = ? where id = ?");
			
			ps.setInt(1, total);
			ps.setString(2, pictureUrl);
			ps.setString(3, screenName);
			ps.setBoolean(4, verified);
			ps.setLong(5, id);
			ps.executeUpdate();
			
			//This query should be called right after the stock update, 
			// since the get_stock_trend_for_x_minutes requires an up to date stock table
			ps = connection
					.prepareStatement("update stock set changePerHour = get_stock_trend_for_x_minutes(?,?) where id = ?");
			
			ps.setLong(1, id);
			ps.setInt(2, STOCK_TREND_IN_MINUTES);
			ps.setLong(3, id);
			ps.executeUpdate();

			ps = connection
					.prepareStatement("insert ignore into stock_history(stock, total, date, hour, lastUpdate) " +
											" select id, total, DATE(NOW()), HOUR(NOW()), lastUpdate from stock where id = ?");
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
			ps = connection
					.prepareStatement("insert into stock(id, name, total, pictureUrl, lastUpdate, verified) values(?, ?, ?, ?, now(), ?)");
			ps.setLong(1, stock.getId());
			ps.setString(2, stock.getName());
			ps.setInt(3, stock.getTotal());
			ps.setString(4, stock.getPictureUrl());
			ps.setBoolean(5, stock.isVerified());

			ps.executeUpdate();
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
		ArrayList<Stock> stockList = new ArrayList<Stock>();
		Connection connection = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			connection = dbMgr.getConnection();
			ps = connection
					.prepareStatement(SELECT_FROM_STOCK +" where ((now() - lastUpdate) > (? / 1000) or lastUpdate is null) and (stock.id in (select distinct stock from portfolio) or stock.id in (select distinct stock_id from user_stock_watch ) )");

			ps.setLong(1, StockUpdateTask.LAST_UPDATE_DIFF);
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
	public ArrayList<Stock> getTrendyStocks() {
		ArrayList<Stock> stockList = new ArrayList<Stock>();
		Connection connection = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			connection = dbMgr.getConnection();
			ps = connection
					.prepareStatement(SELECT_FROM_STOCK +" where  stock_sold(id)<0.9999 and id in (select distinct stock from portfolio) order by (changePerHour/total) desc limit ?;");

			ps.setInt(1, MAX_TRENDS);
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
			ps = connection
					.prepareStatement(SELECT_FROM_STOCK + " where id in (select stock_id from user_stock_watch where user_id=?)");

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
			ps = connection
					.prepareStatement("insert ignore into user_stock_watch(user_id,stock_id) VALUES  (?,?) ");

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
			ps = connection
					.prepareStatement("delete from user_stock_watch where user_id=? and stock_id=? ");

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
}
