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
import com.twitstreet.localization.LocalizationUtil;

public class AnnouncerMgrImpl implements AnnouncerMgr {
	
	private static Logger logger = Logger.getLogger(AnnouncerMgrImpl.class);
	@Inject
	private DBMgr dbMgr;
	private static final String LOAD_ANNOUNCER = "select * from announcer";

	ArrayList<Twitter> announcerList = new ArrayList<Twitter>();
	ArrayList<Announcer> announcerDataList = new ArrayList<Announcer>();
	Twitter twitstreetGame = null;

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
					twitstreetGame.setOAuthConsumer(announcer.getConsumerKey(),
							announcer.getConsumerSecret());
					twitstreetGame.setOAuthAccessToken(new AccessToken(
							announcer.getAccessToken(), announcer
									.getAccessTokenSecret()));
				} else {

					Twitter twitter = new TwitterFactory().getInstance();
					twitter.setOAuthConsumer(announcer.getConsumerKey(),
							announcer.getConsumerSecret());
					twitter.setOAuthAccessToken(new AccessToken(announcer
							.getAccessToken(), announcer.getAccessTokenSecret()));
					announcerList.add(twitter);
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
	public Twitter random() {
		if (announcerList.size() > 0) {
			return announcerList.get((int) (Math.random() * announcerList
					.size()));
		} else {
			return null;
		}
	}

	@Override
	public void announceFromAnnouncer(String message) {
		Twitter twitter = random();
		if (twitter != null) {
			try {
				twitter.updateStatus(message);
			} catch (TwitterException e) {
				try {
					logger.error("Announcement failed: " + twitter.getScreenName(), e);
				} catch (IllegalStateException e1) {
					logger.error(e1);
				} catch (TwitterException e1) {
					logger.error(e1);
				}
			}
		}
		else{
			logger.error("TwitStreet announcer is null");
		}
	}

	public Twitter getTwitstreetGame() {
		return twitstreetGame;
	}

	@Override
	public void announceFromTwitStreetGame(String message) {
		if (twitstreetGame != null) {
			try {
				twitstreetGame.updateStatus(message);
			} catch (TwitterException e) {
				logger.error("Announcement failed", e);
			}
		}
		else{
			logger.error("Twitstreet game is null");
		}
	}
	
	@Override
	public ArrayList<Announcer> getAnnouncerDataList(){
		return announcerDataList;
	}

	@Override
	public Announcer randomAnnouncerData() {
		return announcerDataList.get( (int)(Math.random() * announcerDataList.size()));
	}

}
