package com.twitstreet.db.base;

import java.sql.Connection;
import java.sql.SQLException;

public interface DBMgr {

	public abstract void init();

	public abstract Connection getConnection() throws SQLException;

	public abstract void setDbHost(String dbHost);

	public abstract void setDbPort(int dbPort);

	public abstract void setUserName(String userName);

	public abstract void setPassword(String password);

	public abstract void setDbName(String dbName);

}