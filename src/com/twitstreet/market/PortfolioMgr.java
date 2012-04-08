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

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.twitstreet.db.data.Portfolio;
import com.twitstreet.db.data.Stock;
import com.twitstreet.db.data.User;
import com.twitstreet.db.data.UserStock;
import com.twitstreet.db.data.UserStockDetail;
import com.twitstreet.main.TwitstreetException;

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
	 * @throws TwitstreetException 
	 */
	public boolean buy(User buyer,Stock stock, int amount) throws TwitstreetException;
	
	/**
	 * @param userId is the twitter id of Seller
	 * @param stock 
	 * @param amount
	 * @return
	 * @throws SQLException
	 */
	public boolean sell(User user, Stock stock, int amount);
	
	/**
	 * Returns user portfolio
	 * @param userId - User id
	 * @return
	 */
	public Portfolio getUserPortfolio(User user);
	
	/**
	 *  
	 * @param userId - User Id
	 * @param stockId - Stock Id
	 * @return
	 */
	public double getStockSoldPercentage(long userId, long stockId);
	
	
	/**
	 * Returns user stock state in portfolio
	 * @param buyer
	 * @param stock
	 * @return
	 */
	public UserStock getStockInPortfolio(long buyer, long stock);
	
	public void deleteStockInPortfolio(long seller, long stock);
	
	public List<UserStockDetail> getStockDistribution(long stock);

	ArrayList<Stock> getUserWatchList(long userid);

	void addStockIntoUserWatchList(long userid, long stockid) throws TwitstreetException;

	void removeStockFromUserWatchList(long stockid, long userid);

	
}
