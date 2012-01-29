package com.twitstreet.db.data;

import java.sql.ResultSet;
import java.sql.SQLException;


public class GroupRole implements DataObjectIF{
    long id;
    String name;
   
    public static int USER = 0;
    public static int MODERATOR = 1;
    
    public static int ADMIN = 2;   
    
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
