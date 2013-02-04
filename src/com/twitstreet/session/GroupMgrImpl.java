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

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;

import com.google.inject.Inject;
import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;
import com.twitstreet.db.base.DBConstants;
import com.twitstreet.db.base.DBMgr;
import com.twitstreet.db.data.Group;
import com.twitstreet.db.data.GroupRole;
import com.twitstreet.main.TwitstreetException;

public class GroupMgrImpl implements GroupMgr {
	@Inject
	DBMgr dbMgr;

	private static int USER_ROLE = 0;
	private static int MODERATOR_ROLE = 1;
	private static int ADMIN_ROLE = 2;

	private static String RESERVED_SYM = "$$";

	private static Logger logger = Logger.getLogger(GroupMgrImpl.class);

	private static String SELECT_FROM_GROUPS = " select id, name, adminId, (select userName from users where id=adminId) as adminName, "
			+ " (select count(*) from user_group ug2 where group_id = g.id) as userCount, "
			+ " g.status as status,"
			+ " get_group_total(g.id) as total, "
			+ " get_group_total_alltime(g.id) as totalAllTime, "
			+ " get_group_rank(g.id) as rank, "
			+ " get_group_rank_alltime(g.id) as rankAllTime, "
			+ " sum(user_profit(ug.user_id)) as changePerHour "
			+ " 	from groups g inner join "
			+ " 	user_group ug on ug.group_id = g.id "
			+ " 		where 1=1 "
			+ RESERVED_SYM + " " + " group by g.id ";

	private static String selectFromGroupsWhere(String whereCondition) {

		return SELECT_FROM_GROUPS.replace(RESERVED_SYM, whereCondition);

	}

	private static String selectAllFromGroups() {

		return selectFromGroupsWhere("");

	}

	private long createGroup(String name, long userId)
			throws TwitstreetException {
		Connection connection = null;
		PreparedStatement ps = null;
		long id = -1;
		ResultSet rs = null;
		try {
			connection = dbMgr.getConnection();

			ps = connection
					.prepareStatement(" select count(*) from groups where id in "
							+ " 	(select ug.group_id from user_group ug where user_id = ?) ");
			ps.setLong(1, userId);

			rs = ps.executeQuery();
			if (rs.next()) {

				int groupCount = rs.getInt(1);
				if (groupCount >= GroupMgr.MAX_GROUP_ALLOWED_PER_USER) {
					throw new TwitstreetException(
							this.getClass().getSimpleName(),
							"addUserToGroupWithRole",
							3,
							new Object[] { GroupMgr.MAX_GROUP_ALLOWED_PER_USER });
				}

			}

			ps = connection.prepareStatement(
					" insert into groups(name,adminId) values(?,?) ",
					Statement.RETURN_GENERATED_KEYS);

			ps.setString(1, name);
			ps.setLong(2, userId);
			ps.executeUpdate();
			rs = ps.getGeneratedKeys();

			if (rs.next()) {
				id = rs.getLong(1);
			}

			ps = connection
					.prepareStatement(" insert into user_group(user_id, group_id, role_id) values (?,?,?) ");
			ps.setLong(1, userId);
			ps.setLong(2, id);
			ps.setLong(3, ADMIN_ROLE);
			ps.executeUpdate();
			updateGroupCacheTable();
			logger.debug(DBConstants.QUERY_EXECUTION_SUCC + ps.toString());
		} catch (MySQLIntegrityConstraintViolationException e) {
			logger.warn("DB: Group already exist - Name:" + name
					+ e.getMessage());
			throw new TwitstreetException(this.getClass().getSimpleName(),
					"createGroup", 1, new Object[] { name });

		} catch (SQLException e) {
			// TODO
			// MySQLIntegrityConstraintViolationException cannot be caught
			// above.
			// Something wrong, fix that...
			if (e.getMessage().startsWith("Duplicate")) {
				logger.warn("DB: Group already exist - Name:" + name
						+ e.getMessage());
				throw new TwitstreetException(this.getClass().getSimpleName(),
						"createGroup", 1, new Object[] { name });
			}
			logger.warn("DB: Error in creating group - Name:" + name
					+ e.getMessage());
			logger.error(DBConstants.QUERY_EXECUTION_FAIL + ps.toString(), e);
			throw new TwitstreetException(this.getClass().getSimpleName(),
					"createGroup", 2, new Object[] { name });

		} finally {
			dbMgr.closeResources(connection, ps, null);
		}
		return id;

	}

