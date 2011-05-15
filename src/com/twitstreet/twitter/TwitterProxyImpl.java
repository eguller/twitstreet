package com.twitstreet.twitter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import twitter4j.User;
import twitter4j.conf.ConfigurationBuilder;

import com.google.inject.Inject;
import com.google.inject.name.Named;

public class TwitterProxyImpl implements TwitterProxy {

	private static Logger logger = LoggerFactory.getLogger(TwitterProxyImpl.class);
	
	private final Twitter twitter;
	
	@Inject
	public TwitterProxyImpl(@Named("com.twitstreet.meta.ConsumerKey") String consumerKey,
			@Named("com.twitstreet.meta.ConsumerSecret") String consumerSecret, TwitterAccessData accessData) {
		ConfigurationBuilder confbuilder = new ConfigurationBuilder();
		confbuilder.setOAuthAccessToken(accessData.getOauthToken()).setOAuthAccessTokenSecret(accessData.getOauthTokenSecret())
				.setOAuthConsumerKey(consumerKey).setOAuthConsumerSecret(consumerSecret);

		twitter = new TwitterFactory(confbuilder.build()).getInstance();
	}

	@Override
	public int getFollowerCount(String screenName) {
		try {
			User user = twitter.showUser(screenName);
			return user.getFollowersCount();

		} catch (Exception e) {
			logger.error("twitter exception.", e);
			return -1;
		}
	}
}
