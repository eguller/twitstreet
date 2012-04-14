package com.twitstreet.task;

import java.util.List;

import com.google.inject.Inject;
import com.twitstreet.db.data.User;
import com.twitstreet.localization.LocalizationUtil;
import com.twitstreet.session.UserMgr;
import com.twitstreet.twitter.AnnouncerMgr;
import com.twitstreet.util.Util;

public class NewSeasonInfoSentTask implements Runnable {
	private static final int SEASONINFO_SIZE = 3;
	private static final long ONEHOUR = 1 * 60 * 60 * 1000;
	private static final int SIZE = 1;
	LocalizationUtil lutil = LocalizationUtil.getInstance();
	@Inject UserMgr userMgr;
	@Inject AnnouncerMgr announcerMgr;
	
	@Override
	public void run() {
		while(true){
			
			List<User> userList = userMgr.getNewSeasonInfoNotSentUsers(SIZE);
			userMgr.setNewSeasonInfoSent(userList);
			for(User user : userList){
				int seasonInfoMessageId = (int)( Math.random() * SEASONINFO_SIZE );
				String message = lutil.get("seasoninfo." + seasonInfoMessageId, user.getLanguage());
				announcerMgr.announceFromAnnouncer(Util.mentionMessage(user.getUserName(), message));
			}
			try {
				Thread.sleep(ONEHOUR);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

}
