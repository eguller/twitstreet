package com.twitstreet.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.inject.Singleton;
import com.twitstreet.db.data.User;

@SuppressWarnings("serial")
public class UserProfileServlet extends HttpServlet {
	public void doGet(HttpServletRequest request, HttpServletResponse response)
	throws ServletException, IOException {
		User user = (User) request.getSession().getAttribute(User.USER);
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
