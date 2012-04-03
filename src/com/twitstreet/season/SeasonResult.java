package com.twitstreet.season;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.twitstreet.db.data.DataObjectIF;
import com.twitstreet.db.data.RankingData;

public class SeasonResult  implements DataObjectIF{

	private int seasonId = -1;

	private ArrayList<RankingData> rankingHistory = new ArrayList<RankingData>();

	public int getSeasonId() {
		return seasonId;
	}

	public void setSeasonId(int seasonId) {
		this.seasonId = seasonId;
	}

	public ArrayList<RankingData> getRankingHistory() {
		return rankingHistory;
	}

	public void setRankingHistory(ArrayList<RankingData> rankingHistory) {
		this.rankingHistory = rankingHistory;
	}

	@Override
	public void getDataFromResultSet(ResultSet rs) throws SQLException {
		
		while (rs.next()) {
			setSeasonId(rs.getInt("season_id"));
			RankingData rd = new RankingData();
			rd.getDataFromResultSet(rs);
			getRankingHistory().add(rd);
		}		
	}
}
