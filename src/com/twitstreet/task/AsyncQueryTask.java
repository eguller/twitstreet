package com.twitstreet.task;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.apache.log4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.sun.xml.internal.bind.v2.TODO;
import com.twitstreet.db.base.DBMgr;
import com.twitstreet.db.data.TransactionRecord;
import com.twitstreet.task.asyncquery.TransactionParams;

@Singleton
public class AsyncQueryTask implements Runnable {
	
	@Inject
	private DBMgr dbMgr;
	
	private BlockingQueue<TransactionParams> paramsQ;
	
	private final String TRANSACTION_QUERY = "INSERT INTO transactions(user_id,stock, amount, t_action,t_date) values(?, ?, ?, ?, ?)";
	private static Logger logger = Logger.getLogger(AsyncQueryTask.class);
	
	public AsyncQueryTask() {
		this.paramsQ = new LinkedBlockingQueue<TransactionParams>();
	}
	
	public void addTransactionStatement(long userId, long stockId, int amount, int operation) {
		this.paramsQ.add(new TransactionParams(userId, stockId, amount, operation));
	}
	
	@Override
	public void run() {
		
		while (true) {
			
			// update db every 10 queries
			if (paramsQ.size() >= 10) {
				Connection conn = null;
				PreparedStatement ps = null;
				try {
					conn = dbMgr.getConnection();
					ps = conn.prepareStatement(TRANSACTION_QUERY, Statement.RETURN_GENERATED_KEYS);
				} catch (SQLException e) {
					e.printStackTrace();
				}
				
				// if everything is ok
				if (conn != null && ps != null) {
					for (int i = 0; i < 10; i++) {
						TransactionParams params = paramsQ.poll();
						try {
							// set params
							ps.setLong(1, params.userId);
							ps.setLong(2, params.stockId);
							ps.setInt(3, params.amount);
							ps.setInt(4, params.operation);
							ps.setTimestamp(5, params.timestamp);
							
							// add batch (no db transaction yet)
							ps.addBatch();
						} catch (SQLException e) {
							e.printStackTrace();
						}
					}
					
					int[] affectedRows = null;
					
					// now write all to db at once
					try {
						affectedRows = ps.executeBatch();
						System.out.println("10 transaction query sent to db successfully");
					} catch (SQLException e) {
						e.printStackTrace();
					}
					
					// check all affected rows
					for (int i : affectedRows) {
						if (i <= 0) {
							logger.error("DB: Transaction record insert failed no generated keys affected - "
									+ ps.toString() + " - query no:" + i);
						}
					}
					
					// get generated keys
					ResultSet generatedKeys = null;
					try {
						generatedKeys = ps.getGeneratedKeys();
					} catch (SQLException e) {
						e.printStackTrace();
					}
					
					// TODO :  implement related stuff with generated keys
					try {
						while(generatedKeys.next()) {
							TransactionRecord transactionRecord = new TransactionRecord();
						}
					} catch (SQLException e) {
						e.printStackTrace();
					}
					
					
					// close everything
					try {
						if (generatedKeys != null && !generatedKeys.isClosed()) {
							generatedKeys.close();
						}
						if (ps != null && !ps.isClosed()) {
							ps.close();
						}

						if (conn != null && !conn.isClosed()) {
							conn.close();
						}
					} catch (SQLException e) {
						e.printStackTrace();
					}
					
				}
			}
		}
		
	}

}
