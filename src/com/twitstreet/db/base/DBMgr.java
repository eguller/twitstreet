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

package com.twitstreet.db.base;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public interface DBMgr {

	public abstract void init();

	public abstract Connection getConnection() throws SQLException;
	public abstract boolean closeResources(Connection c, Statement stmt, ResultSet rs);
	
	public abstract void setDbHost(String dbHost);

	public abstract void setDbPort(int dbPort);

	public abstract void setUserName(String userName);

	public abstract void setPassword(String password);

	public abstract void setDbName(String dbName);

}