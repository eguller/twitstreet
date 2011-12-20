package com.twitstreet.db.data;

import java.util.ArrayList;

public class Portfolio {
	long userId;
	String userName;
	ArrayList<StockInPortfolio> stockInPortfolioList = new ArrayList<StockInPortfolio>();
	public Portfolio(User user){
		this.userId = user.getId();
		this.userName = user.getUserName();
	}
	
	public void add(StockInPortfolio stockInPortfolio){
		this.stockInPortfolioList.add(stockInPortfolio);
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public ArrayList<StockInPortfolio> getStockInPortfolioList() {
		return stockInPortfolioList;
	}

	public void setStockInPortfolioList(
			ArrayList<StockInPortfolio> stockInPortfolioList) {
		this.stockInPortfolioList = stockInPortfolioList;
	}
}
