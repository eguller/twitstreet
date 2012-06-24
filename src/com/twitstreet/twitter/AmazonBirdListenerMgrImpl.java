/**
	TwitStreet - Twitter Stock Market Game
    Copyright (C) 2012  Engin Guller (bisanthe@gmail.com), Cagdas Ozek (cagdasozek@gmail.com)

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
**/

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
public class AmazonBirdListenerMgrImpl implements AmazonBirdListenerMgr {
	private static Logger logger = Logger.getLogger(AmazonBirdListenerMgrImpl.class);
	
	private static String[] FILTER_TERMS = new String[]{
		"wanna buy diablo",
		"want to buy diablo",
		"need diablo",
		"i love diablo","diablo3"	
	};
	
	private static final long ONE_MIN =  60 * 1000;
	private static long lastMessage = 0;
	
	
	private static final int REGULAR_TWEET = 0;
	private static final int RETWEEET = 1;
	private static final int FAVOURITE = 2;
	private static final int ACTION_TYPES = 5; // 3 and 4 reserved for default task
	
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
				if(user != null && (System.currentTimeMillis() - lastMessage > ONE_MIN)){
					logger.info("diablobird onstatus:"+screenName+","+status.getText());
					lastMessage = System.currentTimeMillis();
					
					String tweet = "Hey "+screenName+"! You can buy Diablo III from Amazon and play right now!. It's $59.99! http://www.amazon.com/gp/product/B00178630A/ref=as_li_tf_tl?ie=UTF8&tag=amazon-bird-cgds-20";
					int action = (int)(ACTION_TYPES * Math.random());
					switch (action) {
					case REGULAR_TWEET:						
						announcerMgr.announceForDiabloBird(tweet);
						break;
					case RETWEEET:
						announcerMgr.retweetForDiabloBird(status.getId());
						break;
					case FAVOURITE:
						announcerMgr.favouriteForDiabloBird(status.getId());
						break;
					default:						
						announcerMgr.replyForDiabloBird(tweet, status.getId());
						break;
					}
					
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
