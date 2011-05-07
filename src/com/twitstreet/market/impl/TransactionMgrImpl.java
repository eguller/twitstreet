package com.twitstreet.market.impl;

import static com.twitstreet.base.Result.*;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.twitstreet.base.KeyLock;
import com.twitstreet.base.Result;
import com.twitstreet.market.api.PortfolioMgr;
import com.twitstreet.market.api.StockMgr;
import com.twitstreet.market.api.StockPriceMgr;
import com.twitstreet.market.api.TransactionMgr;

@Singleton
public class TransactionMgrImpl implements TransactionMgr {
	enum Error {
		ConcurrentUserTransaction;
	}

	@Inject
	private StockPriceMgr stockPriceMgr;

	@Inject
	private StockMgr stockMgr;

	@Inject
	private PortfolioMgr portfolioMgr;

	@Inject
	private KeyLock<String> stockLock;

	@Inject
	private KeyLock<String> userLock;

	@Override
	public Result<Object> buy(final String buyer, final String stock, int amount) {
		if (!userLock.tryLock(buyer)) {
			return fail(Error.ConcurrentUserTransaction);
		}

		stockLock.waitAndlock(stock);
		try {
			Result<Integer> follower = stockPriceMgr.updateFollowerCount(stock, buyer);
			if (follower.isFailed()) {
				return fail(follower);
			}

			Result<Double> sold = stockMgr.getPercentSold(stock);
			if (sold.isFailed()) {
				return fail(sold);
			}

			double percent = 100.0 * amount / follower.getPayload();
			
			//TODO verify percent <= 100 - sold
			
			Result<Object> result = portfolioMgr.buy(buyer, amount, stock, percent);

			return result;

		} finally {
			stockLock.unlock(stock);
		}
	}

}
