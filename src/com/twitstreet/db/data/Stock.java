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
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.twitstreet.localization.LocalizationUtil;

public class Stock implements DataObjectIF {

	public static final String STOCK = "stock";
	public static final String STOCK_ID = "stock-id";
	//If stock is not updated more than 10 minutes, update is required
	private static final int UPDATE_REQUIRED = 10 * 60 * 1000;

	public static int STOCK_OLDER_THAN_DAYS_AVAILABLE =  7;
	long id;
	String name;
	String longName;
	private String description;
	int total;
	double sold;
	String pictureUrl;
	String language;
	Date lastUpdate;
	int changePerHour;
	boolean changePerHourCalculated;
	boolean verified;
	boolean updateRequired = false;
	private String location;
	public static String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
    private Date createdAt;
    
    
    private boolean suspended;
	SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
	
	public Stock(){}
	
	public Stock(twitter4j.User twUser){
		this.setId(twUser.getId());
		this.setLongName(twUser.getName());
		this.setName(twUser.getScreenName());
		this.setTotal(twUser.getFollowersCount());
		this.setPictureUrl(twUser.getProfileImageURL().toExternalForm());
		this.setSold(0.0D);
		this.setVerified(twUser.isVerified());
		this.setLanguage(twUser.getLang());
		this.setCreatedAt(twUser.getCreatedAt());
		this.setLocation(twUser.getLocation());
		this.setDescription(twUser.getDescription());
	}
	
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
	public int getTotal() {
		return total;
	}
	public void setTotal(int total) {
		this.total = total;
	}
	public double getSold() {
		return sold;
	}
	public void setSold(double sold) {
		this.sold = sold;
	}
	public int getAvailable(){
		return (int)(total * ( 1 - sold));
	}
	public String getPictureUrl() {
		return pictureUrl;
	}
	public void setPictureUrl(String pictureUrl) {
		this.pictureUrl = pictureUrl;
	}
	public Date getLastUpdate() {
		return lastUpdate;
	}
	public void setLastUpdate(Date lastUpdate) {
		this.lastUpdate = lastUpdate;
	}
	public int getChangePerHour() {
		return changePerHour;
	}
	public void setChangePerHour(int changePerHour) {
		this.changePerHour = changePerHour;
	}
	public boolean isVerified() {
		return verified;
	}
	public void setVerified(boolean verified) {
		this.verified = verified;
	}
	@Override
	public void getDataFromResultSet(ResultSet rs) throws SQLException {
		this.setId(rs.getLong("id"));
		this.setName(rs.getString("name"));
		this.setLongName(rs.getString("longName"));
		this.setDescription(rs.getString("description"));
		this.setCreatedAt(rs.getDate("createdAt"));
		this.setLanguage(rs.getString("language"));
		this.setTotal(rs.getInt("total"));
		this.setSold(rs.getDouble("sold"));
		this.setPictureUrl(rs.getString("pictureUrl"));
		this.setLastUpdate(rs.getTimestamp("lastUpdate"));
		this.setLocation(rs.getString("location"));
		
		Integer changePerHour =	rs.getInt("changePerHour");
		
		if(rs.wasNull()){
			
			changePerHour = 0;
			changePerHourCalculated = false;
		}
		else{
			
			changePerHourCalculated = true;
		}
		this.setChangePerHour(changePerHour);
		this.setVerified(rs.getBoolean("verified"));
		
	}
	public boolean isChangePerHourCalculated() {
		return changePerHourCalculated;
	}
	public void setChangePerHourCalculated(boolean changePerHourCalculated) {
		this.changePerHourCalculated = changePerHourCalculated;
	}
	
	@Override
	public boolean equals(Object obj) {

		try {

			return id == ((Stock) obj).getId();
		} catch (Exception ex) {

		}

		return false;

	}
	public boolean isUpdateRequired() {
		return Calendar.getInstance().getTimeInMillis() - lastUpdate.getTime() > UPDATE_REQUIRED ? true : false;
	}
	public String getLongName() {
		return longName;
	}
	public void setLongName(String longName) {
		this.longName = longName;
	}
	public String getLanguage() {
		return language;
	}
	public void setLanguage(String language) {
		
		if(LocalizationUtil.getInstance().getLanguages().contains(language)){
		
			this.language = language;
		}
		else{
			this.language = LocalizationUtil.DEFAULT_LANGUAGE;
			
		}
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
	
	public String toString(){

		String str = "";

		str = "Id: " + str + getId() + "\n";
		str = "Name: " + str + getName() + "\n";
		str = "Long Name: " + str + getLongName() + "\n";
		str = "Follower Count: " + str + getTotal() + "\n";
		str = "Speed: " + str + getChangePerHour() + "\n";
		return str;
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
	public boolean isOldEnough() {
		return getCreatedAt().getTime()< (new Date()).getTime()- 1000 * 60 * 60 * 24 * STOCK_OLDER_THAN_DAYS_AVAILABLE;
	}
	public boolean isSuspended() {
		return suspended;
	}
	public void setSuspended(boolean suspended) {
		this.suspended = suspended;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}
	
}
