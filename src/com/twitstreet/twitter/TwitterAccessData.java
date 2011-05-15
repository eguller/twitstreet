package com.twitstreet.twitter;

public class TwitterAccessData {

	private final String oauthToken;
	private final String oauthTokenSecret;
	private final String userId;
	private final String screenName;
	
	public TwitterAccessData() {
		throw new RuntimeException("Not authenticated.");
	}
	
	public TwitterAccessData(String oauthToken, String oauthTokenSecret, String userId, String screenName) {
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
	
	public String getUserId() {
		return userId;
	}
	
	public String getScreenName() {
		return screenName;
	}
}
