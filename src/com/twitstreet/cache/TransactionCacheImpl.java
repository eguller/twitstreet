package com.twitstreet.cache;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.log4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.twitstreet.db.base.DBMgr;
import com.twitstreet.db.data.TransactionRecord;

@Singleton
public class TransactionCacheImpl implements TransactionCache{
	@Inject DBMgr dbMgr;
	private static final int TRANSACTION_PER_USER = 10;
	private static final int CURRENT_TRANSACTIONS = 10;
	private static final List<TransactionRecord> EMPTY_LIST = Collections.<TransactionRecord>emptyList();
	private static final List<TransactionRecord> currentTransactionsList = Collections.synchronizedList(new ArrayList<TransactionRecord>(CURRENT_TRANSACTIONS)); 
	ConcurrentHashMap<Long, List<TransactionRecord>> transactionRecordCache = new ConcurrentHashMap<Long, List<TransactionRecord>>();
	private static Logger logger = Logger.getLogger(TransactionCacheImpl.class);
	
	@Override
	public List<TransactionRecord> getUserTransactions(long userId){
		List<TransactionRecord> transactionRecordList = transactionRecordCache.get(userId);
		return transactionRecordList == null ? EMPTY_LIST : transactionRecordList;
	}
	
	@Override
	public void addTransaction(long userId, TransactionRecord transactionRecord){
		List<TransactionRecord> transactionRecordList = transactionRecordCache.get(userId);
		if(transactionRecordList == null){
			transactionRecordList = Collections.synchronizedList(new ArrayList<TransactionRecord>(TRANSACTION_PER_USER));
			transactionRecordList.add(transactionRecord);
			transactionRecordCache.put(userId, transactionRecordList);
		}
		else{
			transactionRecordList.add(0, transactionRecord);
			if(transactionRecordList.size() > TRANSACTION_PER_USER) {
				transactionRecordList.remove(TRANSACTION_PER_USER);
			}
		}
		
	}
	
	public void addCurrentTransaction(TransactionRecord transactionRecord){
		currentTransactionsList.add(0, transactionRecord);
		if(currentTransactionsList.size() > CURRENT_TRANSACTIONS){
			currentTransactionsList.remove(CURRENT_TRANSACTIONS);
		}
	}

	@Override
	public List<TransactionRecord> getCurrentTransactions() {
		return currentTransactionsList;
	}

	@Override
	public void load() {
		Connection connection = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			connection = dbMgr.getConnection();
			ps = connection
					.prepareStatement("select distinct user_id from transactions");

			rs = ps.executeQuery();
			while (rs.next()) {
				long userId = rs.getLong(1);
				PreparedStatement psTransaction = connection.prepareStatement("select transactions.amount as amount, transactions.t_date as t_date, transactions.t_action as t_action, transactions.stock as stock, transactions.user_id as user_id, users.userName as userName, stock.name as stockName from transactions, users, stock where transactions.user_id = ? and transactions.stock = stock.id and transactions.user_id = users.id order by t_date desc limit ?");
				psTransaction.setLong(1, userId);
				psTransaction.setInt(2, TRANSACTION_PER_USER);
				ResultSet rsTransaction = psTransaction.executeQuery();
				ArrayList<TransactionRecord> tempTransactionRecordsList = new ArrayList<TransactionRecord>(TRANSACTION_PER_USER);
				while(rsTransaction.next()){
					TransactionRecord transactionRecord = new TransactionRecord();
					transactionRecord.setAmount(rsTransaction.getInt("amount"));
					transactionRecord.setDate(rsTransaction.getDate("t_date"));
					transactionRecord.setOperation(rsTransaction.getInt("t_action"));
					transactionRecord.setStockId(rsTransaction.getLong("stock"));
					transactionRecord.setUserId(rsTransaction.getLong("user_id"));
					transactionRecord.setUserName(rsTransaction.getString("userName"));
					transactionRecord.setStockName(rsTransaction.getString("stockName"));
					tempTransactionRecordsList.add(transactionRecord);
					
				}
				rsTransaction.close();
				psTransaction.close();
				
				Collections.reverse(tempTransactionRecordsList);
				for(TransactionRecord transactionRecord : tempTransactionRecordsList){
					addTransaction(userId, transactionRecord);
				}

				tempTransactionRecordsList = new ArrayList<TransactionRecord>(CURRENT_TRANSACTIONS);
				psTransaction = connection.prepareStatement("select transactions.amount as amount, transactions.t_date as t_date, transactions.t_action as t_action, transactions.stock as stock, transactions.user_id as user_id, users.userName as userName, stock.name as stockName from transactions, users, stock where transactions.stock = stock.id and transactions.user_id = users.id order by t_date desc limit ?");
				psTransaction.setInt(1, CURRENT_TRANSACTIONS);
				rsTransaction = psTransaction.executeQuery(); 
				
				while(rsTransaction.next()){
					TransactionRecord transactionRecord = new TransactionRecord();
					transactionRecord.setAmount(rsTransaction.getInt("amount"));
					transactionRecord.setDate(rsTransaction.getDate("t_date"));
					transactionRecord.setOperation(rsTransaction.getInt("t_action"));
					transactionRecord.setStockId(rsTransaction.getLong("stock"));
					transactionRecord.setUserId(rsTransaction.getLong("user_id"));
					transactionRecord.setUserName(rsTransaction.getString("userName"));
					transactionRecord.setStockName(rsTransaction.getString("stockName"));
					tempTransactionRecordsList.add(transactionRecord);
					
				}
				
				rsTransaction.close();
				psTransaction.close();

				Collections.reverse(tempTransactionRecordsList);
				for(TransactionRecord transactionRecord : tempTransactionRecordsList){
					addCurrentTransaction(transactionRecord);
				}
				
			}
			logger.debug("DB: Query executed successfully - " + ps.toString());
		} catch (SQLException ex) {
			logger.error("DB: Query failed - " + ps.toString(), ex);
		} finally {
			try {
				if (rs != null && !rs.isClosed()) {
					rs.close();
				}
				if (ps != null && !ps.isClosed()) {
					ps.close();
				}
				if (connection != null && !connection.isClosed()) {
					connection.close();
				}
			} catch (SQLException e) {
				logger.error("DB: Resources could not be closed properly", e);
			}
		}
	}
}
