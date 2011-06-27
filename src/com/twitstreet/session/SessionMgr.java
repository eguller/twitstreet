package com.twitstreet.session;

import com.google.inject.Key;
import com.twitstreet.base.Result;
import com.twitstreet.twitter.TwitterAccessData;

public interface SessionMgr {
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
}
