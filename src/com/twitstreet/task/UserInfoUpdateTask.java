/**
	TwitStreet - Twitter Stock Market Game
    Copyright (C) 2012  Engin Guller (bisanth@gmail.com), Cagdas Ozek (cagdasozek@gmail.com)

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
import com.twitstreet.config.ConfigMgr;
import com.twitstreet.db.data.User;
import com.twitstreet.session.UserMgr;
import com.twitstreet.twitter.TwitterProxy;
import com.twitstreet.twitter.TwitterProxyFactory;
import com.twitstreet.twitter.TwitterProxyImpl;

public class UserInfoUpdateTask implements Runnable {
	private static final long ONE_DAY = 24 * 60 * 60 * 1000;
	@Inject
	TwitterProxyFactory twitterProxyFactory = null;
	@Inject
	ConfigMgr configMgr;
	@Inject
	UserMgr userMgr;
	private static Logger logger = Logger.getLogger(UserInfoUpdateTask.class);

	@Override
	public void run() {

		while (true) {
			long start = System.currentTimeMillis();

			try {
				userMgr.resetInvitation();
				List<User> userList = userMgr.getAll();
				ArrayList<Long> idList = new ArrayList<Long>();
				
				for (User user : userList) {
					idList.add(user.getId());				
				}
				User user = userMgr.random();
				TwitterProxy twitterProxy = twitterProxyFactory.create(user.getOauthToken(), user.getOauthTokenSecret());
				while (idList.size() > 0) {
					List<Long> subList = idList.subList(0,Math.min(TwitterProxyImpl.USER_COUNT_FOR_UPDATE,idList.size()));
					ArrayList<twitter4j.User> twitterUserList = twitterProxy.getTwUsers(new ArrayList<Long>(subList));
					for (twitter4j.User twitterUser : twitterUserList) {
						User updatedUser = new User();
						updatedUser.setId(twitterUser.getId());
						updatedUser.setUserName(twitterUser.getScreenName());
						updatedUser.setPictureUrl(twitterUser.getProfileImageURL().toExternalForm());
						updatedUser.setLocation(twitterUser.getLocation());
						updatedUser.setDescription(twitterUser.getDescription());
						updatedUser.setLongName(twitterUser.getName());
						updatedUser.setLanguage(twitterUser.getLang());
						userMgr.updateTwitterData(updatedUser);
					}
					subList.clear();
				}
				
			} catch (Throwable ex) {
				logger.error("Someone tried to kill our precious UserUpdateTask. He says: ", ex);
			}

			long elapsed = System.currentTimeMillis() - start;
			logger.info("UserInfoUpdateTask completed in " + elapsed / 1000 + " seconds");
			if (ONE_DAY - elapsed > 0) {
				try {
					Thread.sleep(ONE_DAY - elapsed);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}

		}
	}

}
