package com.twitstreet.session;

import com.google.inject.Key;
import com.twitstreet.base.Result;
import com.twitstreet.data.SessionData;

public class SessionMgrImpl implements SessionMgr {

	@Override
	public Result<SessionData> start(String userId) {
		return Result.success(new SessionData(userId, new String[]{}));
	}

	@Override
	public Key<SessionData> getKey() {
		return Key.get(SessionData.class);
	}

}
