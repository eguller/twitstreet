package com.twitstreet.market;

import com.twitstreet.base.Result;

/**
 * Manages a cache of "percent available" of stocks.
 * The "percent available" is not stored on DB, but is derived 
 * from Portfolio DB as (100 - percent stocks sold).
 * @author ooktay
 *
 */
public interface StockMgr {
	/**
	 * Returns the percent of sold stocks. May query the sum from DB.
	 * @param stock is the twitter id of stock
	 * @return Result with percent sold for the stock.
	 */
	Result<Double> getPercentSold(String stock);
	
	/**
	 * Updates the percent of sold stocks on cache.
	 * @param stock
	 * @param amount
	 * @return Result with no payload
	 */
	Result<Object> notifyBuy(String stock, double amount);
}
