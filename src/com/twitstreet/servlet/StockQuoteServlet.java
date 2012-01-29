package com.twitstreet.servlet;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import twitter4j.TwitterException;

import com.google.gson.Gson;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.twitstreet.config.ConfigMgr;
import com.twitstreet.db.data.Stock;
import com.twitstreet.db.data.User;
import com.twitstreet.market.PortfolioMgr;
import com.twitstreet.market.StockMgr;
import com.twitstreet.session.UserMgr;
import com.twitstreet.twitter.SimpleTwitterUser;
import com.twitstreet.twitter.TwitterProxy;
import com.twitstreet.twitter.TwitterProxyFactory;

@SuppressWarnings("serial")
@Singleton
public class StockQuoteServlet extends HttpServlet {
	public static final String QUOTE = "quote";
	public static final String OTHER_SEARCH_RESULTS = "other-search-results";
	private static Logger logger = Logger.getLogger(StockQuoteServlet.class);
	@Inject
	private final StockMgr stockMgr = null;
	@Inject
	private final UserMgr userMgr = null;
	@Inject
	private final Gson gson = null;
	@Inject
	TwitterProxyFactory twitterProxyFactory = null;

	@Inject
	private final PortfolioMgr portfolioMgr = null;

	@Inject
	ConfigMgr configMgr;

	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		doPost(request, response);
	}

	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		response.setContentType("application/json;charset=utf-8");
		response.setHeader("Cache-Control", "no-cache"); // HTTP 1.1
		response.setHeader("Pragma", "no-cache"); // HTTP 1.0
		response.setDateHeader("Expires", 0); // prevents caching at the proxy
												// server

		String twUserName = (String) request.getParameter(QUOTE);

		User user = request.getSession(false) == null ? null : (User) request
				.getSession(false).getAttribute(User.USER);
		request.getSession().setAttribute(QUOTE, twUserName);
		TwitterProxy twitterProxy = null;
		Response resp = Response.create();
		if (user == null) {
			// uses someone else account to get quote for unauthenticated users.
			user = userMgr.random();
		}

		twitterProxy = user == null ? null : twitterProxyFactory.create(
				user.getOauthToken(), user.getOauthTokenSecret());

		SimpleTwitterUser twUser = null;
		ArrayList<SimpleTwitterUser> searchResultList = new ArrayList<SimpleTwitterUser>();

		if (twitterProxy != null) {
			// Get user info from twitter.
			try {
				try {
					twUser = new SimpleTwitterUser(
							twitterProxy.getTwUser(twUserName));
				} catch (TwitterException ex) {
					// omit exception, maybe this is search user not get user
				}
				searchResultList = twitterProxy.searchUsers(twUserName);
				if (twUser == null) {
					if (searchResultList != null && searchResultList.size() > 0) {
						twUser = searchResultList.get(0);
						searchResultList.remove(0);
					}
				}
				request.getSession().setAttribute(OTHER_SEARCH_RESULTS,
						searchResultList);

			} catch (TwitterException e1) {
				resp.fail()
						.reason("Something wrong, we could not connected to Twitter. Working on it.");
				response.getWriter().write(gson.toJson(resp));
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
					stockMgr.saveStock(stock);

				} else {
					stockMgr.updateTwitterData(stock.getId(),
							twUser.getFollowerCount(), twUser.getPictureUrl(),
							twUser.getScreenName());

				}
				logger.debug("Servlet: Stock queried successfully. Stock name:"
						+ stock.getName());
				double percentage = 0.0;
				percentage = portfolioMgr.getStockSoldPercentage(user.getId(),
						stock.getId());

				if (stock.getTotal() < configMgr.getMinFollower()) {
					resp.success()
							.resultCode("min-follower-count")
							.setRespOjb(
									new MinFollowerCountResponse(stock,
											configMgr.getMinFollower()));
				} else {
					resp.success().setRespOjb(
							new QuoteResponse(stock, percentage,
									searchResultList));
				}
				response.getWriter().write(gson.toJson(resp));
				return;
			} else {
				logger.error("Servlet: User not found. Search string: "
						+ twUserName);
				resp.fail()
						.reason("Something wrong, we could not retrieved quote info. Working on it. ");
			}

		} else {
			logger.error("Servlet: Twitter proxy could not be created. Username: "
					+ twUserName);
			resp.fail()
					.reason("Something wrong, we could not retrieved quote info. Working on it. ");
		}
	}

}
