package com.twitstreet.db.data;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import com.google.inject.Inject;
import com.twitstreet.twitter.TwitterProxy;

public class User implements DataObjectIF{
	public static final String USER = "user";
    long id;
    String userName;
    Date firstLogin;
    Date lastLogin;
    double cash;
    double portfolio;
    double profit;
    double profitPerHour;
    String lastIp;
    String oauthToken;
    String oauthTokenSecret;
    int rank;
    int oldRank;
    int direction;
    String pictureUrl;
    @Inject TwitterProxy twitterProxy = null;
	public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Date getFirstLogin() {
        return firstLogin;
    }

    public void setFirstLogin(Date firstLogin) {
        this.firstLogin = firstLogin;
    }

    public Date getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(Date lastLogin) {
        this.lastLogin = lastLogin;
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

    public String getLastIp() {
		return lastIp;
	}
	public void setLastIp(String lastIp) {
		this.lastIp = lastIp;
	}

	public String getOauthToken() {
		return oauthToken;
	}

	public void setOauthToken(String oauthToken) {
		this.oauthToken = oauthToken;
	}

	public String getOauthTokenSecret() {
		return oauthTokenSecret;
	}

	public void setOauthTokenSecret(String oauthTokenSecret) {
		this.oauthTokenSecret = oauthTokenSecret;
	}

	public int getRank() {
		return rank;
	}

	public void setRank(int rank) {
		this.rank = rank;
	}

	public int getDirection() {
		return direction;
	}

	public void setDirection(int direction) {
		this.direction = direction;
	}

	public String getPictureUrl() {
		return pictureUrl;
	}

	public void setPictureUrl(String pictureUrl) {
		this.pictureUrl = pictureUrl;
	}

	public int getOldRank() {
		return oldRank;
	}

	public void setOldRank(int oldRank) {
		this.oldRank = oldRank;
	}

	public double getProfit() {
		return profit;
	}

	public void setProfit(double profit) {
		this.profit = profit;
	}

	public void setProfitPerHour(double profitPerHour){
		this.profitPerHour = profitPerHour;		
	}
	
	public double getProfitPerHour() {	
		return profitPerHour;
	}
	public void setProfitPerHour(){
		 setProfitPerHour(profit);
		
	}

	@Override
	public void getDataFromResultSet(ResultSet rs) throws SQLException {
		this.setId(rs.getLong("id"));
		this.setRank(rs.getInt("rank"));
		this.setOldRank(rs.getInt("oldRank"));
		this.setDirection(rs.getInt("direction"));
		this.setUserName(rs.getString("userName"));
		this.setLastLogin(rs.getDate("lastLogin"));
		this.setFirstLogin(rs.getDate("firstLogin"));
		this.setCash(rs.getDouble("cash"));
		this.setPortfolio(rs.getDouble("portfolio"));
		//this.setProfit(rs.getDouble("profit"));
		this.setProfit(rs.getDouble("changePerHour"));
		this.setLastIp(rs.getString("lastIp"));
		this.setOauthToken(rs.getString("oauthToken"));
		this.setOauthTokenSecret(rs.getString("oauthTokenSecret"));
		this.setPictureUrl(rs.getString("pictureUrl"));
		this.setProfitPerHour();
		
	}

	
}
