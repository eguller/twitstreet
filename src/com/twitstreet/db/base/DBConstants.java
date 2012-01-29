package com.twitstreet.db.base;

import org.apache.log4j.Logger;

public class DBConstants {
	
	static final String DRIVER = "com.mysql.jdbc.Driver";
	static final int VALIDATION_INTERVAL = 30000;
	static final int EVICTION_RUN_MILLIS = 30000;
	static final int MIN_EVICTABLE_IDLE_TIME = 30000;
	static final int MIN_IDLE = 10;
	static final int MAX_ACTIVE = 100;
	static final int INITIAL_SIZE = 10;
	static final String VALIDATION_QUERY = "SELECT 1";
	static final int MAX_WAIT = 10000;
	static final int ABANDONED_TIMEOUT = 60;
	
	public static String QUERY_EXECUTION_SUCC = "DB: Query executed successfully - ";
	public static String QUERY_EXECUTION_FAIL ="DB: Query failed - ";
	public static String RESOURCES_NOT_CLOSED ="DB: Resources could not be closed properly";
}
