/**
	TwitStreet - Twitter Stock Market Game
    Copyright (C) 2012  Engin Guller (bisanthe@gmail.com), Cagdas Ozek (cagdasozek@gmail.com)

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
import com.twitstreet.db.data.User;
import com.twitstreet.main.TwitstreetException;
import com.twitstreet.market.PortfolioMgr;
import com.twitstreet.market.StockMgr;

@SuppressWarnings("serial")
@Singleton
public class WatchListServlet extends TwitStreetServlet {

	private static Logger logger = Logger.getLogger(WatchListServlet.class);
	@Inject StockMgr stockMgr; 
	@Inject PortfolioMgr portfolioMgr; 
	public static String STOCK = "stock"; 
	public static String ADD="add";
	public static String REMOVE="remove";
	public static String OPERATION="operation";

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		response.setContentType("text/html;charset=utf-8");
		response.setHeader("Cache-Control","no-cache"); //HTTP 1.1
		response.setHeader("Pragma","no-cache"); //HTTP 1.0
		response.setDateHeader ("Expires", 0); //prevents caching at the proxy server
		
		
		loadUser(request);
		//loadUserFromCookie(request);
		User user = (User) request.getAttribute(User.USER);
		
		String operation = request.getParameter(OPERATION);
		
		String stockIdStr = request.getParameter(STOCK);
		
		if (operation != null) {
			long stockId = -1;
			try{
				stockId = Long.valueOf(stockIdStr);				
			}catch(Exception ex){		
				
			}

			if (stockId != -1) {

				if (ADD.equalsIgnoreCase(operation)) {
					try {
						portfolioMgr.addStockIntoUserWatchList(stockId, user.getId());
					} catch (TwitstreetException e) {
						writeErrorIntoResponse(request, response, e);
						return;
					}
				} else if (REMOVE.equalsIgnoreCase(operation)) {
					portfolioMgr.removeStockFromUserWatchList(stockId, user.getId());
				}
			}

		}
		
	

		try {
			getServletContext().getRequestDispatcher("/WEB-INF/jsp/watchListContent.jsp").forward(request, response);
		} catch (ServletException e) {
			logger.error("Servlet: Dispatch error", e);
		}

	}

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		    doPost(request, response);
	}

}
