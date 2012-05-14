/**
	TwitStreet - Twitter Stock Market Game
    Copyright (C) 2012  Engin Guller (bisanthe@gmail.com), Cagdas Ozek (cagdasozek@gmail.com)

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
public class RankingData implements DataObjectIF{
	
	private double cash;
	private double portfolio;
	private Date lastUpdate;
	private double total;
	private int rank;
	private int seasonId;
	private long userId;
	@Override
	public void getDataFromResultSet(ResultSet rs) throws SQLException {

		setUserId(rs.getLong("user_id"));
		this.setSeasonId(rs.getInt("season_id"));
		this.setRank(rs.getInt("rank"));
		this.setCash(rs.getDouble("cash"));
		this.setPortfolio(rs.getDouble("portfolio"));
		this.setTotal(portfolio+cash);
		long time = rs.getTimestamp("lastUpdate").getTime();
		this.lastUpdate = new Date(time);
	}



	public double getCash() {
		return cash;
	}


	public void setCash(double cash) {
		this.cash = cash;
	}



	public double getPortfolio() {
		return portfolio;
	}

	public void setPortfolio(double portfolio) {
		this.portfolio = portfolio;
	}
	
	public int getRank() {
		return rank;
	}


	public void setRank(int rank) {
		this.rank = rank;
	}



	public double getTotal() {
		return total;
	}



	public void setTotal(double total) {
		this.total = total;
	}



	public Date getLastUpdate() {
		return lastUpdate;
	}



	public void setLastUpdate(Date lastUpdate) {
		this.lastUpdate = lastUpdate;
	}



	public int getSeasonId() {
		return seasonId;
	}



	public void setSeasonId(int seasonId) {
		this.seasonId = seasonId;
	}



	public long getUserId() {
		return userId;
	}



	public void setUserId(long userId) {
		this.userId = userId;
	}


	
	
}