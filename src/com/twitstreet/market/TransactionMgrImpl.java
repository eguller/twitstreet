package com.twitstreet.market;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.apache.log4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.twitstreet.db.base.DBMgr;
import com.twitstreet.db.data.Stock;
import com.twitstreet.db.data.TransactionRecord;
import com.twitstreet.db.data.User;

@Singleton
public class TransactionMgrImpl implements TransactionMgr {
	@Inject
	private DBMgr dbMgr;
	private static Logger logger = Logger.getLogger(StockMgrImpl.class);
	LinkedList<TransactionRecord> currentTransactions = new LinkedList<TransactionRecord>();

	@Override
	public void recordTransaction(User user, Stock stock, double amount,
			int operation) {
		Connection connection = null;
		PreparedStatement ps = null;
		ResultSet generatedKeys = null;
		long currentDate = Calendar.getInstance().getTimeInMillis();
		try {
			connection = dbMgr.getConnection();
			ps = connection
					.prepareStatement(
							"insert into transactions(user_id,stock, amount, t_action,t_date) values(?, ?, ?, ?, ?)",
							Statement.RETURN_GENERATED_KEYS);
			ps.setLong(1, user.getId());
			ps.setLong(2, stock.getId());
			ps.setDouble(3, amount);
			ps.setInt(4, operation);
			ps.setTimestamp(5, new Timestamp(currentDate));
			int affectedRows = ps.executeUpdate();
			if (affectedRows > 0) {
				generatedKeys = ps.getGeneratedKeys();
				if (generatedKeys.next()) {

					/***/
					TransactionRecord transactionRecord = new TransactionRecord();
					transactionRecord.setId(generatedKeys.getLong(1));
					transactionRecord.setAmount(amount);
					transactionRecord.setDate(new Date(currentDate));
					transactionRecord.setStockId(stock.getId());
					transactionRecord.setStockName(stock.getName());
					transactionRecord.setTransactionAction(operation);
					transactionRecord.setUserId(user.getId());
					transactionRecord.setUserName(user.getUserName());

					addTransactionRecord(transactionRecord);

					/***/

					logger.debug("DB: Query executed successfully - "
							+ ps.toString());
				} else {
					logger.error("DB: Transaction record insert failed no generated keys affected - "
							+ ps.toString());
				}

			} else {
				logger.error("DB: Transaction record insert failed no rows affected - "
						+ ps.toString());
			}

		} catch (SQLException e) {
			logger.error("DB: Query failed - " + ps.toString(), e);
		} finally {
			try {
				if (generatedKeys != null && !generatedKeys.isClosed()) {
					generatedKeys.close();
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

	@Override
	public ArrayList<TransactionRecord> queryTransactionRecord(long userId) {
		ArrayList<TransactionRecord> transactionRecordList = new ArrayList<TransactionRecord>();
		Connection connection = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			connection = dbMgr.getConnection();
			ps = connection
					.prepareStatement("select users.id as user_id, users.userName as userName, transactions.id as transaction_id, transactions.amount as amount, transactions.t_action as t_action, stock.name as stock, stock.id as stock_id from users, transactions, stock where stock.id = transactions.stock and transactions.user_id = users.id and users.id = ? order by t_date desc limit ?");

			ps.setLong(1, userId);
			ps.setInt(2, TransactionMgr.CURRENT_TRANSACTIONS);
			rs = ps.executeQuery();
			while (rs.next()) {
				TransactionRecord transactionRecord = new TransactionRecord();
				transactionRecord.setUserId(rs.getLong("user_id"));
				transactionRecord.setUserName(rs.getString("userName"));
				transactionRecord.setId(rs.getLong("transaction_id"));
				transactionRecord.setAmount(rs.getInt("amount"));
				transactionRecord.setTransactionAction(rs.getInt("t_action"));
				transactionRecord.setStockName(rs.getString("stock"));
				transactionRecord.setStockId(rs.getLong("stock_id"));
				transactionRecordList.add(transactionRecord);
			}
			logger.debug("DB: Query executed successfully - " + ps.toString());

		} catch (SQLException e) {
			logger.error("DB: Query failed - " + ps.toString(), e);
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
		return transactionRecordList;
	}

	public synchronized void addTransactionRecord(
			TransactionRecord transactionRecord) {
		if (transactionRecord != null) {
			currentTransactions.addFirst(transactionRecord);
			if (currentTransactions.size() > TransactionMgr.CURRENT_TRANSACTIONS) {
				currentTransactions.pollLast();
			}
		}
	}

	@Override
	public LinkedList<TransactionRecord> getCurrentTransactions() {
		return currentTransactions;
	}
}
