package com.twitstreet.task;

import java.util.List;

import org.apache.log4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.twitstreet.config.ConfigMgr;
import com.twitstreet.db.data.Stock;
import com.twitstreet.market.PortfolioMgr;
import com.twitstreet.market.StockMgr;
import com.twitstreet.session.UserMgr;
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
	public static int LAST_UPDATE_DIFF_MINUTES = 10;// minutes
	public static int LAST_UPDATE_DIFF_MILISECONDS = LAST_UPDATE_DIFF_MINUTES * 60 * 1000;

	@Override
	public void run() {

		while (true) {
			long startTime = System.currentTimeMillis();

			try {
				
				logger.debug("Twitter trends update - begin.");
				stockMgr.updateTwitterTrends();
				logger.debug("Twitter trends update - end.");

				logger.debug("Stock list update - begin.");
				updateStocks();
				logger.debug("Stock list update - end.");

				logger.debug("Reset speed of stocks - begin.");
				stockMgr.resetSpeedOfOldStocks();
				logger.debug("Reset speed of stocks - end.");

				logger.debug("Re-rank begin.");
				userMgr.rerank();
				logger.debug("Re-rank end.");

				logger.debug("Rank history update - begin.");
				userMgr.updateRankingHistory();
				logger.debug("Rank history update - end.");

			} catch (Throwable ex) {
				logger.error("Someone tried to kill our precious. He says: ", ex);
			}
			long endTime = System.currentTimeMillis();
			long diff = endTime - startTime;

			if (diff < LAST_UPDATE_DIFF_MILISECONDS) {
				try {
					Thread.sleep(LAST_UPDATE_DIFF_MILISECONDS - diff);
				} catch (InterruptedException e) {

					e.printStackTrace();
				}
			}
		}
	}

	public void updateStocks() {

		List<Stock> stockList = stockMgr.getUpdateRequiredStocks();

		for (Stock stock : stockList) {

			stockMgr.updateStockData(stock.getId());
		}

	}

}
