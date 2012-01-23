package com.twitstreet.servlet;

import java.io.IOException;
import java.util.Calendar;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.inject.Singleton;
import com.twitstreet.config.ConfigMgr;
import com.twitstreet.db.data.User;
import com.twitstreet.session.UserMgr;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;

@SuppressWarnings("serial")
@Singleton
public class CallBackServlet extends HttpServlet {
	public static final String COOKIE_ID = "id";
	public static final String COOKIE_OAUTHTOKEN = "oauthtoken";
	private static final String REQUEST_TOKEN = "requestToken";
	private static final String OAUTH_VERIFIER = "oauth_verifier";
	public static final double INITIAL_MONEY = 10000;
	/*
	 * I will be dead when this cookie is expired. Wed, 08 Nov 2079 04:24:42 GMT
	 */
	private static final int COOKIE_EXPIRE = Integer.MAX_VALUE;
	@Inject
	UserMgr userMgr;
	@Inject
	ConfigMgr configMgr;

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		
		response.setContentType("text/html");
		response.setContentType("application/json;charset=utf-8");
		response.setHeader("Cache-Control","no-cache"); //HTTP 1.1
		response.setHeader("Pragma","no-cache"); //HTTP 1.0
		response.setDateHeader ("Expires", 0); //prevents caching at the proxy server
		
		Twitter twitter = new TwitterFactory().getInstance();
		twitter.setOAuthConsumer(configMgr.getConsumerKey(),
				configMgr.getConsumerSecret());

		RequestToken requestToken = (RequestToken) request.getSession()
				.getAttribute(REQUEST_TOKEN);
		String verifier = request.getParameter(OAUTH_VERIFIER);
		try {
			AccessToken accessToken = twitter.getOAuthAccessToken(requestToken,
					verifier);
			long userId = accessToken.getUserId();
			String screenName = accessToken.getScreenName();
			String oauthToken = accessToken.getToken();
			String oauthTokenSecret = accessToken.getTokenSecret();
			User user = null;
			user = userMgr.getUserById(userId);
			twitter4j.User twUser = twitter.showUser(userId);
			if (user == null) {
				user = new User();
				user.setId(userId);
				user.setUserName(screenName);
				user.setFirstLogin(Calendar.getInstance().getTime());
				user.setLastIp(request.getRemoteAddr());
				user.setLastLogin(Calendar.getInstance().getTime());
				user.setOauthToken(oauthToken);
				user.setOauthTokenSecret(oauthTokenSecret);
				user.setCash(configMgr.getInitialMoney());
				user.setPictureUrl(twUser.getProfileImageURL().toString());
				userMgr.saveUser(user);
				request.getSession().setAttribute(User.USER, user);
			} else {
				user = new User();
				user.setId(userId);
				user.setLastLogin(Calendar.getInstance().getTime());
				user.setUserName(screenName);
				user.setLastIp(request.getRemoteHost());
				user.setOauthToken(oauthToken);
				user.setOauthTokenSecret(oauthTokenSecret);
				user.setPictureUrl(twUser.getProfileImageURL().toString());
				userMgr.updateUser(user);
				request.getSession().setAttribute(User.USER, user);
			}
			request.getSession().removeAttribute(REQUEST_TOKEN);
			Cookie cookies[] = createCookie(userId, oauthToken);
			writeCookies(response, cookies);
		} catch (TwitterException e) {
			throw new ServletException(e);
		}
		response.sendRedirect(request.getContextPath() + "/");
	}

	public Cookie[] createCookie(long userId, String oauthToken) {
		Cookie ck1 = new Cookie(COOKIE_ID, String.valueOf(userId));
		Cookie ck2 = new Cookie(COOKIE_OAUTHTOKEN, oauthToken);
		ck1.setMaxAge(COOKIE_EXPIRE);
		ck2.setMaxAge(COOKIE_EXPIRE);
		return new Cookie[] { ck1, ck2 };
	}

	public void writeCookies(HttpServletResponse response, Cookie[] cookies) {
		for (Cookie cookie : cookies) {
			response.addCookie(cookie);
		}

	}
}
