package com.twitstreet.servlet;

import java.io.IOException;
import java.util.Calendar;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.HttpResponse;

import com.google.inject.Singleton;
import com.twitstreet.base.Result;
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
			Result<UserDO> result = userMgr.getUserById(userId);
			if(result.getPayload() != null){
				UserDO user = result.getPayload();
				user.setLastLogin(Calendar.getInstance().getTime());
				user.setUserName(screenName);
				user.setLastIp(request.getRemoteHost());
				user.setOauthToken(oauthToken);
				user.setOauthTokenSecret(oauthTokenSecret);
				userMgr.makePersistentUpdate(user);
			}
			else{
				UserDO user = new UserDO();
				user.setId(userId);
				user.setUserName(screenName);
				user.setFirstLogin(Calendar.getInstance().getTime());
				user.setLastIp(request.getRemoteAddr());
				user.setLastLogin(Calendar.getInstance().getTime());
				user.setOauthToken(oauthToken);
				user.setOauthTokenSecret(oauthTokenSecret);
				user.setCash(10000);
				userMgr.makePersistent(user);
			}
			request.getSession().removeAttribute(REQUEST_TOKEN);
			Cookie cookies[] = createCookie(userId, oauthToken);
			writeCookies(response, cookies);
		} catch (TwitterException e) {
			throw new ServletException(e);
		}
		response.sendRedirect(request.getContextPath() + "/");
	}
	
	public Cookie[] createCookie(long userId, String oauthToken){
		Cookie ck1 = new Cookie(COOKIE_ID,String.valueOf(userId));
		Cookie ck2 = new Cookie(COOKIE_OAUTHTOKEN,oauthToken);
		ck1.setMaxAge(-1);
		ck2.setMaxAge(-1);
		return new Cookie[]{ck1,ck2};
	}
	public void writeCookies(HttpServletResponse response, Cookie[] cookies){
		for(Cookie cookie : cookies){
			response.addCookie(cookie);
		}
		
	}
}
