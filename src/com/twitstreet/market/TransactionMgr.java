package com.twitstreet.market;

import com.twitstreet.base.Result;

/**
 * Main facade for stock transactions buy&sell.<br>
 * Concurrent operations of the same user are not permitted,
 * they will return failed Results with a appropriate error.<br>
 * Concurrent operations on a stock will be done sequentially.<br>
 * TODO: throttling
 * 
 * @author ooktay
 * 
 */
public interface TransactionMgr {
	/**
	 * 1. Lock the user, so that concurrent operations for same user FAIL!<br>
	 * 2. Lock the stock, so that other buy/sell operations for that stock wait.<br>
	 * @param buyer is the twitter id of Buyer
	 * @param stock is the twitter id of Stock
	 * @param amount is the amount of followers requested
	 * @return Result with no payload
	 */
	Result<Object> buy(String buyer, String stock, int amount);
}
