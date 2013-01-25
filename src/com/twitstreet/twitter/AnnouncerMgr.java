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

import com.twitstreet.db.data.Announcer;

import twitter4j.Twitter;


public interface AnnouncerMgr {
	public static final String TWITSTREET_GAME = "twitstreet_game";
	public Announcer random();
	public void loadAnnouncers();
	public void announceFromRandomAnnouncer(String message);
	public void announceFromTwitStreetGame(String message);
	public ArrayList<Announcer> getAnnouncerDataList();
	public Announcer randomAnnouncerData();
	public void retweet(long statusId);
	public void follow(long userId);
	public void favourite(long statusId);
	public void reply(String message, long statusId);
	public void setAnnouncerSuspended(long id);
}