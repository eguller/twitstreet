/**
	TwitStreet - Twitter Stock Market Game
    Copyright (C) 2012  Engin Guller (bisanth@gmail.com), Cagdas Ozek (cagdasozek@gmail.com)

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
**/

package com.twitstreet.market;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.twitstreet.db.data.Stock;
import com.twitstreet.db.data.StockHistoryData;
import com.twitstreet.db.data.TrendyStock;

/**
 * Manages a cache of "percent available" of stocks.
 * The "percent available" is not stored on DB, but is derived 
 * from Portfolio DB as (100 - percent stocks sold).
 * @author ooktay
 *
 */
public interface StockMgr {
	public static int MAX_TRENDS_PER_PAGE = 6;
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
	
	public StockHistoryData getStockHistory(long id, Date since);

	ArrayList<Stock> getSuggestedStocks();
	int getSuggestedStockCount();

	void updateTwitterTrends();

	void resetSpeedOfOldStocks();

	boolean updateStockData(long id);

	StockHistoryData getStockHistory(long id);
	StockHistoryData getStockHistory(long id, int forMinutes);

	List<TrendyStock> getTopGrossedStocks(int forhours);

	public List<Stock> getUpdateRequiredStocksByServer();
	public void resetSpeedOfOldStocksByServer();

	void updateStockListData(ArrayList<Long> id);

	List<Stock> getUpdateRequiredStocks(int limit);

	ArrayList<Stock> getTopGrossingStocks();

	ArrayList<Stock> getTopGrossingStocks(int offset, int count);


	ArrayList<Stock> getSuggestedStocks(int offset, int count);

	void loadSuggestedStocks();
	
	public boolean addStockIntoAnnouncement(long stockid);
	public void removeOldRecords(int removeOlderThanMinutes);
	public void removeOldRecordsByServer(int removeOlderThanMinutes);

	public void saveTrend(long id);
	
	public void truncateStockHistory();
}
