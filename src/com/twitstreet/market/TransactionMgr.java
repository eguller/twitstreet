package com.twitstreet.market;

import java.util.ArrayList;
import java.util.LinkedList;

import com.twitstreet.db.data.Stock;
import com.twitstreet.db.data.TransactionRecord;
import com.twitstreet.db.data.User;

public interface TransactionMgr {
	public static final int BUY = 1;
	public static final int SELL = 0;
	
	public static final int USER_TRANSACTIONS = 10;
	
	public static final int CURRENT_TRANSACTIONS = 10;

	public void recordTransaction(User user, Stock stock, int amount, int operation);
	
	public ArrayList<TransactionRecord> queryTransactionRecord(User user);
	
	public LinkedList<TransactionRecord> getCurrentTransactions();
}
