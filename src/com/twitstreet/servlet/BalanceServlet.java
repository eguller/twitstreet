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
import com.twitstreet.db.data.User;
import com.twitstreet.session.UserMgr;

@SuppressWarnings("serial")
@Singleton
public class BalanceServlet extends TwitStreetServlet {

	private static Logger logger = Logger.getLogger(BalanceServlet.class);
	
	@Inject UserMgr userMgr;
	@Inject private final Gson gson = null;
	public void doGet(HttpServletRequest request, HttpServletResponse response)
	throws IOException, ServletException {
		doPost(request, response);
	}
	
	public void doPost(HttpServletRequest request, HttpServletResponse response)
	throws IOException, ServletException {
		response.setContentType("text/html;charset=utf-8");
		response.setHeader("Cache-Control","no-cache"); //HTTP 1.1
		response.setHeader("Pragma","no-cache"); //HTTP 1.0
		response.setDateHeader ("Expires", 0); //prevents caching at the proxy server

		loadUserFromCookie(request);
		
		try {
			getServletContext().getRequestDispatcher(
					"/WEB-INF/jsp/balance.jsp").forward(request, response);
		} catch (ServletException e) {
			logger.error("Servlet: Dispatch error", e);
		}
	}
}
