package com.twitstreet.session;

import com.google.inject.Key;
import com.twitstreet.base.Result;
import com.twitstreet.twitter.TwitterAccessData;

public interface SessionMgr {
	/**
	 * Start a session for registered user. Read from DB.
	 * @param userId
	 * @return 
	 */
	public Result<SessionData> login(long userId);
	
	/**
	 * Start a session for new user. Save to DB.
	 * @param accessData
	 * @return 
	 */
	public Result<SessionData> register(TwitterAccessData accessData);
	
	/**
	 * Gets the guice binding key for Session Data.
	 * @return
	 */
	public Key<SessionData> getKey();
}
