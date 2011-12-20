package com.twitstreet.db.data;

public class StockInPortfolio {
	long stockId;
	String stockName;
	int amount;
	
	public StockInPortfolio(long stockId, String stockName, int amount){
		this.stockId = stockId;
		this.stockName = stockName;
		this.amount = amount;
	}
	
	public long getStockId() {
		return stockId;
	}
	public void setStockId(long stockId) {
		this.stockId = stockId;
	}
	public String getStockName() {
		return stockName;
	}
	public void setStockName(String stockName) {
		this.stockName = stockName;
	}
	public int getAmount() {
		return amount;
	}
	public void setAmount(int amount) {
		this.amount = amount;
	}
}
