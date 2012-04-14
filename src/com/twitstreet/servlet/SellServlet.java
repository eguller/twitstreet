/**
	TwitStreet - Twitter Stock Market Game
    Copyright (C) 2012  Engin Guller (bisanth@gmail.com), Cagdas Ozek (cagdasozek@gmail.com)

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
**/

package com.twitstreet.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

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
public class SellServlet extends TwitStreetServlet{
	private static Logger logger = Logger.getLogger(SellServlet.class);
	@Inject UserMgr userMgr;
	@Inject StockMgr stockMgr;
	@Inject TwitterProxyFactory twitterProxyFactory = null;
	@Inject PortfolioMgr portfolioMgr = null;
	
	public String SELL_ALL = "all";
	public String RESPONSE_NEEDED = "response";
	
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

		loadUser(request);
		//loadUserFromCookie(request);
		User user = (User) request.getAttribute(User.USER);
		if(user != null){
			String stock = request.getParameter("stock");
			String amount = request.getParameter("amount");
			
			try {
//				User seller = userMgr.getUserById(user.getId());
				Stock stockObj = stockMgr.getStockById(Long.parseLong(stock));
				if(stockObj != null){
					int amountInt = 0;
					if(amount.equalsIgnoreCase(SELL_ALL)){
						amountInt = Integer.MAX_VALUE;
					}
					else{
						amountInt = Integer.parseInt(amount);
					}
					
					boolean succ = portfolioMgr.sell(user, stockObj,amountInt);
					

					if(!succ){
						return;
					}
				
					
					
					String responseNeededString = request.getParameter(RESPONSE_NEEDED);
				
					if (!("n".equalsIgnoreCase(responseNeededString))) {

						request.setAttribute(HomePageServlet.STOCK, stockObj);
						getServletContext().getRequestDispatcher(	"/WEB-INF/jsp/buySell.jsp").forward(request, response);
					}
			
				}
			} catch (NumberFormatException e) {
				logger.error("Servlet: Parsin stock id or amount failed", e);
			} catch (ServletException e) {
				logger.error("Servlet: Dispatch error", e);
			}
		}
	}
}
