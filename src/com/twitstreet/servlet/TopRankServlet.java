package com.twitstreet.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.twitstreet.session.UserMgr;

@SuppressWarnings("serial")
@Singleton
public class TopRankServlet extends TwitStreetServlet {

	public static String TOPRANKS_USER_LIST = "topranksuserlist";

	@Inject
	UserMgr userMgr;
	@Inject

	public static String PAGE = "toprankPage";

	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		response.setContentType("text/html;charset=utf-8");
		response.setHeader("Cache-Control", "no-cache"); // HTTP 1.1
		response.setHeader("Pragma", "no-cache"); // HTTP 1.0
		response.setDateHeader("Expires", 0); // prevents caching at the proxy
			
		loadUser(request);
		//loadUserFromCookie(request);
		try {
			getServletContext().getRequestDispatcher(
					"/WEB-INF/jsp/topranks.jsp").forward(request, response);
		} catch (ServletException e) {
			e.printStackTrace();
		}

	}

}
