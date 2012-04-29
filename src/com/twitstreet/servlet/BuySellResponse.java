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

package com.twitstreet.servlet;

import java.util.List;

import com.twitstreet.db.data.Stock;
import com.twitstreet.db.data.User;
import com.twitstreet.db.data.UserStockDetail;

public class BuySellResponse {
	long stockId;
	String stockName;
	double stockTotal;
	double stockSold;
	long userId;
	double userCash;
	double userPortfolio;
	double userStock;
	
	
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

	public double getStockTotal() {
		return stockTotal;
	}

	public void setStockTotal(double stockTotal) {
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

	public double getUserCash() {
		return userCash;
	}

	public void setUserCash(double userCash) {
		this.userCash = userCash;
	}

	public double getUserPortfolio() {
		return userPortfolio;
	}

	public void setUserPortfolio(int userPortfolio) {
		this.userPortfolio = userPortfolio;
	}

}
