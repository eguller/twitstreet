package com.twitstreet.market.api;

import com.twitstreet.base.Result;

/**
 * Manages a cache of stocks and coordinating update from twitter.<br>
 * @author ooktay
 *
 */
public interface StockMgr {
	/**
	 * Updates the follower count for given stock from twitter.
	 * Immediately returns if stock was updated recently (~5min).
	 * Asynchronously writes to DB.
	 * @param stock is the twitter id of stock
	 * @param user is the requesting user (to be used on twitter API).
	 * @return Result with Number of followers of stock.
	 */
	Result<Integer> updateFollowerCount(String stock, String user);
	
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
