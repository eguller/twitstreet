package com.twitstreet.servlet;

import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.twitstreet.db.data.StockDO;
import com.twitstreet.db.mgr.StockMgr;
import com.twitstreet.twitter.TwitterProxy;

@SuppressWarnings("serial")
@Singleton
public class StockQuoteServlet extends HttpServlet {
	private static Logger logger = LoggerFactory.getLogger(StockQuoteServlet.class);
	@Inject StockMgr stockMgr = null;
	@Inject TwitterProxy twitterProxy = null;
	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException{
		
	}
	
	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException{
		String screenName = (String)request.getParameter("twituser");
		StockDO stockFromTwitter = twitterProxy.getStock(screenName);
		StockDO stockDO =  stockMgr.getStock(screenName);
		if(stockDO == null){
			stockMgr.makePersistent(stockFromTwitter);
		}
		else{
			if(stockDO.getTotal() != stockFromTwitter.getTotal()){
				stockMgr.updateTotal(stockDO);
			}
		}
		logger.info("user:{}", screenName);
	}
	
}
