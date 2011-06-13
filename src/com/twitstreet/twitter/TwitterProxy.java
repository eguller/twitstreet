package com.twitstreet.twitter;

/**
 * This interface is not thread safe.
 * Don't inject it to Singletons directly (use a Provider).
 */
public interface TwitterProxy {

	int getFollowerCount(String screenName);

}
