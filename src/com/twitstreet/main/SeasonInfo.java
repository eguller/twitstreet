package com.twitstreet.main;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import com.twitstreet.db.data.DataObjectIF;

public class SeasonInfo implements DataObjectIF {

	private Timestamp startTime = new Timestamp(0);

	private Timestamp endTime = new Timestamp(0);
	  


	private int id = 0;

    private boolean updateInProgress = false;
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

	@Override
	public void getDataFromResultSet(ResultSet rs) throws SQLException {
		setStartTime(rs.getTimestamp("startTime"));
		setEndTime(rs.getTimestamp("endTime"));
		setId(rs.getInt("id"));
		setActive(rs.getBoolean("active"));
		setUpdateInProgress(rs.getBoolean("updateInProgress"));
		
	}

	public boolean isUpdateInProgress() {
		return updateInProgress;
	}

	public void setUpdateInProgress(boolean updateInProgress) {
		this.updateInProgress = updateInProgress;
	}
	
	
	
}
