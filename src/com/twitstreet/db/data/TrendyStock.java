package com.twitstreet.db.data;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;

import com.twitstreet.localization.LocalizationUtil;

public class TrendyStock extends Stock {
	
	private Date oldUpdate = null;
	private int oldValue = 0;
	private int trendDuration = 0; 
	
	
	@Override
	public void getDataFromResultSet(ResultSet rs) throws SQLException {
		this.setId(rs.getLong("id"));
		this.setName(rs.getString("name"));
		this.setLongName(rs.getString("longName"));
		this.setTotal(rs.getInt("total"));
		this.setCreatedAt(rs.getDate("createdAt"));
		this.setPictureUrl(rs.getString("pictureUrl"));
		this.setLastUpdate(rs.getTimestamp("lastUpdate"));
		this.setOldUpdate(rs.getTimestamp("oldUpdate"));
		this.setOldValue(rs.getInt("oldValue"));
		this.setLanguage(rs.getString("language"));
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



	public Date getOldUpdate() {
		return oldUpdate;
	}



	public void setOldUpdate(Date oldUpdate) {
		this.oldUpdate = oldUpdate;
	}



	public int getOldValue() {
		return oldValue;
	}



	public void setOldValue(int oldValue) {
		this.oldValue = oldValue;
	}
	
	
	public String getAnnouncement(String lang){
		
		return LocalizationUtil.getInstance().get("announcement.trendystock", lang, new Object[]{
																								String.valueOf(getTrendDuration()),
																								String.valueOf(getTotal()-getOldValue()),
																								
																								});
		
	}
	public String getAnnouncementStockDetail(String lang){
		
		return LocalizationUtil.getInstance().get("announcement.checkoutyourstock", lang);
		
	}


	public int getTrendDuration() {
		return trendDuration;
	}



	public void setTrendDuration(int trendDuration) {
		this.trendDuration = trendDuration;
	}
}
