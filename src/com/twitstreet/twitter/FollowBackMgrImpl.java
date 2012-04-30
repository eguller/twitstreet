package com.twitstreet.twitter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import org.apache.log4j.Logger;

import twitter4j.FilterQuery;
import twitter4j.Status;
import twitter4j.StatusDeletionNotice;
import twitter4j.StatusListener;
import twitter4j.Twitter;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;
import twitter4j.auth.AccessToken;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.twitstreet.config.ConfigMgr;
import com.twitstreet.db.data.Announcer;
import com.twitstreet.market.StockMgr;
import com.twitstreet.session.UserMgr;

@Singleton
public class FollowBackMgrImpl implements FollowBackMgr {
	private static final long FOLLOW_INTERVAL = 60 * 60 * 1000;
	
	private static Logger logger = Logger.getLogger(FollowBackMgrImpl.class);
	@Inject AnnouncerMgr announcerMgr;
	@Inject StockMgr stockMgr;
	@Inject UserMgr userMgr;
	@Inject ConfigMgr configMgr;
	@Inject
	TwitterProxyFactory twitterProxyFactory = null;
	TwitterProxy twitterProxy = null;
	Twitter twitter = null;
	Set<Long> idSet = new HashSet<Long>();
	ArrayList<Long> validIdList = new ArrayList<Long>();
	
	private static long lastFollow = 0;
	
	private static String[] FILTER_TERMS = new String[]{
		"i follow back"
	};
	
	@Override
	public void start() {
		Announcer announcer = announcerMgr.randomAnnouncerData();
		twitterProxy = twitterProxyFactory.create(announcer.getAccessToken(), announcer.getAccessTokenSecret());

		TwitterStream twitterStream = new TwitterStreamFactory().getInstance();
		twitterStream.setOAuthConsumer(announcer.getConsumerKey(), announcer.getConsumerSecret());
		twitterStream.setOAuthAccessToken(new AccessToken(announcer.getAccessToken(), announcer.getAccessTokenSecret()));
		twitterStream.addListener(new StatusListener() {
			
			@Override
			public void onException(Exception arg0) {
				
			}
			
			@Override
			public void onTrackLimitationNotice(int arg0) {
				
			}
			
			@Override
			public void onStatus(Status status) {
				if(System.currentTimeMillis() - lastFollow > FOLLOW_INTERVAL){
					twitter4j.User user = status.getUser();
					announcerMgr.follow(user.getId());
					lastFollow = System.currentTimeMillis();
				}
				
			}
			
			@Override
			public void onScrubGeo(long arg0, long arg1) {
				
			}
			
			@Override
			public void onDeletionNotice(StatusDeletionNotice arg0) {
			}
		});
		
		FilterQuery filterQuery = new FilterQuery();
		filterQuery.count(0);
		filterQuery.track(FILTER_TERMS);
		twitterStream.filter(filterQuery);
	}
}
