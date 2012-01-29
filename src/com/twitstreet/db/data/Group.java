package com.twitstreet.db.data;

import java.sql.ResultSet;
import java.sql.SQLException;


public class Group implements DataObjectIF{
	
	public static String DEFAULT_NAME = "Overall";
	public static int DEFAULT_ID = 1;
    long id;
    String name;
   
    
    
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
	}

}
