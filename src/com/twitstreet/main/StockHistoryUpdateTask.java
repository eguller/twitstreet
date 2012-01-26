package com.twitstreet.main;

import org.apache.log4j.Logger;

import twitter4j.Twitter;

import com.google.inject.Inject;
import com.twitstreet.market.StockMgr;

public class StockHistoryUpdateTask implements Runnable {
	private static final long HOUR = 60 * 60 * 1000;
	private static final long MIN = 60 * 1000;
	private static final long INITIAL_DELAY = 30 * MIN;
	private static final long PERIOD = 6 * HOUR;
	
	@Inject
	StockMgr stockMgr;

	private static Logger logger = Logger.getLogger(StockUpdateTask.class);
	Twitter twitter = null;

	public void sleep(long millisecs){
		try {
			Thread.sleep(millisecs);
		} catch (InterruptedException e) {
			logger.error("Thread.sleep error in class: "+this.getClass(), e);
		}
	}

	@Override
	public void run() {

		//sleep(INITIAL_DELAY);
		
		while (true) {

			long startTime = System.currentTimeMillis();

			try {
				stockMgr.updateStockHistory();
			} catch (Exception ex) {
				logger.error("Error in stockMgr.updateStockHistory", ex);
			}

			long endTime = System.currentTimeMillis();
			long diff = endTime - startTime;

			if (diff < PERIOD) {
				sleep(PERIOD - diff);
			}
		}
	}
}
