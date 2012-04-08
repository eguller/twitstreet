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

import java.util.ArrayList;

import com.twitstreet.db.data.Group;
import com.twitstreet.db.data.User;

public interface GroupMgr{

	public long createGroup(String name);
	public long  createGroupForUser(String groupName, User userDO);
	
	public void updateGroup(Group group);
	
	public void deleteGroup(long id);
	public void deleteGroup(String name);

	public Group getGroup(long id);
	public Group getGroup(String name);
	
	public ArrayList<Group> getGroupsForUser(User userDO);
	
	public void addUserToGroup(User userDO, long id);
	public void addUserToGroupWithRole(User userDO, long id, int role);

	public void addUserToDefaultGroup(User userDO);
	
	public void removeUserFromGroup(User userDO, long id);



	
	
	
	
}
