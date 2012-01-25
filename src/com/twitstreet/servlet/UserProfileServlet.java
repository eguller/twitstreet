package com.twitstreet.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.twitstreet.db.data.User;
import com.twitstreet.session.UserMgr;

@SuppressWarnings("serial")
@Singleton
public class UserProfileServlet extends HttpServlet {
	@Inject UserMgr userMgr;
	public void doGet(HttpServletRequest request, HttpServletResponse response)
	throws ServletException, IOException {
		response.setContentType("application/json;charset=utf-8");
		response.setHeader("Cache-Control","no-cache"); //HTTP 1.1
		response.setHeader("Pragma","no-cache"); //HTTP 1.0
		response.setDateHeader ("Expires", 0); //prevents caching at the proxy server
		
		User user = (User) request.getSession().getAttribute(User.USER);
		
		String userIdStr = request.getParameter("user");
				
		if(userIdStr == null || userIdStr.length() == 0){
			response.sendRedirect(response.encodeRedirectURL("/"));
			return;
		}
		
		User userObj = userMgr.getUserById(Long.parseLong(userIdStr));
		request.setAttribute("user", userObj);
		
		if (user != null) {
			getServletContext().getRequestDispatcher(
			"/WEB-INF/jsp/userProfileAuth.jsp").forward(request, response);
		}
		else{
			getServletContext().getRequestDispatcher(
			"/WEB-INF/jsp/userProfileUnAuth.jsp").forward(request, response);
		}
	}
	public void doPost(HttpServletRequest request, HttpServletResponse response)
	throws ServletException, IOException {
		doGet(request, response);
	}
}
