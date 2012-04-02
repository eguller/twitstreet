package com.twitstreet.task;

import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import com.google.inject.Inject;
import com.twitstreet.config.ConfigMgr;
import com.twitstreet.db.data.User;
import com.twitstreet.session.SeasonMgr;
import com.twitstreet.session.UserMgr;
import com.twitstreet.twitter.TwitterProxy;
import com.twitstreet.twitter.TwitterProxyFactory;

public class SeasonTask implements Runnable {
	
	//operation starts 1 minute before the start date of next season
	
	
	@Inject
	TwitterProxyFactory twitterProxyFactory = null;
	@Inject
	ConfigMgr configMgr;
	@Inject
	SeasonMgr seasonMgr;
	@Inject
	UserMgr userMgr;
	private static Logger logger = Logger.getLogger(SeasonTask.class);

	@Override
	public void run() {

		while (true) {
			long start = System.currentTimeMillis();
			
			seasonMgr.loadSeasonInfo();
			
			Date nowDate = new Date();
			long now = nowDate.getTime();
			long endTime = seasonMgr.getCurrentSeason().getEndTime().getTime();
			long diff = endTime - now;
			try {
				if(diff>0 && diff <SeasonMgr.bufferToStart){
					seasonMgr.newSeason();
					continue;
				}
			} catch (Throwable ex) {
				logger.error("Someone tried to kill our precious SeasonTask. He says: ", ex);
			}

			long sleep = 10 * 1000;
			
			if(diff>0 && diff<2 * SeasonMgr.bufferToStart){
				sleep = diff-SeasonMgr.bufferToStart;
			}else if(diff>0 && diff>2 * SeasonMgr.bufferToStart){
				sleep = diff/2;
			}
			long elapsed = System.currentTimeMillis() - start;

			logger.info("Time remaining to end season: "+diff/60000+"mins");
			logger.info("Sleep time: "+sleep/60000+"secs");
			
			logger.info("SeasonTask completed in " + elapsed / 1000 + " seconds");
			if (sleep> 0) {
				try {
					Thread.sleep(sleep);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}

		}
	}

}
