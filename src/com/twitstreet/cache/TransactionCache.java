package com.twitstreet.cache;

import java.util.List;

import com.twitstreet.db.data.TransactionRecord;




public interface TransactionCache extends Cache{
	public List<TransactionRecord> getUserTransactions(long userId);
	public List<TransactionRecord> getCurrentTransactions();
	public void addTransaction(long userId, TransactionRecord transactionRecord);
	public void addCurrentTransaction(TransactionRecord transactionRecord);
}
