package com.twitstreet.task;

import java.util.List;

import org.apache.log4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.twitstreet.config.ConfigMgr;
import com.twitstreet.db.data.Stock;
import com.twitstreet.db.data.TrendyStock;
import com.twitstreet.market.PortfolioMgr;
import com.twitstreet.market.StockMgr;
import com.twitstreet.session.UserMgr;
import com.twitstreet.twitter.TwitstreetAnnouncer;
import com.twitstreet.twitter.TwitterProxyFactory;

@Singleton
public class StockUpdateTask implements Runnable {

	@Inject
	PortfolioMgr portfolioMgr;
	@Inject
	TwitstreetAnnouncer twitstreetAnnouncer;
	@Inject
	StockMgr stockMgr;
	@Inject
	ConfigMgr configMgr;
	@Inject
	UserMgr userMgr;
	@Inject
	TwitterProxyFactory twitterProxyFactory = null;
	private static Logger logger = Logger.getLogger(StockUpdateTask.class);
	public static int LAST_UPDATE_DIFF_MINUTES = 10;// minutes
	public static int LAST_UPDATE_DIFF_MILISECONDS = LAST_UPDATE_DIFF_MINUTES * 60 * 1000;

	@Override
	public void run() {

		while (true) {
			long startTime = System.currentTimeMillis();

			try {
				
				
				logger.info("Twitter trends update - begin.");
				stockMgr.updateTwitterTrends();
				logger.info("Twitter trends update - end.");

				logger.info("Stock list update - begin.");
				List<Stock> stockList = updateStocks();
				String users = "";
				if (stockList != null) {
					users ="Updated "+stockList.size()+ " users. \n";
					for (Stock stock : stockList) {

						users = users + stock.getName() + "\n";

					}
				}else{
					users = "No stock found to update";
					
				}
				logger.info("Stock list update - end. "+users);

				logger.info("Reset speed of stocks - begin.");
				stockMgr.resetSpeedOfOldStocks();
				logger.info("Reset speed of stocks - end.");

				logger.info("Re-rank begin.");
				userMgr.rerank();
				logger.info("Re-rank end.");

				logger.info("Rank history update - begin.");
				userMgr.updateRankingHistory();
				logger.info("Rank history update - end. "); 
				
				try{
					TrendyStock ts = stockMgr.getTopGrossingStocks(2).get(0);
					twitstreetAnnouncer.mention(ts, ts.getAnnouncement(ts.getLanguage()));
					twitstreetAnnouncer.mention(ts,ts.getAnnouncementStockDetail(ts.getLanguage())+ " http://www.twitstreet.com/#stock-"+String.valueOf(ts.getId()));
					
				}
				catch(Exception ex){
					
				}
			

			} catch (Throwable ex) {
				logger.error("Someone tried to kill our precious. He says: ", ex);
			}
			
			twitstreetAnnouncer.removeOldRecords(60 * 24);
			
			
			
			long endTime = System.currentTimeMillis();
			long diff = endTime - startTime;

			if (diff < LAST_UPDATE_DIFF_MILISECONDS/2) {
				try {
					Thread.sleep(LAST_UPDATE_DIFF_MILISECONDS/2 - diff);
				} catch (InterruptedException e) {

					e.printStackTrace();
				}
			}
		}
	}

	public List<Stock> updateStocks() {

		List<Stock> stockList = stockMgr.getUpdateRequiredStocksByServer();

		for (Stock stock : stockList) {

			stockMgr.updateStockData(stock.getId());
		}
		return stockList;
	}

}
