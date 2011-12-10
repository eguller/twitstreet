package com.twitstreet.market;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.log4j.Logger;

import com.google.inject.Inject;
import com.twitstreet.db.base.DBMgr;
import com.twitstreet.db.data.Portfolio;
import com.twitstreet.db.data.UserStock;
import com.twitstreet.db.data.Stock;

public class PortfolioMgrImpl implements PortfolioMgr {
	private static Logger logger = Logger.getLogger(PortfolioMgrImpl.class);
	@Inject DBMgr dbMgr;
	@Inject private StockMgr stockMgr;




	@Override
	public Object buy(long buyer,int stock, int amount) {
		return null;
	}

	@Override
	public Portfolio getUserPortfolio(long userId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public UserStock getStockInPortfolio(long userId, long stockId) {
		return null;
	}

	@Override
	public Portfolio getUserPortfolio(String user) {
		return null;
	}

	@Override
	public UserStock getStockInPortfolio(String buyer, String stock) {
		return null;
	}

	@Override
	public double getStockSoldPercentage(long userId, long stockId) throws SQLException{
		Connection connection = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		connection = dbMgr.getConnection();
		ps = connection.prepareStatement("select percentage from portfolio where user_id = ? and stock = ?");
		ps.setLong(1, userId);
		ps.setLong(2, stockId);
		try{
			rs = ps.executeQuery();
			if(rs.next()){
				return rs.getDouble("percentange");

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
		return 0.0;
	}
	
}
