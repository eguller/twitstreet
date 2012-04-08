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

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

public class TransactionRecord  implements DataObjectIF{

	public static final int BUY = 1;
	public static final int SELL = 0;
	public long userId;
	public long stockId;
	public int amount;
	public int operation;
	public Date date;
	String userName;
	String stockName;


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

	public int getAmount() {
		return amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}

	public int getOperation() {
		return operation;
	}

	public void setOperation(int operation) {
		this.operation = operation;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}
	
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getStockName() {
		return stockName;
	}
	public void setStockName(String stockName) {
		this.stockName = stockName;
	}

	@Override
	public void getDataFromResultSet(ResultSet rs) throws SQLException {
		this.userId = rs.getLong("userId");
		this.stockId = rs.getLong("stockId");
		this.userName = rs.getString("userName");
		this.stockName = rs.getString("stockName");
		this.operation = rs.getInt("transactionOperation");
		this.amount = rs.getInt("transactionAmount");
		this.date = new Date(rs.getTimestamp("transactionDate").getTime());
	}
}
