package com.twitstreet.servlet;

import com.twitstreet.db.data.User;

public class BalanceResponse {
	private double cash;
	private double portfolio;
	private double total;
	private int direction;
	private String userName;
	private long userId;
	private int rank;
	
	public BalanceResponse(User user){
		this.cash = user.getCash();
		this.portfolio = user.getPortfolio();
		this.total = cash + portfolio;
		this.direction = user.getDirection();
		this.userName = user.getUserName();
		this.userId = user.getId();
		this.rank = user.getRank();
	}

	public double getCash() {
		return cash;
	}

	public double getPortfolio() {
		return portfolio;
	}

	public double getTotal() {
		return total;
	}

	public int getDirection() {
		return direction;
	}

	public String getUserName() {
		return userName;
	}

	public long getUserId() {
		return userId;
	}

	public int getRank() {
		return rank;
	}
}
