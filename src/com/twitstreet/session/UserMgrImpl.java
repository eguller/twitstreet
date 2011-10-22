package com.twitstreet.session;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.log4j.Logger;


import com.google.inject.Inject;
import com.twitstreet.db.base.DBMgr;
import com.twitstreet.db.data.UserDO;
import com.twitstreet.util.Util;

public class UserMgrImpl implements UserMgr {
	@Inject
	DBMgr dbMgr;
	private static Logger logger = Logger.getLogger(UserMgrImpl.class);


	public UserDO getUserById(long id) throws SQLException {
		Connection connection = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		UserDO userDO = null;
		connection = dbMgr.getConnection();
		ps = connection.prepareStatement("select * from user where id = ?");
		ps.setLong(0, id);
		rs = ps.executeQuery();
		while (rs.next()) {
			userDO = new UserDO();
			userDO.setId(rs.getLong("id"));
			userDO.setUserName(rs.getString("userName"));
			userDO.setLastLogin(rs.getDate("lastLogin"));
			userDO.setFirstLogin(rs.getDate("firstLogin"));
			userDO.setCash(rs.getInt("cash"));
			userDO.setPortfolio(rs.getInt("portfolio"));
			userDO.setLastIp(rs.getString("lastIp"));
			break;
		}
		rs.close();
		ps.close();
		connection.close();
		return userDO;
	}

	public  void saveUser(UserDO userDO) throws SQLException {
		Connection connection = null;
		PreparedStatement ps = null;
		
		ps = connection.prepareStatement(
				"insert into user(id, userName, " +
				"lastLogin, firstLogin, " +
				"cash, portfolio, lastIp) " +
				"values(?, ?, ?, ?, ?, ?))");
		ps.setLong(0, userDO.getId());
		ps.setString(1, userDO.getUserName());
		ps.setDate(2,  Util.toSqlDate(userDO.getLastLogin()));
		ps.setDate(3, Util.toSqlDate(userDO.getFirstLogin()));
		ps.setInt(4, userDO.getCash());
		ps.setInt(5, userDO.getPortfolio());
		ps.setString(6, userDO.getLastIp());
		ps.executeUpdate();
		ps.close();
		connection.close();
		
	}

	public UserDO getUserByName(String userName) throws SQLException {
		Connection connection = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		UserDO userDO = null;
		connection = dbMgr.getConnection();
		ps = connection.prepareStatement("select * from user where userName = ?");
		ps.setString(0, userName);
		rs = ps.executeQuery();
		while (rs.next()) {
			userDO = new UserDO();
			userDO.setId(rs.getLong("id"));
			userDO.setUserName(rs.getString("userName"));
			userDO.setLastLogin(rs.getDate("lastLogin"));
			userDO.setFirstLogin(rs.getDate("firstLogin"));
			userDO.setCash(rs.getInt("cash"));
			userDO.setPortfolio(rs.getInt("portfolio"));
			userDO.setLastIp(rs.getString("lastIp"));
			break;
		}
		rs.close();
		ps.close();
		connection.close();
		return userDO;
	}
}
