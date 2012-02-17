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
import com.twitstreet.db.data.Stock;
import com.twitstreet.db.data.User;
import com.twitstreet.market.PortfolioMgr;
import com.twitstreet.market.StockMgr;
import com.twitstreet.session.UserMgr;
import com.twitstreet.twitter.TwitterProxyFactory;

@SuppressWarnings("serial")
@Singleton
public class BuyServlet extends TwitStreetServlet {
	private static Logger logger = Logger.getLogger(BuyServlet.class);
	@Inject
	TwitterProxyFactory twitterProxyFactory = null;
	@Inject
	PortfolioMgr portfolioMgr = null;
	@Inject
	private final Gson gson = null;
	@Inject StockMgr stockMgr;
	@Inject UserMgr userMgr;

	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		doPost(request, response);
	}

	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		super.doPost(request, response);
		response.setContentType("text/html;charset=utf-8");
		setPageAttributes();
		
		if (user != null) {
			String stock = request.getParameter("stock");
			String amount = request.getParameter("amount");
			try {
				Stock stockObj = stockMgr.getStockById(Long.parseLong(stock));
				if (user != null && stockObj != null) {
					BuySellResponse buySellResponse = portfolioMgr.buy(
							user, stockObj,
							Integer.parseInt(amount));
					
					
					request.setAttribute(HomePageServlet.STOCK, stockObj);
					getServletContext().getRequestDispatcher(
							"/WEB-INF/jsp/buySell.jsp").forward(request, response);
				}
			} catch (NumberFormatException e) {
				logger.error("Servlet: Parsing stock, amount failed. Stock: "
						+ stock + ", amount: " + amount, e);
			} catch (ServletException e) {
				logger.error("Servlet: Dispatch error", e);
			}
		}

	}
}
