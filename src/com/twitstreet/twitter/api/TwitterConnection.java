package com.twitstreet.twitter.api;

import twitter4j.Twitter;

/**
 * @author eguller
 *
 */
public interface TwitterConnection {
	/**
	 * Load token, tokenSecret, consumerKey and consumerSecret
	 * from data provider.
	 */
	public void init();
	/**
	 * Returns a twitter object.
	 * This object is used to query
	 * user's follower count.
	 * <code>
	 * User user = twitter.showUser(userName);
	 * user.getFollowersCount()
	 * </code> 
	 * @return - Twitter object
	 */
	public Twitter getTwitter();
}
