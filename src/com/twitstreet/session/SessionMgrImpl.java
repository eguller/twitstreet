package com.twitstreet.session;

import com.google.inject.Key;
import com.twitstreet.base.Result;
import com.twitstreet.twitter.TwitterAccessData;

public class SessionMgrImpl implements SessionMgr {

	@Override
	public Key<SessionData> getKey() {
		return Key.get(SessionData.class);
	}

	@Override
	public Result<SessionData> login(long userId) {
		//TODO remove this method. We probably don't need it anymore.
		TwitterAccessData accessData = new TwitterAccessData("oauthToken", "oauthTokenSecret", userId, "screenName");
		return Result.success(new SessionData(accessData));
	}

	@Override
	public Result<SessionData> register(TwitterAccessData accessData) {
		// TODO 
		return Result.success(new SessionData(accessData));
	}

}
