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
import com.twitstreet.db.data.User;
import com.twitstreet.main.Twitstreet;
import com.twitstreet.market.PortfolioMgr;
import com.twitstreet.market.StockMgr;
import com.twitstreet.session.UserMgr;
import com.twitstreet.twitter.SimpleTwitterUser;
import com.twitstreet.twitter.TwitterProxy;
import com.twitstreet.twitter.TwitterProxyFactory;

@SuppressWarnings("serial")
@Singleton
public class GetUserServlet extends TwitStreetServlet {
	@Inject
	UserMgr userMgr;
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
	public static final String GET_USER_PARAM = "getuser";
	public static final String GET_USER = "getuser";
	public static final String GET_USER_TEXT = "getUserText";
	public static final String GET_USER_DISPLAY = "getUserDisplay";
	public static final String USER_NOT_FOUND = "user-not-found";
	public static final String GET_USER_OTHER_SEARCH_RESULTS = "getUserOtherSearchResults";

	ArrayList<User> searchResultUsers = new ArrayList<User>();

	private static Logger logger = Logger.getLogger(GetUserServlet.class);

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

		searchResultUsers = new ArrayList<User>();

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

		String searchText = (String) request.getParameter(GET_USER_PARAM);

		queryUserFromDB(searchText);
		queryUserFromTwitter(searchText, request);
		searchResultUsers.removeAll(Collections.singleton(null));

		end = System.currentTimeMillis();


		long seconds =  (end - start)/1000;
		
		if(seconds>2){
			logger.info("queryUser: " +(end - start)+" milliseconds");
			
		}else{

			logger.debug("queryUser: " +(end - start)+" milliseconds");
		}

		
		User user = null;
		if (searchResultUsers.size() > 0) {
			user = searchResultUsers.remove(0);
		}

		request.setAttribute(GET_USER, user);
		request.setAttribute(GET_USER_OTHER_SEARCH_RESULTS, searchResultUsers);

		request.setAttribute(HomePageServlet.SELECTED_TAB_USER_BAR, "user-details-tab");
		
		if (user != null) {
			request.setAttribute(GetUserServlet.GET_USER_DISPLAY, user.getUserName());
		}

		request.setAttribute(GET_USER_TEXT, searchText);

		if (request.getAttribute(User.USER) != null) {
			getServletContext().getRequestDispatcher("/WEB-INF/jsp/userProfile.jsp").forward(request, response);
		} else {
			getServletContext().getRequestDispatcher("/WEB-INF/jsp/userProfile.jsp").forward(request, response);
		}
	}

	public void queryUserFromDB(String searchText) throws ServletException, IOException {

		searchResultUsers.addAll(userMgr.searchUser(searchText));
	}

	public void queryUserFromTwitter(String searchText, HttpServletRequest request) throws ServletException, IOException {
		User userTmp = request.getAttribute(User.USER) == null ? userMgr.random() : (User) request.getAttribute(User.USER);
		if (searchText != null && searchText.length() > 0) {
			// request.setAttribute(GET_USER, twUserName);
			TwitterProxy twitterProxy = null;

			twitterProxy = userTmp == null ? null : twitterProxyFactory.create(userTmp.getOauthToken(), userTmp.getOauthTokenSecret());

			User twUser = null;
			ArrayList<SimpleTwitterUser> searchResultList = new ArrayList<SimpleTwitterUser>();

			twitter4j.User twitterUser = twitterProxy.getTwUser(searchText);
			if (twitterUser != null) {
				twUser = userMgr.getUserById(twitterUser.getId());

				searchResultUsers.remove(twUser);
				searchResultUsers.add(twUser);
			}

			searchResultList = twitterProxy.searchUsers(searchText);

			if (searchResultList != null && searchResultList.size() > 0) {

				ArrayList<Long> idList = new ArrayList<Long>();

				for (SimpleTwitterUser stu : searchResultList) {

					idList.add(stu.getId());
				}

				ArrayList<User> tempList = userMgr.getUsersByIdList(idList);

				searchResultUsers.removeAll(tempList);
				searchResultUsers.addAll(tempList);
			}

		}
	}
}
