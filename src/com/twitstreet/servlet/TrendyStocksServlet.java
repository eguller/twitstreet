package com.twitstreet.servlet;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.twitstreet.db.data.Stock;
import com.twitstreet.market.StockMgr;
import com.twitstreet.session.UserMgr;

@SuppressWarnings("serial")
@Singleton
public class TrendyStocksServlet extends TwitStreetServlet {

	public static String TOPRANKS_USER_LIST = "topranksuserlist";

	@Inject
	UserMgr userMgr;
	@Inject
	StockMgr stockMgr;

	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		response.setContentType("text/html;charset=utf-8");
		response.setHeader("Cache-Control", "no-cache"); // HTTP 1.1
		response.setHeader("Pragma", "no-cache"); // HTTP 1.0
		response.setDateHeader("Expires", 0); // prevents caching at the proxy
												// server

		loadUser(request);
		

		ArrayList<Stock> stocks = stockMgr.getSuggestedStocks();
		request.setAttribute("stockList", stocks);
		request.setAttribute("stockListName", "suggested");
		//loadUserFromCookie(request);
		try {
			getServletContext().getRequestDispatcher(
					"/WEB-INF/jsp/suggestedStocks.jsp").forward(request, response);
		} catch (ServletException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
