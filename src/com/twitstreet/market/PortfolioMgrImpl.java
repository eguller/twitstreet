package com.twitstreet.market;

import com.google.inject.Inject;
import com.twitstreet.base.Result;
import com.twitstreet.db.base.HibernateConnection;
import com.twitstreet.db.base.IConnection;
import com.twitstreet.db.data.PortfolioDO;

public class PortfolioMgrImpl implements PortfolioMgr {
	@Inject private HibernateConnection connection;

	@Override
	public void setConnection(IConnection connection) {
		this.connection = (HibernateConnection) connection;
	}

	@Override
	public IConnection getConnection() {
		return connection;
	}

	@Override
	public Result<PortfolioDO> makePersistent(PortfolioDO var) {
		return Result.fail(new RuntimeException("Method not supported"));
	}

	@Override
	public Result<PortfolioDO> makePersistentUpdate(PortfolioDO var) {
		return Result.fail(new RuntimeException("Method not supported"));
	}

	@Override
	public Result<PortfolioDO> makeTransient(PortfolioDO var) {
		return Result.fail(new RuntimeException("Method not supported"));
	}

	@Override
	public Result<Object> buy(String buyer, int price, String stock,
			double percent) {
		// TODO Auto-generated method stub
		return null;
	}
}
