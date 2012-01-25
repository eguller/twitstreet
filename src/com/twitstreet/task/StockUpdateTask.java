package com.twitstreet.task;

import java.util.List;

import org.apache.log4j.Logger;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.twitstreet.config.ConfigMgr;
import com.twitstreet.db.data.Stock;
import com.twitstreet.db.data.User;
import com.twitstreet.market.StockMgr;
import com.twitstreet.session.UserMgr;

@Singleton
public class StockUpdateTask implements Runnable {
	private static final long TEN_MINUTES = 10 * 60 * 1000;
	private static final int RESERVED_HIT_COUNT = 20;
	@Inject
	StockMgr stockMgr;
	@Inject
	ConfigMgr configMgr;
	@Inject
	UserMgr userMgr;
	private static Logger logger = Logger
			.getLogger(StockUpdateTask.class);
	Twitter twitter = null;

	@Override
	public void run() {
		Twitter twitter = new TwitterFactory().getInstance();
		twitter.setOAuthConsumer(configMgr.getConsumerKey(),
				configMgr.getConsumerSecret());
		while (true) {
			long startTime = System.currentTimeMillis();
			List<Stock> stockList = stockMgr.getUpdateRequiredStocks();

			for (Stock stock : stockList) {
				User user = userMgr.random();
				AccessToken accessToken = new AccessToken(user.getOauthToken(),
						user.getOauthTokenSecret());
				twitter.setOAuthAccessToken(accessToken);
				try {
					int remainingHits = twitter.getRateLimitStatus().getRemainingHits();
					if (remainingHits > RESERVED_HIT_COUNT) {
						twitter4j.User twUser = twitter.showUser(stock.getId());
						stockMgr.updateTwitterData(stock.getId(), twUser.getFollowersCount(), twUser.getProfileImageURL().toString(), twUser.getScreenName());
					}
					else{
						logger.warn("Twitter: " + user.getUserName() + "'s hit count is " + remainingHits);
					}
				} catch (TwitterException twitterException) {
					logger.error(
							"Twitter: Exception while retrieving follower count",
							twitterException);
				}
			}

			long endTime = System.currentTimeMillis();
			long diff = endTime - startTime;

			if (diff < TEN_MINUTES) {
				try {
					Thread.sleep(TEN_MINUTES - diff);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

}