	@Override
	public long createGroupForUser(String groupName, long id)
			throws TwitstreetException {
		long groupId = createGroup(groupName, id);

		return groupId;
	}

	@Override
	public void updateGroup(Group group) {
		Connection connection = null;
		PreparedStatement ps = null;
		try {
			connection = dbMgr.getConnection();
			ps = connection
					.prepareStatement("update groups set name = ? where id = ?");
			ps.setString(1, group.getName());
			ps.setLong(2, group.getId());

			ps.executeUpdate();
			logger.debug(DBConstants.QUERY_EXECUTION_SUCC + ps.toString());
		} catch (SQLException ex) {
			logger.error(DBConstants.QUERY_EXECUTION_FAIL + ps.toString(), ex);
		} finally {
			dbMgr.closeResources(connection, ps, null);
		}

	}

	@Override
	public void deleteGroup(long id) {
		Connection connection = null;
		PreparedStatement ps = null;
		try {
			connection = dbMgr.getConnection();
			ps = connection.prepareStatement("delete from groupcache where id = ?");
			ps.setLong(1, id);
			ps.executeUpdate();
			
			ps = connection.prepareStatement("delete from groups where id = ?");
			ps.setLong(1, id);
			ps.executeUpdate();
			
			logger.debug(DBConstants.QUERY_EXECUTION_SUCC + ps.toString());
		} catch (SQLException ex) {
			logger.error(DBConstants.QUERY_EXECUTION_FAIL + ps.toString(), ex);
		} finally {
			dbMgr.closeResources(connection, ps, null);
		}

	}

	@Override
	public void deleteGroup(String name) {
		Connection connection = null;
		PreparedStatement ps = null;
		try {
			connection = dbMgr.getConnection();
			
			ps = connection.prepareStatement("delete from groupcache where name = ?");
			ps.setString(1, name);
			ps.executeUpdate();
			
			ps = connection.prepareStatement("delete from groups where name = ?");
			ps.setString(1, name);
			ps.executeUpdate();
			
			logger.debug(DBConstants.QUERY_EXECUTION_SUCC + ps.toString());
		} catch (SQLException ex) {
			logger.error(DBConstants.QUERY_EXECUTION_FAIL + ps.toString(), ex);
		} finally {
			dbMgr.closeResources(connection, ps, null);
		}

	}

	@Override
	public Group getGroup(long id) {
		Connection connection = null;
		PreparedStatement ps = null;
		Group group = new Group();
		try {
			connection = dbMgr.getConnection();
			ps = connection
					.prepareStatement(selectFromGroupsWhere(" and g.id = ? "));
			ps.setLong(1, id);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				group.getDataFromResultSet(rs);
			}
			logger.debug(DBConstants.QUERY_EXECUTION_SUCC + ps.toString());
		} catch (SQLException ex) {
			logger.error(DBConstants.QUERY_EXECUTION_FAIL + ps.toString(), ex);
		} finally {
			dbMgr.closeResources(connection, ps, null);
		}

