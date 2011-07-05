package com.twitstreet.twitter;

import com.twitstreet.db.data.StockDO;

/**
 * This interface is not thread safe.
 * Don't inject it to Singletons directly (use a Provider).
 */
public interface TwitterProxy {

	public int getStockCount(String name);
	public StockDO getStock(String name);

}
