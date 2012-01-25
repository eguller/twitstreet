package com.twitstreet.market;

import java.sql.Date;
import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.twitstreet.cache.TransactionCache;
import com.twitstreet.db.data.Stock;
import com.twitstreet.db.data.TransactionRecord;
import com.twitstreet.db.data.User;
import com.twitstreet.task.AsyncQuery;

@Singleton
public class TransactionMgrImpl implements TransactionMgr {
	@Inject
	TransactionCache transactionRecordCache;
	@Inject
	AsyncQuery asyncQuery;
	/*
	 * @Inject private AsyncQueryTask asyncQueryTask;
	 */

	LinkedList<TransactionRecord> currentTransactions = new LinkedList<TransactionRecord>();

	@Override
	public void recordTransaction(User user, Stock stock, int amount,
			int operation) {
		TransactionRecord transactionRecord = new TransactionRecord();
		transactionRecord.setAmount(amount);
		transactionRecord.setDate(new Date(System.currentTimeMillis()));
		transactionRecord.setStockId(stock.getId());
		transactionRecord.setStockName(stock.getName());
		transactionRecord.setOperation(operation);
		transactionRecord.setUserId(user.getId());
		transactionRecord.setUserName(user.getUserName());
		transactionRecordCache.addTransaction(user.getId(), transactionRecord);
		transactionRecordCache.addCurrentTransaction(transactionRecord);
		asyncQuery.addTransaction(transactionRecord);
	}

	@Override
	public List<TransactionRecord> getCurrentTransactions() {
		return transactionRecordCache.getCurrentTransactions();
	}

	@Override
	public List<TransactionRecord> queryTransactionRecord(long userId) {
		return transactionRecordCache.getUserTransactions(userId);
	}
}
