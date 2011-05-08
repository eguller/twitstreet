package com.twitstreet.quote.impl;

import twitter4j.TwitterException;
import twitter4j.User;

import com.google.inject.Inject;
import com.twitstreet.quote.api.FollowerQuote;
import com.twitstreet.quote.data.FollowerQuoteResultData;
import com.twitstreet.twitter.api.TwitterConnection;

public class FollowerQuoteImpl implements FollowerQuote {

	@Inject
	TwitterConnection twitterConnection;
	
	@Override
	public FollowerQuoteResultData getFollowerQuote(String twitterUser) {
		User user = null;
		try {
			user = twitterConnection.getTwitter().showUser(twitterUser);
		} catch (TwitterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(user != null){
			int followerCount = user.getFollowersCount();
		}
		return new FollowerQuoteResultData();
	}

}
