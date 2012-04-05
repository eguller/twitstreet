package com.twitstreet.twitter;

import java.io.Serializable;

import twitter4j.User;

public class SimpleTwitterUser implements Serializable{

	private static final long serialVersionUID = -6740949353639804140L;
	long id;
	String screenName;
	String pictureUrl;
	int followerCount = 0;
	boolean verified;
	
	public SimpleTwitterUser(User user){
		this.id = user.getId();
		this.screenName = user.getScreenName();
		this.pictureUrl = user.getProfileImageURL().toExternalForm();
		this.followerCount = user.getFollowersCount();
		this.verified = user.isVerified();
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getScreenName() {
		return screenName;
	}
	public void setScreenName(String screenName) {
		this.screenName = screenName;
	}
	public String getPictureUrl() {
		return pictureUrl;
	}
	public void setPictureUrl(String pictureUrl) {
		this.pictureUrl = pictureUrl;
	}
	public int getFollowerCount() {
		return followerCount;
	}
	public void setFollowerCount(int followerCount) {
		this.followerCount = followerCount;
	}
	public boolean isVerified() {
		return verified;
	}
}
