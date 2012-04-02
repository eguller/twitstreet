package com.twitstreet.season;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import org.apache.log4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.twitstreet.db.base.DBConstants;
import com.twitstreet.db.base.DBMgr;

@Singleton
public class SeasonImpl implements SeasonMgr{
	private static Logger logger = Logger.getLogger(SeasonImpl.class);

	private static String SELECT_FROM_SEASON_INFO = " select id, startTime, endTime, active from season_info ";

	@Inject DBMgr dbMgr;

	private ArrayList<SeasonInfo> allSeasons = new ArrayList<SeasonInfo>();

	private SeasonInfo currentSeason;
	
	@Override
	public SeasonInfo getSeasonInfo(int id) {
		Connection connection = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		SeasonInfo siDO = null;

		try {
			connection = dbMgr.getConnection();
			ps = connection.prepareStatement(SELECT_FROM_SEASON_INFO
					+ " where id = ?");
			ps.setInt(1, id);
			rs = ps.executeQuery();
			if (rs.next()) {
				siDO = new SeasonInfo();
				siDO.getDataFromResultSet(rs);
			}

			logger.debug(DBConstants.QUERY_EXECUTION_SUCC + ps.toString());
		} catch (SQLException ex) {
			logger.error(DBConstants.QUERY_EXECUTION_FAIL + ps.toString(), ex);
		} finally {
			dbMgr.closeResources(connection, ps, rs);
		}
		return siDO;
	}

	@Override
	public ArrayList<SeasonInfo> getAllSeasons() {
		return allSeasons ;
	}

	@Override
	public ArrayList<SeasonInfo> loadAllSeasons() {

		Connection connection = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		SeasonInfo siDO = null;
		ArrayList<SeasonInfo> siList = new ArrayList<SeasonInfo>();
		try {
			connection = dbMgr.getConnection();
			ps = connection.prepareStatement(SELECT_FROM_SEASON_INFO
					+ " order by id desc");
			rs = ps.executeQuery();

			while (rs.next()) {
				siDO = new SeasonInfo();
				siDO.getDataFromResultSet(rs);
				siList.add(siDO);
			}

			logger.debug(DBConstants.QUERY_EXECUTION_SUCC + ps.toString());
		} catch (SQLException ex) {
			logger.error(DBConstants.QUERY_EXECUTION_FAIL + ps.toString(), ex);
		} finally {
			dbMgr.closeResources(connection, ps, rs);
		}
		return allSeasons = siList;
	}

	public void loadSeasonInfo() {
		try {
			loadAllSeasons();
			loadCurrentSeason();
		} catch (Exception ex) {
			logger.error("Error in getting season info", ex);
		}

	}

	@Override
	public SeasonInfo getCurrentSeason() {
		return currentSeason;

	}
	@Override
	public SeasonInfo loadCurrentSeason() {
		Connection connection = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		SeasonInfo siDO = null;

		try {
			connection = dbMgr.getConnection();
			ps = connection.prepareStatement(SELECT_FROM_SEASON_INFO
					+ " where active = true");

			rs = ps.executeQuery();
			if (rs.next()) {
				siDO = new SeasonInfo();
				siDO.getDataFromResultSet(rs);
			}

			logger.debug(DBConstants.QUERY_EXECUTION_SUCC + ps.toString());
		} catch (SQLException ex) {
			logger.error(DBConstants.QUERY_EXECUTION_FAIL + ps.toString(), ex);
		} finally {
			dbMgr.closeResources(connection, ps, rs);
		}
		return currentSeason = siDO;

	}
}
