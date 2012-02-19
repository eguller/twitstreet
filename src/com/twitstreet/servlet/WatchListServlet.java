package com.twitstreet.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.twitstreet.market.StockMgr;

@SuppressWarnings("serial")
@Singleton
public class WatchListServlet extends TwitStreetServlet {

	private static Logger logger = Logger.getLogger(WatchListServlet.class);
	@Inject StockMgr stockMgr; 
	public static String STOCK = "stock"; 
	public static String ADD="add";
	public static String REMOVE="remove";
	public static String OPERATION="operation";

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		
		super.doPost(request, response);
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

					stockMgr.addStockIntoUserWatchList(stockId, getUser().getId());

				} else if (REMOVE.equalsIgnoreCase(operation)) {
					stockMgr.removeStockFromUserWatchList(stockId, getUser().getId());
				}
			}

		}
		
	

		try {
			getServletContext().getRequestDispatcher("/WEB-INF/jsp/watchList.jsp").forward(request, response);
		} catch (ServletException e) {
			logger.error("Servlet: Dispatch error", e);
		}

	}

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		    doPost(request, response);
	}

}
