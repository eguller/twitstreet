package com.twitstreet.twitter;

public interface TwitterProxy {
	String[] getNewRequestTokenPair();
	String getAuthorizationUrl(String[] requestTokenPair);
	String[] getAccessTokenPair(String[] requestTokenPair, String oauthVerifier);
	String doGet(String[] accessTokenPair, String url);
	
	int getFollowerCount(String userId);
}
