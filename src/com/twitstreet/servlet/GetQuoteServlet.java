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

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.google.inject.Singleton;
import com.twitstreet.config.ConfigMgr;
import com.twitstreet.db.data.Stock;
import com.twitstreet.db.data.User;
import com.twitstreet.main.Twitstreet;
import com.twitstreet.market.PortfolioMgr;
import com.twitstreet.market.StockMgr;
import com.twitstreet.session.UserMgr;
import com.twitstreet.twitter.SimpleTwitterUser;
import com.twitstreet.twitter.TwitterProxy;
import com.twitstreet.twitter.TwitterProxyFactory;
import com.twitstreet.util.Util;

@SuppressWarnings("serial")
@Singleton
public class GetQuoteServlet extends TwitStreetServlet {
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

	public static final String STOCK = "stock";

	public static final String STOCK_ID = "stockId";
	public static final String STOCK_DETAIL_LIST = "stockDetailList";
	public static final String QUOTE = "quote";
	public static final String QUOTE_DISPLAY = "quotedisplay";

	public static final String OTHER_SEARCH_RESULTS = "other-search-results";

	public static final String RESULT = "result";
	public static final String REASON = "reason";

	public static final String USER_NOT_FOUND = "user-not-found";

	private static Logger logger = Logger.getLogger(GetQuoteServlet.class);

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

		request.setAttribute("title", "twitstreet - Twitter stock market game");
		request.setAttribute("meta-desc", "Twitstreet is a twitter stock market game. You buy / sell follower of twitter users in this game. If follower count increases you make profit. To make most money, try to find people who will be popular in near future. A new season begins first day of every month.");

		long start = 0;
		long end = 0;
		start = System.currentTimeMillis();

		if (!twitstreet.isInitialized()) {
			return;
		}

		end = System.currentTimeMillis();

		//logger.info("Init time: " + (end - start));

		loadUser(request);
		// loadUserFromCookie(request);

		start = System.currentTimeMillis();
		queryStockById(request, response);
		end = System.currentTimeMillis();
		logger.debug("queryStockById: " + (end - start));

		queryStockByQuote(request, response);
		end = System.currentTimeMillis();
		
		long seconds =  (end - start)/1000;
		
		if(seconds>2){
			logger.info("queryStockByQuote: " +(end - start)+" milliseconds");
			
		}else{

			logger.debug("queryStockByQuote: " +(end - start)+" milliseconds");
		}

		if (request.getAttribute(User.USER) != null) {
			getServletContext().getRequestDispatcher("/WEB-INF/jsp/dashboard.jsp").forward(request, response);
		} else {
			getServletContext().getRequestDispatcher("/WEB-INF/jsp/dashboard.jsp").forward(request, response);
		}
	}

	public void queryStockById(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String stockIdStr = request.getParameter(STOCK);
		User user = (User) request.getAttribute(User.USER);
		if (user != null && stockIdStr != null && stockIdStr.length() > 0) {
			long stockId = Long.parseLong(stockIdStr);

			Stock stock = stockMgr.getStockById(stockId);
			if (stock == null) {

				return;

			}

			TwitterProxy twitterProxy = user == null ? null : twitterProxyFactory.create(user.getOauthToken(), user.getOauthTokenSecret());

			ArrayList<SimpleTwitterUser> searchResultList = new ArrayList<SimpleTwitterUser>();

			searchResultList = twitterProxy.searchUsers(stock.getName());

			// TODO FIX HERE - low priority
			// Search the name of the stock through the all result list
			// do not assume that the first one is always the exact match.
			if (searchResultList != null && searchResultList.size() > 0 && stock.getName().equalsIgnoreCase(searchResultList.get(0).getScreenName())) {
				searchResultList.remove(0);
			}

			request.setAttribute(GetQuoteServlet.QUOTE_DISPLAY, stock.getName());
			request.setAttribute(STOCK, stock);

			request.setAttribute(STOCK_ID, new Long(stock.getId()));
			request.setAttribute(GetQuoteServlet.OTHER_SEARCH_RESULTS, searchResultList);
		}
	}

	public void queryStockByQuote(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		User userTmp = (User) request.getAttribute(User.USER) == null ? userMgr.random() : (User) request.getAttribute(User.USER);
		String twUserName = (String) request.getParameter(QUOTE);
		twUserName = new String(twUserName.getBytes("8859_1"), "UTF8");
		if (twUserName == null || twUserName.length() < 1) {

			logger.warn("Servlet: Empty username");
			return;

		}
		request.setAttribute(QUOTE, twUserName);

		TwitterProxy twitterProxy = null;
		Response resp = Response.create();

		try {
			twitterProxy = twitterProxyFactory.create(userTmp.getOauthToken(), userTmp.getOauthTokenSecret());

		} catch (Exception ex) {
			logger.error("Servlet: Twitter proxy could not be created. Username: " + twUserName);
			resp.fail().reason("Something wrong, we could not retrieved quote info. Working on it. ");
			return;
		}

		ArrayList<SimpleTwitterUser> searchResultList = new ArrayList<SimpleTwitterUser>();
		Stock stock = null;

		// Get user info from twitter.

		if (Util.isValidTwitterUserName(twUserName)) {
			stock = stockMgr.getStock(twUserName);
		}
		searchResultList = twitterProxy.searchUsers(twUserName);

		if (searchResultList != null && searchResultList.size() > 0) {
			if (stock == null) {

				stock = stockMgr.getStockById(searchResultList.get(0).getId());

				searchResultList.remove(0);

			} else if (stock.getName().equalsIgnoreCase(searchResultList.get(0).getScreenName())) {
				searchResultList.remove(0);
			}
		}

		request.setAttribute(OTHER_SEARCH_RESULTS, searchResultList);

		// Get user info from database
		request.setAttribute(STOCK, stock);

		request.setAttribute(HomePageServlet.SELECTED_TAB_STOCK_BAR, "stock-details-tab");
		if (stock != null) {
			logger.debug("Servlet: Stock queried successfully. Stock name:" + stock.getName());
		} else {
			logger.error("Servlet: User not found. Search string: " + twUserName);
			request.setAttribute(RESULT, USER_NOT_FOUND);
			request.setAttribute(REASON, twUserName + " is not found");

		}

	}

}
