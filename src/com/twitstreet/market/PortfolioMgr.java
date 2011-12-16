package com.twitstreet.market;

import java.sql.SQLException;

import com.twitstreet.db.data.Portfolio;
import com.twitstreet.db.data.Stock;
import com.twitstreet.db.data.UserStock;
import com.twitstreet.servlet.BuySellResponse;

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
	public BuySellResponse buy(long buyer,long stock, int amount) throws SQLException;
	
	/**
	 * Returns user portfolio
	 * @param userId - User id
	 * @return
	 */
	public Portfolio getUserPortfolio(long userId) throws SQLException;
	
	/**
	 *  
	 * @param userId - User Id
	 * @param stockId - Stock Id
	 * @return
	 */
	public double getStockSoldPercentage(long userId, long stockId) throws SQLException;
	
	
	/**
	 * Returns user stock state in portfolio
	 * @param buyer
	 * @param stock
	 * @return
	 */
	public UserStock getStockInPortfolio(long buyer, long stock) throws SQLException;
	
}