		return group;
	}

	@Override
	public Group getGroup(String name) {
		Connection connection = null;
		PreparedStatement ps = null;
		Group group = new Group();
		try {
			connection = dbMgr.getConnection();
			ps = connection
					.prepareStatement(selectFromGroupsWhere(" and g.name = ?"));
			ps.setString(1, name);
			ResultSet rs = ps.executeQuery();
			group.getDataFromResultSet(rs);

			logger.debug(DBConstants.QUERY_EXECUTION_SUCC + ps.toString());
		} catch (SQLException ex) {
			logger.error(DBConstants.QUERY_EXECUTION_FAIL + ps.toString(), ex);
		} finally {
			dbMgr.closeResources(connection, ps, null);
		}

		return group;
	}

	@Override
	public ArrayList<Group> getGroupsForUser(long id) {
		return getGroupsForUser(id, 0, Integer.MAX_VALUE);
	}

	@Override
	public ArrayList<Group> getGroupsForUser(long id, int offset, int count) {
		Connection connection = null;
		PreparedStatement ps = null;
		ArrayList<Group> groups = new ArrayList<Group>();
		try {
			connection = dbMgr.getConnection();
			ps = connection
					.prepareStatement(selectFromGroupsWhere(" and ug.user_id = ?")
							+ " limit ?,? ");
			ps.setLong(1, id);
			ps.setInt(2, offset);
			ps.setInt(3, count);
			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				Group group = new Group();
				group.getDataFromResultSet(rs);
				groups.add(group);
			}

			logger.debug(DBConstants.QUERY_EXECUTION_SUCC + ps.toString());
		} catch (SQLException ex) {
			logger.error(DBConstants.QUERY_EXECUTION_FAIL + ps.toString(), ex);
		} finally {
			dbMgr.closeResources(connection, ps, null);
		}

		return groups;
	}

	@Override
	public void addUserToGroup(long userId, long id) throws TwitstreetException {

		addUserToGroupWithRole(userId, id, GroupRole.USER);
	}

	@Override
	public void disableEntrance(long groupId) {
		Connection connection = null;
		PreparedStatement ps = null;
		try {
			connection = dbMgr.getConnection();
			ps = connection
			.prepareStatement(" update groupcache set status = ? where id = ?");
			ps.setLong(1, Group.STATUS_NEW_USER_DISABLED);
			ps.setLong(2, groupId);
			ps.execute();
			
			ps = connection
					.prepareStatement(" update groups set status = ? where id = ?");
			ps.setLong(1, Group.STATUS_NEW_USER_DISABLED);
			ps.setLong(2, groupId);

			ps.execute();

			logger.debug(DBConstants.QUERY_EXECUTION_SUCC + ps.toString());
		} catch (SQLException ex) {
			logger.error(DBConstants.QUERY_EXECUTION_FAIL + ps.toString(), ex);
		} finally {
			dbMgr.closeResources(connection, ps, null);
		}

	}

	@Override
	public void enableEntrance(long groupId) {
		Connection connection = null;
		PreparedStatement ps = null;
		try {
			connection = dbMgr.getConnection();
			ps = connection.prepareStatement(" update groupcache set status = ? where id = ?");
			ps.setLong(1, Group.STATUS_DEFAULT);
			ps.setLong(2, groupId);
			ps.execute();
			
			ps = connection.prepareStatement(" update groups set status = ? where id = ?");
			ps.setLong(1, Group.STATUS_DEFAULT);
			ps.setLong(2, groupId);

			ps.execute();

			logger.debug(DBConstants.QUERY_EXECUTION_SUCC + ps.toString());
		} catch (SQLException ex) {
			logger.error(DBConstants.QUERY_EXECUTION_FAIL + ps.toString(), ex);
		} finally {
			dbMgr.closeResources(connection, ps, null);
		}

	}

	@Override
	public void addUserToGroupWithRole(long userId, long groupId, int role)
			throws TwitstreetException {
		Connection connection = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			connection = dbMgr.getConnection();

			ps = connection
					.prepareStatement(" select status,name from groups where groups.id = ? ");
			ps.setLong(1, groupId);
			rs = ps.executeQuery();

			if (rs.next()) {
				int status = rs.getInt("status");
				String groupName = rs.getString("name");
				if (status == Group.STATUS_NEW_USER_DISABLED) {
					throw new TwitstreetException(this.getClass()
							.getSimpleName(), "addUserToGroupWithRole", 1,
							new Object[] { groupName });

				}

			}

			ps = connection
					.prepareStatement(" select u.userName as userName,g.name as groupName from user_group_block ugb,groups g,users u "
							+ "	 where u.id = ugb.user_id and g.id=ugb.group_id and ugb.user_id = ? and ugb.group_id = ? ");
			ps.setLong(1, userId);
			ps.setLong(2, groupId);

			rs = ps.executeQuery();
			if (rs.next()) {

				String userName = rs.getString("userName");
				String groupName = rs.getString("groupName");
				throw new TwitstreetException(this.getClass().getSimpleName(),
						"addUserToGroupWithRole", 2, new Object[] { userName,
								groupName });

			}
			ps = connection
					.prepareStatement(" select count(*) from groups where id in "
							+ " 	(select ug.group_id from user_group ug where user_id = ?) ");
			ps.setLong(1, userId);

			rs = ps.executeQuery();
			if (rs.next()) {

				int groupCount = rs.getInt(1);
				if (groupCount >= GroupMgr.MAX_GROUP_ALLOWED_PER_USER) {
					throw new TwitstreetException(
							this.getClass().getSimpleName(),
							"addUserToGroupWithRole",
							3,
							new Object[] { GroupMgr.MAX_GROUP_ALLOWED_PER_USER });
				}

			}
			ps = connection
					.prepareStatement(" insert ignore into user_group(user_id, group_id, role_id) values (?,?,?) ");
			ps.setLong(1, userId);
			ps.setLong(2, groupId);
			ps.setLong(3, role);
			ps.executeUpdate();

			logger.debug(DBConstants.QUERY_EXECUTION_SUCC + ps.toString());

		} catch (MySQLIntegrityConstraintViolationException ex) {

			// Cannot catch MySQLIntegrityConstraintViolationException here
			// TODO - fix the issue
			logger.warn("DB: User already in group - " + ps.toString()
					+ ex.getMessage());

		} catch (SQLException ex) {

			logger.error(DBConstants.QUERY_EXECUTION_FAIL + ps.toString(), ex);
		} finally {
			dbMgr.closeResources(connection, ps, null);
		}

	}

	@Override
	public void removeUserFromGroup(long userId, long id) {
		Connection connection = null;
		PreparedStatement ps = null;
		try {
			connection = dbMgr.getConnection();
			ps = connection
					.prepareStatement(" delete from user_group where user_id = ? and group_id = ? and role_id != "
							+ GroupRole.ADMIN);
			ps.setLong(1, userId);
			ps.setLong(2, id);
			ps.executeUpdate();
			logger.debug(DBConstants.QUERY_EXECUTION_SUCC + ps.toString());
		} catch (SQLException ex) {
			logger.error(DBConstants.QUERY_EXECUTION_FAIL + ps.toString(), ex);
		} finally {
			dbMgr.closeResources(connection, ps, null);
		}

	}

	@Override
	public void blockUserForGroup(long userId, long id)
			throws TwitstreetException {
		Connection connection = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			connection = dbMgr.getConnection();

			ps = connection
					.prepareStatement(" select role_id, u.userName as userName,g.name as groupName from user_group ug,groups g,users u "
							+ "	 where ug.user_id = u.id and g.id=ug.group_id and ug.user_id = ? and ug.group_id = ? ");
			ps.setLong(1, userId);
			ps.setLong(2, id);
			rs = ps.executeQuery();

			if (rs.next()) {

				int roleId = rs.getInt("role_id");
				String userName = rs.getString("userName");
				String groupName = rs.getString("groupName");
				if (roleId == ADMIN_ROLE) {
					throw new TwitstreetException(this.getClass()
							.getSimpleName(), "blockUserForGroup", 1,
							new Object[] { userName, groupName });
				}

			}

			ps = connection
					.prepareStatement(" insert ignore into user_group_block(user_id,group_id) values (?,?) ");
			ps.setLong(1, userId);
			ps.setLong(2, id);
			ps.executeUpdate();

			ps = connection
					.prepareStatement(" delete from user_group where user_id = ? and group_id = ? ");
			ps.setLong(1, userId);
			ps.setLong(2, id);
			ps.executeUpdate();
			logger.debug(DBConstants.QUERY_EXECUTION_SUCC + ps.toString());
		} catch (SQLException ex) {
			logger.error(DBConstants.QUERY_EXECUTION_FAIL + ps.toString(), ex);
		} finally {
			dbMgr.closeResources(connection, ps, rs);
		}

	}

	@Override
	public void unblockUserForGroup(long userId, long id)
			throws TwitstreetException {
		Connection connection = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			connection = dbMgr.getConnection();

			ps = connection
					.prepareStatement(" delete from user_group_block where user_id = ? and group_id = ? ");
			ps.setLong(1, userId);
			ps.setLong(2, id);
			ps.executeUpdate();
			logger.debug(DBConstants.QUERY_EXECUTION_SUCC + ps.toString());
		} catch (SQLException ex) {
			logger.error(DBConstants.QUERY_EXECUTION_FAIL + ps.toString(), ex);
		} finally {
			dbMgr.closeResources(connection, ps, rs);
		}

	}

	@Override
	public void addUserToDefaultGroup(long userId) throws TwitstreetException {
		addUserToGroup(userId, Group.DEFAULT_ID);

	}

	@Override
	public int getRankOfUserForGroup(long userId, long groupId) {

		if (groupId < 0) {

		}
		Connection connection = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		int rank = -1;
		boolean isMember = false;
		try {
			connection = dbMgr.getConnection();
			if (groupId < 0) {
				ps = connection
						.prepareStatement(" select rank from  ranking  where ranking.id =?  ");
				ps.setLong(1, userId);
			} else {
				ps = connection
						.prepareStatement(" select count(*)+1 rankInGroup from user_group ug, ranking r "
								+ " where r.user_id = ug.user_id and "
								+ " 		r.rank < (select rank from ranking where ranking.user_id =? )  and "
								+ "		ug.group_id = ?  ");

				ps.setLong(1, userId);
				ps.setLong(2, groupId);

			}

			rs = ps.executeQuery();
			if (rs.next()) {
				rank = rs.getInt(1);
			}

			logger.debug(DBConstants.QUERY_EXECUTION_SUCC + ps.toString());
		} catch (SQLException ex) {
			logger.error(DBConstants.QUERY_EXECUTION_FAIL + ps.toString(), ex);
		} finally {
			dbMgr.closeResources(connection, ps, rs);
		}

		return rank;
	}

	@Override
	public int getAllTimeRankOfUserForGroup(long userId, long groupId) {

		Connection connection = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		int rank = -1;

		try {
			connection = dbMgr.getConnection();

			if (groupId < 0) {

				ps = connection
						.prepareStatement(" select rankCumulative from  ranking  where ranking.id =?  ");

				ps.setLong(1, userId);
			} else {
				ps = connection
						.prepareStatement(" select count(*)+1 rankInGroup from user_group ug, ranking r "
								+ " where r.user_id = ug.user_id and "
								+ " 		r.rankCumulative< (select rankCumulative from ranking where ranking.user_id =? )  and "
								+ "		ug.group_id = ?  ");

				ps.setLong(1, userId);
				ps.setLong(2, groupId);

			}

			rs = ps.executeQuery();
			if (rs.next()) {
				rank = rs.getInt(1);
			}

			logger.debug(DBConstants.QUERY_EXECUTION_SUCC + ps.toString());
		} catch (SQLException ex) {
			logger.error(DBConstants.QUERY_EXECUTION_FAIL + ps.toString(), ex);
		} finally {
			dbMgr.closeResources(connection, ps, rs);
		}

		return rank;
	}

	@Override
	public boolean userIsMemberOfGroup(long userId, long groupId)
			throws TwitstreetException {

		Connection connection = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		boolean isMember = false;
		try {
			connection = dbMgr.getConnection();
			ps = connection
					.prepareStatement(" select ug.user_id from user_group ug where ug.user_id=? and ug.group_id = ? ");

			ps.setLong(1, userId);
			ps.setLong(2, groupId);

			rs = ps.executeQuery();
			if (rs.next()) {
				isMember = true;
			}

			logger.debug(DBConstants.QUERY_EXECUTION_SUCC + ps.toString());
		} catch (SQLException ex) {
			logger.error(DBConstants.QUERY_EXECUTION_FAIL + ps.toString(), ex);
		} finally {
			dbMgr.closeResources(connection, ps, rs);
		}

		return isMember;

	}

	@Override
	public ArrayList<Group> searchGroup(String searchText) {
		searchText = searchText.replace(" ", "");

		Connection connection = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		Group group = null;
		ArrayList<Group> groupList = new ArrayList<Group>();
		if (searchText.length() > 0) {
			try {
				connection = dbMgr.getConnection();
				ps = connection
						.prepareStatement("select * from groupcache where name LIKE ?");

				ps.setString(1, "%" + searchText + "%");

				rs = ps.executeQuery();
				while (rs.next()) {
					group = new Group();
					group.getDataFromResultSet(rs);
					groupList.add(group);
				}

				logger.debug(DBConstants.QUERY_EXECUTION_SUCC + ps.toString());
			} catch (SQLException ex) {
				logger.error(DBConstants.QUERY_EXECUTION_FAIL + ps.toString(),
						ex);
			} finally {
				dbMgr.closeResources(connection, ps, rs);
			}

		}
		return groupList;
	}

	@Override
	public ArrayList<Group> getAllGroups(int offset, int count) {
		String query = "select id, name, adminId, adminName, userCount, status, total, totalAllTime, rank, rankAllTime, changePerHour from groupcache order by total desc limit ?,?";
		Connection connection = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		Group group = null;
		ArrayList<Group> groupList = new ArrayList<Group>();
		try {
			connection = dbMgr.getConnection();
			ps = connection.prepareStatement(query);
			ps.setInt(1, offset);
			ps.setInt(2, count);

			rs = ps.executeQuery();
			while (rs.next()) {
				group = new Group();
				group.getDataFromResultSet(rs);
				groupList.add(group);
			}

			logger.debug(DBConstants.QUERY_EXECUTION_SUCC + ps.toString());
		} catch (SQLException ex) {
			logger.error(DBConstants.QUERY_EXECUTION_FAIL + ps.toString(), ex);
		} finally {
			dbMgr.closeResources(connection, ps, rs);
		}

		return groupList;
	}
	
	@Override
	public ArrayList<Group> getAllGroupsWithoutLimit() {

		Connection connection = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		Group group = null;
		ArrayList<Group> groupList = new ArrayList<Group>();

		try {
			connection = dbMgr.getConnection();
			ps = connection.prepareStatement(selectAllFromGroups()
					+ " ");

			rs = ps.executeQuery();
			while (rs.next()) {
				group = new Group();
				group.getDataFromResultSet(rs);
				groupList.add(group);
			}

			logger.debug(DBConstants.QUERY_EXECUTION_SUCC + ps.toString());
		} catch (SQLException ex) {
			logger.error(DBConstants.QUERY_EXECUTION_FAIL + ps.toString(), ex);
		} finally {
			dbMgr.closeResources(connection, ps, rs);
		}

		return groupList;

	}


	@Override
	public ArrayList<Group> getAllGroupsFromDb(int offset, int count) {

		Connection connection = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		Group group = null;
		ArrayList<Group> groupList = new ArrayList<Group>();

		try {
			connection = dbMgr.getConnection();
			ps = connection.prepareStatement(selectAllFromGroups()
					+ " order by total desc limit ?,? ");
			ps.setInt(1, offset);
			ps.setInt(2, count);

			rs = ps.executeQuery();
			while (rs.next()) {
				group = new Group();
				group.getDataFromResultSet(rs);
				groupList.add(group);
			}

			logger.debug(DBConstants.QUERY_EXECUTION_SUCC + ps.toString());
		} catch (SQLException ex) {
			logger.error(DBConstants.QUERY_EXECUTION_FAIL + ps.toString(), ex);
		} finally {
			dbMgr.closeResources(connection, ps, rs);
		}

		return groupList;

	}

	@Override
	public ArrayList<Group> getTopGroups(int offset, int count) {

		Connection connection = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		Group group = null;
		ArrayList<Group> groupList = new ArrayList<Group>();

		try {
			connection = dbMgr.getConnection();
			ps = connection.prepareStatement("select * from groupcache order by total desc limit ?,? ");
			ps.setInt(1, offset);
			ps.setInt(2, count);

			rs = ps.executeQuery();
			while (rs.next()) {
				group = new Group();
				group.getDataFromResultSet(rs);
				groupList.add(group);
			}

			logger.debug(DBConstants.QUERY_EXECUTION_SUCC + ps.toString());
		} catch (SQLException ex) {
			logger.error(DBConstants.QUERY_EXECUTION_FAIL + ps.toString(), ex);
		} finally {
			dbMgr.closeResources(connection, ps, rs);
		}

		return groupList;

	}

	@Override
	public ArrayList<Group> getTopGroupsAllTime(int offset, int count) {

		Connection connection = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		Group group = null;
		ArrayList<Group> groupList = new ArrayList<Group>();

		try {
			connection = dbMgr.getConnection();
			ps = connection.prepareStatement("select * from groupcache order by totalAllTime desc limit ?,? ");
			ps.setInt(1, offset);
			ps.setInt(2, count);

			rs = ps.executeQuery();
			while (rs.next()) {
				group = new Group();
				group.getDataFromResultSet(rs);
				groupList.add(group);
			}

			logger.debug(DBConstants.QUERY_EXECUTION_SUCC + ps.toString());
		} catch (SQLException ex) {
			logger.error(DBConstants.QUERY_EXECUTION_FAIL + ps.toString(), ex);
		} finally {
			dbMgr.closeResources(connection, ps, rs);
		}

		return groupList;

	}

	@Override
	public int getGroupCount() {

		Connection connection = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			connection = dbMgr.getConnection();
			ps = connection.prepareStatement("select count(*) from groups ");

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
		return -1;

	}

	@Override
	public int getGroupCountForUser(long id) {

		Connection connection = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			connection = dbMgr.getConnection();
			ps = connection
					.prepareStatement(" select count(*) from groups where id in "
							+ " 	(select ug.group_id from user_group ug where user_id = ?) ");
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
		return -1;
	}
	
	@Override
	public void updateGroupCacheTable(){
		List<Group> groupList = getAllGroupsWithoutLimit();
		
		Connection connection = null;
		PreparedStatement ps = null;
		try {
			connection = dbMgr.getConnection();
			ps = connection.prepareStatement("delete from groupcache");
			ps.execute();
			for(Group group : groupList){
				
				ps = connection.prepareStatement("insert into groupcache(" +
							"id, name, adminId, adminName, " +
							"userCount, status, total, totalAllTime, " +
							"rank, rankAllTime, changePerHour) " +
							"values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
				ps.setLong(1, group.getId());
				ps.setString(2, group.getName());
				ps.setLong(3, group.getAdminId());
				ps.setString(4, group.getAdminName());
				ps.setInt(5, group.getUserCount());
				ps.setInt(6, group.getStatus());
				ps.setDouble(7, group.getTotal());
				ps.setDouble(8, group.getTotalAllTime());
				ps.setInt(9, group.getRank());
				ps.setInt(10, group.getRankAllTime());
				ps.setDouble(11, group.getChangePerHour());
				ps.executeUpdate();
			}
				
			
		} catch (SQLException ex) {
			logger.error(DBConstants.QUERY_EXECUTION_FAIL + ps.toString(), ex);
		} finally {
			dbMgr.closeResources(connection, ps, null);
		}
	}

}
