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
import com.twitstreet.db.base.DBConstants;
import com.twitstreet.db.base.DBMgr;
import com.twitstreet.db.data.Group;
import com.twitstreet.db.data.GroupRole;
import com.twitstreet.db.data.User;

public class GroupMgrImpl implements GroupMgr {
	@Inject
	DBMgr dbMgr;

	private static Logger logger = Logger.getLogger(GroupMgrImpl.class);

	private static String GET_GROUP = " select id, name from groups where 1=1 ";

	private static String BY_GROUP_ID = " and groups.id = ? ";
	private static String BY_GROUP_NAME = " and groups.name = ? ";

	private static String GET_USER_GROUPS = " select g.* from user_group ug, groups g where 1=1 " + " and g.id = ug.group_id " + " and ug.user_id = ? ";

	@Override
	public long createGroup(String name) {
		Connection connection = null;
		PreparedStatement ps = null;
		long id = -1;
		try {
			connection = dbMgr.getConnection();

			ps = connection.prepareStatement("insert into groups(name) values(?)",Statement.RETURN_GENERATED_KEYS);
		
			ps.setString(1, name);
			ps.executeUpdate();
			ResultSet rs = ps.getGeneratedKeys();

			if (rs.next()) {
				id = rs.getLong(1);
			}

			logger.debug(DBConstants.QUERY_EXECUTION_SUCC + ps.toString());
		} catch (MySQLIntegrityConstraintViolationException e) {
			logger.warn("DB: Group already exist - Name:" + name + e.getMessage());
			id = getGroup(name).getId();
		} catch (SQLException ex) {
			logger.error(DBConstants.QUERY_EXECUTION_FAIL + ps.toString(), ex);
		} finally {
			dbMgr.closeResources(connection, ps, null);
		}
		return id;

	}

	@Override
	public long createGroupForUser(String groupName, User userDO) {
		long id = createGroup(groupName);
		addUserToGroupWithRole(userDO, id, GroupRole.ADMIN);
		return id;
	}

	@Override
	public void updateGroup(Group group) {
		Connection connection = null;
		PreparedStatement ps = null;
		try {
			connection = dbMgr.getConnection();
			ps = connection.prepareStatement("update groups set name = ? where id = ?");
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
			ps = connection.prepareStatement(GET_GROUP + BY_GROUP_ID);
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
			ps = connection.prepareStatement(GET_GROUP + BY_GROUP_NAME);
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
	public ArrayList<Group> getGroupsForUser(User userDO) {
		Connection connection = null;
		PreparedStatement ps = null;
		ArrayList<Group> groups = new ArrayList<Group>();
		try {
			connection = dbMgr.getConnection();
			ps = connection.prepareStatement(GET_USER_GROUPS);
			ps.setLong(1, userDO.getId());
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
	public void addUserToGroup(User userDO, long id) {

		addUserToGroupWithRole(userDO, id, GroupRole.USER);
	}

	@Override
	public void addUserToGroupWithRole(User userDO, long id, int role) {
		Connection connection = null;
		PreparedStatement ps = null;

		try {
			connection = dbMgr.getConnection();
			ps = connection.prepareStatement(" insert into user_group(user_id, group_id, role_id) values (?,?,?) ");
			ps.setLong(1, userDO.getId());
			ps.setLong(2, id);
			ps.setLong(3, role);
			ps.executeUpdate();

			logger.debug(DBConstants.QUERY_EXECUTION_SUCC + ps.toString());
			
		} catch (MySQLIntegrityConstraintViolationException ex) {
			
			//Cannot catch MySQLIntegrityConstraintViolationException here
		    //TODO - fix the issue
			logger.warn("DB: User already in group - " + ps.toString() + ex.getMessage());
			
		} catch (SQLException ex) {
			
			logger.error(DBConstants.QUERY_EXECUTION_FAIL + ps.toString(), ex);
		} finally {
			dbMgr.closeResources(connection, ps, null);
		}

	}

	@Override
	public void removeUserFromGroup(User userDO, long id) {
		Connection connection = null;
		PreparedStatement ps = null;
		try {
			connection = dbMgr.getConnection();
			ps = connection.prepareStatement(" delete from user_group where user_id = ? and group_id = ? and role_id != "+GroupRole.ADMIN);
			ps.setLong(1, userDO.getId());
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
	public void addUserToDefaultGroup(User userDO) {
		addUserToGroup(userDO, Group.DEFAULT_ID);

	}

}
