/**
	TwitStreet - Twitter Stock Market Game
    Copyright (C) 2012  Engin Guller (bisanth@gmail.com), Cagdas Ozek (cagdasozek@gmail.com)

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
