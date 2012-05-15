/**
	TwitStreet - Twitter Stock Market Game
    Copyright (C) 2012  Engin Guller (bisanthe@gmail.com), Cagdas Ozek (cagdasozek@gmail.com)

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
 **/

package com.twitstreet.task;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.twitstreet.config.ConfigMgr;
import com.twitstreet.db.data.Stock;
import com.twitstreet.db.data.TrendyStock;
import com.twitstreet.market.PortfolioMgr;
import com.twitstreet.market.StockMgr;
import com.twitstreet.season.SeasonMgr;
import com.twitstreet.session.UserMgr;
import com.twitstreet.twitter.AnnouncerMgr;
import com.twitstreet.twitter.TwitterProxyFactory;
import com.twitstreet.twitter.TwitterProxyImpl;
import com.twitstreet.util.Util;

@Singleton
public class StockUpdateTask implements Runnable {

	@Inject
	PortfolioMgr portfolioMgr;
	@Inject
	StockMgr stockMgr;
	@Inject
	ConfigMgr configMgr;
	@Inject
	SeasonMgr seasonMgr;
	@Inject
	UserMgr userMgr;
	@Inject
	TwitterProxyFactory twitterProxyFactory = null;
	@Inject
	AnnouncerMgr announcerMgr;
	private static Logger logger = Logger.getLogger(StockUpdateTask.class);
	public static int LAST_UPDATE_DIFF_MINUTES = 10;// minutes
	public static int LAST_UPDATE_DIFF_MILISECONDS = LAST_UPDATE_DIFF_MINUTES * 60 * 1000;

	@Override
	public void run() {

		int counter = 0;
		while (true) {
			long startTime = System.currentTimeMillis();

			try {
				seasonMgr.loadSeasonInfo();
				
				logger.info("\n\n************* Stock Update Task - Begin ****************\n\n");

				logger.info("Twitter trends update - begin.");
				stockMgr.updateTwitterTrends();
				logger.info("Twitter trends update - end.");

				logger.info("Stock list update - begin.");
				updateStocks();
				logger.info("Stock list update - end. ");
				
				userMgr.applyLoanInterest();

				logger.info("Reset speed of stocks - begin.");
				stockMgr.resetSpeedOfOldStocks();
				logger.info("Reset speed of stocks - end.");

				logger.info("Load suggested stocks - begin.");
				stockMgr.loadSuggestedStocks();
				logger.info("Load suggested stocks - end.");

				
				
				
				logger.info("Re-rank begin.");
				userMgr.rerank();
				logger.info("Re-rank end.");

				logger.info("Rank history update - begin.");
				userMgr.updateRankingHistory(false);
				logger.info("Rank history update - end. ");

				logger.info("Mention trendy stock - begin.");
				mentionTopGrossingStocks();
				logger.info("Mention trendy stock - end. ");

				logger.info("Remove old records - begin.");
				stockMgr.removeOldRecords(60 * 24);
				logger.info("Remove old records - end.");

				counter++;
				logger.info("\n\n******************** Stock Update Task - End ****************************\n\n");
			} catch (Throwable ex) {
				logger.error(
						"Someone tried to kill our precious StockUpdateTask. He says: ",
						ex);
			}

			long endTime = System.currentTimeMillis();
			long diff = endTime - startTime;

			if (diff < LAST_UPDATE_DIFF_MILISECONDS / 2) {
				try {
					Thread.sleep(LAST_UPDATE_DIFF_MILISECONDS / 2 - diff);
				} catch (InterruptedException e) {

					e.printStackTrace();
				}
			}
		}
	}

	private void mentionTopGrossingStocks() {
		if (!configMgr.isDev() && stockMgr.getTopGrossedStocks(24).size() > 0  ) {
			TrendyStock ts = stockMgr.getTopGrossedStocks(24).get(0);
			if (stockMgr.addStockIntoAnnouncement(ts.getId())) {
				try {
					announcerMgr.announceFromAnnouncer(Util.mentionMessage(
							ts.getName(),
							ts.getAnnouncement(ts.getLanguage())
									+ "www.twitstreet.com/#!stock="
									+ ts.getId()));
				} catch (Exception ex) {
					logger.error(
							"Cannot mention top grossing stock: "
									+ ts.getName(), ex);
				}
			}
		}

	}

	public void updateStocks() {
		List<Stock> updatedStocks = new ArrayList<Stock>();
		while (true) {
			List<Stock> stockList = stockMgr
					.getUpdateRequiredStocks(TwitterProxyImpl.IDS_SIZE);
			if (stockList.size() == 0)
				break;
			ArrayList<Long> idList = new ArrayList<Long>();
			for (Stock stock : stockList) {
				idList.add(stock.getId());
			}
			stockMgr.updateStockListData(idList);
			updatedStocks.addAll(stockList);
		}
		logger.info("Updated " + updatedStocks.size() + " stocks. \n");
		for (Stock stock : updatedStocks) {
			logger.debug(stock.toString() + "\n");
		}

	}

}
