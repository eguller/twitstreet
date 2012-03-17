package com.twitstreet.twitter;

import java.util.ArrayList;

import org.apache.log4j.Logger;

import twitter4j.ResponseList;
import twitter4j.Trend;
import twitter4j.Trends;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.User;
import twitter4j.auth.AccessToken;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.twitstreet.config.ConfigMgr;
import com.twitstreet.session.UserMgr;
import com.twitstreet.util.Util;

public class TwitterProxyImpl implements TwitterProxy {

	public static int USER_COUNT_FOR_UPDATE = 100;
	private static int INVALID_REQUEST = 400;
	private static int UNAUTHORIZED = 401;
	private static int NOT_FOUND = 404;
	private static int USER_SUSPENDED = 403;
	private static int RATE_LIMIT_EXCEEDED = 420;
	private static int TWITTER_SERVERS_OVERLOADED = 503;
	private static Logger logger = Logger.getLogger(TwitterProxyImpl.class);
	@Inject
	ConfigMgr configMgr = null;
	@Inject
	UserMgr userMgr = null;
	private Twitter twitter;
	String consumerKey;
	String consumerSecret;

	private String oToken;
	private String oSecret;
	AccessToken accessToken;
	@Inject
	public TwitterProxyImpl(ConfigMgr configMgr,
			@Assisted("oauthToken") String oauthToken,
			@Assisted("oauthTokenSecret") String oauthTokenSecret) {
		this.configMgr = configMgr;
		Twitter twitter = new TwitterFactory().getInstance();
		twitter.setOAuthConsumer(configMgr.getConsumerKey(),
				configMgr.getConsumerSecret());
		accessToken = new AccessToken(oauthToken, oauthTokenSecret);
		
		oToken = oauthToken;
		oSecret = oauthTokenSecret;
		
		twitter.setOAuthAccessToken(accessToken);
		this.setTwitter(twitter);
	}

	

	
	@Override
	public int getFollowerCount(String name){
		int followerCount = 0;
		try {
			
			User user = twitter.showUser(name);
			followerCount = user.getFollowersCount();
			logger.debug("Twitter: Follower count retrieved. Username: " + name
					+ ", Follower: " + followerCount);

		} catch (TwitterException e) {

			handleError(e,name);
		}
		return followerCount;
	}

