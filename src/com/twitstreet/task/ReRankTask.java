package com.twitstreet.task;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.twitstreet.session.UserMgr;

@Singleton
public class ReRankTask implements Runnable {
	public static final int RERANK_INTERVAL = 5;
	//Re-rank in every 5 minute
	private static final int interval = RERANK_INTERVAL * 60 * 1000;
	
	@Inject UserMgr userMgr;
	@Override
	public void run() {
		while(true){
			userMgr.rerank();
			try {
				Thread.sleep(interval);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

}
