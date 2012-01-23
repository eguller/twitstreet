package com.twitstreet.db.data;

public class StockInPortfolio {
	long stockId;
	String stockName;
	double amount;
	String pictureUrl;
	
	public StockInPortfolio(long stockId, String stockName, double amount, String pictureUrl){
		this.stockId = stockId;
		this.stockName = stockName;
		this.amount = amount;
		this.pictureUrl = pictureUrl;
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
	public double getAmount() {
		return amount;
	}
	public void setAmount(double amount) {
		this.amount = amount;
	}

	public String getPictureUrl() {
		return pictureUrl;
	}

	public void setPictureUrl(String pictureUrl) {
		this.pictureUrl = pictureUrl;
	}
}
