package com.twitstreet.market;

import java.sql.SQLException;

import com.google.inject.Inject;
import com.twitstreet.db.base.DBMgr;
import com.twitstreet.db.data.Portfolio;
import com.twitstreet.db.data.UserStock;
import com.twitstreet.db.data.Stock;

public class PortfolioMgrImpl implements PortfolioMgr {
	@Inject DBMgr dbMgr;
	@Inject private StockMgr stockMgr;




	@Override
	public Object buy(String buyer, int price, String stock,
			double percent) {
		try {
			Stock stockDO = stockMgr.getStock(stock);
		} catch (SQLException e) {
			// TODO Stock could not be retrieved inform user.
			e.printStackTrace();
		}
		
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
	
}
