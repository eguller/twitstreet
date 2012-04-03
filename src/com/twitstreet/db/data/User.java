package com.twitstreet.db.data;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.google.inject.Inject;
import com.twitstreet.twitter.TwitterProxy;

public class User implements DataObjectIF {
	public static final String USER = "user";
	public static final String USER_ID = "user-id";
	public static String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
    long id;
    String userName;
    private Date createdAt;
    Date firstLogin;
    Date lastLogin;
    double cash;
    double portfolio;

	SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
    //profit per hour
    double profit;

    String lastIp;
    String oauthToken;
    String oauthTokenSecret;
    int rank;
    int oldRank;
    int direction;
    String pictureUrl;
    String description;
    String location;
    String longName;
    private boolean inviteActive = true;
    
    @Inject TwitterProxy twitterProxy = null;
	private boolean profitCalculated;
    
    @Override
	public boolean equals(Object obj) {

		try {

			return id == ((User) obj).getId();
		} catch (Exception ex) {

		}

		return false;

	}
    
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
		this.setDescription(rs.getString("description"));
		this.setLongName(rs.getString("longName"));
		this.setLocation(rs.getString("location"));
		this.setInviteActive(rs.getBoolean("inviteActive"));
		
		Double profit =	rs.getDouble("changePerHour");
		
		if(rs.wasNull()){
			
			profit = 0.0;
			setProfitCalculated(false);
		}
		else{
			
			setProfitCalculated(true);
		}
		this.setProfit(profit);
		
		
	
		
		
		this.setLastIp(rs.getString("lastIp"));
		this.setOauthToken(rs.getString("oauthToken"));
		this.setOauthTokenSecret(rs.getString("oauthTokenSecret"));
		this.setPictureUrl(rs.getString("pictureUrl"));
		
		
	}

	public boolean isProfitCalculated() {
		return profitCalculated;
	}

	public void setProfitCalculated(boolean profitCalculated) {
		this.profitCalculated = profitCalculated;
	}

	public Date getCreatedAt() {
		return createdAt;
	}
	public String getCreatedAtStr() {
		return sdf.format(createdAt.getTime());
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getLongName() {
		return longName;
	}

	public void setLongName(String longName) {
		this.longName = longName;
	}

	public boolean isInviteActive() {
		return inviteActive;
	}

	public void setInviteActive(boolean inviteActive) {
		this.inviteActive = inviteActive;
	}

	
}
