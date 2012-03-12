package com.twitstreet.servlet;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.twitstreet.cache.TransactionCache;
import com.twitstreet.db.data.TransactionRecord;
import com.twitstreet.db.data.User;
import com.twitstreet.market.TransactionMgr;

@SuppressWarnings("serial")
@Singleton
public class TransactionServlet extends TwitStreetServlet {
	private static String USER_TRANSACTIONS = "user";
	@Inject
	TransactionMgr transactionMgr;
	@Inject
	TransactionCache transactionRecordCache;
	@Inject
	private final Gson gson = null;

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		doPost(request, response);
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		response.setContentType("text/html;charset=utf-8");
		response.setHeader("Cache-Control","no-cache"); //HTTP 1.1
		response.setHeader("Pragma","no-cache"); //HTTP 1.0
		response.setDateHeader ("Expires", 0); //prevents caching at the proxy server
		
		String type = request.getParameter("type");
		
		loadUser(request);
		//loadUserFromCookie(request);
		try {

			if (USER_TRANSACTIONS.equals(type)) {

				getServletContext().getRequestDispatcher("/WEB-INF/jsp/yourTransactionsContent.jsp").forward(request, response);

			} else {

				getServletContext().getRequestDispatcher("/WEB-INF/jsp/currentTransactionsContent.jsp").forward(request, response);

			}

		} catch (ServletException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
