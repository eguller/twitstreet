package com.twitstreet.twitter;

import com.twitstreet.db.data.StockDO;

public interface TwitterProxy {
	public int getStockCount(String name);
	public StockDO getStock(String name);
}
