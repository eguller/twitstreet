package com.twitstreet.servlet;

import java.io.IOException;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.User;
import twitter4j.auth.AccessToken;

import com.google.inject.Singleton;
import com.twitstreet.base.Result;
import com.twitstreet.db.data.UserDO;
import com.twitstreet.db.table.ConfigMgr;
import com.twitstreet.main.Twitstreet;
import com.twitstreet.session.UserMgr;

@SuppressWarnings("serial")
@Singleton
public class HomePageServlet extends HttpServlet {
	public static final String TWITTER = "twitter";
	@Inject UserMgr userMgr;
	@Inject Twitstreet twitstreet;
	@Inject ConfigMgr configMgr;
	

	@Override
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		if(!twitstreet.isInitialized()){
			getServletContext().getRequestDispatcher(
					"/WEB-INF/jsp/setup.jsp").forward(request, response);
			return;
		}
		
		Twitter twitter = (Twitter) request.getSession()
				.getAttribute("twitter");
		if (twitter != null) {
			try {
				User twitUser = twitter.showUser(273572038);
				System.out.println("Name: " + twitUser.getName());
				System.out.println("Name: " + twitUser.getScreenName());
			} catch (TwitterException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			getServletContext().getRequestDispatcher(
					"/WEB-INF/jsp/homeAuth.jsp").forward(request, response);
		} else if (validateCookies(request)) {
			getServletContext().getRequestDispatcher(
					"/WEB-INF/jsp/homeAuth.jsp").forward(request, response);
		} else {
			getServletContext().getRequestDispatcher(
					"/WEB-INF/jsp/homeUnAuth.jsp").forward(request, response);
		}
	}

	private boolean validateCookies(HttpServletRequest request) {
		Cookie[] cookies = request.getCookies() == null ? new Cookie[]{} : request.getCookies();
		boolean idFound = false;
		boolean oAuthFound = false;
		String idStr = "";
		String oAuth = "";
		boolean valid = false;
		for (Cookie cookie : cookies) {
			if (cookie.getName().equals(CallBackServlet.COOKIE_ID)) {
				idStr = cookie.getValue();
				idFound = true;
			}
			if (cookie.getName().equals(CallBackServlet.COOKIE_OAUTHTOKEN)) {
				oAuth = cookie.getValue();
				oAuthFound = true;
			}

			if (idFound && oAuthFound) {
				try {
					long id = Long.parseLong(idStr);
					Result<UserDO> result = userMgr.getUserById(id);
					if (result.getPayload() != null) {
						UserDO user = result.getPayload();
						if (user.getOauthToken() != null && user.getOauthToken().equals(oAuth)) {
							Twitter twitter = createTwitterInstance(user);
							request.getSession().setAttribute(TWITTER, twitter);
							valid = true;
						}
					}
				} catch (NumberFormatException nfe) {
					// log here someday.
				}
				break;
			}
		}
		return valid;
	}


	public Twitter createTwitterInstance(UserDO user) {
		Twitter twitter = new TwitterFactory().getInstance();
		try {
			User twitUser = twitter.showUser(user.getId());
			System.out.println("Name: " + twitUser.getName());
			System.out.println("Name: " + twitUser.getScreenName());
		} catch (TwitterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		twitter.setOAuthConsumer(configMgr.getConsumerKey(), configMgr.getConsumerSecret());
		AccessToken oathAccessToken = new AccessToken(user.getOauthToken(),
				user.getOauthTokenSecret());
		twitter.setOAuthAccessToken(oathAccessToken);
		return twitter;
	}

}
