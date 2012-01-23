package com.twitstreet.session;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import org.apache.log4j.Logger;

import com.google.inject.Inject;
import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;
import com.twitstreet.config.ConfigMgr;
import com.twitstreet.db.base.DBMgr;
import com.twitstreet.db.data.User;
import com.twitstreet.util.Util;

public class UserMgrImpl implements UserMgr {
	private static final int TOP = 100;
	@Inject
	DBMgr dbMgr;
	@Inject ConfigMgr configMgr;
	private static Logger logger = Logger.getLogger(UserMgrImpl.class);

	public User getUserById(long id) {
		Connection connection = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		User userDO = null;
		try {
			connection = dbMgr.getConnection();
			ps = connection
					.prepareStatement("select " + 
							"id, " + 
							"userName, " + 
							"lastLogin, " + 
							"firstLogin, " + 
							"cash, " + 
							"lastIp, " + 
							"oauthToken, " +
							"oauthTokenSecret, " + 
							"rank, " + 
							"direction, " + 
							"pictureUrl, " + 
							"portfolio_value(id) as portfolio " + 
							"from users where users.id = ?");
			ps.setLong(1, id);
			rs = ps.executeQuery();
			if (rs.next()) {
				userDO = new User();
				userDO.setId(rs.getLong("id"));
				userDO.setRank(rs.getInt("rank"));
				userDO.setDirection(rs.getInt("direction"));
				userDO.setUserName(rs.getString("userName"));
				userDO.setLastLogin(rs.getDate("lastLogin"));
				userDO.setFirstLogin(rs.getDate("firstLogin"));
				userDO.setCash(rs.getDouble("cash"));
				userDO.setPortfolio(rs.getDouble("portfolio"));
				userDO.setLastIp(rs.getString("lastIp"));
				userDO.setOauthToken(rs.getString("oauthToken"));
				userDO.setOauthTokenSecret(rs.getString("oauthTokenSecret"));
				userDO.setPictureUrl(rs.getString("pictureUrl"));
			}
			
			logger.debug("DB: Query executed successfully - " + ps.toString());
		} catch (SQLException ex) {
			logger.error("DB: Query failed - " + ps.toString(), ex);
		} finally {
			try {
				if (rs != null && !rs.isClosed()) {
					rs.close();
				}
				if (ps != null && !ps.isClosed()) {
					ps.close();
				}
				if (connection != null && !connection.isClosed()) {
					connection.close();
				}
			} catch (SQLException ex) {
				logger.error("DB: Releasing resources failed.", ex);
			}
		}
		return userDO;
	}

	public void saveUser(User userDO) {
		Connection connection = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			connection = dbMgr.getConnection();
			
			ps = connection.prepareStatement("(select (count(*)+1) as newrank from users where (portfolio_value(id) + cash) > "+configMgr.getInitialMoney()+")");
			rs = ps.executeQuery();
			int newRank = 9999;
			if(rs.next()){
			  newRank = rs.getInt("newrank");
			}
			
			rs.close();
			ps.close();
			
			ps = connection
					.prepareStatement("insert into users(id, userName, "
							+ "lastLogin, firstLogin, "
							+ "cash, lastIp, oauthToken, oauthTokenSecret, pictureUrl, rank) "
							+ "values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
			ps.setLong(1, userDO.getId());
			ps.setString(2, userDO.getUserName());
			ps.setDate(3, Util.toSqlDate(userDO.getLastLogin()));
			ps.setDate(4, Util.toSqlDate(userDO.getFirstLogin()));
			ps.setDouble(5, userDO.getCash());
			ps.setString(6, userDO.getLastIp());
			ps.setString(7, userDO.getOauthToken());
			ps.setString(8, userDO.getOauthTokenSecret());
			ps.setString(9, userDO.getPictureUrl());
			ps.setInt(10, newRank);
			
			

			ps.executeUpdate();
			logger.debug("DB: Query executed successfully - " + ps.toString());
		} catch (MySQLIntegrityConstraintViolationException e) {
			logger.warn("DB: User already exist - UserId:" + userDO.getId()
					+ " User Name: " + userDO.getUserName() + " - "
					+ e.getMessage());
		} catch (SQLException ex) {
			logger.error("DB: Query failed = " + ps.toString(), ex);
		} finally {
			try {
				if (ps != null && !ps.isClosed()) {
					ps.close();
				}
				if (connection != null && !connection.isClosed()) {
					connection.close();
				}
			} catch (SQLException ex) {
				logger.error("DB: Releasing resources failed.", ex);
			}
		}
	}