	@Override
	public int getFollowerCount(long id){
		int followerCount = 0;
		try {
			User user = twitter.showUser(id);
			followerCount = user.getFollowersCount();
			logger.debug("Twitter: Follower count retrieved. Username: " + id
					+ ", Follower: " + followerCount);

		} catch (TwitterException e) {
			handleError(e, id);
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
	public User getTwUser(String twUserName){
		User user = null;
		try {
			user = twitter.showUser(twUserName);
			logger.debug("Twitter: User retrieved successfully. Username: " + twUserName);
		} catch (TwitterException e) {

			handleError(e, twUserName);
		}
		return user;
	}

	@Override
	public User getTwUser(long userId){
		User user = null;
		try {
			user = twitter.showUser(userId);
			logger.debug("Twitter: User retrieved successfully. Username: "
					+ userId);
		} catch (TwitterException e) {
			handleError(e, userId);
		}
		catch(Exception ex){
			logger.error(ex);
		}
		return user;
	}


	@Override
	public ArrayList<User> getTwUsers(ArrayList<Long> idList){
		
		
		
		
		ResponseList<User> users  = null;
		ArrayList<User> userList = new ArrayList<User>();
		try {
			
			long[] idArray = new long[idList.size()];
			int i =0;
			for(Long id: idList){

				idArray[i] = id;
				i++;
			}
			
			users = twitter.lookupUsers(idArray);
	
		} catch (TwitterException e) {
			handleError(e, idList);
		}
		catch(Exception ex){
			logger.error(ex);
		}
		
		if(users!=null){
			
			userList = new ArrayList<User>(users);
		}
		
		return userList;
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
	public ArrayList<SimpleTwitterUser> searchUsers(String user){
		ArrayList<SimpleTwitterUser> searchResultList = new ArrayList<SimpleTwitterUser>();
		ResponseList<User> userResponseList = null;

		String query = user;
		
		query = query.replace("%23", "");
		query = query.replace("%22", "");
		query = query.replace("%20", " ");
//		try {
//			query = Util.convertStringToValidURL(query);
//		} catch (UnsupportedEncodingException e1) {
//			logger.error("Error converting \"" + query +"\" to valid URL. ", e1);
//		}
	    query = Util.collapseSpaces(query).replace(' ', '+');
		try {
			userResponseList = twitter.searchUsers(query, 1);
		} catch (TwitterException e) {
			handleError(e, user);
		}
		if (userResponseList == null || userResponseList.size() < 1) {
		
		} else {
			for (int i = 0; i < userResponseList.size(); i++) {
				searchResultList.add( new SimpleTwitterUser(userResponseList.get(i)));
			}
		}
		return searchResultList;
	}
	private void handleError(TwitterException e, Object param){
		
		ArrayList<Object> params = null;
		if(param!=null){		
		  	params = new ArrayList<Object>();
		  	
		  	params.add(param);
		}
		handleError(e, params);
			
	}	
	private void handleError(TwitterException e){
		handleError(e, null);		
	}
	
	private void handleError(TwitterException e, ArrayList<Object> params){
		
		String paramsStr = "";
		
		if (params != null) {

			for (Object obj : params) {
				paramsStr = paramsStr + obj.toString()+", ";
			}

		}
		
		if (e.getStatusCode() == NOT_FOUND) {
			logger.debug("Twitter: User not found. Params: " + paramsStr);
		}
		else if (e.getStatusCode() == USER_SUSPENDED) {
			logger.info("Twitter: User suspended. Params: " + paramsStr);
		}
		else if (e.getStatusCode() == TWITTER_SERVERS_OVERLOADED) {
			logger.info("Twitter: The Twitter servers are up, but overloaded with requests. Try again later.");
		}else if (e.getStatusCode() == RATE_LIMIT_EXCEEDED) {
			logger.error("Twitter: Rate limit exceeded.");
		}else if (e.getStatusCode() == UNAUTHORIZED) {
		
			logger.error("Twitter: Authentication credentials were missing or incorrect. Token: "+oToken+", Secret: "+oSecret);
			 
			 com.twitstreet.db.data.User user = userMgr.getUserByTokenAndSecret(oToken, oSecret);
		
			userMgr.deleteUser(user.getId());
			
		}else if (e.getStatusCode() == INVALID_REQUEST) {
		
			logger.error("Twitter: The request was invalid. Possible reason: Query string may be including empty character. ");
			
		}
		else{
			logger.error("Twitter: Unhandled twitter exception.",e);
			
		}
		
		
	}

	@Override
	public ArrayList<Trend> getTrends() {
				
		Trends ts = null;
		try {
			
			ts = twitter.getLocationTrends(1);
		} catch (TwitterException e) {
			handleError(e);
		}
		ArrayList<Trend> trendList = new ArrayList<Trend>();
		if(ts!=null){
			Trend[] trends = ts.getTrends();
			
			if(trends!=null){
				for(Trend trend: trends){
					
					trendList.add(trend);
				}
				
			}
		}
		return trendList;
		
	}

	
	@Override
	public long searchAndGetFirstResult(String searchString) {
		if (searchString == null || searchString.length() < 1) {
			return -1;
		}
		long id = -1L;
	
		SimpleTwitterUser twUser = null;
		ArrayList<SimpleTwitterUser> searchResultList = new ArrayList<SimpleTwitterUser>();


		searchResultList = searchUsers(searchString);
		if (searchResultList != null && searchResultList.size() > 0) {
			twUser = searchResultList.get(0);
			searchResultList.remove(0);
		}

		if (twUser != null) {
			id = twUser.getId();
		}

		return id;

	}

	
	@Override
	public boolean verifyUser() {
		
		try {
			User user = twitter.verifyCredentials();
			if(user==null) return false;
		} catch (TwitterException e) {
			if (e.getStatusCode() == UNAUTHORIZED) {

				return false;

			}
		}

		return true;

	}

}
