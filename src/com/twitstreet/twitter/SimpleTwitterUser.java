package com.twitstreet.twitter;

import twitter4j.User;

public class SimpleTwitterUser {
	long id;
	String screenName;
	String pictureUrl;
	public SimpleTwitterUser(User user){
		this.id = user.getId();
		this.screenName = user.getScreenName();
		this.pictureUrl = user.getProfileImageURL().toExternalForm();
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
}
