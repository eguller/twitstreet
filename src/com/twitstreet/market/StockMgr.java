package com.twitstreet.market;

import java.util.List;

import com.twitstreet.db.data.Stock;

/**
 * Manages a cache of "percent available" of stocks.
 * The "percent available" is not stored on DB, but is derived 
 * from Portfolio DB as (100 - percent stocks sold).
 * @author ooktay
 *
 */
public interface StockMgr {
	
	/**
	 * Updates the percent of sold stocks on cache.
	 * @param stock
	 * @param amount
	 * @return Result with no payload
	 */
	Stock notifyBuy(String stock, double amount);
	
	/**
	 * @param stock - Twitter user screen name
	 * @return - Returns current stock state of twitter user.
	 */
	Stock getStock(String stock);
	
	/**
	 * Returns stock information by given id
	 * @param id - Stock id
	 * @return - Stock
	 */
	public Stock getStockById(long id);
	
	/**
	 * Update total follower count.
	 * Do not call makePersistenUpdate directly,
	 * if there is a sold/buy request
	 * we do not want to sold stock count.
	 * @param stockId - stockId.
     * @param total - total
	 * @return 
	 */
	public void updateTwitterData(long stockId, int total, String pictureUrl, String screenName);
	
	/**
	 * Returns stock sold percentage by name
	 * @param stockName
	 * @return
	 */

	public void saveStock(Stock stock);

	public List<Stock> getUpdateRequiredStocks();
	
	/**
	 * Update stock history table if the daily update has
	 * not been performed. 
	 * @return 
	 */
	public void updateStockHistory();
	
}
