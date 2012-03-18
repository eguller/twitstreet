package com.twitstreet.task;

import java.util.List;

import org.apache.log4j.Logger;

import com.google.inject.Inject;
import com.twitstreet.config.ConfigMgr;
import com.twitstreet.db.data.User;
import com.twitstreet.session.UserMgr;
import com.twitstreet.twitter.TwitterProxy;
import com.twitstreet.twitter.TwitterProxyFactory;

public class EndGameTask implements Runnable {
	private static final long ONE_HOUR =60 * 60 * 1000;
	
	@Inject
	TwitterProxyFactory twitterProxyFactory = null;
	@Inject
	ConfigMgr configMgr;
	@Inject
	UserMgr userMgr;
	private static Logger logger = Logger.getLogger(EndGameTask.class);

	@Override
	public void run() {

		while (true) {
			int nextSleep = 0;
			int  currentSleep = 0;
			long start = System.currentTimeMillis();

			try {
			
				
			} catch (Throwable ex) {
				logger.error("Someone tried to kill our precious EndGameTask. He says: ", ex);
			}

			long elapsed = System.currentTimeMillis() - start;
			logger.info("EndGameTask completed in " + elapsed / 1000 + " seconds");
			if (ONE_HOUR - elapsed > 0) {
				try {
					Thread.sleep(ONE_HOUR  - elapsed);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}

		}
	}

}
