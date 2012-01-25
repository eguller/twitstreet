package com.twitstreet.task;

import org.apache.log4j.Logger;

import twitter4j.Twitter;

import com.google.inject.Inject;
import com.twitstreet.market.StockMgr;

public class StockHistoryUpdateTask implements Runnable {

	private static final long HOUR = 60 * 60 * 1000;

	@Inject
	StockMgr stockMgr;

	private static Logger logger = Logger.getLogger(StockHistoryUpdateTask.class);
	Twitter twitter = null;

	@Override
	public void run() {
		while (true) {
			long startTime = System.currentTimeMillis();

			try {
				stockMgr.updateStockHistory();
			} catch (Exception ex) {
				logger.error("Error in stockMgr.updateStockHistory", ex);
			}

			long endTime = System.currentTimeMillis();
			long diff = endTime - startTime;

			if (diff < HOUR) {
				try {
					Thread.sleep(HOUR - diff);
				} catch (InterruptedException e) {

					e.printStackTrace();
				}
			}
		}
	}
}
