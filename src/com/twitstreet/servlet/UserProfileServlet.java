package com.twitstreet.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.twitstreet.db.data.User;
import com.twitstreet.session.UserMgr;

@SuppressWarnings("serial")
@Singleton
public class UserProfileServlet extends TwitStreetServlet {
	@Inject UserMgr userMgr;

	public static String USER_PROFILE_USER = "userprofileuser";
	public void doGet(HttpServletRequest request, HttpServletResponse response)
	throws ServletException, IOException {
		response.setContentType("text/html;charset=utf-8");
		response.setHeader("Cache-Control","no-cache"); //HTTP 1.1
		response.setHeader("Pragma","no-cache"); //HTTP 1.0
		response.setDateHeader ("Expires", 0); //prevents caching at the proxy server
		
		loadUserFromCookie(request);
		User user = (User) request.getAttribute(User.USER);
		
		request.setAttribute("title", "User profile of " + user.getUserName());
		request.setAttribute("meta-desc", "This page shows profile of "+user.getUserName()+". You can find details of "+user.getUserName()+" like rank, portfolio, cash and portfolio details.");
		
		if (request.getParameter(User.USER) != null) {
			getServletContext().getRequestDispatcher("/WEB-INF/jsp/userProfile.jsp").forward(request, response);
		} else {
			getServletContext().getRequestDispatcher("/WEB-INF/jsp/userProfile.jsp").forward(request, response);
		}

	}
	public void doPost(HttpServletRequest request, HttpServletResponse response)
	throws ServletException, IOException {
		doGet(request, response);
	}
}
