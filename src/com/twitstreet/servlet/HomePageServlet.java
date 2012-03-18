package com.twitstreet.servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import twitter4j.ResponseList;
import twitter4j.Trends;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;

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

	public static final String STOCK = "stock";

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

	private static Logger logger = Logger.getLogger(HomePageServlet.class);
	
	public void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

	@Override
	public void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html;charset=utf-8");
		response.setHeader("Cache-Control","no-cache"); //HTTP 1.1
		response.setHeader("Pragma","no-cache"); //HTTP 1.0
		response.setDateHeader ("Expires", 0); //prevents caching at the proxy server
		
		request.setAttribute("title", "TwitStreet - Twitter Stock Market Game");
		request.setAttribute(
				"meta-desc",
				"Twitstreet is a twitter stock market game. You buy / sell follower of twitter users in this game. If follower count increases you make profit. To make most money, try to find people who will be popular in near future. A new season begins first day of every month.");

		//Keep this until google removes broken links from its index
		String queryString = request.getQueryString();
		if(queryString!= null && queryString.startsWith("stock")){
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
			return;
		}
		//Keep this until google removes broken links from its index
		
		String command = request.getParameter("_escaped_fragment_");
		
		try{
			if(command.startsWith("suggestedstocks")){

				request.setAttribute(SUGGESTED_STOCKS, "true");
			}
			else if(command.startsWith("topgrossingusers")){

				request.setAttribute(TOP_GROSSING_USERS, "true");
			}
			else if(command.startsWith("stock=")){
				long stockId =Long.valueOf(command.split("stock=")[1]);
				Stock stock = stockMgr.getStockById(stockId);
				request.setAttribute(STOCK, stock);
			}
			else if(command.startsWith("user=")){
				long stockId =Long.valueOf(command.split("user=")[1]);
				User user = userMgr.getUserById(stockId);
				request.setAttribute(GetUserServlet.GET_USER, user);
			}
		
			else {
				
				//Default view for twitstreet.com is Suggested Stocks
				request.setAttribute(SUGGESTED_STOCKS, "true");

			}
			
			
		}catch(Exception ex){
			
			
			
		}
		
		long start = 0;
		long end = 0;
		start = System.currentTimeMillis();
		
		if (!twitstreet.isInitialized()) {
			getServletContext().getRequestDispatcher("/WEB-INF/jsp/setup.jsp")
					.forward(request, response);
			return;
		}
		
		end = System.currentTimeMillis();
		
		logger.info("Init time: " + (end - start));
		

		loadUser(request);

		
		
		if ( request.getSession().getAttribute(User.USER_ID) != null ) {
			getServletContext().getRequestDispatcher(
					"/WEB-INF/jsp/homeAuth.jsp").forward(request, response);
		} else {
			String referenceId = request.getParameter(REF);
			if(referenceId != null){
				request.getSession().setAttribute(REFERENCE_ID, referenceId);
			}
			getServletContext().getRequestDispatcher(
					"/WEB-INF/jsp/homeUnAuth.jsp").forward(request, response);
		}
	}
}
