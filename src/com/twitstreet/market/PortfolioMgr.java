package com.twitstreet.market;

import com.twitstreet.base.Result;

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
	Result<Object> buy(String buyer, int price, String stock, double percent);
}
