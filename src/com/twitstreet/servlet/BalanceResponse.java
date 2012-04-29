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
