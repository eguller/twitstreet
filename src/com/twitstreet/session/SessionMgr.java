package com.twitstreet.session;

import com.twitstreet.base.Result;
import com.twitstreet.data.SessionData;

public interface SessionMgr {
	/**
	 * Start a session for user. Read TokerPair from DB.
	 * @param userId
	 * @return Result with SessionData
	 */
	public Result<SessionData> start(String userId);
}
