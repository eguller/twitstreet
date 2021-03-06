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

package com.twitstreet.servlet;

import java.io.IOException;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.google.inject.Singleton;
import com.twitstreet.config.ConfigMgr;
import com.twitstreet.db.data.Group;
import com.twitstreet.db.data.Stock;
import com.twitstreet.db.data.User;
import com.twitstreet.localization.LocalizationUtil;
import com.twitstreet.main.Twitstreet;
import com.twitstreet.market.PortfolioMgr;
import com.twitstreet.market.StockMgr;
import com.twitstreet.session.GroupMgr;
import com.twitstreet.session.UserMgr;
import com.twitstreet.twitter.TwitterProxyFactory;

@SuppressWarnings("serial")
@Singleton
public class HomePageServlet extends TwitStreetServlet {
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
	@Inject
	GroupMgr groupMgr = null;

	public static final String STOCK = "stock";
	public static final String GROUP = "group";

	public static final String SELECTED_TAB_STOCK_BAR = "selectedTabStockBar";
	public static final String SELECTED_TAB_USER_BAR = "selectedTabUserBar";
	public static final String SELECTED_TAB_GROUP_BAR = "selectedTabGroupBar";
	public static final String TOP_GROSSING_USERS = "topgrossingusers";
	public static final String SUGGESTED_STOCKS = "suggestedstocks";
	public static final String STOCK_ID = "stockId";
	public static final String STOCK_DETAIL_LIST = "stockDetailList";
	public static final String QUOTE = "quote";
	public static final String QUOTE_DISPLAY = "quotedisplay";
	public static final String REFERENCE_ID = "reference-id";
	public static final String REF = "ref";
	
	public static final String OTHER_SEARCH_RESULTS = "other-search-results";

	
	public static final String RESULT = "result";
	public static final String REASON = "reason";

	public static final String USER_NOT_FOUND = "user-not-found";
	
	public static final String TITLE = "title";
	
	public static String PAGE = "page";

	private static Logger logger = Logger.getLogger(HomePageServlet.class);
	LocalizationUtil lutil = LocalizationUtil.getInstance();
	
	String lang = "";
	
	public void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

	
	@Override
	public void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		
		
		lang = (String)request.getSession().getAttribute(LocalizationUtil.LANGUAGE);
		if(lang == null){
			lang = LocalizationUtil.DEFAULT_LANGUAGE;
		}
		
		response.setContentType("text/html;charset=utf-8");
		response.setHeader("Cache-Control","no-cache"); //HTTP 1.1
		response.setHeader("Pragma","no-cache"); //HTTP 1.0
		response.setDateHeader ("Expires", 0); //prevents caching at the proxy server
		
		request.setAttribute("title", lutil.get("title", lang));
		request.setAttribute(
				"meta-desc",
				"Twitstreet is a twitter stock market game. You buy / sell follower of twitter users in this game. If follower count increases you make profit. To make most money, try to find people who will be popular in near future. A new season begins first day of every month.");

		long start = 0;
		long end = 0;
		start = System.currentTimeMillis();
		
		if (!twitstreet.isInitialized()) {
			return;
		}
		//Keep this until google removes broken links from its index
		String queryString = request.getQueryString();
		if(queryString!= null && queryString.startsWith("stock")){
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
			return;
		}
		
		
		

		
		setDefaultTabs(request);
		String command = request.getParameter("_escaped_fragment_");
		if(command != null){
			handleGoogleCrawler(request, command);

		}
		
		loadUser(request);
		
		end = System.currentTimeMillis();
		logger.debug("Servlet Init Time: " + (end - start));
		
		if ( request.getSession().getAttribute(User.USER_ID) != null ) {
			
			
			
			if(!dispatchIfMobile(request,response, "/WEB-INF/jsp/mobile/homeAuth.jsp")){
				getServletContext().getRequestDispatcher(
						"/WEB-INF/jsp/homeAuth.jsp").forward(request, response);
				
			}
			
		} else {
			String referenceId = request.getParameter(REF);
			if(referenceId != null){
				request.getSession().setAttribute(REFERENCE_ID, referenceId);
			}
			if(!dispatchIfMobile(request,response, "/WEB-INF/jsp/mobile/homeUnAuth.jsp")){
				
				getServletContext().getRequestDispatcher("/WEB-INF/jsp/homeUnAuth.jsp").forward(request, response);
			}
		}
	}

	private void setDefaultTabs(HttpServletRequest request) {
		request.setAttribute(SELECTED_TAB_STOCK_BAR, "suggested-stocks-tab");
		request.setAttribute(SELECTED_TAB_GROUP_BAR, "group-list-tab");
		request.setAttribute(SELECTED_TAB_USER_BAR, "top-grossing-users-tab");
	}
	
	private void handleGoogleCrawler(HttpServletRequest request, String command){
		try{
			if(command.startsWith("suggestedstocks")){
				request.setAttribute(SELECTED_TAB_STOCK_BAR, "suggested-stocks-tab");
				request.setAttribute(TITLE, lutil.get("suggestedstocks.title", lang));
			}	
			else if(command.startsWith("topgrossingstocks")){
				request.setAttribute(SELECTED_TAB_STOCK_BAR, "top-grossing-stocks-tab");
				request.setAttribute(TITLE, lutil.get("topgrossingstocks.title", lang));
			}
			else if(command.startsWith("stock=")){
				long stockId =Long.valueOf(command.split("stock=")[1]);
				Stock stock = stockMgr.getStockById(stockId);
				request.setAttribute(STOCK, stock);
				request.setAttribute(TITLE, lutil.get("stock.bar.profile", lang, stock.getName()));
			}
			else if(command.startsWith("topgrossingusers")){
				request.setAttribute(SELECTED_TAB_USER_BAR, "top-grossing-users-tab");
				request.setAttribute(TITLE, lutil.get("topgrossingusers.title", lang));
			}
			else if(command.startsWith("newusers")){
				request.setAttribute(SELECTED_TAB_USER_BAR, "new-users-tab");
				request.setAttribute(TITLE, lutil.get("newusers.title", lang));
			}
			else if(command.startsWith("user=")){
				long stockId =Long.valueOf(command.split("user=")[1]);
				User user = userMgr.getUserById(stockId);
				request.setAttribute(GetUserServlet.GET_USER, user);
				request.setAttribute(TITLE, lutil.get("user.bar.profile", lang, user.getUserName()));
			}
			else if(command.startsWith("grouplist")){
				request.setAttribute(SELECTED_TAB_GROUP_BAR, "group-list-tab");
				request.setAttribute(TITLE, lutil.get("groups.list.title", lang));
			}
			else if(command.startsWith("mygroups")){
				request.setAttribute(SELECTED_TAB_GROUP_BAR, "my-groups-tab");
				request.setAttribute(TITLE, lutil.get("groups.list.title", lang));
			}
			else if(command.startsWith("seasonresults")){
				request.setAttribute(SELECTED_TAB_GROUP_BAR, "oldseasons-tab");
				request.setAttribute(TITLE, lutil.get("groups.list.title", lang));
			}
			else if(command.startsWith("group=")){
				long groupId =Long.valueOf(command.split("group=")[1]);
				Group group = groupMgr.getGroup(groupId);
				request.setAttribute(GroupServlet.GROUP, group);
				request.setAttribute(TITLE, lutil.get("user.bar.profile", lang, group.getName()));
			}
		}catch(Exception ex){
			logger.debug("Error while handling google crawler", ex);
		}
	}
}
