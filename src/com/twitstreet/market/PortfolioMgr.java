package com.twitstreet.market;

import com.twitstreet.db.data.Portfolio;
import com.twitstreet.db.data.UserStockDO;

/**
 * Maintains a cache of stock portfolio and money of Users.
 * Handles buy&sell operations from user perspective, and updates DB;
 * e.g. Checks that buyer has enough money to buy the stock.
 * @author ooktay
 *
 */
public interface PortfolioMgr {
	/**
	 * Checks that buyer has enough enough money and updates buyer's portfolio
	 * adding stocks.
	 * @param buyer is the twitter id of Buyer
	 * @param price is the total money to be paid.
	 * @param stock is the twitter id of stock.
	 * @param percent is the ratio of bought followers to the total followers.
	 * @return
	 */
	public Object buy(String buyer, int price, String stock, double percent);
	
	/**
	 * Returns user portfolio
	 * @param userId - User id
	 * @return
	 */
	public Portfolio getUserPortfolio(long userId);
	
	/**
	 * Returns user stock state in portfolio
	 * @param userId - User Id
	 * @param stockId - Stock Id
	 * @return
	 */
	public UserStockDO getStockInPortfolio(long userId, long stockId);
	
	/**
	 * Returns user portfolio
	 * @param user
	 * @return
	 */
	public Portfolio getUserPortfolio(String user);
	
	/**
	 * Returns user stock state in portfolio
	 * @param buyer
	 * @param stock
	 * @return
	 */
	public UserStockDO getStockInPortfolio(String buyer, String stock);
}
