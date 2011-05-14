package com.twitstreet.data;

public class SessionData {
	final String twitterUserId;
	final String[] accessTokenPair;
	
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
