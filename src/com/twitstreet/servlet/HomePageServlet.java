package com.twitstreet.servlet;

import java.io.IOException;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.inject.Singleton;
import com.twitstreet.config.ConfigMgr;
import com.twitstreet.db.data.User;
import com.twitstreet.main.Twitstreet;
import com.twitstreet.session.UserMgr;

@SuppressWarnings("serial")
@Singleton
public class HomePageServlet extends HttpServlet {
	@Inject
	UserMgr userMgr;
	@Inject
	Twitstreet twitstreet;
	@Inject
	ConfigMgr configMgr;

	@Override
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		if (!twitstreet.isInitialized()) {
			getServletContext().getRequestDispatcher("/WEB-INF/jsp/setup.jsp")
					.forward(request, response);
			return;
		}

		User user = (User) request.getSession().getAttribute(User.USER);
		if (user != null) {
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
		Cookie[] cookies = request.getCookies() == null ? new Cookie[] {}
				: request.getCookies();
		boolean idFound = false;
		boolean oAuthFound = false;
		String idStr = "";
		String oAuth = "";
		boolean valid = false;
		for (Cookie cookie : cookies) {
			if (cookie.getName().equals(CallBackServlet.COOKIE_ID)
					&& cookie.getMaxAge() > 0) {
				idStr = cookie.getValue();
				idFound = true;
			}
			if (cookie.getName().equals(CallBackServlet.COOKIE_OAUTHTOKEN)
					&& cookie.getMaxAge() > 0) {
				oAuth = cookie.getValue();
				oAuthFound = true;
			}

			if (idFound && oAuthFound) {
				try {
					long id = Long.parseLong(idStr);
					User user = null;
					user = userMgr.getUserById(id);
					if (user != null && oAuth.equals(user.getOauthToken())) {
						request.getSession().setAttribute(User.USER, user);
						valid = true;
						break;
					}
				} catch (NumberFormatException nfe) {
					// log here someday.
				}
				break;
			}
		}
		return valid;
	}
}