	@Override
	public void updateUser(User user) {
		Connection connection = null;
		PreparedStatement ps = null;
		try {
			connection = dbMgr.getConnection();
			ps = connection
					.prepareStatement("update users set userName = ?, "
							+ "lastLogin = ?, "
							+ "lastIp = ?, oauthToken = ?, oauthTokenSecret = ?, pictureUrl = ? where id = ?");
			ps.setString(1, user.getUserName());
			ps.setDate(2, Util.toSqlDate(user.getLastLogin()));
			ps.setString(3, user.getLastIp());
			ps.setString(4, user.getOauthToken());
			ps.setString(5, user.getOauthTokenSecret());
			ps.setLong(6, user.getId());
			ps.setString(7, user.getPictureUrl());

			ps.executeUpdate();
			logger.debug("DB: Query executed successfully - " + ps.toString());
		} catch (SQLException ex) {
			logger.error("DB: Query failed = " + ps.toString(), ex);
		} finally {
			try {
				if (ps != null && !ps.isClosed()) {
					ps.close();
				}
				if (connection != null && !connection.isClosed()) {
					connection.close();
				}
			} catch (SQLException ex) {
				logger.error("DB: Releasing resources failed.", ex);
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
					.executeQuery("select " + 
							"id, " + 
							"userName, " + 
							"lastLogin, " + 
							"firstLogin, " + 
							"cash, " + 
							"lastIp, " + 
							"oauthToken, " + 
							"oauthTokenSecret, " + 
							"rank, " + 
							"direction, " + 
							"pictureUrl, " + 
							"portfolio_value(id) as portfolio " + 
							"from users where users.id >= (select floor( max(id) * rand()) from users ) " + 
							"order by users.id limit 1");
			if (rs.next()) {
				user = new User();
				user.setId(rs.getLong("id"));
				user.setRank(rs.getInt("rank"));
				user.setDirection(rs.getInt("direction"));
				user.setUserName(rs.getString("userName"));
				user.setLastLogin(rs.getDate("lastLogin"));
				user.setFirstLogin(rs.getDate("firstLogin"));
				user.setCash(rs.getDouble("cash"));
				user.setPortfolio((int)rs.getDouble("portfolio"));
				user.setLastIp(rs.getString("lastIp"));
				user.setOauthToken(rs.getString("oauthToken"));
				user.setOauthTokenSecret(rs.getString("oauthTokenSecret"));
				user.setPictureUrl(rs.getString("pictureUrl"));
			} else {
				logger.error("DB: Random user selection query is not working properly");
			}
		} catch (SQLException e) {
			logger.error("DB: Query failed = " + stmt.toString(), e);
		} finally {
			try {
				if (rs != null && !rs.isClosed()) {
					rs.close();
				}
				if (stmt != null && !stmt.isClosed()) {
					stmt.close();
				}
				if (connection != null && !connection.isClosed()) {
					connection.close();
				}
			} catch (SQLException e) {
				logger.error("DB: Resources could not be closed properly", e);
			}

		}
		return user;
	}

	@Override
	public void increaseCash(long userId, double cash) {
		Connection connection = null;
		PreparedStatement ps = null;
		try {
			connection = dbMgr.getConnection();
			ps = connection
					.prepareStatement("update users set cash = (cash + ?) where id = ?");
			ps.setDouble(1, cash);
			ps.setLong(2, userId);

			ps.executeUpdate();
			logger.debug("DB: Query executed successfully - " + ps.toString());
		} catch (SQLException ex) {
			logger.error("DB: Query failed = " + ps.toString(), ex);
		} finally {
			try {
				if (ps != null && !ps.isClosed()) {
					ps.close();
				}
				if (connection != null && !connection.isClosed()) {
					connection.close();
				}
			} catch (SQLException ex) {
				logger.error("DB: Releasing resources failed.", ex);
			}
		}
	}

	@Override
	public void updateCash(long userId, double amount) {
		Connection connection = null;
		PreparedStatement ps = null;
		try {
		connection = dbMgr.getConnection();
		ps = connection
				.prepareStatement("update users set cash = (cash - ?) where id = ?");
		ps.setDouble(1, amount);
		ps.setLong(2, userId);
		
			ps.executeUpdate();
			logger.debug("DB: Query executed successfully - " + ps.toString());
		} catch (SQLException ex) {
			logger.error("DB: Query failed = " + ps.toString(), ex);
		} finally {
			try {
				if (!ps.isClosed()) {
					ps.close();
				}
				if (connection != null && !connection.isClosed()) {
					connection.close();
				}
			} catch (SQLException ex) {
				logger.error("DB: Releasing resources failed.", ex);
			}
		}
	}

	@Override
	public ArrayList<User> getTopRank() {
		ArrayList<User> userList = new ArrayList<User>(100);
		Connection connection = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		User userDO = null;
		try {
			connection = dbMgr.getConnection();
			ps = connection
					.prepareStatement("select id, userName, "
							+ "lastLogin, firstLogin, cash, "
							+ "portfolio_value(id) as portfolio, lastIp, oauthToken, "
							+ "oauthTokenSecret, rank, direction, pictureUrl from users order by rank asc limit "
							+ TOP);
			rs = ps.executeQuery();
			logger.debug("DB: Query executed successfully - " + ps.toString());
			while (rs.next()) {
				userDO = new User();
				userDO.setId(rs.getLong("id"));
				userDO.setRank(rs.getInt("rank"));
				userDO.setDirection(rs.getInt("direction"));
				userDO.setUserName(rs.getString("userName"));
				userDO.setLastLogin(rs.getDate("lastLogin"));
				userDO.setFirstLogin(rs.getDate("firstLogin"));
				userDO.setCash(rs.getDouble("cash"));
				userDO.setPortfolio(rs.getDouble("portfolio"));
				userDO.setLastIp(rs.getString("lastIp"));
				userDO.setOauthToken(rs.getString("oauthToken"));
				userDO.setOauthTokenSecret(rs.getString("oauthTokenSecret"));
				userDO.setPictureUrl(rs.getString("pictureUrl"));
				userList.add(userDO);
			}
		} catch (SQLException ex) {
			logger.error("DB: Query failed = " + ps.toString(), ex);
		} finally {
			try {
				rs.close();
				ps.close();
				connection.close();
			} catch (SQLException e) {
				logger.error("DB: Resources could not be closed properly", e);
			}
		}
		return userList;
	}
}
