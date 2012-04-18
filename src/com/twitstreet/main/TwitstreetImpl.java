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


package com.twitstreet.main;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

import javax.servlet.ServletContext;

import org.apache.log4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;
import com.twitstreet.config.ConfigMgr;
import com.twitstreet.db.base.DBMgr;
import com.twitstreet.market.StockMgr;
import com.twitstreet.season.SeasonMgr;
import com.twitstreet.session.GroupMgr;
import com.twitstreet.session.UserMgr;
import com.twitstreet.task.DetectInvalidTokensTask;
import com.twitstreet.task.NewSeasonInfoSentTask;
import com.twitstreet.task.SeasonTask;
import com.twitstreet.task.StockUpdateTask;
import com.twitstreet.task.UserInfoUpdateTask;
import com.twitstreet.twitter.AdsListenerMgr;
import com.twitstreet.twitter.AnnouncerMgr;

@Singleton
public class TwitstreetImpl implements Twitstreet {

	private boolean initialized = false;
	private static Logger logger = Logger.getLogger(TwitstreetImpl.class);
	
	DBMgr dbMgr;
	ConfigMgr configMgr;
	ServletContext servletContext;
	Injector injector;
	@Inject
	GroupMgr groupMgr;
	@Inject
	UserMgr userMgr;

	@Inject
	SeasonMgr seasonMgr;

	@Inject
	StockMgr stockMgr;
	
	@Inject AnnouncerMgr announcerMgr;
	
	@Inject AdsListenerMgr adsListenerMgr;
	

	@Inject
	public TwitstreetImpl(DBMgr dbMgr, ConfigMgr configMgr) {
		this.dbMgr = dbMgr;
		this.configMgr = configMgr;
	}


	@Override
	public void initialize() {
		loadConfiguration();
		
		seasonMgr.loadSeasonInfo();
		announcerMgr.loadAnnouncers();
		
		adsListenerMgr.start();
		if (configMgr.isDev() || !configMgr.isMaster()) {

			startTasks();

		}
		if(configMgr.isMaster() || configMgr.isDev() ){
			startSeasonTask();
		}
		if(!configMgr.isDev()){
			startNewSeasonInfoSentTask();
		}
		initialized = true;
	}


	private void startNewSeasonInfoSentTask() {
		NewSeasonInfoSentTask newSeasonInfoSentTask = injector.getInstance(NewSeasonInfoSentTask.class);
		Thread newSeasonInfoSentThread = new Thread(newSeasonInfoSentTask);
		newSeasonInfoSentThread.setName("New Season Info Sent Task");
		newSeasonInfoSentThread.start();
	}

	private void startSeasonTask() {
		SeasonTask seasonTask = injector.getInstance(SeasonTask.class);

		Thread seasonTaskThread = new Thread(seasonTask);
		seasonTaskThread.setName("Season Task");

		seasonTaskThread.start();

	}


	private void startTasks() {
		StockUpdateTask updateFollowerCountTask = injector.getInstance(StockUpdateTask.class);
		UserInfoUpdateTask userInfoUpdateTask = injector.getInstance(UserInfoUpdateTask.class);
		DetectInvalidTokensTask detectInvalidTokensTask = injector.getInstance(DetectInvalidTokensTask.class);
		
		
		
		Thread detectInvalidTokensThread = new Thread(detectInvalidTokensTask);
		detectInvalidTokensThread.setName("Detect Invalid Tokens Task");

		Thread updateFollowerCountThread = new Thread(updateFollowerCountTask);
		updateFollowerCountThread.setName("Stock Update Task");

		Thread updateUserInfoThread = new Thread(userInfoUpdateTask);
		updateUserInfoThread.setName("User Info Update Task");
		
		updateFollowerCountThread.start();
		
		if (!configMgr.isDev()) {
			detectInvalidTokensThread.start();
			updateUserInfoThread.start();
			
		}
		
		
	}

	private void loadConfiguration(){
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

		int serverId = properties.getProperty(ConfigMgr.SERVER_ID) == null ? 0 : Integer.parseInt(properties.getProperty(ConfigMgr.SERVER_ID));
		boolean dev = properties.getProperty(ConfigMgr.STAGE) == null ? true : properties.getProperty(ConfigMgr.STAGE).equalsIgnoreCase(ConfigMgr.DEV);
		int dbPort = Integer.parseInt(dbPortStr);

		dbMgr.setDbHost(dbHost);
		dbMgr.setUserName(dbAdmin);
		dbMgr.setPassword(dbPassword);
		dbMgr.setDbName(dbName);
		dbMgr.setDbPort(dbPort);
		dbMgr.init();
		configMgr.load();
		configMgr.setServerId(serverId);
		configMgr.setDev(dev);
		
	}
	
	public boolean isInitialized() {
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
