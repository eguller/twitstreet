package com.twitstreet.twitter;

import org.apache.log4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.twitstreet.db.data.Announcer;
import com.twitstreet.localization.LocalizationUtil;

import twitter4j.FilterQuery;
import twitter4j.HashtagEntity;
import twitter4j.Status;
import twitter4j.StatusDeletionNotice;
import twitter4j.StatusListener;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;
import twitter4j.User;
import twitter4j.auth.AccessToken;

@Singleton
public class AdsListenerMgrImpl implements AdsListenerMgr {
	private static Logger logger = Logger.getLogger(AdsListenerMgrImpl.class);
	
	private static String[] FILTER_TERMS = new String[]{
		"I am bored",
		"I got bored",
		"\\u00e7ok s\u0131k\u0131ld\u0131m"
	};
	
	private static final long TWENTY_MIN = 20 * 60 * 1000;
	private static long lastMessage = 0;
	
	@Inject AnnouncerMgr announcerMgr;
	/* (non-Javadoc)
	 * @see com.twitstreet.twitter.AdsListenerMgr#start()
	 */
	@Override
	public void start(){
		TwitterStream twitterStream = new TwitterStreamFactory().getInstance();
		Announcer announcer = announcerMgr.randomAnnouncerData();
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
				HashtagEntity[] hashtagEntities = status.getHashtagEntities();
				String screenName = status.getUser().getScreenName();
				User user = status.getUser();
				if(user != null && (System.currentTimeMillis() - lastMessage > TWENTY_MIN)){
					lastMessage = System.currentTimeMillis();
					String message = constructAdsMessage(screenName, hashtagEntities, status.getUser().getLang());
					announcerMgr.announceFromAnnouncer(message);
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
	
	private static String constructAdsMessage(String screenName, HashtagEntity[] hashtagEntities, String lang){
		String message = "@" + screenName;
		for(HashtagEntity hashtagEntity : hashtagEntities){
			message += " #" + hashtagEntity.getText();
		}
		return message + " " + getRandomMessage(lang);
	}
	
	private static String getRandomMessage(String lang){
		LocalizationUtil lutil = LocalizationUtil.getInstance();
		int randomAdsIndex = (int)(Math.random() * Integer.parseInt(lutil.get("ads.size", LocalizationUtil.DEFAULT_LANGUAGE)));
		return lutil.get("ads." + randomAdsIndex, lang);
	}
	
}
