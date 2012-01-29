package com.twitstreet.twitter;

import twitter4j.User;

public class SimpleTwitterUser {
	long id;
	String screenName;
	String pictureUrl;
	int followerCount = 0;
	public SimpleTwitterUser(User user){
		this.id = user.getId();
		this.screenName = user.getScreenName();
		this.pictureUrl = user.getProfileImageURL().toExternalForm();
		this.followerCount = user.getFollowersCount();
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
}
