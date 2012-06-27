/**
	TwitStreet - Twitter Stock Market Game
    Copyright (C) 2012  Engin Guller (bisanthe@gmail.com), Cagdas Ozek (cagdasozek@gmail.com)

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

package com.twitstreet.session;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.apache.log4j.Logger;

import com.google.inject.Inject;
import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;
import com.twitstreet.config.ConfigMgr;
import com.twitstreet.db.base.DBConstants;
import com.twitstreet.db.base.DBMgr;
import com.twitstreet.db.base.DBMgrImpl;
import com.twitstreet.db.data.RankingHistoryData;
import com.twitstreet.db.data.User;
import com.twitstreet.season.SeasonMgr;
import com.twitstreet.util.Util;

public class UserMgrImpl implements UserMgr {

	@Inject
	DBMgr dbMgr;
	@Inject
	ConfigMgr configMgr;
	@Inject
	GroupMgr groupMgr;
	@Inject
	SeasonMgr seasonMgr;

	private static final int AUTOPLAYER_DAY_LIMIT = 30;

	static SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private static Logger logger = Logger.getLogger(UserMgrImpl.class);
	private static String SELECT_FROM_RANKING_HISTORY = " select "
			+ " rh.season_id as season_id, " + " rh.user_id as user_id, "
			+ " rh.cash as cash, " + " rh.portfolio as portfolio, "
			+ " rh.rank as rank, " + " rh.loan as loan, "
			+ " rh.lastUpdate as lastUpdate " + " from ranking_history rh ";
	private static String SELECT_FROM_USERS_RANKING = "select " + "id, "
			+ "userName, " + "longName, " + "lastLogin, " + "firstLogin, "
			+ "users.cash as cash, " + "lastIp, " + "oauthToken, "
			+ "oauthTokenSecret, " + "user_profit(users.id) as changePerHour,"
			+ "valueCumulative, rankCumulative," + "rank, " + "oldRank, "
			+ "direction, " + "pictureUrl, "
			+ "portfolio_value(id) as portfolio, "
			+ " users.cash+portfolio_value(id)-users.loan as total, "
			+ "description, " + "location, " + "inviteActive, " + "language, "
			+ " users.loan, " + "users.url, " + "users.autoplayer "
			+ "from users,ranking ";

	private static String SELECT_FROM_USERS_JOIN_RANKING = SELECT_FROM_USERS_RANKING
			+ " where ranking.user_id = users.id ";

	private static String SELECT_FROM_USERS_JOIN_RANKING_BY_GROUP_ID = SELECT_FROM_USERS_JOIN_RANKING
			+ " and users.id in  	(select user_id from user_group where group_id = ?) ";

	public User getUserById(long id) {
		Connection connection = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		User userDO = null;
		try {
			connection = dbMgr.getConnection();
			ps = connection.prepareStatement(SELECT_FROM_USERS_JOIN_RANKING
					+ " and users.id = ?");
			ps.setLong(1, id);
			rs = ps.executeQuery();
			if (rs.next()) {
				userDO = new User();
				userDO.getDataFromResultSet(rs);
			}

			logger.debug(DBConstants.QUERY_EXECUTION_SUCC + ps.toString());
		} catch (SQLException ex) {
			logger.error(DBConstants.QUERY_EXECUTION_FAIL + ps.toString(), ex);
		} finally {
			dbMgr.closeResources(connection, ps, rs);
		}
		return userDO;
	}

	@Override
	public User getUserByTokenAndSecret(String token, String secret) {
		Connection connection = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		User userDO = null;
		try {
			connection = dbMgr.getConnection();
			ps = connection
					.prepareStatement(SELECT_FROM_USERS_JOIN_RANKING
							+ " and users.oauthToken = ? and users.oauthTokenSecret = ? ");
			ps.setString(1, token);
			ps.setString(2, secret);
			rs = ps.executeQuery();
			if (rs.next()) {
				userDO = new User();
				userDO.getDataFromResultSet(rs);
			}

			logger.debug(DBConstants.QUERY_EXECUTION_SUCC + ps.toString());
		} catch (SQLException ex) {
			logger.error(DBConstants.QUERY_EXECUTION_FAIL + ps.toString(), ex);
		} finally {
			dbMgr.closeResources(connection, ps, rs);
		}
		return userDO;
	}

	@Override
	public int getUserCountForGroup(long id) {

		int count = 0;
		Connection connection = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			connection = dbMgr.getConnection();
			String query = " select count(*) from users u inner join user_group ug on ug.user_id = u.id where ug.group_id = ? ";
			ps = connection.prepareStatement(query);

			ps.setLong(1, id);
			rs = ps.executeQuery();
			while (rs.next()) {
				return rs.getInt(1);
			}

			logger.debug(DBConstants.QUERY_EXECUTION_SUCC + ps.toString());
		} catch (SQLException ex) {
			logger.error(DBConstants.QUERY_EXECUTION_FAIL + ps.toString(), ex);
		} finally {
			dbMgr.closeResources(connection, ps, rs);
		}

		return count;
	}

	@Override
	public int getUserCount() {

		int count = 0;
		Connection connection = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {

			connection = dbMgr.getConnection();

			String query = " select count(*) from users ";
			ps = connection.prepareStatement(query);

			rs = ps.executeQuery();
			while (rs.next()) {
				return rs.getInt(1);
			}

			logger.debug(DBConstants.QUERY_EXECUTION_SUCC + ps.toString());
		} catch (SQLException ex) {
			logger.error(DBConstants.QUERY_EXECUTION_FAIL + ps.toString(), ex);
		} finally {
			dbMgr.closeResources(connection, ps, rs);
		}

		return count;
	}

	@Override
	public ArrayList<User> getUsersForGroup(long id, int offset, int count) {

		return getTopRankForGroup(id, offset, count);
	}

	public void assignInitialRankToUser(User userDO) {

		Connection connection = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			connection = dbMgr.getConnection();

			ps = connection
					.prepareStatement(" select (count(*)+1) as newrank from ranking,users "
							+ " where ranking.user_id = users.id and "
							+ " ( "
							+ " (portfolio + ranking.cash) > ? or "
							+ " (ranking.portfolio + ranking.cash = ? and username <?) "
							+ " ) ");

			ps.setDouble(1, userDO.getCash());
			ps.setDouble(2, userDO.getCash());
			ps.setString(3, userDO.getUserName());
			rs = ps.executeQuery();
			int newRank = 999999;
			if (rs.next()) {
				newRank = rs.getInt("newrank");
			}

			rs.close();
			ps.close();

			ps = connection
					.prepareStatement("insert into ranking(user_id, cash,portfolio,rank,oldRank,direction,lastUpdate)"
							+ " values(?,?,?,?,?,?,NOW())");
			ps.setLong(1, userDO.getId());
			ps.setDouble(2, userDO.getCash());
			ps.setDouble(3, 0);
			ps.setInt(4, newRank);
			ps.setInt(5, newRank);
			ps.setInt(6, 0);

			ps.executeUpdate();

			CallableStatement cs = null;

			cs = connection.prepareCall("{call rerank()}");
			cs.execute();

			logger.debug(DBConstants.QUERY_EXECUTION_SUCC + ps.toString());
		} catch (MySQLIntegrityConstraintViolationException e) {
			logger.warn("DB: User already exists in ranking - UserId:"
					+ userDO.getId() + " User Name: " + userDO.getUserName()
					+ " - " + e.getMessage());
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
							+ "lastLogin,  "
							+ "cash, lastIp, oauthToken, oauthTokenSecret, pictureUrl, language, url, longName, location, description) "
							+ "values(?, ?, NOW() , ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
			ps.setLong(1, userDO.getId());
			ps.setString(2, userDO.getUserName());
			ps.setDouble(3, userDO.getCash());
			ps.setString(4, userDO.getLastIp());
			ps.setString(5, userDO.getOauthToken());
			ps.setString(6, userDO.getOauthTokenSecret());
			ps.setString(7, userDO.getPictureUrl());
			ps.setString(8, userDO.getLanguage());
			ps.setString(9, userDO.getUrl());
			ps.setString(10, userDO.getLongName());
			ps.setString(11, userDO.getLocation());
			ps.setString(12, userDO.getDescription());
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
	}

	@Override
	public void updateUser(User user) {
		Connection connection = null;
		PreparedStatement ps = null;
		try {
			connection = dbMgr.getConnection();
			ps = connection
					.prepareStatement("update users set userName = ?, "
							+ "lastLogin = now(), "
							+ "lastIp = ?, oauthToken = ?, oauthTokenSecret = ?, pictureUrl = ?, url = ?, autoplayer = ?  where id = ?");
			ps.setString(1, user.getUserName());
			ps.setString(2, user.getLastIp());
			ps.setString(3, user.getOauthToken());
			ps.setString(4, user.getOauthTokenSecret());
			ps.setString(5, user.getPictureUrl());
			ps.setString(6, user.getUrl());
			ps.setBoolean(7, user.isAutoPlayer());
			ps.setLong(8, user.getId());
			ps.executeUpdate();

			// just in case...
			resurrectUser(user.getId());

			logger.debug(DBConstants.QUERY_EXECUTION_SUCC + ps.toString());
		} catch (SQLException ex) {
			logger.error(DBConstants.QUERY_EXECUTION_FAIL + ps.toString(), ex);
		} finally {
			dbMgr.closeResources(connection, ps, null);
		}
	}

	@Override
	public void deleteUser(long id) {
		Connection connection = null;
		PreparedStatement ps = null;
		try {
			connection = dbMgr.getConnection();
			ps = connection
					.prepareStatement("insert ignore into inactive_user values (?) ");
			ps.setLong(1, id);

			ps.executeUpdate();
			logger.debug(DBConstants.QUERY_EXECUTION_SUCC + ps.toString());

			logger.info("***********User Inactivated: "
					+ getUserById(id).getUserName() + "***********");
		} catch (SQLException ex) {
			logger.error(DBConstants.QUERY_EXECUTION_FAIL + ps.toString(), ex);
		} finally {
			dbMgr.closeResources(connection, ps, null);
		}
	}

	@Override
	public void resurrectUser(long id) {
		Connection connection = null;
		PreparedStatement ps = null;
		try {
			connection = dbMgr.getConnection();
			ps = connection
					.prepareStatement("delete from inactive_user where user_id=? ");
			ps.setLong(1, id);

			ps.executeUpdate();
			logger.debug(DBConstants.QUERY_EXECUTION_SUCC + ps.toString());

			logger.info("***********User Resurrected: "
					+ getUserById(id).getUserName() + "***********");
		} catch (SQLException ex) {
			logger.error(DBConstants.QUERY_EXECUTION_FAIL + ps.toString(), ex);
		} finally {
			dbMgr.closeResources(connection, ps, null);
		}
	}

	@Override
	public User random() {
		Connection connection = null;
		PreparedStatement ps = null;
		User user = null;
		ResultSet rs = null;
		try {
			connection = dbMgr.getConnection();

			ps = connection
					.prepareStatement(SELECT_FROM_USERS_JOIN_RANKING
							+ " and users.id >= (select floor( max(id) * rand()) from users ) "
							+ "   and users.id not in (select user_id from inactive_user) "
							+ " order by users.id limit 1");

			rs = ps.executeQuery();

			if (rs.next()) {
				user = new User();
				user.getDataFromResultSet(rs);

			} else {
				logger.error("DB: Random user selection query is not working properly");
			}

		} catch (SQLException e) {
			logger.error(DBConstants.QUERY_EXECUTION_FAIL + ps.toString(), e);
		} finally {
			dbMgr.closeResources(connection, ps, rs);

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
	public void addInviteMoney(long userId) {
		Connection connection = null;
		PreparedStatement ps = null;
		User user = getUserById(userId);
		if (user.isInviteActive()) {
			try {
				connection = dbMgr.getConnection();
				ps = connection
						.prepareStatement("update users set cash = (cash + (sqrt(cash + portfolio_value(id)) * ?)), inviteActive = ? where id = ?");
				ps.setDouble(1, UserMgr.INVITE_MONEY_RATE);
				ps.setBoolean(2, false);
				ps.setLong(3, userId);
				ps.executeUpdate();
				logger.debug(DBConstants.QUERY_EXECUTION_SUCC + ps.toString());
			} catch (SQLException ex) {
				logger.error(DBConstants.QUERY_EXECUTION_FAIL + ps.toString(),
						ex);
			} finally {
				dbMgr.closeResources(connection, ps, null);
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
			logger.debug(DBConstants.QUERY_EXECUTION_SUCC + ps.toString());
		} catch (SQLException ex) {
			logger.error(DBConstants.QUERY_EXECUTION_FAIL + ps.toString(), ex);
		} finally {
			dbMgr.closeResources(connection, ps, null);
		}
	}

	@Override
	public ArrayList<User> getTopRank(int offset, int count) {

		ArrayList<User> userList = new ArrayList<User>(100);
		Connection connection = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		User userDO = null;
		try {
			connection = dbMgr.getConnection();
			ps = connection.prepareStatement(SELECT_FROM_USERS_JOIN_RANKING
					+ " order by rank asc limit ?,? ");
			ps.setInt(1, offset);
			ps.setInt(2, count);
			rs = ps.executeQuery();
			logger.debug(DBConstants.QUERY_EXECUTION_SUCC + ps.toString());
			while (rs.next()) {
				userDO = new User();
				userDO.getDataFromResultSet(rs);
				userList.add(userDO);
			}
		} catch (SQLException ex) {
			logger.error(DBConstants.QUERY_EXECUTION_FAIL + ps.toString(), ex);
		} finally {
			dbMgr.closeResources(connection, ps, rs);
		}
		return userList;
	}

	@Override
	public ArrayList<User> getTopRankForGroup(long id, int offset, int count) {

		if (id < 0) {
			return getTopRank(offset, count);
		}
		ArrayList<User> userList = new ArrayList<User>(100);
		Connection connection = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		User userDO = null;
		try {
			connection = dbMgr.getConnection();
			ps = connection
					.prepareStatement(SELECT_FROM_USERS_JOIN_RANKING_BY_GROUP_ID
							+ " order by rank asc limit ?,?");

			ps.setLong(1, id);
			ps.setInt(2, offset);
			ps.setInt(3, count);
			rs = ps.executeQuery();
			logger.debug(DBConstants.QUERY_EXECUTION_SUCC + ps.toString());
			while (rs.next()) {
				userDO = new User();
				userDO.getDataFromResultSet(rs);
				userList.add(userDO);
			}
		} catch (SQLException ex) {
			logger.error(DBConstants.QUERY_EXECUTION_FAIL + ps.toString(), ex);
		} finally {
			dbMgr.closeResources(connection, ps, rs);
		}
		return userList;
	}

	@Override
	public ArrayList<User> getTopRankAllTime(int offset, int count) {

		ArrayList<User> userList = new ArrayList<User>(100);
		Connection connection = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		User userDO = null;
		try {
			connection = dbMgr.getConnection();
			ps = connection.prepareStatement(SELECT_FROM_USERS_JOIN_RANKING
					+ " order by rankCumulative asc limit ?,? ");
			ps.setInt(1, offset);
			ps.setInt(2, count);
			rs = ps.executeQuery();
			logger.debug(DBConstants.QUERY_EXECUTION_SUCC + ps.toString());
			while (rs.next()) {
				userDO = new User();
				userDO.getDataFromResultSet(rs);
				userList.add(userDO);
			}
		} catch (SQLException ex) {
			logger.error(DBConstants.QUERY_EXECUTION_FAIL + ps.toString(), ex);
		} finally {
			dbMgr.closeResources(connection, ps, rs);
		}
		return userList;
	}

	@Override
	public ArrayList<User> getTopRankAllTimeForGroup(long id, int offset,
			int count) {

		if (id < 0) {
			return getTopRankAllTime(offset, count);
		}
		ArrayList<User> userList = new ArrayList<User>(100);
		Connection connection = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		User userDO = null;
		try {
			connection = dbMgr.getConnection();
			ps = connection
					.prepareStatement(SELECT_FROM_USERS_JOIN_RANKING_BY_GROUP_ID
							+ " order by rankCumulative asc limit ?,? ");
			ps.setLong(1, id);
			ps.setInt(2, offset);
			ps.setInt(3, count);
			rs = ps.executeQuery();
			logger.debug(DBConstants.QUERY_EXECUTION_SUCC + ps.toString());
			while (rs.next()) {
				userDO = new User();
				userDO.getDataFromResultSet(rs);
				userList.add(userDO);
			}
		} catch (SQLException ex) {
			logger.error(DBConstants.QUERY_EXECUTION_FAIL + ps.toString(), ex);
		} finally {
			dbMgr.closeResources(connection, ps, rs);
		}
		return userList;
	}

	@Override
	public void rerank() {
		Connection connection = null;
		CallableStatement cs = null;
		try {
			connection = dbMgr.getConnection();
			cs = connection.prepareCall("{call rerank()}");
			cs.execute();
			logger.debug(DBConstants.QUERY_EXECUTION_SUCC + cs.toString());
		} catch (SQLException ex) {
			logger.error(DBConstants.QUERY_EXECUTION_FAIL + cs.toString(), ex);
		} finally {
			dbMgr.closeResources(connection, cs, null);
		}
	}

	@Override
	public void updateRankingHistory() {

		updateRankingHistory(false);
	}

	@Override
	public void updateRankingHistory(boolean neededOnly) {
		Connection connection = null;
		PreparedStatement ps = null;
		String neededString = (neededOnly) ? " where "
				+ "ranking.user_id in"
				+ "("
				+ "select distinct user_id from ranking r where "
				+ " 15< TIMESTAMPDIFF(minute,( "
				+ " select distinct rh.lastUpdate from ranking_history rh where rh.user_id=r.user_id order by rh.lastUpdate desc limit 1"
				+ "   ), now()) "
				+ " OR "
				+ " 1 > (select count(*) from ranking_history rh where rh.user_id = r.user_id ) "
				+ " )"
				: "";
		try {
			connection = dbMgr.getConnection();
			ps = connection
					.prepareStatement("insert ignore into ranking_history(user_id, cash, portfolio, loan, lastUpdate, rank, season_id) "
							+ "select user_id, cash, portfolio, loan,  lastUpdate, rank, (select id from season_info where active is true) from ranking "
							+ neededString);

			ps.executeUpdate();
			logger.debug(DBConstants.QUERY_EXECUTION_SUCC + ps.toString());
		} catch (SQLException ex) {
			logger.error(DBConstants.QUERY_EXECUTION_FAIL + ps.toString(), ex);
		} finally {
			dbMgr.closeResources(connection, ps, null);
		}

	}

	@Override
	public RankingHistoryData getRankingHistoryForUser(long id,
			Timestamp start, Timestamp end) {

		return getRankingHistoryForUser(id, df.format(start), df.format(end));
	}

	@Override
	public RankingHistoryData getRankingHistoryForUser(long id, String from,
			String to) {

		RankingHistoryData rhd = new RankingHistoryData();
		Connection connection = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		String fromStr = " TIMESTAMP('"
				+ df.format(seasonMgr.getCurrentSeason().getStartTime())
				+ "') ";
		String toStr = " TIMESTAMP('"
				+ df.format(seasonMgr.getCurrentSeason().getEndTime()) + "') ";
		if (from != null) {
			fromStr = " TIMESTAMP('" + from + "') ";
		}
		if (to != null) {
			toStr = " TIMESTAMP('" + to + "') ";
		}
		try {
			connection = dbMgr.getConnection();
			ps = connection.prepareStatement(SELECT_FROM_RANKING_HISTORY
					+ "  where user_id = ? " + " and rh.lastUpdate >= "
					+ fromStr + " and rh.lastUpdate <= " + toStr
					+ " order by lastUpdate asc ");
			ps.setLong(1, id);
			rs = ps.executeQuery();
			logger.debug(DBConstants.QUERY_EXECUTION_SUCC + ps.toString());

			rhd.getDataFromResultSet(rs);

		} catch (SQLException ex) {
			logger.error(DBConstants.QUERY_EXECUTION_FAIL + ps.toString(), ex);
		} finally {
			dbMgr.closeResources(connection, ps, rs);
		}
		return rhd;
	}

	@Override
	public RankingHistoryData getRankingHistoryForUser(long id, int seasonId) {

		RankingHistoryData rhd = new RankingHistoryData();
		Connection connection = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			connection = dbMgr.getConnection();
			ps = connection.prepareStatement(SELECT_FROM_RANKING_HISTORY
					+ "  where user_id = ? and season_id = ?"
					+ " order by lastUpdate asc ");
			ps.setLong(1, id);
			ps.setInt(2, seasonId);
			rs = ps.executeQuery();
			logger.debug(DBConstants.QUERY_EXECUTION_SUCC + ps.toString());

			rhd.getDataFromResultSet(rs);

		} catch (SQLException ex) {
			logger.error(DBConstants.QUERY_EXECUTION_FAIL + ps.toString(), ex);
		} finally {
			dbMgr.closeResources(connection, ps, rs);
		}
		return rhd;
	}

	@Override
	public int count() {
		Connection connection = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		int count = 0;
		try {
			connection = dbMgr.getConnection();
			ps = connection.prepareStatement("SELECT count(*) FROM users");
			rs = ps.executeQuery();
			if (rs.next()) {
				count = rs.getInt(1);
			}
		} catch (SQLException exception) {
			logger.error(DBConstants.QUERY_EXECUTION_FAIL + ps.toString(),
					exception);
		} finally {
			dbMgr.closeResources(connection, ps, rs);
		}
		return count;
	}

	@Override
	public ArrayList<User> getUsersByIdList(ArrayList<Long> idList) {
		Connection connection = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		User userDO = null;
		ArrayList<User> userList = new ArrayList<User>();

		String listStr = DBMgrImpl.getIdListAsCommaSeparatedString(idList);
		try {
			connection = dbMgr.getConnection();
			ps = connection.prepareStatement(SELECT_FROM_USERS_JOIN_RANKING
					+ " and users.id in (" + listStr + ")");

			rs = ps.executeQuery();
			if (rs.next()) {
				userDO = new User();
				userDO.getDataFromResultSet(rs);
				userList.add(userDO);
			}

			logger.debug(DBConstants.QUERY_EXECUTION_SUCC + ps.toString());
		} catch (SQLException ex) {
			logger.error(DBConstants.QUERY_EXECUTION_FAIL + ps.toString(), ex);
		} finally {
			dbMgr.closeResources(connection, ps, rs);
		}
		return userList;
	}

	@Override
	public ArrayList<User> searchUser(String searchText) {

		searchText = searchText.replace(" ", "");

		Connection connection = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		User userDO = null;
		ArrayList<User> userList = new ArrayList<User>();
		if (searchText.length() > 0) {
			try {
				connection = dbMgr.getConnection();
				ps = connection.prepareStatement(SELECT_FROM_USERS_JOIN_RANKING
						+ " and userName LIKE ? ");

				ps.setString(1, "%" + searchText + "%");

				rs = ps.executeQuery();
				while (rs.next()) {
					userDO = new User();
					userDO.getDataFromResultSet(rs);
					userList.add(userDO);
				}

				logger.debug(DBConstants.QUERY_EXECUTION_SUCC + ps.toString());
			} catch (SQLException ex) {
				logger.error(DBConstants.QUERY_EXECUTION_FAIL + ps.toString(),
						ex);
			} finally {
				dbMgr.closeResources(connection, ps, rs);
			}

		}
		return userList;
	}

	@Override
	public List<User> getAll() {
		List<User> userList = new ArrayList<User>();
		Connection connection = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		User userDO = null;
		try {
			connection = dbMgr.getConnection();
			ps = connection.prepareStatement(SELECT_FROM_USERS_JOIN_RANKING);
			rs = ps.executeQuery();
			while (rs.next()) {
				userDO = new User();
				userDO.getDataFromResultSet(rs);
				userList.add(userDO);
			}

			logger.debug(DBConstants.QUERY_EXECUTION_SUCC + ps.toString());
		} catch (SQLException ex) {
			logger.error(DBConstants.QUERY_EXECUTION_FAIL + ps.toString(), ex);
		} finally {
			dbMgr.closeResources(connection, ps, rs);
		}
		return userList;
	}

	@Override
	public List<User> getAllActive() {
		List<User> userList = new ArrayList<User>();
		Connection connection = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		User userDO = null;
		try {
			connection = dbMgr.getConnection();
			ps = connection
					.prepareStatement(SELECT_FROM_USERS_JOIN_RANKING
							+ " and users.id not in (select user_id from inactive_user) ");
			rs = ps.executeQuery();
			while (rs.next()) {
				userDO = new User();
				userDO.getDataFromResultSet(rs);
				userList.add(userDO);
			}

			logger.debug(DBConstants.QUERY_EXECUTION_SUCC + ps.toString());
		} catch (SQLException ex) {
			logger.error(DBConstants.QUERY_EXECUTION_FAIL + ps.toString(), ex);
		} finally {
			dbMgr.closeResources(connection, ps, rs);
		}
		return userList;
	}

	@Override
	public void updateTwitterData(User user) {
		Connection connection = null;
		PreparedStatement ps = null;
		try {
			connection = dbMgr.getConnection();
			ps = connection
					.prepareStatement("update users set userName = ?, pictureUrl = ?, location = ?, description = ?, longName = ?, language = ?, url = ? where id = ?");
			ps.setString(1, user.getUserName());
			ps.setString(2, user.getPictureUrl());
			ps.setString(3, user.getLocation());
			ps.setString(4, user.getDescription());
			ps.setString(5, user.getLongName());
			ps.setString(6, user.getLanguage());
			ps.setString(7, user.getUrl());
			ps.setLong(8, user.getId());

			ps.executeUpdate();
			logger.debug(DBConstants.QUERY_EXECUTION_SUCC + ps.toString());
		} catch (SQLException ex) {
			logger.error(DBConstants.QUERY_EXECUTION_FAIL + ps.toString(), ex);
		} finally {
			dbMgr.closeResources(connection, ps, null);
		}
	}

	@Override
	public ArrayList<User> getTopGrossingUsers(int limit) {
		ArrayList<User> userList = new ArrayList<User>();
		Connection connection = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		User userDO = null;
		try {
			connection = dbMgr.getConnection();
			ps = connection
					.prepareStatement(SELECT_FROM_USERS_JOIN_RANKING
							+ " order by ranking.profit/(ranking.cash+ranking.portfolio) desc limit ? ");
			ps.setInt(1, limit);

			rs = ps.executeQuery();
			while (rs.next()) {
				userDO = new User();
				userDO.getDataFromResultSet(rs);
				userList.add(userDO);
			}

			logger.debug(DBConstants.QUERY_EXECUTION_SUCC + ps.toString());
		} catch (SQLException ex) {
			logger.error(DBConstants.QUERY_EXECUTION_FAIL + ps.toString(), ex);
		} finally {
			dbMgr.closeResources(connection, ps, rs);
		}
		return userList;

	}

	@Override
	public void invite(long invitor, long invited) {
		Connection connection = null;
		PreparedStatement ps = null;
		try {
			connection = dbMgr.getConnection();
			ps = connection
					.prepareStatement("insert into invite (invitor, invited, invite_date) values(?, ?, ?)");
			ps.setLong(1, invitor);
			ps.setLong(2, invited);
			ps.setDate(3, Util.toSqlDate(Calendar.getInstance().getTime()));
			ps.execute();
		} catch (SQLException ex) {
			logger.error(DBConstants.QUERY_EXECUTION_FAIL + ps.toString(), ex);
		} finally {
			dbMgr.closeResources(connection, ps, null);
			addInviteMoney(invitor);
		}
	}

	@Override
	public void resetInvitation() {
		Connection connection = null;
		PreparedStatement ps = null;
		try {
			connection = dbMgr.getConnection();
			ps = connection
					.prepareStatement("update users set inviteActive = true");
			ps.execute();
		} catch (SQLException ex) {
			logger.error(DBConstants.QUERY_EXECUTION_FAIL + ps.toString(), ex);
		} finally {
			dbMgr.closeResources(connection, ps, null);
		}
	}

	@Override
	public List<User> getNewSeasonInfoNotSentUsers(int size) {
		List<User> userList = new ArrayList<User>();
		Connection connection = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			connection = dbMgr.getConnection();
			ps = connection
					.prepareStatement("select id, userName from users where newSeasonInfoSent = 0 limit ? ");
			ps.setInt(1, size);

			rs = ps.executeQuery();
			while (rs.next()) {
				User userDO = new User();
				userDO.setId(rs.getLong("id"));
				userDO.setUserName(rs.getString("userName"));
				userList.add(userDO);
			}
			logger.debug(DBConstants.QUERY_EXECUTION_SUCC + ps.toString());
		} catch (SQLException ex) {
			logger.error(DBConstants.QUERY_EXECUTION_FAIL + ps.toString(), ex);
		} finally {
			dbMgr.closeResources(connection, ps, rs);
		}
		return userList;
	}

	@Override
	public void setNewSeasonInfoSent(List<User> userList) {
		if (userList.size() > 0) {
			Connection connection = null;
			PreparedStatement ps = null;
			try {
				connection = dbMgr.getConnection();
				ps = connection
						.prepareStatement("update users set newSeasonInfoSent = 1 where id in "
								+ getIdListAsCommaSeparatedString(userList));

				ps.executeUpdate();
				logger.debug(DBConstants.QUERY_EXECUTION_SUCC + ps.toString());
			} catch (SQLException ex) {
				logger.error(DBConstants.QUERY_EXECUTION_FAIL + ps.toString(),
						ex);
			} finally {
				dbMgr.closeResources(connection, ps, null);
			}
		}
	}

	private static String getIdListAsCommaSeparatedString(List<User> userList) {
		String idListStr = "(";
		for (int i = 0; i < userList.size(); i++) {
			if (i != 0) {
				idListStr = idListStr + ",";
			}
			idListStr = idListStr + userList.get(i).getId();
		}
		idListStr += ")";
		return idListStr;
	}

	@Override
	public List<User> getTopNUsers(int n) {
		List<User> userList = new ArrayList<User>();
		Connection connection = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			connection = dbMgr.getConnection();
			ps = connection
					.prepareStatement("select id, userName, (cash + portfolio_value(id) - loan) as total from users order by total desc limit ?");
			ps.setInt(1, n);
			rs = ps.executeQuery();
			while (rs.next()) {
				User userDO = new User();
				userDO.setId(rs.getLong("id"));
				userDO.setUserName(rs.getString("userName"));
				userDO.setTotal(rs.getDouble("total"));
				userList.add(userDO);
			}

			logger.debug(DBConstants.QUERY_EXECUTION_SUCC + ps.toString());
		} catch (SQLException ex) {
			logger.error(DBConstants.QUERY_EXECUTION_FAIL + ps.toString(), ex);
		} finally {
			dbMgr.closeResources(connection, ps, rs);
		}
		return userList;
	}

	@Override
	public void truncateRankingHistory() {
		Connection connection = null;
		CallableStatement ps = null;
		try {
			connection = dbMgr.getConnection();
			ps = connection.prepareCall("{call refine_ranking_history(?)}");

			Date twoDaysAgo = new Date((new java.util.Date()).getTime() - 2
					* 24 * 60 * 60 * 1000);

			ps.setDate(1, twoDaysAgo);
			ps.execute();

		} catch (SQLException ex) {
			logger.error(DBConstants.QUERY_EXECUTION_FAIL + ps.toString(), ex);
		} finally {
			dbMgr.closeResources(connection, ps, null);
		}
	}

	@Override
	public ArrayList<User> getNewUsers(int offset, int count) {

		ArrayList<User> userList = new ArrayList<User>();
		Connection connection = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		User userDO = null;
		try {
			connection = dbMgr.getConnection();
			ps = connection.prepareStatement(SELECT_FROM_USERS_JOIN_RANKING
					+ " order by firstLogin desc limit ?,?");

			ps.setInt(1, offset);
			ps.setInt(2, count);
			rs = ps.executeQuery();
			logger.debug(DBConstants.QUERY_EXECUTION_SUCC + ps.toString());
			while (rs.next()) {
				userDO = new User();
				userDO.getDataFromResultSet(rs);
				userList.add(userDO);
			}
		} catch (SQLException ex) {
			logger.error(DBConstants.QUERY_EXECUTION_FAIL + ps.toString(), ex);
		} finally {
			dbMgr.closeResources(connection, ps, rs);
		}
		return userList;
	}

	@Override
	public void receiveLoan(long userId, double amount) {
		Connection connection = null;
		PreparedStatement ps = null;
		User user = null;
		try {
			user = getUserById(userId);
			if (user != null && user.getLoan() < UserMgr.MAX_LOAN) {
				if (user.getLoan() + amount > UserMgr.MAX_LOAN) {
					amount = UserMgr.MAX_LOAN - user.getLoan();
				}
				connection = dbMgr.getConnection();
				ps = connection
						.prepareStatement("update users set loan = loan + ?, cash = cash + ? where id = ?");
				ps.setDouble(1, amount);
				ps.setDouble(2, amount);
				ps.setLong(3, userId);
				ps.executeUpdate();
				logger.debug(DBConstants.QUERY_EXECUTION_SUCC + ps.toString());
			}
		} catch (SQLException ex) {
			logger.error(DBConstants.QUERY_EXECUTION_FAIL + ps.toString(), ex);
		} finally {
			dbMgr.closeResources(connection, ps, null);
		}
	}

	@Override
	public void payLoanBack(long userId, double amount) {
		Connection connection = null;
		PreparedStatement ps = null;
		try {
			connection = dbMgr.getConnection();

			ps = connection
					.prepareStatement("update users set loan = (case when cash < ? then loan - cash else loan - ? end), cash = (case when ? > cash then 0 else cash - ? end) where id = ?");
			ps.setDouble(1, amount);
			ps.setDouble(2, amount);
			ps.setDouble(3, amount);
			ps.setDouble(4, amount);
			ps.setLong(5, userId);
			ps.executeUpdate();
			logger.debug(DBConstants.QUERY_EXECUTION_SUCC + ps.toString());
		} catch (SQLException ex) {
			logger.error(DBConstants.QUERY_EXECUTION_FAIL + ps.toString(), ex);
		} finally {
			dbMgr.closeResources(connection, ps, null);
		}
	}

	@Override
	public void payAllLoanBack(long userId) {
		Connection connection = null;
		PreparedStatement ps = null;
		User user = getUserById(userId);
		try {
			if (user != null) {
				double loan = user.getCash() >= user.getLoan() ? 0 : user
						.getLoan() - user.getCash();
				double cash = user.getLoan() >= user.getCash() ? 0 : user
						.getCash() - user.getLoan();

				connection = dbMgr.getConnection();
				ps = connection
						.prepareStatement("update users set loan = ? , cash = ? where id = ?");
				ps.setDouble(1, loan);
				ps.setDouble(2, cash);
				ps.setLong(3, userId);
				ps.executeUpdate();
				logger.debug(DBConstants.QUERY_EXECUTION_SUCC + ps.toString());
			}
		} catch (SQLException ex) {
			logger.error(DBConstants.QUERY_EXECUTION_FAIL + ps.toString(), ex);
		} finally {
			dbMgr.closeResources(connection, ps, null);
		}
	}

	@Override
	public void applyLoanInterest() {
		Connection connection = null;
		PreparedStatement ps = null;
		try {
			connection = dbMgr.getConnection();
			ps = connection
					.prepareStatement("update users set loan = loan + loan * ?");
			ps.setDouble(1, UserMgr.LOAN_INTEREST_RATE);
			ps.executeUpdate();
			logger.debug(DBConstants.QUERY_EXECUTION_SUCC + ps.toString());
		} catch (SQLException ex) {
			logger.error(DBConstants.QUERY_EXECUTION_FAIL + ps.toString(), ex);
		} finally {
			dbMgr.closeResources(connection, ps, null);
		}
	}

	@Override
	public void bankrupt(Long userId) {
		Connection connection = null;
		PreparedStatement ps = null;
		CallableStatement cs = null;
		User user = getUserById(userId);
		try {
			if (user != null) {

				connection = dbMgr.getConnection();
				ps = connection
						.prepareStatement("update users set loan = 0 , cash = ? where id = ?");

				ps.setDouble(1, configMgr.getInitialMoney());
				ps.setLong(2, userId);
				ps.executeUpdate();
				ps = connection
						.prepareStatement("delete from portfolio where user_id = ?");

				ps.setLong(1, userId);
				ps.executeUpdate();

				cs = connection.prepareCall("{call rerank()}");
				cs.execute();
				logger.debug(DBConstants.QUERY_EXECUTION_SUCC + ps.toString());
			}
		} catch (SQLException ex) {
			logger.error(DBConstants.QUERY_EXECUTION_FAIL + ps.toString(), ex);
		} finally {
			dbMgr.closeResources(connection, ps, null);
		}
	}

	@Override
	public List<User> getAllAutoPlayers() {
		List<User> userList = new ArrayList<User>();
		Connection connection = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		User userDO = null;
		try {
			connection = dbMgr.getConnection();
			ps = connection.prepareStatement(SELECT_FROM_USERS_JOIN_RANKING
					+ " and autoplayer=true");
			rs = ps.executeQuery();
			while (rs.next()) {
				userDO = new User();
				userDO.getDataFromResultSet(rs);
				userList.add(userDO);
			}

			logger.debug(DBConstants.QUERY_EXECUTION_SUCC + ps.toString());
		} catch (SQLException ex) {
			logger.error(DBConstants.QUERY_EXECUTION_FAIL + ps.toString(), ex);
		} finally {
			dbMgr.closeResources(connection, ps, rs);
		}
		return userList;
	}

	@Override
	public void updateUserLastLoginDate(long userId, java.util.Date date) {
		Connection connection = null;
		PreparedStatement ps = null;
		try {
			connection = dbMgr.getConnection();
			ps = connection
					.prepareStatement("update users set lastLogin = ?  where id = ?");
			ps.setDate(1, new Date(date.getTime()));
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
	public void detectAutoPlayers() {
		Connection connection = null;
		PreparedStatement ps = null;
		try {
			connection = dbMgr.getConnection();
			ps = connection
					.prepareStatement("update users set autoplayer = true  where datediff(now(),lastLogin) > ?");
			ps.setInt(1, AUTOPLAYER_DAY_LIMIT);
			ps.executeUpdate();
			
			ps.close();
			
			ps = connection
			.prepareStatement("update users set autoplayer = false  where datediff(now(),lastLogin) < ?");
			ps.setInt(1, AUTOPLAYER_DAY_LIMIT);
			ps.executeUpdate();
			
			logger.debug(DBConstants.QUERY_EXECUTION_SUCC + ps.toString());
		} catch (SQLException ex) {
			logger.error(DBConstants.QUERY_EXECUTION_FAIL + ps.toString(), ex);
		} finally {
			dbMgr.closeResources(connection, ps, null);
		}
	}

	@Override
	public void updateAutoPlayerStatus(long userId, boolean autoplayer) {
		Connection connection = null;
		PreparedStatement ps = null;
		try {
			connection = dbMgr.getConnection();
			ps = connection
					.prepareStatement("update users set autoplayer = ?  where id = ?");
			ps.setBoolean(1, autoplayer);
			ps.setLong(2, userId);
			ps.executeUpdate();
			logger.debug(DBConstants.QUERY_EXECUTION_SUCC + ps.toString());
		} catch (SQLException ex) {
			logger.error(DBConstants.QUERY_EXECUTION_FAIL + ps.toString(), ex);
		} finally {
			dbMgr.closeResources(connection, ps, null);
		}
	}
}
