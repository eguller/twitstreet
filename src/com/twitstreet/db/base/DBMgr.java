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