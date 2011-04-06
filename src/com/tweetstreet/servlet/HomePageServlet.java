package com.tweetstreet.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.inject.Singleton;
import com.tweetstreet.data.HomeQ1User;

@SuppressWarnings("serial")
@Singleton
public class HomePageServlet extends HttpServlet {
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// populate request beans
		HomeQ1User q1user = new HomeQ1User();
		q1user.isVisible = false;
		q1user.userName = "ooktay";
		req.setAttribute("q1user", q1user);
		
		// Let the view render
		getServletContext().getRequestDispatcher("/WEB-INF/jsp/home.jsp").forward(req, resp);
	}
}
