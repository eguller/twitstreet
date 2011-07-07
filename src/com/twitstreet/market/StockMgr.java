package com.twitstreet.market;

import com.twitstreet.base.IGenericMgr;
import com.twitstreet.base.Result;
import com.twitstreet.db.data.StockDO;

/**
 * Manages a cache of "percent available" of stocks.
 * The "percent available" is not stored on DB, but is derived 
 * from Portfolio DB as (100 - percent stocks sold).
 * @author ooktay
 *
 */
public interface StockMgr extends IGenericMgr<StockDO>{
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
	Result<StockDO> notifyBuy(String stock, double amount);
	
	/**
	 * @param stock - Twitter user screen name
	 * @return - Returns current stock state of twitter user.
	 */
	Result<StockDO> getStock(String stock);
	
	/**
	 * Update total follower count.
	 * Do not call makePersistenUpdate directly,
	 * if there is a sold/buy request
	 * we do not want to sold stock count.
	 * @param stockId - stockId.
     * @param total - total
	 */
	public void updateTotal(long stockId, int total);
}
