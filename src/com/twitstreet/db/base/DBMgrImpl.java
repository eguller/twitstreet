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
import java.util.ArrayList;

import org.apache.log4j.Logger;
import org.apache.tomcat.jdbc.pool.DataSource;
import org.apache.tomcat.jdbc.pool.PoolProperties;

import com.google.inject.Singleton;

@Singleton
public class DBMgrImpl implements DBMgr {

	String dbHost;
	int dbPort;
	String userName;
	String password;
	String dbName;
	String connectURI;
	private DataSource dataSource;
	private static Logger logger = Logger.getLogger(DBMgrImpl.class);
	
	/* (non-Javadoc)
	 * @see com.twitstreet.db.base.DBMgr#init()
	 */
	@Override
	public void init() {
		setupDataSource();
		logger.debug("Database Manager initialized.");
	}

	private void setupDataSource() {
		PoolProperties p = new PoolProperties();
        p.setUrl(getConnectionURL());
        p.setDriverClassName(DBConstants.DRIVER);
        p.setUsername(getUserName());
        p.setPassword(getPassword());
        p.setJmxEnabled(true);
        p.setTestWhileIdle(false);
        p.setTestOnBorrow(true);
        p.setValidationQuery(DBConstants.VALIDATION_QUERY);
        p.setTestOnReturn(false);
        p.setValidationInterval(DBConstants.VALIDATION_INTERVAL);
        p.setTimeBetweenEvictionRunsMillis(DBConstants.EVICTION_RUN_MILLIS);
        p.setMaxActive(DBConstants.MAX_ACTIVE);
        p.setInitialSize(DBConstants.INITIAL_SIZE);
        p.setMaxWait(DBConstants.MAX_WAIT);
        p.setRemoveAbandonedTimeout(DBConstants.ABANDONED_TIMEOUT);
        p.setMinEvictableIdleTimeMillis(DBConstants.MIN_EVICTABLE_IDLE_TIME);
        p.setMinIdle(DBConstants.MIN_IDLE);
        p.setLogAbandoned(false);
        p.setRemoveAbandoned(true);
        p.setJdbcInterceptors("org.apache.tomcat.jdbc.pool.interceptor.ConnectionState;"+
          "org.apache.tomcat.jdbc.pool.interceptor.StatementFinalizer");
        dataSource = new DataSource();
        dataSource.setPoolProperties(p); 
	}
	
	private String getConnectionURL(){
		return "jdbc:mysql://"+getDbHost()+":"+getDbPort()+ "/"+getDbName() + "?characterEncoding=utf-8&useUnicode=true";
	}
	
	/* (non-Javadoc)
	 * @see com.twitstreet.db.base.DBMgr#getConnection()
	 */
	@Override
	public synchronized Connection getConnection() throws SQLException{
		return dataSource.getConnection();
	}

	public String getDbHost() {
		return dbHost;
	}

	/* (non-Javadoc)
	 * @see com.twitstreet.db.base.DBMgr#setDbHost(java.lang.String)
	 */
	@Override
	public void setDbHost(String dbHost) {
		this.dbHost = dbHost;
	}

	public int getDbPort() {
		return dbPort;
	}

	/* (non-Javadoc)
	 * @see com.twitstreet.db.base.DBMgr#setDbPort(java.lang.String)
	 */
	@Override
	public void setDbPort(int dbPort) {
		this.dbPort = dbPort;
	}

	public String getUserName() {
		return userName;
	}

	/* (non-Javadoc)
	 * @see com.twitstreet.db.base.DBMgr#setUserName(java.lang.String)
	 */
	@Override
	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	/* (non-Javadoc)
	 * @see com.twitstreet.db.base.DBMgr#setPassword(java.lang.String)
	 */
	@Override
	public void setPassword(String password) {
		this.password = password;
	}

	public String getDbName() {
		return dbName;
	}

	/* (non-Javadoc)
	 * @see com.twitstreet.db.base.DBMgr#setDbName(java.lang.String)
	 */
	@Override
	public void setDbName(String dbName) {
		this.dbName = dbName;
	}
	@Override
	public boolean closeResources(Connection c, Statement stmt, ResultSet rs) {
		try {
			if (rs != null && !rs.isClosed()) {
				rs.close();
			}
			if (stmt != null && !stmt.isClosed()) {
				stmt.close();
			}
			if (c != null && !c.isClosed()) {
				c.close();
			}
		} catch (SQLException e) {
			logger.error(DBConstants.RESOURCES_NOT_CLOSED, e);
			return false;
		}
		return true;
	}
	
	public static String getIdListAsCommaSeparatedString(ArrayList<Long> idList){
		String idListStr = "";
		for(int i= 0;i <idList.size(); i++){
			if(i!=0){
				idListStr = idListStr +",";
			}
			idListStr = idListStr + "("+String.valueOf(idList.get(i)+(")"));
		}
		return idListStr;
	}
}
