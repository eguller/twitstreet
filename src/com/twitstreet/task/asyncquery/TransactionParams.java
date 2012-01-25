package com.twitstreet.task.asyncquery;

import java.util.Date;

public class TransactionParams {
	
	public long userId;
	public long stockId;
	public int amount;
	public int operation;
	public Date date;
	
	public TransactionParams(){}
	
	public TransactionParams(long userId, long stockId, int amount,
			int operation) {
		this.userId = userId;
		this.stockId = stockId;
		this.amount = amount;
		this.operation = operation;
		this.date = new Date(System.currentTimeMillis());
	}

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
	
}
