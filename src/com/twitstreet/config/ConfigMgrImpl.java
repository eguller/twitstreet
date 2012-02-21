package com.twitstreet.config;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;

import org.apache.log4j.Logger;

import twitter4j.auth.RequestToken;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.twitstreet.db.base.DBConstants;
import com.twitstreet.db.base.DBMgr;
import com.twitstreet.db.data.Config;
import com.twitstreet.market.StockMgrImpl;

@Singleton
public class ConfigMgrImpl implements ConfigMgr{
	private static final double INITIAL_MONEY = 10000;
	private static Logger logger = Logger.getLogger(ConfigMgrImpl.class);
	@Inject DBMgr dbMgr;
	private HashMap<String, Config> configMap = new HashMap<String, Config>();
	private RequestToken requestToken;
	
	@Inject public ConfigMgrImpl(DBMgr dbMgr) {
		this.dbMgr = dbMgr;
	}

	public void load(){
		Connection connection = null;
		Statement stmt = null;
		ResultSet rs = null;
		try {
			connection = dbMgr.getConnection();
			stmt = connection.createStatement();
			rs = stmt.executeQuery("select id, parm, val from config");
			
			while(rs.next()){
				Config config = new Config();
				config.setId(rs.getLong(Config.ID));
				config.setParm(rs.getString(Config.PARM));
				config.setVal(rs.getString(Config.VAL));
				configMap.put(config.getParm(), config);
			}
			logger.debug(DBConstants.QUERY_EXECUTION_SUCC + stmt.toString());
			logger.debug("Config manager initialized successfully.");

		} catch (SQLException e) {
			logger.error(DBConstants.QUERY_EXECUTION_FAIL + stmt == null ? "Query is null" : stmt.toString(), e);
			logger.error("Config manager initialization failed.");
		}
		finally{
			dbMgr.closeResources(connection, stmt, rs);
		}
		
		
	}
	public String get(String parm){
		Config config = configMap.get(parm);
		return config == null ? Config.NONE : config.getVal();
	}
	
	public String getConsumerKey(){
		return get(ConfigMgr.CONSUMER_KEY);
	}
	public String getConsumerSecret(){
		return get(ConfigMgr.CONSUMER_SECRET);
	}
	@Override
	public int getMinFollower() {
		return Integer.parseInt(get(ConfigMgr.MIN_FOLLOWER));
	}

	@Override
	public String getGaAccount() {
		return get(ConfigMgr.GA_ACCOUNT);
	}

	@Override
	public double getInitialMoney() {
		return INITIAL_MONEY;
	}

	@Override
	public int getComissionTreshold() {
		return Integer.parseInt(get(ConfigMgr.COMISSION_TRESHOLD));
	}
}
