package com.twitstreet.db.data;


import com.twitstreet.task.asyncquery.TransactionParams;

public class TransactionRecord  extends TransactionParams{

	public static final int BUY = 1;
	public static final int SELL = 0;
	String userName;
	String stockName;


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
}
