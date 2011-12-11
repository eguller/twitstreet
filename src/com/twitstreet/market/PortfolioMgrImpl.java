package com.twitstreet.market;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.log4j.Logger;

import com.google.inject.Inject;
import com.twitstreet.db.base.DBMgr;
import com.twitstreet.db.data.Portfolio;
import com.twitstreet.db.data.User;
import com.twitstreet.db.data.UserStock;
import com.twitstreet.db.data.Stock;
import com.twitstreet.session.UserMgr;

public class PortfolioMgrImpl implements PortfolioMgr {
	private static Logger logger = Logger.getLogger(PortfolioMgrImpl.class);
	@Inject DBMgr dbMgr;
	@Inject private StockMgr stockMgr;
	@Inject private UserMgr userMgr;

	@Override
	public Stock buy(long buyer,long stock, int amount) throws SQLException{
		User user = userMgr.getUserById(buyer);
		int amount2Buy = user.getCash() < amount ? user.getCash() : amount;
		userMgr.updateCash(buyer, amount2Buy);
		Stock stockObj = stockMgr.getStockById(buyer);
		double sold = amount2Buy / stockObj.getTotal();
		stockObj.setSold(stockObj.getSold() + sold);
	    stockMgr.updateSold(stock, sold);
	    UserStock userStock = getStockInPortfolio(buyer, stock);
	    
	    
	    
	    if(userStock == null){
	    	addStock2Portfolio(buyer, stock, sold);
	    }
	    else{
	    	updateStockInPortfolio(buyer, stock, sold);
	    }
	    return null;

	}


	private void updateStockInPortfolio(long buyer, long stock, double sold) {
		
	}


	private void addStock2Portfolio(long buyer, long stock, double sold) {
		
	}


	@Override
	public Portfolio getUserPortfolio(long userId) {
		return null;
	}

	@Override
	public UserStock getStockInPortfolio(long userId, long stockId) throws SQLException{
	    Connection connection = null;
		PreparedStatement ps = null;
		connection = dbMgr.getConnection();
		ps = connection.prepareStatement("select id, percentage from portfolio where user_id = ? and stock = ?");
		ps.executeQuery();
		if(!ps.isClosed()) { ps.close(); }
		if(!connection.isClosed()){ connection.close(); }
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
