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


package com.twitstreet.season;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import org.apache.log4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.twitstreet.config.ConfigMgr;
import com.twitstreet.db.base.DBConstants;
import com.twitstreet.db.base.DBMgr;
import com.twitstreet.db.data.RankingData;

@Singleton
public class SeasonMgrImpl implements SeasonMgr {
	private static Logger logger = Logger.getLogger(SeasonMgrImpl.class);
	private static String INSERT_UPDATE_SEASON_INFO = "insert into season_info (id, startTime, endTime, active, updateInProgress)  values (?,?,?,?,?) on duplicate key update startTime=?, endTime=?, active=?, updateInProgress=? ";
	private static String SELECT_FROM_SEASON_INFO = " select id, startTime, endTime, active, updateInProgress from season_info ";

	private static String GET_SEASON_RESULT = " select rh.* from ranking_history rh inner join season_result sr on " + " 	rh.id = sr.ranking_history_id where rh.season_id = ? " + " order by rh.rank asc ";

	private HashMap<Integer, SeasonResult> seasonResults = new HashMap<Integer, SeasonResult>();

	@Inject
	DBMgr dbMgr;

	private ArrayList<SeasonInfo> allSeasons = new ArrayList<SeasonInfo>();

	@Inject
	ConfigMgr configMgr;
	private SeasonInfo currentSeason;

	@Override
	public SeasonInfo getSeasonInfo(int id) {
		Connection connection = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		SeasonInfo siDO = null;

		try {
			connection = dbMgr.getConnection();
			ps = connection.prepareStatement(SELECT_FROM_SEASON_INFO + " where id = ?");
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
		if (allSeasons.size() < 1) {
			loadAllSeasons();
		}
		return allSeasons;
	}

	private ArrayList<SeasonInfo> loadAllSeasons() {

		Connection connection = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		SeasonInfo siDO = null;
		ArrayList<SeasonInfo> siList = new ArrayList<SeasonInfo>();
		try {
			connection = dbMgr.getConnection();
			ps = connection.prepareStatement(SELECT_FROM_SEASON_INFO + " order by id desc");
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
	
			getSeasonResult(getCurrentSeason().getId());
		
		
		} catch (Exception ex) {
			logger.error("Error in getting season info", ex);
		}

	}

	@Override
	public SeasonInfo getCurrentSeason() {
		if (currentSeason.getId() < 1) {
			loadCurrentSeason();
		}
		return currentSeason;

	}

	public void setCurrentSeason(SeasonInfo currentSeason) {
		this.currentSeason = currentSeason;
	}

	private SeasonInfo loadCurrentSeason() {
		Connection connection = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		SeasonInfo siDO = null;

		try {
			connection = dbMgr.getConnection();
			ps = connection.prepareStatement(SELECT_FROM_SEASON_INFO + " where active = true");

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

	@Override
	public SeasonInfo setSeasonInfo(SeasonInfo si) {
		Connection connection = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		SeasonInfo siDO = null;

		try {
			connection = dbMgr.getConnection();
			ps = connection.prepareStatement(INSERT_UPDATE_SEASON_INFO);
			ps.setInt(1, si.getId());
			ps.setTimestamp(2, si.getStartTime());
			ps.setTimestamp(3, si.getEndTime());
			ps.setBoolean(4, si.isActive());
			ps.setBoolean(5, si.isUpdateInProgress());
			ps.setTimestamp(6, si.getStartTime());
			ps.setTimestamp(7, si.getEndTime());
			ps.setBoolean(8, si.isActive());
			ps.setBoolean(9, si.isUpdateInProgress());
			ps.executeUpdate();

			logger.debug(DBConstants.QUERY_EXECUTION_SUCC + ps.toString());
		} catch (SQLException ex) {
			logger.error(DBConstants.QUERY_EXECUTION_FAIL + ps.toString(), ex);
		} finally {
			dbMgr.closeResources(connection, ps, rs);
		}
		return siDO;
	}

	@Override
	public void newSeason() {
		Connection connection = null;
		CallableStatement cs = null;

		loadSeasonInfo();
		if (getCurrentSeason().isUpdateInProgress()) {
			return;
		}
		
		SeasonInfo current = getCurrentSeason();
		current.setUpdateInProgress(true);
		setSeasonInfo(current);
		
		Date nowDate = new Date();
		
		long now = nowDate.getTime();
		long endTime = getCurrentSeason().getEndTime().getTime();
		
		try {
			connection = dbMgr.getConnection();
			cs = connection.prepareCall("{call new_season(?)}");
			cs.setDouble(1, configMgr.getInitialMoney());
			cs.execute();
			logger.debug(DBConstants.QUERY_EXECUTION_SUCC + cs.toString());
		} catch (SQLException ex) {
			logger.error(DBConstants.QUERY_EXECUTION_FAIL + cs.toString(), ex);
		} finally {
			dbMgr.closeResources(connection, cs, null);
		}

		current.setActive(false);
		current.setUpdateInProgress(false);
		setSeasonInfo(current);

		loadSeasonInfo();

	}

	@Override
	public SeasonResult getSeasonResult(int seasonId) {

		return getSeasonResult(seasonId, 0, 3);
	}

	@Override
	public SeasonResult getSeasonResult(int seasonId, int offset, int count) {
		
		SeasonResult seasonResult = new SeasonResult();
		try {
			if (getSeasonResults().containsKey(seasonId)) {
				seasonResult = getSeasonResults().get(seasonId);
				seasonResult.setRankingHistory(new ArrayList<RankingData>(seasonResult.getRankingHistory().subList(offset, offset + count)));

			}else{
				loadSeasonResults();
				if (getSeasonResults().containsKey(seasonId)) {
				    seasonResult = getSeasonResults().get(seasonId);
					seasonResult.setRankingHistory(new ArrayList<RankingData>(seasonResult.getRankingHistory().subList(offset, offset + count)));

				}
			}
				
		} catch (Exception ex) {

		}

		return seasonResult;
		

	}

	private void loadSeasonResults() {

		ArrayList<SeasonInfo> allSeasons = getAllSeasons();
		Connection connection = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			connection = dbMgr.getConnection();
			for (SeasonInfo si : allSeasons) {
				if (si.isActive()) {
					continue;
				}
				ps = connection.prepareStatement(GET_SEASON_RESULT);
				ps.setInt(1, si.getId());
			
				rs = ps.executeQuery();

				SeasonResult seasonResult = new SeasonResult();
				seasonResult.getDataFromResultSet(rs);
				if (seasonResult.getSeasonId() > 0) {
					seasonResults.put(seasonResult.getSeasonId(), seasonResult);
				}
			}
			logger.debug(DBConstants.QUERY_EXECUTION_SUCC + ps.toString());
		} catch (SQLException ex) {
			logger.error(DBConstants.QUERY_EXECUTION_FAIL + ps.toString(), ex);
		} finally {
			dbMgr.closeResources(connection, ps, rs);
		}

	}

	public  HashMap<Integer, SeasonResult> getSeasonResults() {
		if(seasonResults.size()<1){
			loadSeasonResults();
		}
		return seasonResults;
	}

}
