package com.twitstreet.market;

import java.util.List;

import com.twitstreet.db.data.Stock;
import com.twitstreet.db.data.TransactionRecord;
import com.twitstreet.db.data.User;

public interface TransactionMgr {
	public static final int BUY = 1;
	public static final int SELL = 0;
	public void recordTransaction(User user, Stock stock, int amount, int operation);
	public List<TransactionRecord> getCurrentTransactions();
	public List<TransactionRecord> queryTransactionRecord(long userId);
	
}
