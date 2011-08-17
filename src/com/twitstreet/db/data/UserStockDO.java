package com.twitstreet.db.data;

public class UserStockDO {
	UserDO user;
	StockDO stock;
	double  percent;
	
	public double getPercent() {
		return percent;
	}
	public void setPercent(double percent) {
		this.percent = percent;
	}
	public UserDO getUser() {
		return user;
	}
	public void setUser(UserDO user) {
		this.user = user;
	}
	public StockDO getStock() {
		return stock;
	}
	public void setStock(StockDO stock) {
		this.stock = stock;
	}
}
