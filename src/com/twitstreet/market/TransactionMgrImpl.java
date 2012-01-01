package com.twitstreet.market;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedList;
import org.apache.log4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.twitstreet.db.base.DBMgr;
import com.twitstreet.db.data.TransactionRecord;
import com.twitstreet.db.data.User;

@Singleton
public class TransactionMgrImpl implements TransactionMgr {
	@Inject
	private DBMgr dbMgr;
	private static Logger logger = Logger.getLogger(StockMgrImpl.class);
	LinkedList<TransactionRecord> currentTransactions = new LinkedList<TransactionRecord>();

	@Override
	public void recordTransaction(User user, long stockId, int amount,
			int operation) {
		Connection connection = null;
		PreparedStatement ps = null;
		ResultSet generatedKeys = null;
		long currentDate = Calendar.getInstance().getTimeInMillis();
		try {
			connection = dbMgr.getConnection();
			ps = connection
					.prepareStatement(
							"insert into transactions(user_id,stock,t_action) values(?, ?, ?, ?)",
							Statement.RETURN_GENERATED_KEYS);
			ps.setLong(1, user.getId());
			ps.setLong(2, stockId);
			ps.setInt(3, operation);
			ps.setDate(4, new Date(currentDate));
			int affectedRows = ps.executeUpdate();
			if (affectedRows > 0) {
				generatedKeys = ps.getGeneratedKeys();
				if (generatedKeys.next()) {

					/***/
					TransactionRecord transactionRecord = new TransactionRecord();
					transactionRecord.setId(generatedKeys.getLong(1));
					transactionRecord.setAmount(amount);
					transactionRecord.setDate(new Date(currentDate));
					transactionRecord.setStockId(stockId);
					transactionRecord.setTransactionAction(operation);
					transactionRecord.setUserId(user.getId());
					transactionRecord.setUserName(user.getUserName());

					synchronized (transactionRecord) {
						currentTransactions.add(transactionRecord);
						if (currentTransactions.size() > TransactionMgr.CURRENT_TRANSACTIONS) {
							currentTransactions.poll();
						}
					}

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
				if (!generatedKeys.isClosed()) {
					generatedKeys.close();
				}
				if (!ps.isClosed()) {
					ps.close();
				}

				if (!connection.isClosed()) {
					connection.close();
				}
			} catch (SQLException e) {
				logger.error("DB: Resources could not be closed properly", e);
			}
		}
	}

	@Override
	public ArrayList<TransactionRecord> queryTransactionRecord(User user) {
		ArrayList<TransactionRecord> transactionRecordList = new ArrayList<TransactionRecord>();
		Connection connection = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			connection = dbMgr.getConnection();
			ps = connection
					.prepareStatement("select users.id as user_id, users.userName as userName, transactions.id as transaction_id, transactions.amount as amount, transactions.t_action as t_action from users, transactions where users.id = ? limit ?");

			ps.setLong(1, user.getId());
			ps.setInt(2, TransactionMgr.CURRENT_TRANSACTIONS);
			rs = ps.executeQuery();
			while(rs.next()){
				TransactionRecord transactionRecord = new TransactionRecord();
				transactionRecord.setUserId(rs.getLong("user_id"));
				transactionRecord.setUserName(rs.getString("user_name"));
				transactionRecord.setId(rs.getLong("transaction_id"));
				transactionRecord.setAmount(rs.getInt("amount"));
				transactionRecord.setTransactionAction(rs.getInt("t_action"));
				transactionRecordList.add(transactionRecord);
			}
			logger.debug("DB: Query executed successfully - " + ps.toString());

		} catch (SQLException e) {
			logger.error("DB: Query failed - " + ps.toString(), e);
		} finally {
			try {
				if (!rs.isClosed()) {
					rs.close();
				}
				if (!ps.isClosed()) {
					ps.close();
				}

				if (!connection.isClosed()) {
					connection.close();
				}
			} catch (SQLException e) {
				logger.error("DB: Resources could not be closed properly", e);
			}
		}
		return transactionRecordList;
	}

	@Override
	public LinkedList<TransactionRecord> getCurrentTransactions() {
		return currentTransactions;
	}
}
