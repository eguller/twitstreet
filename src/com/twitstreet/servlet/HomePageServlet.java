package com.twitstreet.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import com.twitstreet.data.DashboardData;
import com.twitstreet.data.HomeData;
import com.twitstreet.data.SessionData;

@SuppressWarnings("serial")
@Singleton
public class HomePageServlet extends HttpServlet {
	
	//@Inject	private final Provider<SessionData> sessionDataProvider = null;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		//sessionDataProvider.get();
		
		// * Populate request beans
		DashboardData dashboardData = new DashboardData();
		dashboardData.isVisible = false;

		HomeData data = new HomeData();
		data.dashboard = dashboardData;
		req.setAttribute("data", data);

		// * Let the view render
		getServletContext().getRequestDispatcher("/WEB-INF/jsp/home.jsp").forward(req, resp);
	}

}
