package com.twitstreet.twitter;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.log4j.Logger;

import com.google.inject.Inject;
import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;
import com.twitstreet.config.ConfigMgr;
import com.twitstreet.db.base.DBConstants;
import com.twitstreet.db.base.DBMgr;
import com.twitstreet.db.data.Stock;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;


public class TwitstreetAnnouncerImpl implements TwitstreetAnnouncer {
	
	private Twitter twitter = null;

	@Inject
	ConfigMgr configMgr;
	@Inject
	DBMgr dbMgr;

	private static Logger logger = Logger.getLogger(TwitstreetAnnouncerImpl.class);
	
	@Override
	public boolean mention(Stock stock, String message){

		if (addStockIntoAnnouncement(stock.getId())) {

			twitter = new TwitterFactory().getInstance();
			twitter.setOAuthConsumer(configMgr.getAnnouncerConsumerKey(), configMgr.getAnnouncerConsumerSecret());

			twitter.setOAuthAccessToken(new AccessToken(configMgr.getAnnouncerAccessToken(), configMgr.getAnnouncerAccessSecret()));

			try {
				twitter.updateStatus("@" + stock.getName() + " " + message + " #twitstreet");
			} catch (TwitterException e) {
				logger.error("sendMessage:" + stock.getName() + " " + message);
			}

			return true;
		}
		
		return false;
	}
	
	
	public boolean addStockIntoAnnouncement(long stockid) {
		Connection connection = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			connection = dbMgr.getConnection();
			ps = connection.prepareStatement("insert into announcement(stock_id) VALUES  (?) ");
			ps.setLong(1, stockid);
			ps.executeUpdate();

			logger.debug(DBConstants.QUERY_EXECUTION_SUCC + ps.toString());
			
			return true;
//		} catch (MySQLIntegrityConstraintViolationException ex) {
//			
//			return false;
		} catch (SQLException e) {
			
			return false;
		} finally {
			dbMgr.closeResources(connection, ps, rs);
		}
	}


	@Override
	public void removeOldRecords(int removeOlderThanMinutes) {
		Connection connection = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			connection = dbMgr.getConnection();
			ps = connection.prepareStatement(" delete from announcement where timestampdiff(minute,timeSent,now()) > ? ");
			ps.setInt(1, removeOlderThanMinutes);
			ps.executeUpdate();

			logger.debug(DBConstants.QUERY_EXECUTION_SUCC + ps.toString());

		} catch (SQLException e) {
			logger.error(DBConstants.QUERY_EXECUTION_FAIL + ps.toString(), e);
		
		} finally {
			dbMgr.closeResources(connection, ps, rs);
		}
		
	}


}
