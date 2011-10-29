package com.twitstreet.market;


import java.sql.SQLException;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.twitstreet.base.KeyLock;
import com.twitstreet.db.data.Stock;
import com.twitstreet.twitter.TwitterProxy;

@Singleton
public class TransactionMgrImpl implements TransactionMgr {
	enum Error {
		ConcurrentUserTransaction("Concurrent Transaction Error"),
		NoStocksAvailable("All followers sold out.");
		
		
		String error;
		
		private Error(String error){
			this.error = error;
		}
		
		public String toString(){
			return error;
		}
	}

	@Inject
	private StockMgr stockMgr;

	@Inject
	private PortfolioMgr portfolioMgr;
	
	@Inject
	private TwitterProxy twitterProxy;

	@Inject
	private KeyLock<String> stockLock;

	@Inject
	private KeyLock<String> userLock;

	@Override
	public Object buy(final String buyer, final String stock, int amount) throws SQLException {
		Object result = null;
		if (!userLock.tryLock(buyer)) {
			return null;
		}

		stockLock.waitAndlock(stock);
		try {
			Stock stockDO = null; //twitterProxy.getTwUser(stock);
			stockMgr.updateTotal(stockDO.getId(), stockDO.getTotal());
			
			int available = stockDO.getAvailable();
			double percent = amount / stockDO.getTotal() * 100;
			
			
			if(available < 1){
				result = null;
			}
			else if (available < amount){
				result = portfolioMgr.buy(buyer, amount, stock, percent);
			}
			else{
				result = portfolioMgr.buy(buyer, amount, stock, percent);
			}

		} finally {
			stockLock.unlock(stock);
		}
		
		return result;
	}

}
