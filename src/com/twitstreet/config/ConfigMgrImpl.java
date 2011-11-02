package com.twitstreet.config;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.twitstreet.db.base.DBMgr;
import com.twitstreet.db.data.Config;

@Singleton
public class ConfigMgrImpl implements ConfigMgr{
	@Inject DBMgr dbMgr;
	private HashMap<String, Config> configMap = new HashMap<String, Config>();
	
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
			rs.close();
			stmt.close();
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
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
	public String getMinFollower() {
		return get(ConfigMgr.MIN_FOLLOWER);
	}
}
