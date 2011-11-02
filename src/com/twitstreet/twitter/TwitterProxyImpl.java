package com.twitstreet.twitter;

import org.apache.log4j.Logger;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.twitstreet.config.ConfigMgr;

import twitter4j.ResponseList;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.User;
import twitter4j.auth.AccessToken;

public class TwitterProxyImpl implements TwitterProxy {
	private static final int NOT_FOUND = 404;

	private static Logger logger = Logger.getLogger(TwitterProxyImpl.class);
	@Inject
	ConfigMgr configMgr = null;
	private Twitter twitter;
	String consumerKey;
	String consumerSecret;

	@Inject
	public TwitterProxyImpl(ConfigMgr configMgr,
			@Assisted("ouathToken") String ouathToken,
			@Assisted("oauthTokenSecret") String oauthTokenSecret) {
		this.configMgr = configMgr;
		Twitter twitter = new TwitterFactory().getInstance();
		twitter.setOAuthConsumer(configMgr.getConsumerKey(),
				configMgr.getConsumerSecret());
		AccessToken accessToken = new AccessToken(ouathToken, oauthTokenSecret);
		twitter.setOAuthAccessToken(accessToken);
		this.setTwitter(twitter);
	}

	@Override
	public int getFollowerCount(String name) throws TwitterException {
		int followerCount = 0;
		try {
			User user = twitter.showUser(name);
			followerCount = user.getFollowersCount();
			logger.debug("Twitter: Follower count retrieved. Username: " + name
					+ ", Follower: " + followerCount);

		} catch (TwitterException e) {
			logger.error(
					"Twitter: Retrieving follower count failed for username: "
							+ name, e);
			throw e;
		}
		return followerCount;
	}

	@Override
	public int getFollowerCount(long id) throws TwitterException {
		int followerCount = 0;
		try {
			User user = twitter.showUser(id);
			followerCount = user.getFollowersCount();
			logger.debug("Twitter: Follower count retrieved. Username: " + id
					+ ", Follower: " + followerCount);

		} catch (TwitterException e) {
			logger.error(
					"Twitter: Retrieving follower count failed for username: "
							+ id, e);
			throw e;
		}
		return followerCount;
	}

	public String getConsumerKey() {
		return consumerKey;
	}

	public void setConsumerKey(String consumerKey) {
		this.consumerKey = consumerKey;
	}

	public String getConsumerSecret() {
		return consumerSecret;
	}

	public void setConsumerSecret(String consumerSecret) {
		this.consumerSecret = consumerSecret;
	}

	@Override
	public User getTwUser(String name) throws TwitterException {
		User user = null;
		try {
			user = twitter.showUser(name);
			logger.debug("Twitter: User retrieved successfully. Username: "
					+ name);
		} catch (TwitterException e) {
			if (e.getStatusCode() == NOT_FOUND) {
				logger.debug("Twitter: User not found. Username: " + name);
			} else {
				logger.error(
						"Twitter: Error while retrieving twitter user:" + name, e);
				throw e;
			}
		}
		return user;
	}

	@Override
	public User getTwUser(long userId) throws TwitterException {
		User user = null;
		try {
			user = twitter.showUser(userId);
			logger.debug("Twitter: User retrieved successfully. Username: "
					+ userId);
		} catch (TwitterException e) {
			logger.error("Twitter: Error while retrieving twitter user:"
					+ userId, e);
			throw e;
		}
		return user;
	}

	@Override
	public void setTwitter(Twitter twitter) {
		this.twitter = twitter;
	}

	@Override
	public Twitter getTwitter() {
		return twitter;
	}

	@Override
	public SimpleTwitterUser[] searchUsers(String user)
			throws TwitterException {
		SimpleTwitterUser[] searchResult = null;
		ResponseList<User> userResponseList = null;
		try {
			userResponseList = twitter.searchUsers(user, 10);
		} catch (TwitterException e) {
			logger.error("Twitter: User search failed for query: " + user, e);
			throw e;
		}
		if (userResponseList != null) {
			searchResult = new SimpleTwitterUser[userResponseList.size()];
			for (int i = 0; i < userResponseList.size(); i++) {
				searchResult[i] = new SimpleTwitterUser(userResponseList.get(i));
			}
		}
		return searchResult;
	}
}
