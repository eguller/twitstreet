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
