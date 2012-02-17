package com.twitstreet.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.google.gson.Gson;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.twitstreet.market.PortfolioMgr;
import com.twitstreet.session.UserMgr;

@SuppressWarnings("serial")
@Singleton
public class PortfolioServlet extends TwitStreetServlet {

	private static Logger logger = Logger.getLogger(PortfolioServlet.class);
	@Inject
	private final Gson gson = null;
	@Inject
	private final PortfolioMgr portfolioMgr = null;
	@Inject
	private final UserMgr userMgr = null;

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		
		super.doPost(request, response);
		
		try {
			getServletContext().getRequestDispatcher(
					"/WEB-INF/jsp/portfolio.jsp").forward(request, response);
		} catch (ServletException e) {
			logger.error("Servlet: Dispatch error", e);
		}
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		    doPost(request, response);
	}

}
