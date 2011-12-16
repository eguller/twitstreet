package com.twitstreet.session;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.log4j.Logger;

import com.google.inject.Inject;
import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;
import com.twitstreet.db.base.DBMgr;
import com.twitstreet.db.data.User;
import com.twitstreet.util.Util;

public class UserMgrImpl implements UserMgr {
	@Inject
	DBMgr dbMgr;
	private static Logger logger = Logger.getLogger(UserMgrImpl.class);

	public User getUserById(long id) throws SQLException {
		Connection connection = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		User userDO = null;
		connection = dbMgr.getConnection();
		ps = connection
				.prepareStatement("select id, userName, "
						+ "lastLogin, firstLogin, cash, "
						+ "portfolio, lastIp, oauthToken, oauthTokenSecret from users where id = ?");
		ps.setLong(1, id);

		try {
			rs = ps.executeQuery();
			logger.debug("DB: Query executed successfully - " + ps.toString());
			while (rs.next()) {
				userDO = new User();
				userDO.setId(rs.getLong("id"));
				userDO.setUserName(rs.getString("userName"));
				userDO.setLastLogin(rs.getDate("lastLogin"));
				userDO.setFirstLogin(rs.getDate("firstLogin"));
				userDO.setCash(rs.getInt("cash"));
				userDO.setPortfolio(rs.getInt("portfolio"));
				userDO.setLastIp(rs.getString("lastIp"));
				userDO.setOauthToken(rs.getString("oauthToken"));
				userDO.setOauthTokenSecret(rs.getString("oauthTokenSecret"));
				break;
			}
		} catch (SQLException ex) {
			logger.error("DB: Query failed - " + ps.toString(), ex);
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

	public void saveUser(User userDO) throws SQLException {
		Connection connection = null;
		PreparedStatement ps = null;
		connection = dbMgr.getConnection();
		ps = connection.prepareStatement("insert into users(id, userName, "
				+ "lastLogin, firstLogin, "
				+ "cash, portfolio, lastIp, oauthToken, oauthTokenSecret) "
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
		} catch (MySQLIntegrityConstraintViolationException e) {
			logger.warn("DB: User already exist - UserId:" + userDO.getId()
					+ " User Name: " + userDO.getUserName() + " - "
					+ e.getMessage());
		} catch (SQLException ex) {
			logger.error("DB: Query failed = " + ps.toString(), ex);
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

	public User getUserByName(String userName) throws SQLException {
		Connection connection = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		User userDO = null;
		connection = dbMgr.getConnection();
		ps = connection.prepareStatement("select id, userName, "
				+ "lastLogin, firstLogin, cash, "
				+ "portfolio, lastIp, oauthToken, "
				+ "oauthTokenSecret from users where userName = ?");
		ps.setString(1, userName);
		try {
			rs = ps.executeQuery();
			logger.debug("DB: Query executed successfully - " + ps.toString());
			while (rs.next()) {
				userDO = new User();
				userDO.setId(rs.getLong("id"));
				userDO.setUserName(rs.getString("userName"));
				userDO.setLastLogin(rs.getDate("lastLogin"));
				userDO.setFirstLogin(rs.getDate("firstLogin"));
				userDO.setCash(rs.getInt("cash"));
				userDO.setPortfolio(rs.getInt("portfolio"));
				userDO.setLastIp(rs.getString("lastIp"));
				userDO.setOauthToken(rs.getString("oauthToken"));
				userDO.setOauthTokenSecret(rs.getString("oauthTokenSecret"));
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

	@Override
	public void updateUser(User user) throws SQLException {
		Connection connection = null;
		PreparedStatement ps = null;
		connection = dbMgr.getConnection();
		ps = connection.prepareStatement("update users set userName = ?, "
				+ "lastLogin = ?, "
				+ "lastIp = ?, oauthToken = ?, oauthTokenSecret = ?");
		ps.setString(1, user.getUserName());
		ps.setDate(2, Util.toSqlDate(user.getLastLogin()));
		ps.setString(3, user.getLastIp());
		ps.setString(4, user.getOauthToken());
		ps.setString(5, user.getOauthTokenSecret());
		try {
			ps.executeUpdate();
			logger.debug("DB: Query executed successfully - " + ps.toString());
		} catch (SQLException ex) {
			logger.error("DB: Query failed = " + ps.toString(), ex);
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

	@Override
	public User random() {
		Connection connection = null;
		Statement stmt = null;
		User user = null;
		ResultSet rs = null;
		try {
			connection = dbMgr.getConnection();
			stmt = connection.createStatement();
			rs = stmt
					.executeQuery("select id, userName, "
							+ "lastLogin, firstLogin, cash, "
							+ "portfolio, lastIp, oauthToken, oauthTokenSecret from users where id >= (select floor( max(id) * rand()) from users ) order by id limit 1;");
			if (rs.next()) {
				user = new User();
				user.setId(rs.getLong("id"));
				user.setUserName(rs.getString("userName"));
				user.setLastLogin(rs.getDate("lastLogin"));
				user.setFirstLogin(rs.getDate("firstLogin"));
				user.setCash(rs.getInt("cash"));
				user.setPortfolio(rs.getInt("portfolio"));
				user.setLastIp(rs.getString("lastIp"));
				user.setOauthToken(rs.getString("oauthToken"));
				user.setOauthTokenSecret(rs.getString("oauthTokenSecret"));
			} else {
				logger.error("DB: Random user selection query is not working properly");
			}
		} catch (SQLException e) {
			logger.error("DB: Query failed = " + stmt.toString(), e);
		} finally {
			try {
				if (!rs.isClosed()) {
					rs.close();
				}
				if (!stmt.isClosed()) {
					stmt.close();
				}
				if (!connection.isClosed()) {
					connection.close();
				}
			} catch (SQLException e) {
				logger.error("DB: Resources could not be closed properly", e);
			}

		}
		return user;
	}

	@Override
	public void increaseCash(long userId, int cash) throws SQLException{
		Connection connection = null;
		PreparedStatement ps = null;
		connection = dbMgr.getConnection();
		ps = connection.prepareStatement("update users set cash = (cash + ?) where id = ?");
		ps.setInt(1, cash);
		ps.setLong(2, userId);

		try {
			ps.executeUpdate();
			logger.debug("DB: Query executed successfully - " + ps.toString());
		} catch (SQLException ex) {
			logger.error("DB: Query failed = " + ps.toString(), ex);
			throw ex;
		} finally {
			if (!ps.isClosed()) {
				ps.close();
			}
			if (!connection.isClosed()) {
				connection.close();
			}
		}
	}

	@Override
	public void updateCashAndPortfolio(long userId, int amount) throws SQLException{
		Connection connection = null;
		PreparedStatement ps = null;
		connection = dbMgr.getConnection();
		ps = connection.prepareStatement("update users set cash = (cash - ?), portfolio = (portfolio + ?)  where id = ?");
		ps.setInt(1, amount);
		ps.setInt(2, amount);
		ps.setLong(3, userId);
		try {
			ps.executeUpdate();
			logger.debug("DB: Query executed successfully - " + ps.toString());
		} catch (SQLException ex) {
			logger.error("DB: Query failed = " + ps.toString(), ex);
			throw ex;
		} finally {
			if (!ps.isClosed()) {
				ps.close();
			}
			if (!connection.isClosed()) {
				connection.close();
			}
		}
	}
}
