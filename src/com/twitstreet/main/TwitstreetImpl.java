package com.twitstreet.main;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

import javax.servlet.ServletContext;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.twitstreet.config.ConfigMgr;
import com.twitstreet.db.base.DBMgr;
import com.twitstreet.servlet.HomePageServlet;


@SuppressWarnings("serial")
@Singleton
public class TwitstreetImpl implements Twitstreet {
	private boolean initialized = false;
	@Inject HomePageServlet homePageServlet;
	@Inject DBMgr dbMgr;
	@Inject ConfigMgr configMgr;
	ServletContext servletContext;
	public TwitstreetImpl(){
		
	}
	
	@Override
	public void initialize() {
		Properties properties = new Properties();
		try {
			properties.load(new FileReader(new File(Twitstreet.TWITSTREET_PROPERTIES)));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//String consumerKey = properties.get(Twitstreet.)
		String dbHost = properties.getProperty(Twitstreet.DB_HOST);
		String dbAdmin = properties.getProperty(Twitstreet.DB_ADMIN);
		String dbPassword = properties.getProperty(Twitstreet.DB_PASSWORD);
		String dbName = properties.getProperty(Twitstreet.DATABASE);
		String dbPortStr = properties.getProperty(Twitstreet.DB_PORT);
		int dbPort = Integer.parseInt(dbPortStr);
		
		dbMgr.setDbHost(dbHost);
		dbMgr.setUserName(dbAdmin);
		dbMgr.setPassword(dbPassword);
		dbMgr.setDbName(dbName);
		dbMgr.setDbPort(dbPort);
		dbMgr.init();
		configMgr.load();

		initialized = true;
	}
	public boolean isInitialized(){
		return initialized;
	}

	@Override
	public ServletContext getServletContext() {
		return servletContext;
	}

	@Override
	public void setServletContext(ServletContext applicationPath) {
		this.servletContext = applicationPath;
	}
}
