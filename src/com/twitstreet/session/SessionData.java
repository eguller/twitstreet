package com.twitstreet.session;

import com.google.inject.Inject;
import com.google.inject.servlet.SessionScoped;
import com.twitstreet.twitter.TwitterAccessData;

@SessionScoped
public class SessionData {
	private final TwitterAccessData accessData;
	
	@Inject
	public SessionData() {
		throw new RuntimeException("Not authenticated.");
	}
	
	public SessionData(TwitterAccessData accessData) {
		this.accessData = accessData;
	}
	
	public TwitterAccessData getTwitterAccess() {
		return accessData;
	}
}
