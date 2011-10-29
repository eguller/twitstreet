package com.twitstreet.twitter;


import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.User;


public interface TwitterProxy {
	public int getFollowerCount(String name) throws TwitterException;
	public int getFollowerCount(long id) throws TwitterException;
	public User getTwUser(String name) throws TwitterException;
	public void setTwitter(Twitter twitter);
	public Twitter getTwitter();
	User getTwUser(long userId) throws TwitterException;
	public SimpleTwitterUser[] searchUsers(String query) throws TwitterException;
}
