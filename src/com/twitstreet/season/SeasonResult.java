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
