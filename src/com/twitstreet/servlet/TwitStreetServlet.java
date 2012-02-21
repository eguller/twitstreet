package com.twitstreet.servlet;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;

import com.google.inject.Inject;
import com.twitstreet.db.data.User;
import com.twitstreet.session.UserMgr;

public class TwitStreetServlet extends HttpServlet{
	/**
	 * 
	 */
	private static final long serialVersionUID = 4132204849052813369L;
	@Inject UserMgr userMgr;
	public void loadUserFromCookie(HttpServletRequest request) {
		
		if(request.getAttribute(User.USER) != null){
			return;
		}
		
		Cookie[] cookies = request.getCookies() == null ? new Cookie[] {}
				: request.getCookies();
		boolean idFound = false;
		boolean oAuthFound = false;
		boolean active = false;
		String idStr = "";
		String oAuth = "";
		User user = null;
		for (Cookie cookie : cookies) {
			if (cookie.getName().equals(CallBackServlet.COOKIE_ID)) {
				idStr = cookie.getValue();
				idFound = true;
			}
			if (cookie.getName().equals(CallBackServlet.COOKIE_OAUTHTOKEN)) {
				oAuth = cookie.getValue();
				oAuthFound = true;
			}
			
			if(cookie.getName().equals(CallBackServlet.COOKIE_ACTIVE)){
				active = Boolean.parseBoolean(cookie.getValue());
			}

			if (idFound && oAuthFound && active) {
				try {
					long id = Long.parseLong(idStr);
					user = userMgr.getUserById(id);
					if (user != null && oAuth.equals(user.getOauthToken())) {
						break;
					}
				} catch (NumberFormatException nfe) {
					// log here someday.
				}
				break;
			}
		}
		request.setAttribute(User.USER, user);
	}
}
