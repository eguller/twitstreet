package com.twitstreet.session;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.log4j.Logger;

import com.google.inject.Inject;
import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;
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
		ps = connection.prepareStatement("select id, userName, "
				+ "lastLogin, firstLogin, cash, "
				+ "portfolio, lastIp from users where id = ?");
		ps.setLong(1, id);

		try {
			rs = ps.executeQuery();
			logger.debug("DB: Query executed successfully - " + ps.toString());
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
		} catch (SQLException ex) {
			logger.debug("DB: Query failed - " + ps.toString());
			throw ex;
		} finally {
			if (!rs.isClosed()) {
				rs.close();
			}
			if (!ps.isClosed()) {
				ps.close();
			}
			if (!connection.isClosed()) {
				connection.close();
			}
		}
		return userDO;
	}

	public void saveUser(UserDO userDO) throws SQLException {
		Connection connection = null;
		PreparedStatement ps = null;
		connection = dbMgr.getConnection();
		ps = connection.prepareStatement("insert into users(id, userName, "
				+ "lastLogin, firstLogin, " + "cash, portfolio, lastIp, oauthToken, oauthTokenSecret) "
				+ "values(?, ?, ?, ?, ?, ?, ?, ?, ?)");
		ps.setLong(1, userDO.getId());
		ps.setString(2, userDO.getUserName());
		ps.setDate(3, Util.toSqlDate(userDO.getLastLogin()));
		ps.setDate(4, Util.toSqlDate(userDO.getFirstLogin()));
		ps.setInt(5, userDO.getCash());
		ps.setInt(6, userDO.getPortfolio());
		ps.setString(7, userDO.getLastIp());
		ps.setString(8, userDO.getOauthToken());
		ps.setString(9, userDO.getOauthTokenSecret());
		try {
			ps.executeUpdate();
			logger.debug("DB: Query executed successfully - " + ps.toString());
		} 
		catch (MySQLIntegrityConstraintViolationException e) {
			logger.debug("DB: User already exists");
			throw e;
		}
		catch (SQLException ex) {
			logger.debug("DB: Query failed = " + ps.toString());
			throw ex;
		} finally {
			if (!ps.isClosed()) {
				ps.close();
			}
			if (!ps.isClosed()) {
				connection.close();
			}
		}

	}

	public UserDO getUserByName(String userName) throws SQLException {
		Connection connection = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		UserDO userDO = null;
		connection = dbMgr.getConnection();
		ps = connection
				.prepareStatement("select id, userName, " +
						"lastLogin, firstLogin, " +
						"cash, portfolio, lastIp " +
						"from users where userName = ?");
		ps.setString(1, userName);
		try {
			rs = ps.executeQuery();
			logger.debug("DB: Query executed successfully - " + ps.toString());
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
		} catch (SQLException ex) {
			throw ex;
		} finally {
			rs.close();
			ps.close();
			connection.close();
		}
		return userDO;
	}
}
