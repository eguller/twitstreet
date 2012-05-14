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

import java.util.ArrayList;

public interface SeasonMgr {

	public int INITIAL_SEASON_ID = 4;
	public void newSeason();
	
	
	public ArrayList<SeasonInfo> getAllSeasons();
	public SeasonInfo getSeasonInfo(int id);
	public SeasonInfo getCurrentSeason();
	public void loadSeasonInfo();


	SeasonInfo setSeasonInfo(SeasonInfo si);
	
	public SeasonResult getSeasonResult(int seasonId);


	SeasonResult getSeasonResult(int seasonId, int start, int stop);
}
