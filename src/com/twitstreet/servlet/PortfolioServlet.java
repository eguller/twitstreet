package com.twitstreet.servlet;

import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.twitstreet.db.data.Portfolio;
import com.twitstreet.db.data.User;
import com.twitstreet.market.PortfolioMgr;
import com.twitstreet.session.UserMgr;

@SuppressWarnings("serial")
@Singleton
public class PortfolioServlet extends HttpServlet {
	@Inject
	private final Gson gson = null;
	@Inject
	private final PortfolioMgr portfolioMgr = null;
	@Inject
	private final UserMgr userMgr = null;

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		response.setContentType("application/json;charset=utf-8");
		response.setContentType("application/json;charset=utf-8");
		response.setHeader("Cache-Control","no-cache"); //HTTP 1.1
		response.setHeader("Pragma","no-cache"); //HTTP 1.0
		response.setDateHeader ("Expires", 0); //prevents caching at the proxy server
		
		String userIdStr = (String) request.getAttribute("user");
		if (userIdStr == null) {
			User user = request.getSession(false) == null ? null : (User)request.getSession().getAttribute(User.USER);
			if (user != null) {
				userIdStr = String.valueOf(user.getId());
			}
		}

		if (userIdStr != null) {
			long userId = Long.parseLong(userIdStr);
			User user = userMgr.getUserById(userId);
			if (user != null) {
				Portfolio portfolio = portfolioMgr.getUserPortfolio(user);
				response.getWriter().write(gson.toJson(portfolio));
			}
		}
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		doPost(request, response);
	}

}
