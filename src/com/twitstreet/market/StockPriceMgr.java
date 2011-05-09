package com.twitstreet.market;

import com.twitstreet.base.Result;

/**
 * Manages a cache of "price (follower count)" of stocks.
 * Coordinates updates from twitter API and writes price to DB.<br>
 * @author ooktay
 *
 */
public interface StockPriceMgr {
	/**
	 * Updates the follower count for given stock from twitter.
	 * Immediately returns if stock was updated recently (~5min).
	 * Asynchronously writes to DB.
	 * @param stock is the twitter id of stock
	 * @param user is the requesting user (to be used on twitter API).
	 * @return Result with Number of followers of stock.
	 */
	Result<Integer> updateFollowerCount(String stock, String user);

}
