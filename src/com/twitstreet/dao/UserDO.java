package com.twitstreet.dao;

import java.util.Date;

public class UserDO {
	int id;
	String userName;
	Date lastLogin;
	Date firstLogin;
	int cash;
	//user's total portfolio value
	int portfolio;
	//cash + portfolio = total money

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public Date getLastLogin() {
		return lastLogin;
	}

	public void setLastLogin(Date lastLogin) {
		this.lastLogin = lastLogin;
	}

	public Date getFirstLogin() {
		return firstLogin;
	}

	public void setFirstLogin(Date firstLogin) {
		this.firstLogin = firstLogin;
	}

	public int getCash() {
		return cash;
	}

	public void setCash(int cash) {
		this.cash = cash;
	}

	public int getPortfolio() {
		return portfolio;
	}

	public void setPortfolio(int portfolio) {
		this.portfolio = portfolio;
	}
}
