package com.twitstreet.session;

import java.util.ArrayList;
import com.twitstreet.db.data.User;

public interface UserMgr{
	
	

    /**
     * Return user by given twitter id
     * @param userId - User id given by twitter.
     * @return - user details.
     */
    public User getUserById(long userId);


	public void saveUser(User user);
	
	public void updateUser(User user);
	
	public User random();
	
	public void increaseCash(long userId, int cash);
	
	public void updateCash(long userId, int amount);
	
	public ArrayList<User> getTopRank();

}
