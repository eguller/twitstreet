package com.twitstreet.main;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

import javax.servlet.ServletContext;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;
import com.twitstreet.cache.TransactionCache;
import com.twitstreet.config.ConfigMgr;
import com.twitstreet.db.base.DBMgr;
import com.twitstreet.market.StockMgr;
import com.twitstreet.session.GroupMgr;
import com.twitstreet.session.UserMgr;
import com.twitstreet.task.AsyncQuery;
import com.twitstreet.task.StockUpdateTask;
import com.twitstreet.task.UserInfoUpdateTask;


@Singleton
public class TwitstreetImpl implements Twitstreet {
	private boolean initialized = false;
	DBMgr dbMgr;
	ConfigMgr configMgr;
	ServletContext servletContext;
	Injector injector;
	@Inject AsyncQuery asyncQueryTask;
	@Inject TransactionCache transactionCache;
	@Inject GroupMgr groupMgr;
	@Inject UserMgr userMgr;
	@Inject StockMgr stockMgr;
	@Inject public TwitstreetImpl(DBMgr dbMgr, ConfigMgr configMgr){
		this.dbMgr = dbMgr;
		this.configMgr = configMgr;
	}
	
	@Override
	public void initialize() {
		Properties properties = new Properties();
		try {
			properties.load(new FileReader(new File(Twitstreet.TWITSTREET_PROPERTIES)));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		String dbHost = properties.getProperty(Twitstreet.DB_HOST);
		String dbAdmin = properties.getProperty(Twitstreet.DB_ADMIN);
		String dbPassword = properties.getProperty(Twitstreet.DB_PASSWORD);
		String dbName = properties.getProperty(Twitstreet.DATABASE);
		String dbPortStr = properties.getProperty(Twitstreet.DB_PORT);
		
		int serverCount = properties.getProperty(ConfigMgr.SERVER_COUNT) == null ? 1 : Integer.parseInt(properties.getProperty(ConfigMgr.SERVER_COUNT));
		int serverId = properties.getProperty(ConfigMgr.SERVER_ID) == null ? 0 : Integer.parseInt(properties.getProperty(ConfigMgr.SERVER_ID));
		
		int dbPort = Integer.parseInt(dbPortStr);
		
		dbMgr.setDbHost(dbHost);
		dbMgr.setUserName(dbAdmin);
		dbMgr.setPassword(dbPassword);
		dbMgr.setDbName(dbName);
		dbMgr.setDbPort(dbPort);
		dbMgr.init();
		configMgr.load();
		configMgr.setServerCount(serverCount);
		configMgr.setServerId(serverId);
		
		
//		ReRankTask reRankTask = injector.getInstance(ReRankTask.class);
		StockUpdateTask updateFollowerCountTask = injector.getInstance(StockUpdateTask.class);
		UserInfoUpdateTask userInfoUpdateTask = injector.getInstance(UserInfoUpdateTask.class);
		//StockHistoryUpdateTask stockHistoryUpdateTask = injector.getInstance(StockHistoryUpdateTask.class);
		
//		Thread reRankThread = new Thread(reRankTask);
//		reRankThread.setName("Re-Rank");
//		reRankThread.start();
		
		Thread updateFollowerCountThread = new Thread (updateFollowerCountTask);
		updateFollowerCountThread.setName("Stock Update Task");
		updateFollowerCountThread.start();
		
		Thread asyncQueryTaskThread = new Thread(asyncQueryTask);
		asyncQueryTaskThread.setName("Async query task");
		asyncQueryTaskThread.start();
		
		Thread updateUserInfoThread = new Thread(userInfoUpdateTask);
		updateUserInfoThread.setName("User Info Update Task");
		updateUserInfoThread.start();
		
		//Thread stockHistoryUpdateThread = new Thread (stockHistoryUpdateTask);
		///stockHistoryUpdateThread.setName("Update Stock History");
		//stockHistoryUpdateThread.start();
		
		transactionCache.load();
		
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

	@Override
	public void setInjector(Injector injector) {
		this.injector = injector;
	}
}
