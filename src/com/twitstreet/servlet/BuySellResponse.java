package com.twitstreet.servlet;

import com.twitstreet.db.data.Stock;
import com.twitstreet.db.data.User;

public class BuySellResponse {
	long stockId;
	String stockName;
	int stockTotal;
	double stockSold;
	long userId;
	int userCash;
	int userPortfolio;
	int userStock;
	
	public BuySellResponse(User user, Stock stock, int userStockValue){
		this.stockId = stock.getId();
		this.stockTotal = stock.getTotal();
		this.stockSold = stock.getSold();
		this.stockName = stock.getName();
		
		this.userId = user.getId();
		this.userCash = user.getCash();
		this.userPortfolio = user.getPortfolio();
		
		this.userStock = userStockValue;
	}

	public long getStockId() {
		return stockId;
	}

	public void setStockId(long stockId) {
		this.stockId = stockId;
	}

	public int getStockTotal() {
		return stockTotal;
	}

	public void setStockTotal(int stockTotal) {
		this.stockTotal = stockTotal;
	}

	public double getStockSold() {
		return stockSold;
	}

	public void setStockSold(double stockSold) {
		this.stockSold = stockSold;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public int getUserCash() {
		return userCash;
	}

	public void setUserCash(int userCash) {
		this.userCash = userCash;
	}

	public int getUserPortfolio() {
		return userPortfolio;
	}

	public void setUserPortfolio(int userPortfolio) {
		this.userPortfolio = userPortfolio;
	}
}
