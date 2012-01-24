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
import com.twitstreet.db.base.DBMgr;
import com.twitstreet.db.data.Stock;

public class StockMgrImpl implements StockMgr {
	@Inject
	private DBMgr dbMgr;
	private static Logger logger = Logger.getLogger(StockMgrImpl.class);
	// update in every 20 minutes
	private static final int LAST_UPDATE_DIFF = 20 * 60;

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
					.prepareStatement("select id, name, total, stock_sold(id) as sold, lastUpdate, pictureUrl from stock where name = ?");
			ps.setString(1, name);
			rs = ps.executeQuery();
			while (rs.next()) {
				stockDO = new Stock();
				stockDO.setId(rs.getLong("id"));
				stockDO.setName(rs.getString("name"));
				stockDO.setTotal(rs.getInt("total"));
				stockDO.setSold(rs.getDouble("sold"));
				stockDO.setPictureUrl(rs.getString("pictureUrl"));
				stockDO.setLastUpdate(rs.getTimestamp("lastUpdate"));
				break;
			}
			logger.debug("DB: Query executed successfully - " + ps.toString());
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
				logger.error("DB: Resources could not be closed properly", e);
			}
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
					.prepareStatement("select id, name, total, stock_sold(id) as sold, pictureUrl, lastUpdate from stock where id = ?");
			ps.setLong(1, id);

			rs = ps.executeQuery();
			if (rs.next()) {
				stockDO = new Stock();
				stockDO.setId(rs.getLong("id"));
				stockDO.setName(rs.getString("name"));
				stockDO.setTotal(rs.getInt("total"));
				stockDO.setSold(rs.getDouble("sold"));
				stockDO.setPictureUrl(rs.getString("pictureUrl"));
				stockDO.setLastUpdate(rs.getTimestamp("lastUpdate"));
			}
			logger.debug("DB: Query executed successfully - " + ps.toString());
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
				logger.error("DB: Resources could not be closed properly", e);
			}
		}
		return stockDO;
	}

	public void updateStockHistory(){
		Connection connection = null;
		PreparedStatement ps = null;

		try {
			connection = dbMgr.getConnection();
			ps = connection
					.prepareStatement("insert into stock_history(stock, total, name, day, stockLastUpdate) select distinct id, total, name, DATE(NOW()), lastUpdate from stock order by lastUpdate desc ");
		
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
	
	
	
	public void updateTwitterData(long id, int total, String pictureUrl,
			String screenName) {
		Connection connection = null;
		PreparedStatement ps = null;

		try {
			connection = dbMgr.getConnection();
			ps = connection
					.prepareStatement("update stock set total = ?, pictureUrl = ?, lastUpdate = now(), name = ? where id = ?");
			ps.setInt(1, total);
			ps.setString(2, pictureUrl);
			ps.setString(3, screenName);
			ps.setLong(4, id);
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
	public void saveStock(Stock stock) {
		Connection connection = null;
		PreparedStatement ps = null;
		try {
			connection = dbMgr.getConnection();
			ps = connection
					.prepareStatement("insert into stock(id, name, total, pictureUrl, lastUpdate) values(?, ?, ?, ?, now())");
			ps.setLong(1, stock.getId());
			ps.setString(2, stock.getName());
			ps.setInt(3, stock.getTotal());
			ps.setString(4, stock.getPictureUrl());

			ps.executeUpdate();
			logger.debug("DB: Query executed successfully - " + ps.toString());
		} catch (MySQLIntegrityConstraintViolationException e) {
			logger.warn("DB: Stock already exist - Stock Id:" + stock.getId()
					+ " User Name: " + stock.getName() + " - " + e.getMessage());
		} catch (SQLException ex) {
			logger.error("DB: Query failed = " + ps.toString(), ex);
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
	public List<Stock> getUpdateRequiredStocks() {
		ArrayList<Stock> stockList = new ArrayList<Stock>();
		Connection connection = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			connection = dbMgr.getConnection();
			ps = connection
					.prepareStatement("select id, name, total, stock_sold(id) as sold, pictureUrl, lastUpdate from stock where ((now() - lastUpdate) > ? or lastUpdate is null) and stock.id in (select distinct stock from portfolio)");

			ps.setLong(1, LAST_UPDATE_DIFF);
			rs = ps.executeQuery();
			while (rs.next()) {
				Stock stockDO = new Stock();
				stockDO.setId(rs.getLong("id"));
				stockDO.setName(rs.getString("name"));
				stockDO.setTotal(rs.getInt("total"));
				stockDO.setSold(rs.getDouble("sold"));
				stockDO.setPictureUrl(rs.getString("pictureUrl"));
				stockDO.setLastUpdate(rs.getTimestamp("lastUpdate"));
				stockList.add(stockDO);
			}
			logger.debug("DB: Query executed successfully - " + ps.toString());
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
				logger.error("DB: Resources could not be closed properly", e);
			}
		}
		return stockList;
	}
}
