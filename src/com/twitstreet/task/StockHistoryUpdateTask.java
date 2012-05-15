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

import org.apache.log4j.Logger;

import twitter4j.Twitter;

import com.google.inject.Inject;
import com.twitstreet.market.StockMgr;

public class StockHistoryUpdateTask implements Runnable {
	private static final long HOUR = 60 * 60 * 1000;
	private static final long MIN = 60 * 1000;
	private static final long INITIAL_DELAY = 2 * MIN;
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
		sleep(INITIAL_DELAY);
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
