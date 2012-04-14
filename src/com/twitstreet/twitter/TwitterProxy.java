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


import java.util.ArrayList;
import java.util.Date;
import java.util.Set;

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
	

	public Set<String> getTrends();
	public long searchAndGetFirstResult(String searchString);
	ArrayList<User> getTwUsers(ArrayList<Long> idList);
	boolean verifyUser();
	Date getFirstTweetDate(long stockId);
}
