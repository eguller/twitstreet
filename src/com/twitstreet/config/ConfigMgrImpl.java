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
package com.twitstreet.config;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;

import org.apache.log4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.twitstreet.db.base.DBConstants;
import com.twitstreet.db.base.DBMgr;
import com.twitstreet.db.data.Config;

@Singleton
public class ConfigMgrImpl implements ConfigMgr{
	private static Logger logger = Logger.getLogger(ConfigMgrImpl.class);
	@Inject DBMgr dbMgr;
	private HashMap<String, Config> configMap = new HashMap<String, Config>();
	
	int serverCount;
	int serverId;
	boolean dev = true;

	
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
			
			
			if(isDev()){
				setServerCount(1);
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

	private void setConfig(String parm, Config config){
		configMap.put(parm,config);
	}
	private Config getConfig(String parm){
		return configMap.get(parm);
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
		return Double.parseDouble(get(ConfigMgr.INITIAL_MONEY));
	}

	@Override
	public int getComissionTreshold() {
		return Integer.parseInt(get(ConfigMgr.COMISSION_TRESHOLD));
	}

	@Override
	public int getServerCount() {
		return Integer.valueOf(get(ConfigMgr.SERVER_COUNT));
	}

	@Override
	public void setServerCount(int serverCount) {
		
		Config config = getConfig(SERVER_COUNT);
		if(config != null){
			config.setVal(String.valueOf(serverCount));
		}
		else{
			config = new Config();
			config.setParm(SERVER_COUNT);
			config.setVal(ConfigMgr.DEFAULT_SERVER_COUNT);
		}
		setConfig(SERVER_COUNT, config);
	}

	@Override
	public int getServerId() {
		return serverId;
	}

	@Override
	public void setServerId(int serverId) {
		this.serverId = serverId;
	}

	@Override
	public String getAnnouncerConsumerKey() {
		return get(ConfigMgr.ANNOUNCER_CONSUMER_KEY);
	}

	@Override
	public String getAnnouncerConsumerSecret() {
		return get(ConfigMgr.ANNOUNCER_CONSUMER_SECRET);
	}

	@Override
	public String getAnnouncerAccessToken() {
		return get(ConfigMgr.ANNOUNCER_ACCESS_TOKEN);
	}

	@Override
	public String getAnnouncerAccessSecret() {
		return get(ConfigMgr.ANNOUNCER_ACCESS_SECRET);
	}

	@Override
	public boolean isDev() {
		return dev;
	}

	@Override
	public void setDev(boolean dev) {
		this.dev = dev;
	}

	@Override
	public boolean isMaster() {
		return ConfigMgr.masterIdSet.contains(getServerId());
	}

	
}
