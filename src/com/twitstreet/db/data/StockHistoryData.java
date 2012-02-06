package com.twitstreet.db.data;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.LinkedHashMap;

public class StockHistoryData implements DataObjectIF{

	private String name;
	
	private long id;
	
	private LinkedHashMap<Date, Integer> dateValueMap = new LinkedHashMap<Date, Integer>(); 
	
	@Override
	public void getDataFromResultSet(ResultSet rs) throws SQLException {
		while (rs.next()) {
			setName(rs.getString("name"));
			setId(rs.getLong("id"));
			
			long time = rs.getTimestamp("lastUpdate").getTime();
			dateValueMap.put(new Date(time),new Integer(rs.getInt("total")));
		}
		
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public LinkedHashMap<Date, Integer> getDateValueMap() {
		return dateValueMap;
	}

	public void setDateValueMap(LinkedHashMap<Date, Integer> dateValueMap) {
		this.dateValueMap = dateValueMap;
	}

}
