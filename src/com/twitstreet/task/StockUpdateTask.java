package com.twitstreet.task;

import java.util.List;

import org.apache.log4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.twitstreet.config.ConfigMgr;
import com.twitstreet.db.data.Stock;
import com.twitstreet.db.data.User;
import com.twitstreet.market.PortfolioMgr;
import com.twitstreet.market.StockMgr;
import com.twitstreet.session.UserMgr;
import com.twitstreet.twitter.TwitterProxy;
import com.twitstreet.twitter.TwitterProxyFactory;

@Singleton
public class StockUpdateTask implements Runnable {

	@Inject
	PortfolioMgr portfolioMgr;
	@Inject
	StockMgr stockMgr;
	@Inject
	ConfigMgr configMgr;
	@Inject
	UserMgr userMgr;
	@Inject
	TwitterProxyFactory twitterProxyFactory = null;
	private static Logger logger = Logger.getLogger(StockUpdateTask.class);
	public static final int LAST_UPDATE_DIFF = 10 * 60 * 1000;

	@Override
	public void run() {

		while (true) {
			long startTime = System.currentTimeMillis();
		

			logger.debug("witter trends update - begin.");
			stockMgr.updateTwitterTrends();
			logger.debug("Twitter trends update - end.");
			

			logger.debug("Stock list update - begin.");
			List<Stock> stockList = updateStocks();
			logger.debug("Stock list update - end. Number of stocks: "+ stockList.size());			
		
			logger.debug("Stock history update - begin.");
			stockMgr.updateStockHistory();
			logger.debug("Stock history update - end.");
			
			logger.debug("Re-rank begin.");
			userMgr.rerank();			
			logger.debug("Re-rank end.");
			
			logger.debug("Rank history update - begin.");
			userMgr.updateRankingHistory();			
			logger.debug("Rank history update - end.");
			

			long endTime = System.currentTimeMillis();
			long diff = endTime - startTime;

			if (diff < LAST_UPDATE_DIFF) {
				try {
					Thread.sleep(LAST_UPDATE_DIFF - diff);
				} catch (InterruptedException e) {

					e.printStackTrace();
				}
			}
		}
	}
	
	public List<Stock> updateStocks(){
		
		List<Stock> stockList = stockMgr.getUpdateRequiredStocks();

		for (Stock stock : stockList) {
			User user = userMgr.random();
			TwitterProxy twitterProxy = user == null ? null : twitterProxyFactory.create(user.getOauthToken(), user.getOauthTokenSecret());
			twitter4j.User twUser = null;

			try {
				twUser = twitterProxy.getTwUser(stock.getId());
			} catch (Exception ex) {
			}
			if (twUser != null) {
				stockMgr.updateTwitterData(stock.getId(), twUser.getFollowersCount(), twUser.getProfileImageURL().toExternalForm(), twUser.getScreenName(), twUser.isVerified());
			}

		}	
		
		return stockList;
		
		
	}

}
