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
		//TODO read from DB
		TwitterAccessData accessData = new TwitterAccessData("oauthToken", "oauthTokenSecret", userId, "screenName");
		return Result.success(new SessionData(accessData));
	}

	@Override
	public Result<SessionData> register(TwitterAccessData accessData) {
		// TODO Save to DB (insert or update, screenName may change)
		return Result.success(new SessionData(accessData));
	}

}
