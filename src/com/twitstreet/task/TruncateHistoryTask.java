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

import com.google.inject.Inject;
import com.twitstreet.config.ConfigMgr;
import com.twitstreet.market.StockMgr;
import com.twitstreet.season.SeasonMgr;
import com.twitstreet.session.UserMgr;
import com.twitstreet.twitter.TwitterProxyFactory;

public class TruncateHistoryTask implements Runnable {
	
	private static long  ONE_HOUR =  60* 60 *1000;
	@Inject
	TwitterProxyFactory twitterProxyFactory = null;
	@Inject
	ConfigMgr configMgr;
	@Inject
	SeasonMgr seasonMgr;
	@Inject
	UserMgr userMgr;
	@Inject
	StockMgr stockMgr;
	private static Logger logger = Logger.getLogger(TruncateHistoryTask.class);

	@Override
	public void run() {
	
 
		while (true) {
			try {
				logger.info("********************    PERFORMING TRUNCATE HISTORY OPERATION    ********************");
			
				userMgr.truncateRankingHistory();
				stockMgr.truncateStockHistory();
				logger.info("********************      END OF TRUNCATE HISTORY OPERATION      ********************");
				
			
			} catch (Throwable ex) {
				logger.error("Someone tried to kill our precious TruncateHistoryTask. He says: ", ex);
			}
		
			
			try {
				Thread.sleep(ONE_HOUR);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

		}
	}

}
