package com.tweetstreet.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.inject.Singleton;
import com.tweetstreet.data.DashboardData;
import com.tweetstreet.data.HomeData;

@SuppressWarnings("serial")
@Singleton
public class HomePageServlet extends HttpServlet {
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// populate request beans
		DashboardData dashboardData = new DashboardData();
		dashboardData.isVisible = false;
		dashboardData.userName = "ooktay";
		
		HomeData data = new HomeData();
		data.dashboard = dashboardData;
		req.setAttribute("data", data);
		
		// Let the view render
		getServletContext().getRequestDispatcher("/WEB-INF/jsp/home.jsp").forward(req, resp);
	}
}
