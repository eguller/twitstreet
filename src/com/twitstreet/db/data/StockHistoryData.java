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
