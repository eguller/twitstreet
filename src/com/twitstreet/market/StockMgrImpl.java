package com.twitstreet.market;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.log4j.Logger;


import com.google.inject.Inject;
import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;
import com.twitstreet.db.base.DBMgr;
import com.twitstreet.db.data.Stock;

public class StockMgrImpl implements StockMgr {
	@Inject
	private DBMgr dbMgr;
	private static Logger logger = Logger.getLogger(StockMgrImpl.class);

	public Stock notifyBuy(String stock, double amount) {
		return null;
	}

	public Stock getStock(String name) throws SQLException {
		Connection connection = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		Stock stockDO = null;
		connection = dbMgr.getConnection();
		try{
			ps = connection.prepareStatement("select id, name, total, sold from stock where id = ?");
			ps.setString(1, name);
			rs = ps.executeQuery();
			while(rs.next()){
				stockDO = new Stock();
				stockDO.setId(rs.getLong("id"));
				stockDO.setName(rs.getString("name"));
				stockDO.setTotal(rs.getInt("total"));
				stockDO.setSold(rs.getDouble("sold"));
				break;
			}
			logger.debug("DB: Query executed successfully - " + ps.toString());
		}
		catch(SQLException ex){
			logger.debug("DB: Query failed - " + ps.toString(), ex);
			throw ex;
			
		}
		finally{
			if(!rs.isClosed()) { rs.close(); }
			if(!ps.isClosed()) { ps.close(); }
			if(!connection.isClosed()){ connection.close(); }
		}
		return stockDO;
	}

	public Stock getStockById(long id) throws SQLException {
		Connection connection = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		Stock stockDO = null;
		connection = dbMgr.getConnection();
		ps = connection.prepareStatement("select id, name, total, sold from stock where id = ?");
		ps.setLong(1, id);
		try{
			rs = ps.executeQuery();
			if(rs.next()){
				stockDO = new Stock();
				stockDO.setId(rs.getLong("id"));
				stockDO.setName(rs.getString("name"));
				stockDO.setTotal(rs.getInt("total"));
				stockDO.setSold(rs.getDouble("sold"));
			}
			logger.debug("DB: Query executed successfully - " + ps.toString());
		}
		catch(SQLException ex){
			logger.debug("DB: Query failed - " + ps.toString(), ex);
			throw ex;
		}
		finally{
			if(!rs.isClosed()) { rs.close(); }
			if(!ps.isClosed()) { ps.close(); }
			if(!connection.isClosed()){ connection.close(); }
		}
		return stockDO;
	}

	public void updateTotal(long id, int total) throws SQLException {
		Connection connection = null;
		PreparedStatement ps = null;
		connection = dbMgr.getConnection();
		try{
			ps = connection.prepareStatement("update stock set total = ? where id = ?");
			ps.setInt(1, total);
			ps.setLong(2, id);
			ps.executeUpdate();
			logger.debug("DB: Query executed successfully - " + ps.toString());
		}
		catch(SQLException ex){
			logger.debug("DB: Query failed - " + ps.toString(), ex);
			throw ex;
		}
		finally{
			if(!ps.isClosed()) { ps.close(); }
			if(!connection.isClosed()){ connection.close(); }
		}
	}

	@Override
	public double getPercentSold(String stockName) {
		return 0.0;
	}

	@Override
	public void saveStock(Stock stock) throws SQLException {
		Connection connection = null;
		PreparedStatement ps = null;
		connection = dbMgr.getConnection();
		ps = connection.prepareStatement("insert into stock(id, name, total, sold) values(?, ?, ?, ?)");
		ps.setLong(1, stock.getId());
		ps.setString(2, stock.getName());
		ps.setInt(3, stock.getTotal());
		ps.setDouble(4, stock.getSold());
		try{
			ps.executeUpdate();
			logger.debug("DB: Query executed successfully - " + ps.toString());
		}
		catch (MySQLIntegrityConstraintViolationException e) {
			logger.warn("DB: Stock already exist - Stock Id:" + stock.getId()
					+ " User Name: " + stock.getName() + " - "
					+ e.getMessage());
		} catch (SQLException ex) {
			logger.error("DB: Query failed = " + ps.toString(), ex);
			throw ex;
		} finally {
			if (!ps.isClosed()) {
				ps.close();
			}
			if (!ps.isClosed()) {
				connection.close();
			}
		}
	}

	@Override
	public void updateTotalAndName(long id, int total,
			String name) throws SQLException {
		Connection connection = null;
		PreparedStatement ps = null;
		connection = dbMgr.getConnection();
		try{
			ps = connection.prepareStatement("update stock set total = ?, set name = ? where id = ?");
			ps.setInt(1, total);
			ps.setString(2, name);
			ps.setLong(3, id);
			ps.executeUpdate();
			logger.debug("DB: Query executed successfully - " + ps.toString());
		}
		catch(SQLException ex){
			logger.error("DB: Query failed = " + ps.toString(), ex);
			throw ex;
		}
		finally{
			if(!ps.isClosed()) { ps.close(); }
			if(!connection.isClosed()){ connection.close(); }
		}
	}

	@Override
	public void updateName(long id, String name) throws SQLException {
		Connection connection = null;
		PreparedStatement ps = null;
		connection = dbMgr.getConnection();
		try{
			ps = connection.prepareStatement("update stock set name = ? where id = ?");
			ps.setString(1, name);
			ps.setLong(2, id);
			ps.executeUpdate();
			logger.debug("DB: Query executed successfully - " + ps.toString());
		}
		catch(SQLException ex){
			logger.error("DB: Query failed = " + ps.toString(), ex);
			throw ex;
		}
		finally{
			if(!ps.isClosed()) { ps.close(); }
			if(!connection.isClosed()){ connection.close(); }
		}
	}

	@Override
	public void updateSold(long stock, double sold) throws SQLException {
		Connection connection = null;
		PreparedStatement ps = null;
		connection = dbMgr.getConnection();
		try{
			ps = connection.prepareStatement("update stock set sold = (sold + ?) where id = ?");
			ps.setDouble(1, sold);
			ps.setLong(2, stock);
			ps.executeUpdate();
			logger.debug("DB: Query executed successfully - " + ps.toString());
		}
		catch(SQLException ex){
			logger.error("DB: Query failed = " + ps.toString(), ex);
			throw ex;
		}
		finally{
			if(!ps.isClosed()) { ps.close(); }
			if(!connection.isClosed()){ connection.close(); }
		}
	}
}
