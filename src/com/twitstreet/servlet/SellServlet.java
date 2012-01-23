package com.twitstreet.servlet;

import java.io.IOException;

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
import com.twitstreet.session.UserMgrImpl;
import com.twitstreet.twitter.TwitterProxyFactory;

@SuppressWarnings("serial")
@Singleton
public class SellServlet extends HttpServlet {
	private static Logger logger = Logger.getLogger(UserMgrImpl.class);
	@Inject UserMgr userMgr;
	@Inject StockMgr stockMgr;
	@Inject TwitterProxyFactory twitterProxyFactory = null;
	@Inject PortfolioMgr portfolioMgr = null;
	@Inject private final Gson gson = null;
	
	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		doPost(request, response);
	}

	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		response.setContentType("application/json;charset=utf-8");
		response.setContentType("application/json;charset=utf-8");
		response.setHeader("Cache-Control","no-cache"); //HTTP 1.1
		response.setHeader("Pragma","no-cache"); //HTTP 1.0
		response.setDateHeader ("Expires", 0); //prevents caching at the proxy server
		
		User user = (User) request.getSession(false).getAttribute(User.USER);
		if(user != null){
			String stock = request.getParameter("stock");
			String amount = request.getParameter("amount");
			
			try {
				User seller = userMgr.getUserById(user.getId());
				Stock stockObj = stockMgr.getStockById(Long.parseLong(stock));
				if(seller != null && stockObj != null){
					BuySellResponse buySellResponse = portfolioMgr.sell(seller, stockObj, Integer.parseInt(amount));
					response.getWriter().write(gson.toJson(buySellResponse));
				}
			} catch (NumberFormatException e) {
				logger.error("Servlet: Parsin stock id or amount failed", e);
			}
		}
	}
}
