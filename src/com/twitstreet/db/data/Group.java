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


public class Group implements DataObjectIF{

	public static int STATUS_DEFAULT = 0;
	public static int STATUS_NEW_USER_DISABLED = 1;
	
	public static String DEFAULT_NAME = "Overall";
	public static int DEFAULT_ID = 1;
    long id;
    String name;
    private long adminId;
    private String adminName;
    private double total;
    private double totalAllTime;
    private int rank;
    private int rankAllTime;
    public double getTotalAllTime() {
		return totalAllTime;
	}

	public void setTotalAllTime(double totalAllTime) {
		this.totalAllTime = totalAllTime;
	}

	public int getRank() {
		return rank;
	}

	public void setRank(int rank) {
		this.rank = rank;
	}

	public int getRankAllTime() {
		return rankAllTime;
	}

	public void setRankAllTime(int rankAllTime) {
		this.rankAllTime = rankAllTime;
	}

	private double changePerHour;
    private int status = 0;
    
    private int userCount;
    
	public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

	@Override
	public void getDataFromResultSet(ResultSet rs) throws SQLException {
		this.id = rs.getLong("id");
		this.name = rs.getString("name");
		this.adminId = rs.getLong("adminId");
		this.adminName = rs.getString("adminName");
		this.userCount = rs.getInt("userCount");
		this.changePerHour = rs.getDouble("changePerHour");
		this.total = rs.getDouble("total");
		this.totalAllTime = rs.getDouble("totalAllTime");
		this.rank = rs.getInt("rank");
		this.rankAllTime = rs.getInt("rankAllTime");
		this.status = rs.getInt("status");
		
	}

	@Override
	public boolean equals(Object obj){
		if(obj==null)return false;
		Group g = (Group) obj;
		return this.getId() == g.getId();		
		
	}

	public int getUserCount() {
		return userCount;
	}

	public void setUserCount(int userCount) {
		this.userCount = userCount;
	}

	public long getAdminId() {
		return adminId;
	}

	public void setAdminId(long adminId) {
		this.adminId = adminId;
	}

	public String getAdminName() {
		return adminName;
	}

	public void setAdminName(String adminName) {
		this.adminName = adminName;
	}

	public double getTotal() {
		return total;
	}

	public void setTotal(double total) {
		this.total = total;
	}

	public double getChangePerHour() {
		return changePerHour;
	}

	public void setChangePerHour(double changePerHour) {
		this.changePerHour = changePerHour;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	
}
