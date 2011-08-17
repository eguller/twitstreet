package com.twitstreet.data;

import java.util.List;

import com.twitstreet.db.data.UserStockDO;

public class CallbackData {
	public String userId;
	public String screenName;
	public int cash;
	public int portfolio;
	public List<UserStockDO> portfolioDOList;
	
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getScreenName() {
		return screenName;
	}
	public void setScreenName(String screenName) {
		this.screenName = screenName;
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
	public List<UserStockDO> getPortfolioDOList() {
		return portfolioDOList;
	}
	public void setPortfolioDOList(List<UserStockDO> portfolioDOList) {
		this.portfolioDOList = portfolioDOList;
	}
}
