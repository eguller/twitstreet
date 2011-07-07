package com.twitstreet.session;

import com.google.inject.Key;
import com.twitstreet.base.IGenericMgr;
import com.twitstreet.base.Result;
import com.twitstreet.db.data.UserDO;
import com.twitstreet.twitter.TwitterAccessData;

/**
 * TODO
 * Session Manager may be a bad place to make user-db operations.
 * If this class has another intend make a new class for user-db operations.
 * */
public interface SessionMgr extends IGenericMgr<UserDO> {
	//TODO remove this method. We probably don't need it anymore.
	public Result<SessionData> login(long userId);
	
	/**
	 * Start a session for new user. At least log it.
	 * @param accessData
	 * @return 
	 */
	public Result<SessionData> register(TwitterAccessData accessData);
	
	/**
	 * Gets the guice binding key for Session Data.
	 * @return
	 */
	public Key<SessionData> getKey();

    /**
     * Return user by given twitter id
     * @param userId - User id given by twitter.
     * @return - user details.
     */
    public Result<UserDO> getUserById(long userId);

}
