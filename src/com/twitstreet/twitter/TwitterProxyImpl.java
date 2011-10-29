package com.twitstreet.twitter;

import java.util.ArrayList;

import org.apache.log4j.Logger;

import twitter4j.ResponseList;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.User;

import com.twitstreet.db.data.Stock;

public class TwitterProxyImpl implements TwitterProxy {

	private static Logger logger = Logger.getLogger(TwitterProxyImpl.class);

	private Twitter twitter;
	String consumerKey;
	String consumerSecret;

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
			logger.error(
					"Twitter: Error while retrieving twitter user:" + name, e);
			throw e;
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
	public SimpleTwitterUser[] searchUsers(String query) throws TwitterException {
		SimpleTwitterUser[] searchResult = null;
		ResponseList<User> userResponseList = null;
		try {
			userResponseList = twitter.searchUsers(query, 1);
		} catch (TwitterException e) {
			logger.error("Twitter: User search failed for query: " + query, e);
			throw e;
		}
		if (userResponseList != null) {
			searchResult = new SimpleTwitterUser[userResponseList.size()];
			for (int i = 0; i < userResponseList.size(); i ++) {
				searchResult[i] = new SimpleTwitterUser(userResponseList.get(i));
			}
		}
		return searchResult;
	}
}
