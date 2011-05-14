package com.twitstreet.twitter;

import com.twitstreet.base.Result;

public interface TwitterProxy {
	String[] getNewRequestTokenPair();
	String getAuthorizationUrl(String[] requestTokenPair);
	String[] getAccessTokenPair(String[] requestTokenPair, String oauthVerifier);
	String doGet(String[] accessTokenPair, String url);
	
	/**
	 * Return OAuth 1.0a access token pair for OAuth 2.0 token & bridge code
	 * @param requestToken
	 * @param oauthBridgeCode
	 * @return
	 */
	Result<String[]> getAccessTokenWithBridge(String requestToken, String oauthBridgeCode);
	
	
	int getFollowerCount(String[] accessTokenPair, String screenName);
}
