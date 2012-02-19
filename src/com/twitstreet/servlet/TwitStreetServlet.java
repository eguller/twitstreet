package com.twitstreet.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.inject.Inject;
import com.twitstreet.db.data.User;
import com.twitstreet.session.UserMgr;

public abstract class TwitStreetServlet extends HttpServlet {
	@Inject UserMgr userMgr;
	HttpServletRequest request;
	HttpServletResponse response;
	protected User user = null;
	/**
	 * 
	 */
	private static final long serialVersionUID = 8842472067124473768L;
	
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		this.request = request;
		this.response = response;
		setPageAttributes();
		loadUserFromCookie();
	}
	
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		this.request = request;
		this.response = response;
		setPageAttributes();
		loadUserFromCookie();
	}
	
	public void setPageAttributes(){
		response.setCharacterEncoding("UTF-8");
		response.setHeader("Cache-Control", "no-cache"); // HTTP 1.1
		response.setHeader("Pragma", "no-cache"); // HTTP 1.0
		response.setDateHeader("Expires", 0); // prevents caching at the proxy
												// server
	}
	
	public void loadUserFromCookie() {
		Cookie[] cookies = request.getCookies() == null ? new Cookie[] {}
				: request.getCookies();
		boolean idFound = false;
		boolean oAuthFound = false;
		String idStr = "";
		String oAuth = "";
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

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

}
