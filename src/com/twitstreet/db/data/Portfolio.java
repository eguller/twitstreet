/**
	TwitStreet - Twitter Stock Market Game
    Copyright (C) 2012  Engin Guller (bisanth@gmail.com), Cagdas Ozek (cagdasozek@gmail.com)

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
**/

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
