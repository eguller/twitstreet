package com.twitstreet.main;

import javax.servlet.ServletContext;

public interface Twitstreet {
	public static final String PROPERTIES_COMMENT = "Twitstreet properties";
	public static final String DB_HOST = "dbhost";
	public static final String DB_PORT = "dbport";
	public static final String DB_ADMIN = "dbadmin";
	public static final String DB_PASSWORD = "dbpassword";
	public static final String DATABASE = "database";
	public static final String TWITSTREET_PROPERTIES = System.getProperty("user.home") + "/.twitstreet/twitstreet.properties";
	
	public void initialize();
	public boolean isInitialized();
	public ServletContext getServletContext();
	public void setServletContext(ServletContext servletContext);
}
