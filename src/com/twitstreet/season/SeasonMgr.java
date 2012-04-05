package com.twitstreet.season;

import java.util.ArrayList;

public interface SeasonMgr {

	public int bufferToStart = 10 * 1000;
	public void newSeason();
	
	
	public ArrayList<SeasonInfo> getAllSeasons();
	public SeasonInfo getSeasonInfo(int id);
	public SeasonInfo getCurrentSeason();
	public void loadSeasonInfo();


	SeasonInfo setSeasonInfo(SeasonInfo si);
	
	public SeasonResult getSeasonResult(int seasonId);


	SeasonResult getSeasonResult(int seasonId, int start, int stop);
}
