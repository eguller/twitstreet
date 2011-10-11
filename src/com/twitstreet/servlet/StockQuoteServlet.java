package com.twitstreet.servlet;

import com.google.gson.Gson;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.twitstreet.db.data.StockDO;
import com.twitstreet.market.StockMgr;
import com.twitstreet.twitter.TwitterProxy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@SuppressWarnings("serial")
@Singleton
public class StockQuoteServlet extends HttpServlet {
	private static Logger logger = LoggerFactory.getLogger(StockQuoteServlet.class);
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
		StockDO stockDO =  stockMgr.getStock(stockName).getPayload();
        StockDO currentStock = new StockDO();
		if(stockDO == null){
			stockMgr.makePersistent(stockFromTwitter);
            //New stock sold is 0
            currentStock = stockFromTwitter;
		}
		else{
            //stock already exist
            currentStock = stockDO;
			if(stockDO.getTotal() != stockFromTwitter.getTotal()){
				stockMgr.updateTotal(stockFromTwitter.getId(), stockFromTwitter.getTotal());
                //Update total value from twitter.
                currentStock.setTotal(stockFromTwitter.getTotal());
			}
		}
		logger.info("user:{}", stockName);
        response.getWriter().write(gson.toJson(currentStock));
	}
	
}