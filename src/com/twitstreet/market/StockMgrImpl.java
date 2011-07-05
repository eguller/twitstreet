package com.twitstreet.market;

import com.google.inject.Inject;
import com.twitstreet.base.Result;
import com.twitstreet.twitter.TwitterProxy;

public class StockMgrImpl implements StockMgr {
	@Inject TwitterProxy twitterProxy = null;
	@Override
	public Result<Double> getPercentSold(String stock) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Result<Object> notifyBuy(String stock, double amount) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Result<Object> getStock(String stock) {
		int stockCount = twitterProxy.getStockCount(stock);
		return null;
	}
}
