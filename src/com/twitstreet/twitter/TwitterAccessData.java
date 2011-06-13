package com.twitstreet.twitter;

public class TwitterAccessData {

	private final String oauthToken;
	private final String oauthTokenSecret;
	private final long userId;
	private final String screenName;
	
	public TwitterAccessData() {
		throw new RuntimeException("Not authenticated.");
	}
	
	public TwitterAccessData(String oauthToken, String oauthTokenSecret, long userId, String screenName) {
		this.oauthToken = oauthToken;
		this.oauthTokenSecret = oauthTokenSecret;
		this.userId = userId;
		this.screenName = screenName;
	}
	
	public String getOauthToken() {
		return oauthToken;
	}
	
	public String getOauthTokenSecret() {
		return oauthTokenSecret;
	}
	
	public long getUserId() {
		return userId;
	}
	
	public String getUserIdStr() {
		return String.valueOf(userId);
	}
	
	public String getScreenName() {
		return screenName;
	}
}
