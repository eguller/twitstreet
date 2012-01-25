package com.twitstreet.task.asyncquery;

import java.sql.Timestamp;
import java.util.Calendar;

public class TransactionParams {
	
	public long userId;
	public long stockId;
	public int amount;
	public int operation;
	public Timestamp timestamp;
	
	public TransactionParams(long userId, long stockId, int amount,
			int operation) {
		this.userId = userId;
		this.stockId = stockId;
		this.amount = amount;
		this.operation = operation;
		this.timestamp = new Timestamp(Calendar.getInstance().getTimeInMillis());
	}
	
}
