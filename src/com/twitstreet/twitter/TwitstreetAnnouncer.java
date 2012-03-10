package com.twitstreet.twitter;

import com.twitstreet.db.data.Stock;



public interface TwitstreetAnnouncer {
	
	
	
	public boolean mention(Stock user, String message);

	public void removeOldRecords(int removeOlderThanMinutes);
	
}
