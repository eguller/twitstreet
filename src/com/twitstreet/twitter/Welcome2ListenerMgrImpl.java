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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;

import twitter4j.FilterQuery;
import twitter4j.ResponseList;
import twitter4j.Status;
import twitter4j.StatusDeletionNotice;
import twitter4j.StatusListener;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;
import twitter4j.User;
import twitter4j.UserMentionEntity;
import twitter4j.auth.AccessToken;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.twitstreet.config.ConfigMgr;
import com.twitstreet.db.data.Announcer;
import com.twitstreet.db.data.Stock;
import com.twitstreet.market.StockMgr;
import com.twitstreet.session.UserMgr;

@Singleton
public class Welcome2ListenerMgrImpl implements Welcome2ListenerMgr {
	private static final int MIN_FOLLOWER_COUNT_FOR_TREND = 500;
	private static Logger logger = Logger.getLogger(Welcome2ListenerMgrImpl.class);
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
	private static String[] FILTER_TERMS = new String[]{
		"welcome to twitter",
		"joined twitter"
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
				UserMentionEntity[] userMentionEntities  = status.getUserMentionEntities();
				for(UserMentionEntity userMentionEntity : userMentionEntities){
					idSet.add(userMentionEntity.getId());
					if(idSet.size() >= TwitterProxyImpl.IDS_SIZE){
						List<User> userList = twitterProxy.getTwUsers(new ArrayList<Long>(idSet));
						if(userList != null){
							for(User user : userList){
								if(user.getFollowersCount() > MIN_FOLLOWER_COUNT_FOR_TREND){
									Stock stock = new Stock(user);
									stockMgr.saveStock(stock);
									stockMgr.saveTrend(stock.getId());
								}
							}
						}
						idSet.clear();
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
}