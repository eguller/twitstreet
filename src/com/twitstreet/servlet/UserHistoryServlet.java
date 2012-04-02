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
