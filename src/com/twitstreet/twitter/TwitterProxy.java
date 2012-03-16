package com.twitstreet.twitter;


import java.util.ArrayList;

import twitter4j.Trend;
import twitter4j.Twitter;
import twitter4j.User;


public interface TwitterProxy {
	public int getFollowerCount(String name);
	public int getFollowerCount(long id);
	public User getTwUser(String name);
	public void setTwitter(Twitter twitter);
	public Twitter getTwitter();
	User getTwUser(long userId);
	public ArrayList<SimpleTwitterUser> searchUsers(String query);
	

	public ArrayList<Trend> getTrends();
	public long searchAndGetFirstResult(String searchString);
	ArrayList<User> getTwUsers(ArrayList<Long> idList);
	boolean verifyUser();
}
