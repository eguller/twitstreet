package com.twitstreet.db.data;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class RankingHistoryData implements DataObjectIF{


	private long id;
	
	private ArrayList<RankingData> rankingHistory = new ArrayList<RankingData>();
	
	@Override
	public void getDataFromResultSet(ResultSet rs) throws SQLException {
		
		while (rs.next()) {
			setId(rs.getLong("user_id"));
			RankingData rd = new RankingData();
			rd.getDataFromResultSet(rs);
			getRankingHistory().add(rd);
		}		
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public ArrayList<RankingData> getRankingHistory() {
		return rankingHistory;
	}

	public void setRankingHistory(ArrayList<RankingData> rankingHistory) {
		this.rankingHistory = rankingHistory;
	}




}
