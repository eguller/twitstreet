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

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import org.apache.log4j.Logger;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;

import com.google.inject.Inject;
import com.twitstreet.db.base.DBConstants;
import com.twitstreet.db.base.DBMgr;
import com.twitstreet.db.data.Announcer;

public class AnnouncerMgrImpl implements AnnouncerMgr {

	private static Logger logger = Logger.getLogger(AnnouncerMgrImpl.class);
	@Inject
	private DBMgr dbMgr;
	private static final String LOAD_ANNOUNCER = "select * from announcer where suspended = 0";
	private static final String SUSPEND_ANNOUNCER = "update announcer set suspended = 1 where id = ?";

	ArrayList<Announcer> announcerList = new ArrayList<Announcer>();
	ArrayList<Announcer> announcerDataList = new ArrayList<Announcer>();
	Twitter twitstreetGame = null;
	Twitter diablobird = null;

	public void loadAnnouncers() {
		Connection connection = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			connection = dbMgr.getConnection();
			ps = connection.prepareStatement(LOAD_ANNOUNCER);
			rs = ps.executeQuery();
			while (rs.next()) {
				Announcer announcer = new Announcer();
				announcer.getDataFromResultSet(rs);
				announcerDataList.add(announcer);
				if (TWITSTREET_GAME.equals(announcer.getName())) {
					twitstreetGame = new TwitterFactory().getInstance();
					twitstreetGame.setOAuthConsumer(announcer.getConsumerKey(), announcer.getConsumerSecret());
					twitstreetGame.setOAuthAccessToken(new AccessToken(announcer.getAccessToken(), announcer.getAccessTokenSecret()));
				} else {
					announcerList.add(announcer);
				}
			}
			logger.debug(DBConstants.QUERY_EXECUTION_SUCC + ps.toString());
		} catch (SQLException ex) {
			logger.error(DBConstants.QUERY_EXECUTION_FAIL + ps.toString(), ex);

		} finally {
			dbMgr.closeResources(connection, ps, rs);
		}

	}

	@Override
	public Announcer random() {
		if (announcerList.size() > 0) {
			return announcerList.get((int) (Math.random() * announcerList.size()));
		} else {
			return null;
		}
	}

	@Override
	public void announceFromRandomAnnouncer(String message) {
		TwitterClient twitter = new TwitterClient(random(), this);
				twitter.tweet(message);
	}

	public Twitter getTwitstreetGame() {
		return twitstreetGame;
	}

	@Override
	public void announceFromTwitStreetGame(String message) {
		String screenName = "";
		if (twitstreetGame != null) {
			try {
				twitstreetGame.updateStatus(message);

			} catch (TwitterException e) {
				logger.error("Announcement failed: " + screenName, e);
			}
		} else {
			logger.error("Twitstreet game is null");
		}
	}

	@Override
	public ArrayList<Announcer> getAnnouncerDataList() {
		return announcerDataList;
	}

	@Override
	public Announcer randomAnnouncerData() {
		return announcerDataList.get((int) (Math.random() * announcerDataList.size()));
	}

	@Override
	public void retweet(long statusId) {
		TwitterClient twitter = new TwitterClient(random(), this);
		try {
			twitter.retweet(statusId);
		} catch (Exception e) {
			logger.error("Error while retweeting: " + statusId + " Announcer: " + twitter.getAnnouncer().getName(), e);
		}
	}

	@Override
	public void follow(long userId) {
		TwitterClient twitter = new TwitterClient(random(), this);
		try {
			twitter.follow(userId);
		} catch (Exception e) {
			logger.error("Error while following: " + userId + " Announcer: " + twitter.getAnnouncer().getName(), e);
		}
	}


	@Override
	public void favourite(long statusId) {
		TwitterClient twitter = new TwitterClient(random(), this);
		try {
			twitter.favorite(statusId);
		} catch (Exception e) {
			logger.error("Error while creating favorite: " + statusId + " Announcer: " + twitter.getAnnouncer().getName(), e);
		}
	}

	@Override
	public void reply(String message, long statusId) {
		TwitterClient twitter = new TwitterClient(random(), this);
		twitter.reply(message,statusId);
	}

	@Override
	public void setAnnouncerSuspended(long id) {
		Connection connection = null;
		PreparedStatement ps = null;
		try {
			connection = dbMgr.getConnection();
			ps = connection.prepareStatement(SUSPEND_ANNOUNCER);
			ps.setLong(1, id);
			ps.executeUpdate();
		}
		catch (SQLException ex) {
			logger.error(DBConstants.QUERY_EXECUTION_FAIL + ps.toString(), ex);
		} finally {
			dbMgr.closeResources(connection, ps, null);
		}
	}
}
