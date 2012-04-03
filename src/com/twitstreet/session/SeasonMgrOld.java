package com.twitstreet.session;

import java.util.ArrayList;

import com.twitstreet.season.SeasonInfo;

public interface SeasonMgrOld {

	public int bufferToStart = 60 * 1000;
	public void newSeason();
	public static int INITIAL_CASH = 1000;
    /**
     * Return user by given twitter id
     * @param userId - User id given by twitter.
     * @return - user details.
     */
	public ArrayList<SeasonInfo> getAllSeasons();
	public SeasonInfo getSeasonInfo(int id);
	public SeasonInfo getCurrentSeason();
	public ArrayList<SeasonInfo> loadAllSeasons();
	public SeasonInfo loadCurrentSeason();
	public void loadSeasonInfo();

	SeasonInfo setSeasonInfo(SeasonInfo si);
}
