package com.twitstreet.market;

import java.sql.SQLException;
import java.util.List;

import com.twitstreet.db.data.Portfolio;
import com.twitstreet.db.data.Stock;
import com.twitstreet.db.data.UserStock;
import com.twitstreet.db.data.UserStockDetail;
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
	 * @param userId - twitter id of Buyer
	 * @param stock - twitter id of stock.
	 * @param amount - Amount bought
	 * @return
	 */
	public BuySellResponse buy(long userId,long stock, int amount) throws SQLException;
	
	/**
	 * @param userId is the twitter id of Seller
	 * @param stock 
	 * @param amount
	 * @return
	 * @throws SQLException
	 */
	public BuySellResponse sell(long userId, long stock, int amount) throws SQLException;
	
	/**
	 * Returns user portfolio
	 * @param userId - User id
	 * @return
	 */
	public Portfolio getUserPortfolio(long userId);
	
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
	
	public void deleteStockInPortfolio(long seller, long stock) throws SQLException;
	
	public List<UserStockDetail> getStockDistribution(long stock);

	public void rerank();
	
}
