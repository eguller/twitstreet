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
    double total;
    double valueCumulative;
    private int rankCumulative;
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
    String language;
    boolean newSeasonInfoSent = true;
    String url;
    boolean autoPlayer = false;

    @Inject TwitterProxy twitterProxy = null;

	private boolean profitCalculated;
	public double loan = 0;

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
		this.setLastLogin(new Date(rs.getTimestamp("lastLogin").getTime()));
		this.setFirstLogin(new Date(rs.getTimestamp("firstLogin").getTime()));
		this.setCash(rs.getDouble("cash"));
		this.setPortfolio(rs.getDouble("portfolio"));
		this.setTotal(rs.getDouble("total"));
		this.setDescription(rs.getString("description"));
		this.setLongName(rs.getString("longName"));
		this.setLocation(rs.getString("location"));
		this.setInviteActive(rs.getBoolean("inviteActive"));
		this.setValueCumulative(rs.getDouble("valueCumulative"));
		this.setRankCumulative(rs.getInt("rankCumulative"));
		this.setLoan(rs.getDouble("loan"));
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
		this.setLanguage(rs.getString("language"));
		this.setUrl(rs.getString("url"));
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

	public double getValueCumulative() {
		return valueCumulative;
	}

	public void setValueCumulative(double cumulativeValue) {
		this.valueCumulative = cumulativeValue;
	}

	public double getTotal() {
		return total;
	}

	public void setTotal(double total) {
		this.total = total;
	}

	public int getRankCumulative() {
		return rankCumulative;
	}

	public void setRankCumulative(int rankCumulative) {
		this.rankCumulative = rankCumulative;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public double getLoan() {
		return loan;
	}

	public void setLoan(double loan) {
		this.loan = loan;
	}
	
	public String getAvatarUrl(){
		if(pictureUrl != null && pictureUrl.length() > 0){
			int extIndex = pictureUrl.lastIndexOf('.');
			return pictureUrl.substring(0, extIndex - "_normal".length()) + "_reasonably_small" +pictureUrl.substring(extIndex);
		}
		return "";
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public boolean isAutoPlayer() {
		return autoPlayer;
	}

	public void setAutoPlayer(boolean autoPlayer) {
		this.autoPlayer = autoPlayer;
	}
}

