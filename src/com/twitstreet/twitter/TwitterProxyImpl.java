package com.twitstreet.twitter;


import org.apache.log4j.Logger;

import twitter4j.Twitter;
import twitter4j.User;

import com.twitstreet.db.data.StockDO;

public class TwitterProxyImpl implements TwitterProxy {

	private static Logger logger = Logger.getLogger(TwitterProxyImpl.class);
	
	private Twitter twitter;
	String consumerKey;
	String consumerSecret;
	

	@Override
	public int getStockCount(String name) {
		try {
			User user = twitter.showUser(name);
			return user.getFollowersCount();

		} catch (Exception e) {
			logger.error("twitter exception.", e);
			return -1;
		}
	}
	
	public String getConsumerKey() {
		return consumerKey;
	}

	public void setConsumerKey(
			 String consumerKey) {
		this.consumerKey = consumerKey;
	}

	public String getConsumerSecret() {
		return consumerSecret;
	}

	public void setConsumerSecret(String consumerSecret) {
		this.consumerSecret = consumerSecret;
	}

	@Override
	public StockDO getStock(String name) {
		StockDO stockDO = new StockDO();
		try {
			User user = twitter.showUser(name);
			stockDO.setId(user.getId());
			stockDO.setTotal(user.getFollowersCount());
			stockDO.setName(user.getScreenName());
			stockDO.setSold(0.0);

		} catch (Exception e) {
			logger.error("twitter exception.", e);
		}
		return stockDO;
	}
	
	
}
