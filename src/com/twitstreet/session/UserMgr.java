package com.twitstreet.session;

import java.sql.SQLException;

import com.google.inject.Key;
import com.twitstreet.db.data.UserDO;

/**
 * TODO
 * Session Manager may be a bad place to make user-db operations.
 * If this class has another intend make a new class for user-db operations.
 * */
public interface UserMgr{
	
	

    /**
     * Return user by given twitter id
     * @param userId - User id given by twitter.
     * @return - user details.
     */
    public UserDO getUserById(long userId) throws SQLException;


	public void saveUser(UserDO user) throws SQLException;

}
