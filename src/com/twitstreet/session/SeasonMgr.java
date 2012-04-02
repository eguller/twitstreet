package com.twitstreet.session;

import java.util.ArrayList;

import com.twitstreet.main.SeasonInfo;

public interface SeasonMgr {

	public int bufferToStart = 60 * 1000;
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
	public void newSeason();
	SeasonInfo setSeasonInfo(SeasonInfo si);
}
