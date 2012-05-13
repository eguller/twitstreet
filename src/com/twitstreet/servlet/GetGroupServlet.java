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
import java.util.ArrayList;
import java.util.Collections;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.google.inject.Singleton;
import com.twitstreet.config.ConfigMgr;
import com.twitstreet.db.data.Group;
import com.twitstreet.main.Twitstreet;
import com.twitstreet.market.PortfolioMgr;
import com.twitstreet.market.StockMgr;
import com.twitstreet.session.GroupMgr;
import com.twitstreet.twitter.TwitterProxyFactory;

@SuppressWarnings("serial")
@Singleton
public class GetGroupServlet extends TwitStreetServlet {
	@Inject
	GroupMgr groupMgr;
	@Inject
	Twitstreet twitstreet;
	@Inject
	ConfigMgr configMgr;
	@Inject
	StockMgr stockMgr;
	@Inject
	TwitterProxyFactory twitterProxyFactory = null;
	@Inject
	PortfolioMgr portfolioMgr = null;

	public static final String RESULT = "result";
	public static final String REASON = "reason";
	public static final String GET_GROUP_PARAM = "getgroup";
	public static final String GET_GROUP = "getgroup";
	public static final String GET_GROUP_TEXT = "getUserText";
	public static final String GET_GROUP_DISPLAY = "getUserDisplay";
	public static final String GROUP_NOT_FOUND = "user-not-found";
	public static final String GET_GROUP_OTHER_SEARCH_RESULTS = "getUserOtherSearchResults";

	ArrayList<Group> searchResultGroups = new ArrayList<Group>();

	private static Logger logger = Logger.getLogger(GetGroupServlet.class);

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html;charset=utf-8");
		response.setHeader("Cache-Control", "no-cache"); // HTTP 1.1
		response.setHeader("Pragma", "no-cache"); // HTTP 1.0
		response.setDateHeader("Expires", 0); // prevents caching at the proxy
												// server

		searchResultGroups = new ArrayList<Group>();

		request.setAttribute("title", "twitstreet - Twitter stock market game");
		request.setAttribute("meta-desc", "Twitstreet is a twitter stock market game. You buy / sell follower of twitter users in this game. If follower count increases you make profit. To make most money, try to find people who will be popular in near future. A new season begins first day of every month.");

		long start = 0;
		long end = 0;

		start = System.currentTimeMillis();
		if (!twitstreet.isInitialized()) {
			return;
		}
		
	
		loadUser(request);
		// loadUserFromCookie(request);

		String searchText = (String) request.getParameter(GET_GROUP_PARAM);

		queryGroupFromDB(searchText);
		searchResultGroups.removeAll(Collections.singleton(null));

		end = System.currentTimeMillis();


		long seconds =  (end - start)/1000;
		
		if(seconds>2){
			logger.info("queryGroup: " +(end - start)+" milliseconds");
			
		}else{

			logger.debug("queryGroup: " +(end - start)+" milliseconds");
		}

		
		Group group = null;
		if (searchResultGroups.size() > 0) {
			group = searchResultGroups.remove(0);
		}

		request.setAttribute(GET_GROUP, group.getName());
		request.setAttribute(HomePageServlet.GROUP, group);
		request.setAttribute(GET_GROUP_OTHER_SEARCH_RESULTS, searchResultGroups);

		request.setAttribute(HomePageServlet.SELECTED_TAB_GROUP_BAR, "group-details-tab");
		
		if (group != null) {
			request.setAttribute(GetGroupServlet.GET_GROUP_DISPLAY, group.getName());
		}

		request.setAttribute(GET_GROUP_TEXT, searchText);

	
		getServletContext().getRequestDispatcher("/WEB-INF/jsp/groupsContent.jsp").forward(request, response);
	
	}

	public void queryGroupFromDB(String searchText) throws ServletException, IOException {

		searchResultGroups.addAll(groupMgr.searchGroup(searchText));
	}

}
