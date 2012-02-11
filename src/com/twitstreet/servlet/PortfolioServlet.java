package com.twitstreet.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.google.gson.Gson;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.twitstreet.db.data.Portfolio;
import com.twitstreet.db.data.User;
import com.twitstreet.market.PortfolioMgr;
import com.twitstreet.session.UserMgr;
import com.twitstreet.session.UserMgrImpl;

@SuppressWarnings("serial")
@Singleton
public class PortfolioServlet extends HttpServlet {

	private static Logger logger = Logger.getLogger(PortfolioServlet.class);
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
		
		try {
			getServletContext().getRequestDispatcher(
					"/WEB-INF/jsp/portfolio.jsp").forward(request, response);
		} catch (ServletException e) {
			logger.error("Servlet: Dispatch error", e);
		}
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		doPost(request, response);
	}

}
