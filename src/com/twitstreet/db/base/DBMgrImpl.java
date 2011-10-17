package com.twitstreet.db.base;

import java.sql.Connection;
import java.sql.SQLException;

import org.apache.tomcat.jdbc.pool.DataSource;
import org.apache.tomcat.jdbc.pool.PoolProperties;

import com.google.inject.Singleton;

@Singleton
public class DBMgrImpl implements DBMgr {
	private static final String DRIVER = "com.mysql.jdbc.Driver";
	private static final int VALIDATION_INTERVAL = 30000;
	private static final int EVICTION_RUN_MILLIS = 30000;
	private static final int MIN_EVICTABLE_IDLE_TIME = 30000;
	private static final int MIN_IDLE = 10;
	private static final int MAX_ACTIVE = 100;
	private static final int INITIAL_SIZE = 10;
	private static final String VALIDATION_QUERY = "SELECT 1";
	private static final int MAX_WAIT = 10000;
	private static final int ABANDONED_TIMEOUT = 60;
	
	String dbHost;
	int dbPort;
	String userName;
	String password;
	String dbName;
	String connectURI;
	private DataSource dataSource;

	/* (non-Javadoc)
	 * @see com.twitstreet.db.base.DBMgr#init()
	 */
	@Override
	public void init() {
		setupDataSource();
	}

	private void setupDataSource() {
		PoolProperties p = new PoolProperties();
        p.setUrl(getConnectionURL());
        p.setDriverClassName(DRIVER);
        p.setUsername(getUserName());
        p.setPassword(getPassword());
        p.setJmxEnabled(true);
        p.setTestWhileIdle(false);
        p.setTestOnBorrow(true);
        p.setValidationQuery(VALIDATION_QUERY);
        p.setTestOnReturn(false);
        p.setValidationInterval(VALIDATION_INTERVAL);
        p.setTimeBetweenEvictionRunsMillis(EVICTION_RUN_MILLIS);
        p.setMaxActive(MAX_ACTIVE);
        p.setInitialSize(INITIAL_SIZE);
        p.setMaxWait(MAX_WAIT);
        p.setRemoveAbandonedTimeout(ABANDONED_TIMEOUT);
        p.setMinEvictableIdleTimeMillis(MIN_EVICTABLE_IDLE_TIME);
        p.setMinIdle(MIN_IDLE);
        p.setLogAbandoned(true);
        p.setRemoveAbandoned(true);
        p.setJdbcInterceptors("org.apache.tomcat.jdbc.pool.interceptor.ConnectionState;"+
          "org.apache.tomcat.jdbc.pool.interceptor.StatementFinalizer");
        dataSource = new DataSource();
        dataSource.setPoolProperties(p); 
	}
	
	private String getConnectionURL(){
		return "jdbc:mysql://"+getDbHost()+":"+getDbPort()+ "/"+getDbName();
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
}
