package com.twitstreet.season;

import java.util.ArrayList;

public interface SeasonMgr {
	public ArrayList<SeasonInfo> getAllSeasons();
	public SeasonInfo getSeasonInfo(int id);
	public SeasonInfo getCurrentSeason();
	public ArrayList<SeasonInfo> loadAllSeasons();
	public SeasonInfo loadCurrentSeason();
	public void loadSeasonInfo();
}
