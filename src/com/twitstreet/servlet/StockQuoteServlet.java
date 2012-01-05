package com.twitstreet.servlet;

import com.google.gson.Gson;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.twitstreet.db.data.Stock;
import com.twitstreet.db.data.User;
import com.twitstreet.market.PortfolioMgr;
import com.twitstreet.market.StockMgr;
import com.twitstreet.session.UserMgr;
import com.twitstreet.twitter.SimpleTwitterUser;
import com.twitstreet.twitter.TwitterProxy;
import com.twitstreet.twitter.TwitterProxyFactory;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import twitter4j.TwitterException;

import java.io.IOException;
import java.sql.SQLException;

@SuppressWarnings("serial")
@Singleton
public class StockQuoteServlet extends HttpServlet {
	private static final String QUOTE = "quote";
	private static Logger logger = Logger.getLogger(StockQuoteServlet.class);
	@Inject
	private final StockMgr stockMgr = null;
	@Inject
	private final UserMgr userMgr = null;
	@Inject
	private final Gson gson = null;
	@Inject TwitterProxyFactory twitterProxyFactory = null;

	@Inject 
	private final PortfolioMgr portfolioMgr = null;
	
	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws IOException {

	}

	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		response.setContentType("application/json;charset=utf-8");
		String twUserName = (String) request.getParameter(QUOTE);

		User user =  request.getSession(false) == null ? null : (User)request.getSession(false).getAttribute(User.USER);
		TwitterProxy twitterProxy = null;
		Response resp = Response.create();
		if (user == null) {
			// uses someone else account to get quote for unauthenticated users.
			user = userMgr.random();
		}

		twitterProxy = user == null ? null : twitterProxyFactory.create(user.getOauthToken(), user.getOauthTokenSecret());

		if (twitterProxy != null) {

			// Get user info from twitter.
			twitter4j.User twUser = null;
			try {
				twUser = twitterProxy.getTwUser(twUserName);
			} catch (TwitterException e1) {
				resp.fail()
						.reason("Something wrong, we could not connected to Twitter. Working on it.");
				response.getWriter().write(gson.toJson(resp));
				return;
			}

			// Get user info from database
			Stock stock = null;
			if (twUser != null) {
				try {
					stock = stockMgr.getStockById(twUser.getId());
				} catch (SQLException e) {
					resp.fail()
							.reason("Something wrong, we could not retrieved quote info. Working on it");
					response.getWriter().write(gson.toJson(resp));
					return;
				}

				// User info retrieved both from twitter and database.
				if (stock == null) {
					// This user was not queried before.
					logger.debug("Servlet: Stock queried first time. Stock name: "
							+ twUserName);
					stock = new Stock();
					stock.setId(twUser.getId());
					stock.setName(twUser.getScreenName());
					stock.setTotal(twUser.getFollowersCount());
					stock.setPictureUrl(twUser.getProfileImageURL().toString());
					stock.setSold(0.0D);
					try {
						stockMgr.saveStock(stock);
					} catch (SQLException e) {
						// Save failed but give same response. We could not
						// go further without saving stock info.
						resp.fail()
								.reason("Something wrong, we could not retrieved quote info. Working on it");
						response.getWriter().write(gson.toJson(resp));
						return;
					}
				} else {
					// User queried before. Check that follower count
					// changed or not.
					if (stock.getTotal() != twUser.getFollowersCount()
							&& stock.getName().equals(twUser.getScreenName())) {
						// if follower count changed update database.
						try {
							stockMgr.updateTotal(stock.getId(),
									twUser.getFollowersCount());
						} catch (SQLException e) {
							resp.fail()
									.reason("Something wrong, we could not retrieved quote info. Working on it");
							response.getWriter().write(gson.toJson(resp));
							return;
						}
						// Update total value with latest one from twitter
						stock.setTotal(twUser.getFollowersCount());
					} else if (stock.getTotal() != twUser.getFollowersCount()
							&& !stock.getName().equals(twUser.getScreenName())) {
						try {
							stockMgr.updateTotalAndName(stock.getId(),
									twUser.getFollowersCount(),
									twUser.getScreenName());
						} catch (SQLException e) {
							resp.fail()
									.reason("Something wrong, we could not retrieved quote info. Working on it");
							response.getWriter().write(gson.toJson(resp));
							return;
						}
					} else if (stock.getTotal() == twUser.getFollowersCount()
							&& !stock.getName().equals(twUser.getScreenName())) {
						try {
							stockMgr.updateName(stock.getId(),
									twUser.getScreenName());
						} catch (SQLException e) {
							resp.fail()
									.reason("Something wrong, we could not retrieved quote info. Working on it");
							response.getWriter().write(gson.toJson(resp));
							return;
						}
					}
				}
				logger.debug("Servlet: Stock queried successfully. Stock name:"
						+ stock.getName());
				double percentage = 0.0;
				try{
					percentage = portfolioMgr.getStockSoldPercentage(user.getId(), stock.getId());
				}
				catch(SQLException ex){
					logger.warn("Servlet: Query percentage failed", ex);
				}
				resp.success().setRespOjb(new QuoteResponse(stock, percentage));
				response.getWriter().write(gson.toJson(resp));
				return;
			} else {
				logger.debug("Servlet: User name does not exist. Making twitter search. Query: "
						+ twUserName);
				try {
					SimpleTwitterUser[] searchResult = twitterProxy.searchUsers(twUserName);
					resp.success().resultCode("user-notfound").reason("We could not find \""+twUserName+"\" but following ones close.").setRespOjb(searchResult);
					return;
				} catch (TwitterException e) {
					resp.fail()
							.reason("Something wrong, we could not retrieved quote info. Working on it");
					response.getWriter().write(gson.toJson(resp));
					return;
				}
			}

		} else {
			logger.error("Servlet: Twitter proxy could not be created. Username: "
					+ twUserName);
			resp.fail()
					.reason("Something wrong, we could not retrieved quote info. Working on it");
		}
	}
}
