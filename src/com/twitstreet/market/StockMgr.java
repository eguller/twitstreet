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
	 * Returns stock information by given id
	 * @param id - Stock id
	 * @return - Stock
	 */
	public Result<StockDO> getStockById(long id);
	
	/**
	 * Update total follower count.
	 * Do not call makePersistenUpdate directly,
	 * if there is a sold/buy request
	 * we do not want to sold stock count.
	 * @param stockId - stockId.
     * @param total - total
	 */
	public StockDO updateTotal(long stockId, int total);
	
	/**
	 * Returns stock sold percentage by name
	 * @param stockName
	 * @return
	 */
	public Result<Double> getPercentSold(String stockName);
}
