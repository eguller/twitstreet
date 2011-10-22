package com.twitstreet.market;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.log4j.Logger;


import com.google.inject.Inject;
import com.twitstreet.db.base.DBMgr;
import com.twitstreet.db.data.StockDO;

public class StockMgrImpl implements StockMgr {
	@Inject
	private DBMgr dbMgr;
	private static Logger logger = Logger.getLogger(StockMgrImpl.class);

	public StockDO notifyBuy(String stock, double amount) {
		return null;
	}

	public StockDO getStock(String name) throws SQLException {
		Connection connection = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		StockDO stockDO = null;
		connection = dbMgr.getConnection();
		ps = connection.prepareStatement("select id, name, total, sold from stock where id = ?");
		ps.setString(0, name);
		rs = ps.executeQuery();
		while(rs.next()){
			stockDO = new StockDO();
			stockDO.setId(rs.getLong("id"));
			stockDO.setName(rs.getString("name"));
			stockDO.setTotal(rs.getInt("total"));
			stockDO.setSold(rs.getDouble("sold"));
			break;
		}
		if(!rs.isClosed()) { rs.close(); }
		if(!ps.isClosed()) { ps.close(); }
		if(!connection.isClosed()){ connection.close(); }
		return stockDO;
	}

	public StockDO getStockById(long id) throws SQLException {
		Connection connection = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		StockDO stockDO = null;
		connection = dbMgr.getConnection();
		ps = connection.prepareStatement("select id, name, total, sold from stock where id = ?");
		ps.setLong(0, id);
		rs = ps.executeQuery();
		while(rs.next()){
			stockDO = new StockDO();
			stockDO.setId(rs.getLong("id"));
			stockDO.setName(rs.getString("name"));
			stockDO.setTotal(rs.getInt("total"));
			stockDO.setSold(rs.getDouble("sold"));
			break;
		}
		if(!rs.isClosed()) { rs.close(); }
		if(!ps.isClosed()) { ps.close(); }
		if(!connection.isClosed()){ connection.close(); }
		return stockDO;
	}

	public void updateTotal(long id, int total) throws SQLException {
		Connection connection = null;
		PreparedStatement ps = null;
		connection = dbMgr.getConnection();
		ps = connection.prepareStatement("update stock set total = ? where id = ?");
		ps.setInt(0, total);
		ps.setLong(1, id);
		ps.executeUpdate();
		if(!ps.isClosed()) { ps.close(); }
		if(!connection.isClosed()){ connection.close(); }
	}

	@Override
	public double getPercentSold(String stockName) {
		return 0.0;
	}

	@Override
	public void saveStock(StockDO stock) throws SQLException {
		Connection connection = null;
		PreparedStatement ps = null;
		connection = dbMgr.getConnection();
		ps = connection.prepareStatement("insert into stock(name, total, sold) values(?, ?, ?)");
		ps.setString(0, stock.getName());
		ps.setInt(1, stock.getTotal());
		ps.setDouble(3, stock.getSold());
		ps.executeUpdate();
		if(!ps.isClosed()) { ps.close(); }
		if(!connection.isClosed()){ connection.close(); }
	}
}
