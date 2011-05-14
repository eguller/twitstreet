package com.twitstreet.data;

import com.google.inject.Inject;
import com.google.inject.servlet.SessionScoped;

@SessionScoped
public class SessionData {
	private final String twitterUserId;
	private final String[] accessTokenPair;
	
	@Inject
	public SessionData() {
		throw new RuntimeException("Not authenticated.");
	}
	
	public SessionData(String twitterUserId, String[] accessTokenPair) {
		this.twitterUserId = twitterUserId;
		this.accessTokenPair = accessTokenPair;
	}
	
	public String getTwitterUserId() {
		return twitterUserId;
	}

	public String[] getAccessTokenPair() {
		return accessTokenPair;
	}
}
