package com.twitstreet.twitter;

import com.twitstreet.base.Result;

public interface TwitterAuth {
	
	/**
	 * Validates the cookie and returns the user id.
	 * @param value Twitter Anywhere Cookie value
	 * @return Twitter user id
	 */
	Result<String> getUserIdFromTACookie(String value);
	
	/**
	 * Return access data for REST API (OAuth 1.0a) using Anywhere (OAuth 2.0) access token & bridge code
	 * @param accessToken
	 * @param oauthBridgeCode
	 * @return Result with TwitterAccessData
	 */
	Result<TwitterAccessData> getAccessDataWithBridge(String accessToken, String oauthBridgeCode);

}
