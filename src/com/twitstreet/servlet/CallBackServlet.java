package com.twitstreet.servlet;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Calendar;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import com.google.inject.Singleton;
import com.twitstreet.db.data.UserDO;
import com.twitstreet.session.UserMgr;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;
@SuppressWarnings("serial")
@Singleton
public class CallBackServlet extends HttpServlet{
	public static final String COOKIE_ID = "id";
	public static final String COOKIE_OAUTHTOKEN = "oauthtoken";
	private static final String REQUEST_TOKEN = "requestToken";
	private static final String OAUTH_VERIFIER = "oauth_verifier";
	/*
	 * I will be dead when this cookie is expired.
	 * Wed, 08 Nov 2079 04:24:42 GMT
	 * */
	private static final int COOKIE_EXPIRE = Integer.MAX_VALUE;
	@Inject UserMgr userMgr;
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		Twitter twitter = (Twitter) request.getSession()
				.getAttribute(HomePageServlet.TWITTER);
		RequestToken requestToken = (RequestToken) request.getSession()
				.getAttribute(REQUEST_TOKEN);
		String verifier = request.getParameter(OAUTH_VERIFIER);
		try {
			AccessToken accessToken = twitter.getOAuthAccessToken(requestToken, verifier);
			long userId = accessToken.getUserId();
			String screenName = accessToken.getScreenName();
			String oauthToken = accessToken.getToken();
			String oauthTokenSecret = accessToken.getTokenSecret();
			UserDO user = null;
			try {
				user = userMgr.getUserById(userId);
			} catch (SQLException e) {
				// TODO Log here and take appropriate action to inform user.
				e.printStackTrace();
			}
			if(user != null){
				user.setLastLogin(Calendar.getInstance().getTime());
				user.setUserName(screenName);
				user.setLastIp(request.getRemoteHost());
				user.setOauthToken(oauthToken);
				user.setOauthTokenSecret(oauthTokenSecret);
				userMgr.saveUser(user);
			}
			else{
				user = new UserDO();
				user.setId(userId);
				user.setUserName(screenName);
				user.setFirstLogin(Calendar.getInstance().getTime());
				user.setLastIp(request.getRemoteAddr());
				user.setLastLogin(Calendar.getInstance().getTime());
				user.setOauthToken(oauthToken);
				user.setOauthTokenSecret(oauthTokenSecret);
				user.setCash(10000);
				userMgr.saveUser(user);
			}
			request.getSession().removeAttribute(REQUEST_TOKEN);
			Cookie cookies[] = createCookie(userId, oauthToken);
			writeCookies(response, cookies);
		} catch (TwitterException e) {
			throw new ServletException(e);
		} catch (SQLException e) {
			// TODO Log here and inform user
			e.printStackTrace();
		}
		response.sendRedirect(request.getContextPath() + "/");
	}
	
	public Cookie[] createCookie(long userId, String oauthToken){
		Cookie ck1 = new Cookie(COOKIE_ID,String.valueOf(userId));
		Cookie ck2 = new Cookie(COOKIE_OAUTHTOKEN,oauthToken);
		ck1.setMaxAge(COOKIE_EXPIRE);
		ck2.setMaxAge(COOKIE_EXPIRE);
		return new Cookie[]{ck1,ck2};
	}
	public void writeCookies(HttpServletResponse response, Cookie[] cookies){
		for(Cookie cookie : cookies){
			response.addCookie(cookie);
		}
		
	}
}
