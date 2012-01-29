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
