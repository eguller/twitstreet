package com.twitstreet.twitter;

import com.twitstreet.base.Result;

public interface TwitterAuth {

	/**
	 * Validates the cookie and returns the user id.
	 * 
	 * @param value
	 *            Twitter Anywhere Cookie value
	 * @return Twitter user id
	 */
	Result<String> getUserIdFromTACookie(String value);

	/**
	 * Return access data for REST API (OAuth 1.0a) using OAuth verifier
	 * 
	 * @param requestToken
	 * @param requestVerifier
	 * @return Result with TwitterAccessData
	 */
	Result<TwitterAccessData> getAccess(String requestToken, String requestVerifier);

	/**
	 * Retrieve OAuth request token from twitter, save token secret and return
	 * authentication url that includes the request token. Token secret is saved
	 * in a cache with TTL=30 sec.
	 * 
	 * @param callbackURL The url that twitter will redirect after authentication
	 * @return Result with authentication url
	 */
	Result<String> getAuthenticationUrl(String callbackURL);

}
