package com.twitstreet.session;

import com.google.inject.Key;
import com.twitstreet.base.Result;
import com.twitstreet.data.SessionData;

public class SessionMgrImpl implements SessionMgr {

	@Override
	public Result<SessionData> start(String userId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Key<SessionData> getKey() {
		return Key.get(SessionData.class);
	}

}
