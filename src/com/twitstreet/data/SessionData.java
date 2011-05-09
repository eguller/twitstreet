package com.twitstreet.data;

public class SessionData {
	final String twitterUserId;
	final String accessToken;
	final String accessTokenSecret;
	
	public SessionData(String twitterUserId, String accessToken,
			String accessTokenSecret) {
		this.twitterUserId = twitterUserId;
		this.accessToken = accessToken;
		this.accessTokenSecret = accessTokenSecret;
	}
	
	public String getTwitterUserId() {
		return twitterUserId;
	}

	public String getAccessToken() {
		return accessToken;
	}

	public String getAccessTokenSecret() {
		return accessTokenSecret;
	}
}
