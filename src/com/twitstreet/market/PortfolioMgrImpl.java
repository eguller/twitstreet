package com.twitstreet.market;

import com.google.inject.Inject;
import com.twitstreet.base.Result;
import com.twitstreet.db.base.HibernateConnection;
import com.twitstreet.db.base.IConnection;
import com.twitstreet.db.data.Portfolio;
import com.twitstreet.db.data.UserStockDO;
import com.twitstreet.db.data.StockDO;

public class PortfolioMgrImpl implements PortfolioMgr {
	@Inject private HibernateConnection connection;
	@Inject private StockMgr stockMgr;
	@Override
	public void setConnection(IConnection connection) {
		this.connection = (HibernateConnection) connection;
	}

	@Override
	public IConnection getConnection() {
		return connection;
	}

	@Override
	public Result<UserStockDO> makePersistent(UserStockDO var) {
		return Result.fail(new RuntimeException("Method not supported"));
	}

	@Override
	public Result<UserStockDO> makePersistentUpdate(UserStockDO var) {
		return Result.fail(new RuntimeException("Method not supported"));
	}

	@Override
	public Result<UserStockDO> makeTransient(UserStockDO var) {
		return Result.fail(new RuntimeException("Method not supported"));
	}

	@Override
	public Result<Object> buy(String buyer, int price, String stock,
			double percent) {
		StockDO stockDO = stockMgr.getStock(stock).getPayload();
		
		return null;
	}

	@Override
	public Result<Portfolio> getUserPortfolio(long userId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Result<UserStockDO> getStockInPortfolio(long userId, long stockId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Result<Portfolio> getUserPortfolio(String user) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Result<UserStockDO> getStockInPortfolio(String buyer, String stock) {
		// TODO Auto-generated method stub
		return null;
	}
	
}
