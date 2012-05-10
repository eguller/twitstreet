package com.twitstreet.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.twitstreet.db.data.User;
import com.twitstreet.session.UserMgr;

@Singleton
public class LoanServlet extends TwitStreetServlet {
	private static Logger logger = Logger.getLogger(SeasonServlet.class);
	private static final String RECEIVE_LOAN = "receive-loan";
	private static final String PAY_BACK = "pay-back";
	private static final String PAY_BACK_ALL = "pay-back-all";
	@Inject
	UserMgr userMgr;

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException{
		doPost(request, response);
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException{
		response.setContentType("text/html;charset=utf-8");
		response.setHeader("Cache-Control", "no-cache"); // HTTP 1.1
		response.setHeader("Pragma", "no-cache"); // HTTP 1.0
		response.setDateHeader("Expires", 0); // prevents caching at the proxy
		loadUser(request);
		Long userId = null;										// server
		if ( (userId = (Long)request.getSession().getAttribute(User.USER_ID)) != null) {
			String amountStr = request.getParameter("amount");
			String action = request.getParameter("action");
			if (amountStr != null && amountStr.length() > 0) {
				try {
					double amount = Double.parseDouble(amountStr);
					if (RECEIVE_LOAN.equals(action)) {
						userMgr.receiveLoan(userId, amount);
					} else if (PAY_BACK.equals(action)) {
						userMgr.payLoanBack(userId, amount);
					} else if (PAY_BACK_ALL.equals(action)) {
						userMgr.payAllLoanBack(userId);
					}
				} catch (NumberFormatException e) {
					logger.error("Error while parsing loan", e);
				}
			}
		}
		
		try {
			getServletContext().getRequestDispatcher(
					"/WEB-INF/jsp/balance.jsp").forward(request, response);
		} catch (ServletException e) {
			logger.error("Servlet: Dispatch error", e);
		}
	}
}
