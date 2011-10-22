package com.twitstreet.servlet;

import com.google.gson.Gson;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.twitstreet.db.data.StockDO;
import com.twitstreet.market.StockMgr;
import com.twitstreet.twitter.TwitterProxy;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import java.io.IOException;
import java.sql.SQLException;

@SuppressWarnings("serial")
@Singleton
public class StockQuoteServlet extends HttpServlet {
	private static Logger logger = Logger.getLogger(StockQuoteServlet.class);
	@Inject private final StockMgr stockMgr = null;
	@Inject TwitterProxy twitterProxy = null;
    @Inject private final Gson gson = null;
	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException{
		
	}
	
	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException{
        response.setContentType("application/json;charset=utf-8");
		String stockName = (String)request.getParameter("stock");
		StockDO stockFromTwitter = twitterProxy.getStock(stockName);
		StockDO stockDO = null;
		try {
			stockDO = stockMgr.getStock(stockName);
		} catch (SQLException e) {
			// TODO stock could not be retrieved inform user
			e.printStackTrace();
		}
        StockDO currentStock = new StockDO();
		if(stockDO == null){
			try {
				stockMgr.saveStock(stockFromTwitter);
			} catch (SQLException e) {
				// TODO Stock could not be save inform user
				e.printStackTrace();
			}
            //New stock sold is 0
            currentStock = stockFromTwitter;
		}
		else{
            //stock already exist
            currentStock = stockDO;
			if(stockDO.getTotal() != stockFromTwitter.getTotal()){
				try {
					stockMgr.updateTotal(stockFromTwitter.getId(), stockFromTwitter.getTotal());
				} catch (SQLException e) {
					// TODO Stock could not be update inform user.
					e.printStackTrace();
				}
                //Update total value from twitter.
                currentStock.setTotal(stockFromTwitter.getTotal());
			}
		}
		logger.info("info");
        response.getWriter().write(gson.toJson(currentStock));
	}
	
}
