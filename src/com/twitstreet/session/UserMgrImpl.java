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
import com.twitstreet.db.base.DBConstants;
import com.twitstreet.db.base.DBMgr;
import com.twitstreet.db.data.Group;
import com.twitstreet.db.data.User;
import com.twitstreet.util.Util;

public class UserMgrImpl implements UserMgr {
	private static final int TOP = 100;
	@Inject
	DBMgr dbMgr;
	@Inject ConfigMgr configMgr;
	@Inject GroupMgr groupMgr;
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
							"users.cash, " + 
							"lastIp, " + 
							"oauthToken, " +
							"oauthTokenSecret, " + 
							"rank, " +
							"oldRank, " + 
							"direction, " + 
							"pictureUrl, " + 
							"portfolio_value(id) as portfolio " + 
							"from users,ranking where ranking.user_id = users.id and users.id = ?");
			ps.setLong(1, id);
			rs = ps.executeQuery();
			if (rs.next()) {
				userDO = new User();
				userDO.setId(rs.getLong("id"));
				userDO.setRank(rs.getInt("rank"));
				userDO.setOldRank(rs.getInt("oldRank"));
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
			
			logger.debug(DBConstants.QUERY_EXECUTION_SUCC + ps.toString());
		} catch (SQLException ex) {
			logger.error(DBConstants.QUERY_EXECUTION_FAIL + ps.toString(), ex);
		} finally {
			dbMgr.closeResources(connection, ps, rs);
		}
		return userDO;
	}
	public ArrayList<User> getUsersByGroup(Group group) {
		ArrayList<User> users = new ArrayList<User>();
		if(group.getId()<1 && group.getName() != null && group.getName().length()>0){
			group = groupMgr.getGroup(group.getName());		
		}
		
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
							"users.cash, " + 
							"lastIp, " + 
							"oauthToken, " +
							"oauthTokenSecret, " + 
							"rank, " +
							"oldRank, " + 
							"direction, " + 
							"pictureUrl, " + 
							"portfolio_value(id) as portfolio " + 
							"from users,ranking, user_group where ranking.user_id = users.id and user_group.user_id = users.id and user_group.group_id = ? ");
			ps.setLong(1, group.getId());
			rs = ps.executeQuery();
			while (rs.next()) {
				userDO = new User();
				userDO.setId(rs.getLong("id"));
				userDO.setRank(rs.getInt("rank"));
				userDO.setOldRank(rs.getInt("oldRank"));
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
				
				users.add(userDO);
			}
			
			logger.debug(DBConstants.QUERY_EXECUTION_SUCC + ps.toString());
		} catch (SQLException ex) {
			logger.error(DBConstants.QUERY_EXECUTION_FAIL + ps.toString(), ex);
		} finally {
			dbMgr.closeResources(connection, ps, rs);
		}
		return users;
	}
	
	
	public void assignInitialRankToUser(User userDO){

		Connection connection = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			connection = dbMgr.getConnection();
			
			ps = connection.prepareStatement("(select (count(*)+1) as newrank from ranking where (portfolio + cash) > ? " +
					" or  ? > (select userName from users,ranking where ranking.user_id = users.id and (ranking.portfolio + ranking.cash) = ?)) ");
			
			ps.setDouble(1, userDO.getCash());
			ps.setString(2, userDO.getUserName());
			ps.setDouble(3, userDO.getCash());
			rs = ps.executeQuery();
			int newRank = 999999;
			if(rs.next()){
			  newRank = rs.getInt("newrank");
			}
			
			rs.close();
			ps.close();
			
			ps = connection.prepareStatement("insert into ranking(user_id, cash,portfolio,rank,oldRank,direction,lastUpdate)" + " values(?,?,?,?,?,?,NOW())");
			ps.setLong(1, userDO.getId());
			ps.setDouble(2, userDO.getCash());
			ps.setDouble(3, 0);
			ps.setInt(4, newRank);
			ps.setInt(5, newRank);
			ps.setInt(6, 0);

			ps.executeUpdate();
			logger.debug(DBConstants.QUERY_EXECUTION_SUCC + ps.toString());
		} catch (MySQLIntegrityConstraintViolationException e) {
			logger.warn("DB: User already exists in ranking - UserId:" + userDO.getId()
					+ " User Name: " + userDO.getUserName() + " - "
					+ e.getMessage());
		} catch (SQLException ex) {
			logger.error(DBConstants.QUERY_EXECUTION_FAIL + ps.toString(), ex);
		} finally {
			dbMgr.closeResources(connection, ps, rs);
		}
	
		
	}

	public void saveUser(User userDO) {
		Connection connection = null;
		PreparedStatement ps = null;
		try {
			connection = dbMgr.getConnection();
			
		
			
			ps = connection
					.prepareStatement("insert into users(id, userName, "
							+ "lastLogin, firstLogin, "
							+ "cash, lastIp, oauthToken, oauthTokenSecret, pictureUrl) "
							+ "values(?, ?, ?, ?, ?, ?, ?, ?, ?)");
			ps.setLong(1, userDO.getId());
			ps.setString(2, userDO.getUserName());
			ps.setDate(3, Util.toSqlDate(userDO.getLastLogin()));
			ps.setDate(4, Util.toSqlDate(userDO.getFirstLogin()));
			ps.setDouble(5, userDO.getCash());
			ps.setString(6, userDO.getLastIp());
			ps.setString(7, userDO.getOauthToken());
			ps.setString(8, userDO.getOauthTokenSecret());
			ps.setString(9, userDO.getPictureUrl());

			ps.executeUpdate();
			
			
			
			
			logger.debug(DBConstants.QUERY_EXECUTION_SUCC + ps.toString());
		} catch (MySQLIntegrityConstraintViolationException e) {
			logger.warn("DB: User already exist - UserId:" + userDO.getId()
					+ " User Name: " + userDO.getUserName() + " - "
					+ e.getMessage());
		} catch (SQLException ex) {
			logger.error(DBConstants.QUERY_EXECUTION_FAIL + ps.toString(), ex);
		} finally {
			dbMgr.closeResources(connection, ps, null);
		}
		
		assignInitialRankToUser(userDO);
		groupMgr.addUserToDefaultGroup(userDO);
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
			logger.debug(DBConstants.QUERY_EXECUTION_SUCC + ps.toString());
		} catch (SQLException ex) {
			logger.error(DBConstants.QUERY_EXECUTION_FAIL + ps.toString(), ex);
		} finally {
			dbMgr.closeResources(connection, ps, null);
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
							"ranking.cash, " + 
							"lastIp, " + 
							"oauthToken, " + 
							"oauthTokenSecret, " + 
							"rank, " +
							"oldRank, " + 
							"direction, " + 
							"pictureUrl, " + 
							"ranking.portfolio " + 
							"from users,ranking where  ranking.user_id = users.id and users.id >= (select floor( max(id) * rand()) from users ) " + 
							"order by users.id limit 1");
			if (rs.next()) {
				user = new User();
				user.setId(rs.getLong("id"));
				user.setRank(rs.getInt("rank"));
				user.setOldRank(rs.getInt("oldRank"));
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
			logger.error(DBConstants.QUERY_EXECUTION_FAIL + stmt.toString(), e);
		} finally {
			dbMgr.closeResources(connection, stmt, rs);

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
			logger.debug(DBConstants.QUERY_EXECUTION_SUCC + ps.toString());
		} catch (SQLException ex) {
			logger.error(DBConstants.QUERY_EXECUTION_FAIL + ps.toString(), ex);
		} finally {
			dbMgr.closeResources(connection, ps, null);
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
			logger.debug(DBConstants.QUERY_EXECUTION_SUCC + ps.toString());
		} catch (SQLException ex) {
			logger.error(DBConstants.QUERY_EXECUTION_FAIL + ps.toString(), ex);
		} finally {
			dbMgr.closeResources(connection, ps, null);
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
							+ "lastLogin, firstLogin, ranking.cash as userCash, "
							+ "ranking.portfolio, lastIp, oauthToken, "
							+ "oauthTokenSecret, rank, oldRank, direction, pictureUrl from users,ranking where ranking.user_id= users.id " 
							+" order by rank asc limit "
							+ TOP);
			rs = ps.executeQuery();
			logger.debug(DBConstants.QUERY_EXECUTION_SUCC + ps.toString());
			while (rs.next()) {
				userDO = new User();
				userDO.setId(rs.getLong("id"));
				userDO.setRank(rs.getInt("rank"));
				userDO.setOldRank(rs.getInt("oldRank"));
				userDO.setDirection(rs.getInt("direction"));
				userDO.setUserName(rs.getString("userName"));
				userDO.setLastLogin(rs.getDate("lastLogin"));
				userDO.setFirstLogin(rs.getDate("firstLogin"));
				userDO.setCash(rs.getDouble("userCash"));
				userDO.setPortfolio(rs.getDouble("portfolio"));
				userDO.setLastIp(rs.getString("lastIp"));
				userDO.setOauthToken(rs.getString("oauthToken"));
				userDO.setOauthTokenSecret(rs.getString("oauthTokenSecret"));
				userDO.setPictureUrl(rs.getString("pictureUrl"));
				userList.add(userDO);
			}
		} catch (SQLException ex) {
			logger.error(DBConstants.QUERY_EXECUTION_FAIL + ps.toString(), ex);
		} finally {
			dbMgr.closeResources(connection, ps, rs);
		}
		return userList;
	}
}
