package com.twitstreet.session;

import java.util.ArrayList;
import com.twitstreet.db.data.User;

public interface UserMgr {
	
	public static final int MAX_RANK = 20;

    /**
     * Return user by given twitter id
     * @param userId - User id given by twitter.
     * @return - user details.
     */
    public User getUserById(long userId);


	public void saveUser(User user);
	
	public void updateUser(User user);
	
	public User random();
	
	public void increaseCash(long userId, double cash);
	
	public void updateCash(long userId, double amount);
	
	public ArrayList<User> getTopRank(int pageNumber);
	
	public int count();

}
