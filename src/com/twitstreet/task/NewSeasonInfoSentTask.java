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

import java.util.List;

import org.apache.log4j.Logger;

import com.google.inject.Inject;
import com.twitstreet.db.data.User;
import com.twitstreet.localization.LocalizationUtil;
import com.twitstreet.session.UserMgr;
import com.twitstreet.twitter.AnnouncerMgr;
import com.twitstreet.util.Util;

public class NewSeasonInfoSentTask implements Runnable {
	private static Logger logger = Logger.getLogger(NewSeasonInfoSentTask.class);
	private static final int SEASONINFO_SIZE = 3;
	private static final long FIFTEEN_MINUTE = 15 * 60 * 1000;
	private static final int SIZE = 1;
	LocalizationUtil lutil = LocalizationUtil.getInstance();
	@Inject UserMgr userMgr;
	@Inject AnnouncerMgr announcerMgr;
	
	@Override
	public void run() {
		while(true){
			List<User> userList = userMgr.getNewSeasonInfoNotSentUsers(SIZE);
			userMgr.setNewSeasonInfoSent(userList);
			logger.info("New season info sent task started. User count : " + userList.size());
			for(User user : userList){
				int seasonInfoMessageId = (int)( Math.random() * SEASONINFO_SIZE );
				String message = lutil.get("seasoninfo." + seasonInfoMessageId, user.getLanguage());
				announcerMgr.announceFromAnnouncer(Util.mentionMessage(user.getUserName(), message));
			}
			try {
				Thread.sleep(FIFTEEN_MINUTE);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			logger.info("New season info sent task completed");
		}
	}

}
