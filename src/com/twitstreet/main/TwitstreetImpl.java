package com.twitstreet.main;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Properties;

import javax.servlet.ServletContext;

import org.apache.log4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;
import com.twitstreet.config.ConfigMgr;
import com.twitstreet.db.base.DBConstants;
import com.twitstreet.db.base.DBMgr;
import com.twitstreet.db.base.DBMgrImpl;
import com.twitstreet.db.data.User;
import com.twitstreet.market.StockMgr;
import com.twitstreet.market.StockMgrImpl;
import com.twitstreet.session.GroupMgr;
import com.twitstreet.session.UserMgr;
import com.twitstreet.task.DetectInvalidTokensTask;
import com.twitstreet.task.StockUpdateTask;
import com.twitstreet.task.UserInfoUpdateTask;


@Singleton
public class TwitstreetImpl implements Twitstreet {
	private static String SELECT_FROM_SEASON_INFO = " select id, startTime, endTime, active from season_info ";
	private boolean initialized = false;
	DBMgr dbMgr;
	ConfigMgr configMgr;
	ServletContext servletContext;
	Injector injector;
	@Inject GroupMgr groupMgr;
	@Inject UserMgr userMgr;
	private static Logger logger = Logger.getLogger(TwitstreetImpl.class);
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
		boolean dev = properties.getProperty(ConfigMgr.STAGE) == null ? true : properties.getProperty(ConfigMgr.STAGE).equalsIgnoreCase(ConfigMgr.DEV);
		
		
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
		configMgr.setDev(dev);
		
		
		StockUpdateTask updateFollowerCountTask = injector.getInstance(StockUpdateTask.class);
		UserInfoUpdateTask userInfoUpdateTask = injector.getInstance(UserInfoUpdateTask.class);
		DetectInvalidTokensTask detectInvalidTokensTask = injector.getInstance(DetectInvalidTokensTask.class);

		Thread detectInvalidTokensThread= new Thread (detectInvalidTokensTask);
		detectInvalidTokensThread.setName("Detect Invalid Tokens Task");
		
		Thread updateFollowerCountThread = new Thread (updateFollowerCountTask);
		updateFollowerCountThread.setName("Stock Update Task");

		Thread updateUserInfoThread = new Thread(userInfoUpdateTask);
		updateUserInfoThread.setName("User Info Update Task");
		
		

		updateFollowerCountThread.start();
		
		if (!configMgr.isDev()) {
			detectInvalidTokensThread.start();		
			updateUserInfoThread.start();
		}
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

	@Override
	public SeasonInfo getCurrentSeasonInfo(){
		Connection connection = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		SeasonInfo siDO = null;

		try {
			connection = dbMgr.getConnection();
			ps = connection.prepareStatement(SELECT_FROM_SEASON_INFO + " where active = true");

			rs = ps.executeQuery();
			if (rs.next()) {
				siDO = new SeasonInfo();
				siDO.getDataFromResultSet(rs);
			}

			logger.debug(DBConstants.QUERY_EXECUTION_SUCC + ps.toString());
		} catch (SQLException ex) {
			logger.error(DBConstants.QUERY_EXECUTION_FAIL + ps.toString(), ex);
		} finally {
			dbMgr.closeResources(connection, ps, rs);
		}
		return siDO;

	}
	@Override
	public SeasonInfo getSeasonInfo(int id){
		Connection connection = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		SeasonInfo siDO = null;

		try {
			connection = dbMgr.getConnection();
			ps = connection.prepareStatement(SELECT_FROM_SEASON_INFO + " where id = ?");
			ps.setInt(1, id);
			rs = ps.executeQuery();
			if (rs.next()) {
				siDO = new SeasonInfo();
				siDO.getDataFromResultSet(rs);
			}

			logger.debug(DBConstants.QUERY_EXECUTION_SUCC + ps.toString());
		} catch (SQLException ex) {
			logger.error(DBConstants.QUERY_EXECUTION_FAIL + ps.toString(), ex);
		} finally {
			dbMgr.closeResources(connection, ps, rs);
		}
		return siDO;
	}
	@Override
	public ArrayList<SeasonInfo> getAllSeasons(){
		Connection connection = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		SeasonInfo siDO = null;
		ArrayList<SeasonInfo> siList = new ArrayList<SeasonInfo>();
		try {
			connection = dbMgr.getConnection();
			ps = connection.prepareStatement(SELECT_FROM_SEASON_INFO + " order by id desc");
			rs = ps.executeQuery();
			
			while (rs.next()) {
				siDO = new SeasonInfo();
				siDO.getDataFromResultSet(rs);
				siList.add(siDO);
			}

			logger.debug(DBConstants.QUERY_EXECUTION_SUCC + ps.toString());
		} catch (SQLException ex) {
			logger.error(DBConstants.QUERY_EXECUTION_FAIL + ps.toString(), ex);
		} finally {
			dbMgr.closeResources(connection, ps, rs);
		}
		return siList;
	}
}
