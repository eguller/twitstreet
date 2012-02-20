package com.twitstreet.servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
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

	public static final String STOCK_ID = "stockId";
	public static final String STOCK_DETAIL_LIST = "stockDetailList";
	public static final String QUOTE = "quote";
	public static final String QUOTE_DISPLAY = "quotedisplay";
	
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
		super.doGet(request, response);
		request.setAttribute("title", "twitstreet - Twitter stock market game");
		request.setAttribute(
				"meta-desc",
				"Twitstreet is a twitter stock market game. You buy / sell follower of twitter users in this game. If follower count increases you make profit. To make most money, try to find people who will be popular in near future. A new season begins first day of every month.");

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
		
		
//		Twitter twitter = new TwitterFactory().getInstance();
//		try {
//			ResponseList<Trends> ts = twitter.getDailyTrends(new Date(), true);
//			String name =ts.get(0).getTrends()[0].getName();
//			
//			queryStockByQuote(request, response);
//		} catch (TwitterException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		
		
		if (request.getParameter("signout") != null) {
			request.getSession(false).invalidate();
			request.removeAttribute(User.USER);
			invalidateCookies(new String[] { CallBackServlet.COOKIE_ID,
					CallBackServlet.COOKIE_OAUTHTOKEN }, request, response);
			getServletContext().getRequestDispatcher(
					"/WEB-INF/jsp/homeUnAuth.jsp").forward(request, response);
			return;
		}


		start = System.currentTimeMillis();
		queryStockById(request, response);
		end = System.currentTimeMillis();
		logger.info("queryStockById: " + (end - start));
		
		start = System.currentTimeMillis();
		queryStockByQuote(request, response);
		end = System.currentTimeMillis();
		logger.info("queryStockByQuote: " + (end - start));

		if (getUser() != null) {
			getServletContext().getRequestDispatcher(
					"/WEB-INF/jsp/homeAuth.jsp").forward(request, response);
		} else {
			getServletContext().getRequestDispatcher(
					"/WEB-INF/jsp/homeUnAuth.jsp").forward(request, response);
		}
	}

	public void queryStockById(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String stockIdStr = request.getParameter(STOCK);

		if (user != null && stockIdStr != null && stockIdStr.length() > 0) {
			long stockId = Long.parseLong(stockIdStr);
			TwitterProxy twitterProxy = user == null ? null
					: twitterProxyFactory.create(user.getOauthToken(),
							user.getOauthTokenSecret());
			Stock stock = stockMgr.getStockById(stockId);
			if (stock == null) {
				try {
					twitter4j.User twUser = twitterProxy.getTwUser(stockId);
					stock = new Stock();
					stock.setId(twUser.getId());
					stock.setName(twUser.getScreenName());
					stock.setTotal(twUser.getFollowersCount());
					stock.setPictureUrl(twUser.getProfileImageURL()
							.toExternalForm());
					stock.setSold(0.0D);
					stock.setVerified(twUser.isVerified());
					stockMgr.saveStock(stock);
				} catch (TwitterException e) {
					logger.error("Servlet: Twitter exception occured", e);
				}
			}

			ArrayList<SimpleTwitterUser> searchResultList = new ArrayList<SimpleTwitterUser>();
			try {
				searchResultList = twitterProxy.searchUsers(stock.getName());
			} catch (TwitterException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			request.setAttribute(HomePageServlet.QUOTE_DISPLAY,
					stock.getName());
			request.setAttribute(STOCK, stock);

			request.setAttribute(STOCK_ID,new Long(stock.getId()));
			request.setAttribute(HomePageServlet.OTHER_SEARCH_RESULTS,
					searchResultList);
		}
	}

	public void queryStockByQuote(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String twUserName = (String) request.getParameter(QUOTE);
		if (twUserName != null && twUserName.length() > 0) {
			User userTmp = getUser() == null ? userMgr.random() : getUser();

			request.setAttribute(QUOTE, twUserName);
			TwitterProxy twitterProxy = null;
			Response resp = Response.create();

			twitterProxy = twitterProxyFactory.create(
					userTmp.getOauthToken(), userTmp.getOauthTokenSecret());

			SimpleTwitterUser twUser = null;
			ArrayList<SimpleTwitterUser> searchResultList = new ArrayList<SimpleTwitterUser>();

			if (twitterProxy != null) {
				// Get user info from twitter.
				try {
					try {
						twitter4j.User twitterUser = twitterProxy.getTwUser(twUserName);
						if(twitterUser != null){
							twUser = new SimpleTwitterUser(twitterUser);
						}
					} catch (TwitterException ex) {
						// omit exception, maybe this is search user not get
						// user
					}
					searchResultList = twitterProxy.searchUsers(twUserName);
					if (twUser == null) {
						if (searchResultList != null
								&& searchResultList.size() > 0) {
							twUser = searchResultList.get(0);
							searchResultList.remove(0);
						}
					}
					else if(searchResultList.size() > 0 &&  twUser.getScreenName().equalsIgnoreCase(searchResultList.get(0).getScreenName())){
						
						searchResultList.remove(0);
						
					}

					request.setAttribute(OTHER_SEARCH_RESULTS, searchResultList);

				} catch (TwitterException e1) {
					resp.fail()
							.reason("Something wrong, we could not connected to Twitter. Working on it.");
					return;
				}

				// Get user info from database
				Stock stock = null;
				if (twUser != null) {
					stock = stockMgr.getStockById(twUser.getId());

					// User info retrieved both from twitter and database.
					if (stock == null) {
						// This user was not queried before.
						logger.debug("Servlet: Stock queried first time. Stock name: "
								+ twUserName);
						stock = new Stock();
						stock.setId(twUser.getId());
						stock.setName(twUser.getScreenName());
						stock.setTotal(twUser.getFollowerCount());
						stock.setPictureUrl(twUser.getPictureUrl());
						stock.setSold(0.0D);
						stock.setVerified(twUser.isVerified());
						stockMgr.saveStock(stock);

					} else {
						stockMgr.updateTwitterData(stock.getId(),
								twUser.getFollowerCount(),
								twUser.getPictureUrl(), twUser.getScreenName(), twUser.isVerified());

					}
					request.setAttribute(STOCK, stock);
			
					//request.getParameterMap().put("stock", new String[]{ String.valueOf(stock.getId())});
					logger.debug("Servlet: Stock queried successfully. Stock name:"
							+ stock.getName());
				} else {
					logger.error("Servlet: User not found. Search string: "
							+ twUserName);
					request.setAttribute(RESULT, USER_NOT_FOUND);
					request.setAttribute(REASON, twUserName + " is not found");
				}

			} else {
				logger.error("Servlet: Twitter proxy could not be created. Username: "
						+ twUserName);
				resp.fail()
						.reason("Something wrong, we could not retrieved quote info. Working on it. ");
			}
		}
	}

	private void invalidateCookies(String[] cookieNames,
			HttpServletRequest request, HttpServletResponse response) {
		List<String> cookieNameList = Arrays.asList(cookieNames);
		for (Cookie cookie : request.getCookies()) {
			if (cookieNameList.contains(cookie.getName())) {
				cookie.setMaxAge(0);
				cookie.setValue("");
				cookie.setPath("/");
				cookie.setDomain(request.getHeader("host"));
				response.addCookie(cookie);
			}
		}
	}
}
