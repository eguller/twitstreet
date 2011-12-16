package com.twitstreet.session;

import java.sql.SQLException;

import com.google.inject.Key;
import com.twitstreet.db.data.User;

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
    public User getUserById(long userId) throws SQLException;


	public void saveUser(User user) throws SQLException;
	
	public void updateUser(User user) throws SQLException;
	
	public User random();
	
	public void increaseCash(long userId, int cash) throws SQLException;
	
	public void updateCashAndPortfolio(long userId, int amount) throws SQLException;

}
