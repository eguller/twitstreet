package com.twitstreet.servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import twitter4j.TwitterException;

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
		super.doGet(request, response);
		
		
		 searchResultUsers = new ArrayList<User>();
		 
		setPageAttributes();
		response.setContentType("text/html");
		request.setAttribute("title", "twitstreet - Twitter stock market game");
		request.setAttribute("meta-desc", "Twitstreet is a twitter stock market game. You buy / sell follower of twitter users in this game. If follower count increases you make profit. To make most money, try to find people who will be popular in near future. A new season begins first day of every month.");

		long start = 0;
		long end = 0;
		
		start = System.currentTimeMillis();
		if (!twitstreet.isInitialized()) {
			getServletContext().getRequestDispatcher("/WEB-INF/jsp/setup.jsp").forward(request, response);
			return;
		}
		end = System.currentTimeMillis();

		logger.info("Init time: " + (end - start));

		start = System.currentTimeMillis();	
		
		String searchText = (String) request.getParameter(GET_USER_PARAM);
		
		
		
		queryUserFromDB(searchText);
		queryUserFromTwitter(searchText);	
		searchResultUsers.removeAll(Collections.singleton(null));
		
		end = System.currentTimeMillis();
		logger.info("queryStockByQuote: " + (end - start));

		
		
		User user = searchResultUsers.remove(0);
		
		
		
		
		request.setAttribute(GET_USER, user);
		request.setAttribute(GET_USER_OTHER_SEARCH_RESULTS, searchResultUsers);
		
		if(user!=null){
			request.setAttribute(GetUserServlet.GET_USER_DISPLAY,user.getUserName());						
		}

		request.setAttribute(GET_USER_TEXT, searchText);
		
		
		
		if (getUser() != null) {
			getServletContext().getRequestDispatcher("/WEB-INF/jsp/userProfile.jsp").forward(request, response);
		} else {
			getServletContext().getRequestDispatcher("/WEB-INF/jsp/userProfile.jsp").forward(request, response);
		}
	}
	public void queryUserFromDB(String searchText) throws ServletException, IOException {
		
		
		searchResultUsers.addAll(userMgr.searchUser(searchText));
	}
	public void queryUserFromTwitter(String searchText) throws ServletException, IOException {
		User userTmp = getUser();
		
			
		if (searchText != null && searchText.length() > 0) {
			//request.setAttribute(GET_USER, twUserName);
			TwitterProxy twitterProxy = null;
			Response resp = Response.create();
			if (userTmp == null) {
				// uses someone else account to get quote for unauthenticated
				// users.
				userTmp = userMgr.random();
			}

			twitterProxy = userTmp == null ? null : twitterProxyFactory.create(userTmp.getOauthToken(), userTmp.getOauthTokenSecret());

			User twUser = null;
			ArrayList<SimpleTwitterUser> searchResultList = new ArrayList<SimpleTwitterUser>();

			if (twitterProxy != null) {
				// Get user info from twitter.
				try {
					try {
						twitter4j.User twitterUser = twitterProxy.getTwUser(searchText);
						if (twitterUser != null) {
							twUser = userMgr.getUserById(twitterUser.getId());
							
							searchResultUsers.remove(twUser);
							searchResultUsers.add(twUser);
						}
					} catch (TwitterException ex) {
						
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

				
					

				} catch (TwitterException e1) {
					resp.fail().reason("Something wrong, we could not connect to Twitter. Working on it.");
					return;
				}

			} else {
				logger.error("Servlet: Twitter proxy could not be created. Username: " + searchText);
				resp.fail().reason("Something wrong, we could not retrieved quote info. Working on it. ");
			}
		}
	}
}
