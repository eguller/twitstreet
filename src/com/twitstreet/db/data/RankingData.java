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
	
	@Override
	public void getDataFromResultSet(ResultSet rs) throws SQLException {

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


	
	
}