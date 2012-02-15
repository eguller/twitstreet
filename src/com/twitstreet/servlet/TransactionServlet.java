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
		super.doPost(request, response);
		setPageAttributes();
		response.setContentType("text/html;charset=utf-8");
		List<TransactionRecord> transactionRecordList = null;
		String type = request.getParameter("type");

		try {

			if (USER_TRANSACTIONS.equals(type)) {

				getServletContext().getRequestDispatcher("/WEB-INF/jsp/yourtransactions.jsp").forward(request, response);

			} else {

				getServletContext().getRequestDispatcher("/WEB-INF/jsp/latesttransactions.jsp").forward(request, response);

			}

		} catch (ServletException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
