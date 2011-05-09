package com.twitstreet.session;

import com.twitstreet.data.SessionData;

public interface SessionMgr {
	public SessionData start(String userId, String bridgeCode);
}
