/**
	TwitStreet - Twitter Stock Market Game
    Copyright (C) 2012  Engin Guller (bisanth@gmail.com), Cagdas Ozek (cagdasozek@gmail.com)

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

package com.twitstreet.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.twitstreet.main.Twitstreet;
import com.twitstreet.market.PortfolioMgr;
import com.twitstreet.season.SeasonInfo;
import com.twitstreet.season.SeasonMgr;

@SuppressWarnings("serial")
@Singleton
public class UserHistoryServlet extends TwitStreetServlet {

	@Inject PortfolioMgr portfolioMgr;
	@Inject Twitstreet twitstreet;
	@Inject SeasonMgr seasonMgr;
	
	public static String SELECTED_SEASON = "selectedSeason";
	public static String  SEASON_ID = "season";
	
	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		response.setContentType("text/html;charset=utf-8");
		response.setHeader("Cache-Control","no-cache"); //HTTP 1.1
		response.setHeader("Pragma","no-cache"); //HTTP 1.0
		response.setDateHeader ("Expires", 0); //prevents caching at the proxy server
		
		loadUser(request);
		
		SeasonInfo selectedSeason = null;
		try{
			String seasonId = request.getParameter(SEASON_ID);
			selectedSeason = seasonMgr.getSeasonInfo(Integer.valueOf(seasonId));
		}catch(Exception ex){
			return;
		}
		//loadUserFromCookie(request);
		try {
			request.setAttribute(SELECTED_SEASON, selectedSeason);
			getServletContext().getRequestDispatcher("/WEB-INF/jsp/userRankingHistory.jsp").forward(request, response);
		} catch (ServletException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
