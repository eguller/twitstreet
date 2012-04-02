package com.twitstreet.season;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DateFormatSymbols;
import java.util.Calendar;
import java.util.Locale;

import com.twitstreet.db.data.DataObjectIF;
import com.twitstreet.localization.LocalizationUtil;

public class SeasonInfo implements DataObjectIF {

	private Timestamp startTime = new Timestamp(0);

	private Timestamp endTime = new Timestamp(0);
	
	private int id = 0;
	
	private boolean active = false;

	public Timestamp getStartTime() {
		return startTime;
	}

	public void setStartTime(Timestamp startTime) {
		this.startTime = startTime;
	}

	public Timestamp getEndTime() {
		return endTime;
	}

	public void setEndTime(Timestamp endTime) {
		this.endTime = endTime;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}
	
	public String localizedSeasonTime(String lang){
		String seasonTimeText = "Always";
		LocalizationUtil lutil = LocalizationUtil.getInstance();
		Calendar starttimeCalendar = Calendar.getInstance();
		starttimeCalendar.setTime(startTime);
		int startTimeMonthIndex = starttimeCalendar.get(Calendar.MONTH);
		int startTimeDay = starttimeCalendar.get(Calendar.DAY_OF_MONTH);
		
		Calendar endTimeCalendar = Calendar.getInstance();
		endTimeCalendar.setTime(endTime);
		int endtimeMonthIndex = endTimeCalendar.get(Calendar.MONTH);
		int endTimeDay = endTimeCalendar.get(Calendar.DAY_OF_MONTH);
		
		if(startTimeMonthIndex == endtimeMonthIndex){
			String monthName = lutil.get("month."+startTimeMonthIndex, lang);
			seasonTimeText = monthName + " " + startTimeDay + " - " + endTimeDay;
		}
		else{
			String startMonthName = lutil.get("month."+startTimeMonthIndex, lang);
			String endMonthName = lutil.get("month."+endtimeMonthIndex, lang);
			seasonTimeText = startMonthName + " " + startTimeDay + " - " + endMonthName + " " + endTimeDay;
		}
		return seasonTimeText;
	}

	@Override
	public void getDataFromResultSet(ResultSet rs) throws SQLException {
		setStartTime(rs.getTimestamp("startTime"));
		setEndTime(rs.getTimestamp("endTime"));
		setId(rs.getInt("id"));
		setActive(rs.getBoolean("active"));
	}
}
