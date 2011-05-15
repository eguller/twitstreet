package com.twitstreet.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import com.twitstreet.data.DashboardData;
import com.twitstreet.data.HomeData;
import com.twitstreet.session.SessionData;

@SuppressWarnings("serial")
@Singleton
public class HomePageServlet extends HttpServlet {
	
	@Inject
	private final Provider<SessionData> sessionDataProvider = null;
	
	@Inject
	@Named("com.twitstreet.meta.ConsumerKey") 
	private final String consumerKey = null;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		DashboardData dashboardData = new DashboardData();

		if( req.getSession(false) != null ) {
			SessionData sessionData = sessionDataProvider.get();
			dashboardData.userid = sessionData.getTwitterAccess().getUserId();
			dashboardData.isVisible = true;
		}
		else {
			dashboardData.isVisible = false;			
		}

		HomeData data = new HomeData();
		
		data.dashboard = dashboardData;
		data.taApiKey = consumerKey;
		
		req.setAttribute("data", data);

		// * Let the view render
		getServletContext().getRequestDispatcher("/WEB-INF/jsp/home.jsp").forward(req, resp);
	}

}
