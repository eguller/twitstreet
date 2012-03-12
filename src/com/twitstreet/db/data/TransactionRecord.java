package com.twitstreet.db.data;


import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import com.twitstreet.task.asyncquery.TransactionParams;

public class TransactionRecord  implements DataObjectIF{

	public static final int BUY = 1;
	public static final int SELL = 0;
	public long userId;
	public long stockId;
	public int amount;
	public int operation;
	public Date date;
	String userName;
	String stockName;


	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public long getStockId() {
		return stockId;
	}

	public void setStockId(long stockId) {
		this.stockId = stockId;
	}

	public int getAmount() {
		return amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}

	public int getOperation() {
		return operation;
	}

	public void setOperation(int operation) {
		this.operation = operation;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}
	
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getStockName() {
		return stockName;
	}
	public void setStockName(String stockName) {
		this.stockName = stockName;
	}

	@Override
	public void getDataFromResultSet(ResultSet rs) throws SQLException {
		this.userId = rs.getLong("userId");
		this.stockId = rs.getLong("stockId");
		this.userName = rs.getString("userName");
		this.stockName = rs.getString("stockName");
		this.operation = rs.getInt("transactionOperation");
		this.amount = rs.getInt("transactionAmount");
		this.date = rs.getDate("transactionDate");
	}
}
