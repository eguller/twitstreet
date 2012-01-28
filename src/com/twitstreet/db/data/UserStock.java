package com.twitstreet.db.data;

public class UserStock {
	long id;
	long userId;
	long stockId;
	double  percent;
	private double capital;
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
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
	public double getPercent() {
		return percent;
	}
	public void setPercent(double percent) {
		this.percent = percent;
	}
	public double getCapital() {
		return capital;
	}
	public void setCapital(double capital) {
		this.capital = capital;
	}
}
