package com.twitstreet.task;

import java.util.ArrayList;
import java.util.List;

import com.google.inject.Inject;
import com.twitstreet.db.data.Portfolio;
import com.twitstreet.db.data.Stock;
import com.twitstreet.db.data.StockInPortfolio;
import com.twitstreet.db.data.User;
import com.twitstreet.main.TwitstreetException;
import com.twitstreet.market.PortfolioMgr;
import com.twitstreet.market.StockMgr;
import com.twitstreet.session.UserMgr;

public class AutoPlayerTask implements Runnable{
	
	@Inject UserMgr userMgr;
	@Inject StockMgr stockMgr;
	@Inject PortfolioMgr portfolioMgr;
	List<User> autoplayerList = new ArrayList<User>();
	List<Stock> suggestedStocks = new ArrayList<Stock>();
	private static final long MIN_SLEEP = 5000; //5 sec
	private static final long MAX_SLEEP = 60000; //1 min
	private static final int SUGGESTED_STOCK_LIMIT = 200;
	
	private long lastSuggestedStockLoadTime = 0;
	private long lastAutoPlayerLoadTime = 0;
	private static final long MAX_SUGGESTED_STOCK_LOAD_DURATION = 20 * 60 * 1000; //20 min
	private static final long MAX_AUTOPLAYER_LOAD_DURATION = 4 * 60 * 60 * 1000; //4 hours
	
	private User getRandomAutoPlayer(){
		if(autoplayerList.size() > 0){
			int rnd = (int)(autoplayerList.size() * Math.random());
			return autoplayerList.get(rnd);
		}
		return null;
	}
	
	@Override
	public void run() {
		while(true){
			reloadAutoPlayers();
			reloadSuggestedStocks();
			User user = getRandomAutoPlayer();
			try {
				if(user.getPortfolio() > user.getCash()){
					Portfolio portfolio = portfolioMgr.getUserPortfolio(user);
					List<StockInPortfolio> stockInPortfolioList =  portfolio.getStockInPortfolioList();
					if(stockInPortfolioList != null && stockInPortfolioList.size() > 0){
						int size = stockInPortfolioList.size();
						int rnd = (int)(Math.random() * size);
						StockInPortfolio stockInPortfolio = stockInPortfolioList.get(rnd);
						Stock stock = stockMgr.getStockById(stockInPortfolio.getStockId());
						portfolioMgr.sell(user, stock, (int)stockInPortfolio.getAmount());
					}
				}
				else{
					Stock stock = getRandomSuggestedStock();
					try {
						if(stock != null){
							portfolioMgr.buy(user, stock, stock.getAvailable());
						}
					} catch (TwitstreetException e) {
						e.printStackTrace();
					}
				}
				Thread.sleep(randomSleep());
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	private long randomSleep(){
		return ((long)(Math.random() * (MAX_SLEEP - MIN_SLEEP))) + MIN_SLEEP;
	}
	
	private Stock getRandomSuggestedStock(){
		reloadSuggestedStocks();
		int size = suggestedStocks.size();
		if(size > 0){
			int rnd = (int)(Math.random() * size);
			return suggestedStocks.get(rnd);
		}
		return null;
	}
	
	private void reloadAutoPlayers(){
		long elapsed = System.currentTimeMillis() - lastAutoPlayerLoadTime;
		if(elapsed > MAX_AUTOPLAYER_LOAD_DURATION){
			userMgr.detectAutoPlayers();
			autoplayerList = userMgr.getAllAutoPlayers();
			lastAutoPlayerLoadTime = System.currentTimeMillis();
		}
	}
	
	private void reloadSuggestedStocks(){
		long elapsed = System.currentTimeMillis() - lastSuggestedStockLoadTime;
		if(elapsed > MAX_SUGGESTED_STOCK_LOAD_DURATION){
			suggestedStocks = stockMgr.getSuggestedStocks(0, SUGGESTED_STOCK_LIMIT);
			lastSuggestedStockLoadTime = System.currentTimeMillis();
		}
	}
}
