package com.twitstreet.db.mgr;

import com.twitstreet.db.data.StockDO;

public interface StockMgr extends IGenericMgr<StockDO>{
	public StockDO getStock(String name);
	/**
	 * Update total follower count.
	 * Do not call makePersistenUpdate directly,
	 * if there is a sold/buy request
	 * we do not want to sold stock count.
	 * @param stockDO - stock object with updated <code>total</code>. 
	 */
	public void updateTotal(StockDO stockDO);
}
