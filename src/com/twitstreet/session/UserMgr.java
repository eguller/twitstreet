package com.twitstreet.session;

import com.google.inject.Key;
import com.twitstreet.base.IGenericMgr;
import com.twitstreet.base.Result;
import com.twitstreet.db.data.UserDO;

/**
 * TODO
 * Session Manager may be a bad place to make user-db operations.
 * If this class has another intend make a new class for user-db operations.
 * */
public interface UserMgr extends IGenericMgr<UserDO> {
	
	/**
	 * Start a session for new user. At least log it.
	 * @param accessData
	 * @return 
	 */
	public Result<UserDO> signup(UserDO user);
	

    /**
     * Return user by given twitter id
     * @param userId - User id given by twitter.
     * @return - user details.
     */
    public Result<UserDO> getUserById(long userId);

}
