/**
	TwitStreet - Twitter Stock Market Game
    Copyright (C) 2012  Engin Guller (bisanthe@gmail.com), Cagdas Ozek (cagdasozek@gmail.com)

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
**/

package com.twitstreet.market;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.twitstreet.db.base.DBConstants;
import com.twitstreet.db.base.DBMgr;
import com.twitstreet.db.data.Stock;
import com.twitstreet.db.data.TransactionRecord;
import com.twitstreet.db.data.User;

@Singleton
public class TransactionMgrImpl implements TransactionMgr {
	@Inject
	DBMgr dbMgr;
	private static Logger logger = Logger.getLogger(TransactionMgrImpl.class);
	LinkedList<TransactionRecord> currentTransactions = new LinkedList<TransactionRecord>();
	private static final String ADD_TRANSACTION_QUERY = "INSERT INTO transactions(user_id,stock, amount, t_action,t_date) values(?, ?, ?, ?, now())";
	private static final String UPDATE_TRANSACTION_QUERY = "update transactions set amount = amount + ?, t_date = now() where user_id = ? and stock = ? and t_action = ? and timestampdiff(second, t_date, now()) < 30";
	private static final String GET_CURRENT_TRANSACTIONS_QUERY = "select u.id as userId, u.userName as userName, s.id as stockId, s.name as stockName, t.t_action as transactionOperation, t.amount as transactionAmount, t.t_date as transactionDate  from transactions t, stock s, users u where t.user_id = u.id and t.stock = s.id order by t.t_date desc limit ?";
	private static final String GET_USER_TRANSACTIONS_QUERY = "select u.id as userId, u.userName as userName, s.id as stockId, s.name as stockName, t.t_action as transactionOperation, t.amount as transactionAmount, t.t_date as transactionDate  from transactions t, stock s, users u where t.user_id = u.id and t.stock = s.id and t.user_id = ? order by t.t_date desc limit ?";
	private static final String GET_STOCK_TRANSACTIONS_QUERY = "select u.id as userId, u.userName as userName, s.id as stockId, s.name as stockName, t.t_action as transactionOperation, t.amount as transactionAmount, t.t_date as transactionDate  from transactions t, stock s, users u where t.user_id = u.id and t.stock = s.id and s.id = ? order by t.t_date desc limit ?";
	
	@Override
	public void recordTransaction(User user, Stock stock, int amount,
			int operation) {
		Connection connection = null;
		PreparedStatement ps = null;

		try {
			connection = dbMgr.getConnection();
			ps = connection.prepareStatement(UPDATE_TRANSACTION_QUERY);
			ps.setInt(1, amount);
			ps.setLong(2, user.getId());
			ps.setLong(3, stock.getId());
			ps.setInt(4, operation);
			int rowsUpdated = ps.executeUpdate();
			if (rowsUpdated == 0) {
				ps.close();
				ps = connection.prepareStatement(ADD_TRANSACTION_QUERY);
				ps.setLong(1, user.getId());
				ps.setLong(2, stock.getId());
				ps.setInt(3, amount);
				ps.setInt(4, operation);
				ps.execute();
			}
		} catch (SQLException ex) {
			logger.error(DBConstants.QUERY_EXECUTION_FAIL + ps.toString(), ex);
		} finally {
			dbMgr.closeResources(connection, ps, null);
		}
	}

	@Override
	public List<TransactionRecord> getCurrentTransactions() {
		ArrayList<TransactionRecord> transactionRecordList = new ArrayList<TransactionRecord>();
		Connection connection = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			connection = dbMgr.getConnection();
			ps = connection.prepareStatement(GET_CURRENT_TRANSACTIONS_QUERY);
			ps.setInt(1, TransactionMgr.CURRENT_TRANSACTION_LIMIT);
			rs = ps.executeQuery();
			while(rs.next()){
				TransactionRecord transactionRecord = new TransactionRecord();
				transactionRecord.getDataFromResultSet(rs);
				transactionRecordList.add(transactionRecord);
			}
		} catch (SQLException ex) {
			logger.error(DBConstants.QUERY_EXECUTION_FAIL + ps.toString(), ex);
		} finally {
			dbMgr.closeResources(connection, ps, rs);
		}
		return transactionRecordList;
	}

	@Override
	public List<TransactionRecord> queryTransactionRecord(long userId) {
		ArrayList<TransactionRecord> transactionRecordList = new ArrayList<TransactionRecord>();
		Connection connection = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			connection = dbMgr.getConnection();
			ps = connection.prepareStatement(GET_USER_TRANSACTIONS_QUERY);
			ps.setLong(1, userId);
			ps.setInt(2, TransactionMgr.CURRENT_TRANSACTION_LIMIT);
			rs = ps.executeQuery();
			while(rs.next()){
				TransactionRecord transactionRecord = new TransactionRecord();
				transactionRecord.getDataFromResultSet(rs);
				transactionRecordList.add(transactionRecord);
			}
		} catch (SQLException ex) {
			logger.error(DBConstants.QUERY_EXECUTION_FAIL + ps.toString(), ex);
		} finally {
			dbMgr.closeResources(connection, ps, rs);
		}
		return transactionRecordList;
	}

	@Override
	public List<TransactionRecord> queryTransactionRecordByStock(long stockId) {
		ArrayList<TransactionRecord> transactionRecordList = new ArrayList<TransactionRecord>();
		Connection connection = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			connection = dbMgr.getConnection();
			ps = connection.prepareStatement(GET_STOCK_TRANSACTIONS_QUERY);
			ps.setLong(1, stockId);
			ps.setInt(2, TransactionMgr.CURRENT_TRANSACTION_LIMIT);
			rs = ps.executeQuery();
			while(rs.next()){
				TransactionRecord transactionRecord = new TransactionRecord();
				transactionRecord.getDataFromResultSet(rs);
				transactionRecordList.add(transactionRecord);
			}
		} catch (SQLException ex) {
			logger.error(DBConstants.QUERY_EXECUTION_FAIL + ps.toString(), ex);
		} finally {
			dbMgr.closeResources(connection, ps, rs);
		}
		return transactionRecordList;
	}
}
