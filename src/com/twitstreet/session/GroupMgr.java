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

import java.util.ArrayList;

import com.twitstreet.db.data.Group;
import com.twitstreet.main.TwitstreetException;

public interface GroupMgr{

	public static int GROUP_COUNT_PER_PAGE = 20; 
	public static int MAX_GROUP_ALLOWED_PER_USER= 10; 
	
	public long  createGroupForUser(String groupName, long id) throws TwitstreetException;
	
	public void updateGroup(Group group);
	
	public void deleteGroup(long id);
	public void deleteGroup(String name);

	public Group getGroup(long id);
	public Group getGroup(String name);
	
	public ArrayList<Group> getGroupsForUser(long id, int offset, int count);
	public ArrayList<Group> getGroupsForUser(long id);
	
	public void addUserToGroup(long userId, long id) throws TwitstreetException;
	public void addUserToGroupWithRole(long userId, long id, int role) throws TwitstreetException;

	public void addUserToDefaultGroup(long userId) throws TwitstreetException;
	
	public void removeUserFromGroup(long userId, long id);


	public ArrayList<Group> getAllGroups(int offset, int count);
	public ArrayList<Group> getTopGroups(int offset, int count);
	public ArrayList<Group> getTopGroupsAllTime(int offset, int count);
	
	public ArrayList<Group> searchGroup(String text);
	//ArrayList<Group> getAllGroups();
	int getGroupCount();

	int getGroupCountForUser(long id);

	void blockUserForGroup(long userId, long id) throws TwitstreetException;

	void unblockUserForGroup(long userId, long groupId) throws TwitstreetException;

	void disableEntrance(long groupId);

	void enableEntrance(long groupId);

	boolean userIsMemberOfGroup(long userId, long groupId) throws TwitstreetException;

	int getRankOfUserForGroup(long userId, long groupId);

	int getAllTimeRankOfUserForGroup(long userId, long groupId);
	ArrayList<Group> getAllGroupsFromDb(int offset, int count);
	ArrayList<Group> getAllGroupsWithoutLimit();
	void updateGroupCacheTable();
	
	
	
}
