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

public class StockInPortfolio implements DataObjectIF{
	long stockId;
	String stockName;
	private String stockLongName;
	double amount;
	private double total;
	String pictureUrl;
	double capital;
	double changePerHour;
	private double totalChangePerHour;
	private double percentage;
	private boolean verified;
	private boolean changePerHourCalculated;

	public StockInPortfolio() {
		// TODO Auto-generated constructor stub
	}

	public long getStockId() {
		return stockId;
	}

	public void setStockId(long stockId) {
		this.stockId = stockId;
	}

	public String getStockName() {
		return stockName;
	}

	public void setStockName(String stockName) {
		this.stockName = stockName;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public String getPictureUrl() {
		return pictureUrl;
	}

	public void setPictureUrl(String pictureUrl) {
		this.pictureUrl = pictureUrl;
	}

	public double getCapital() {
		return capital;
	}

	public void setCapital(double capital) {
		this.capital = capital;
	}

	public double getChangePerHour() {
		return changePerHour;
	}

	public void setChangePerHour(double changePerHour) {
		this.changePerHour = changePerHour;
	}

	public double getPercentage() {
		return percentage;
	}

	public void setPercentage(double percentage) {
		this.percentage = percentage;
	}

	public boolean isVerified() {
		return verified;
	}

	public void setVerified(boolean verified) {
		this.verified = verified;
	}

	@Override
	public void getDataFromResultSet(ResultSet rs) throws SQLException {
	
		setStockId(rs.getLong("stockId"));
		setStockName(rs.getString("stockName"));
		setStockLongName(rs.getString("stockLongName"));
		setAmount(rs.getDouble("amount"));
		setPictureUrl(rs.getString("pictureUrl"));
		setCapital(rs.getDouble("capital"));
		setChangePerHour(rs.getDouble("changePerHour"));
		if(rs.wasNull()){
			
			changePerHour = 0;
			changePerHourCalculated = false;
		}
		else{
			
			changePerHourCalculated = true;
		}
		setPercentage(rs.getDouble("percentage"));
		setVerified(rs.getBoolean("verified"));
		setTotal(rs.getDouble("total"));	
		setTotalChangePerHour(rs.getDouble("totalChangePerHour"));		
		
		
	}

	public boolean isChangePerHourCalculated() {
		return changePerHourCalculated;
	}

	public void setChangePerHourCalculated(boolean changePerHourCalculated) {
		this.changePerHourCalculated = changePerHourCalculated;
	}

	public double getTotal() {
		return total;
	}

	public void setTotal(double total) {
		this.total = total;
	}

	public double getTotalChangePerHour() {
		return totalChangePerHour;
	}

	public void setTotalChangePerHour(double totalChangePerHour) {
		this.totalChangePerHour = totalChangePerHour;
	}

	public String getStockLongName() {
		return stockLongName;
	}

	public void setStockLongName(String stockLongName) {
		this.stockLongName = stockLongName;
	}
}
