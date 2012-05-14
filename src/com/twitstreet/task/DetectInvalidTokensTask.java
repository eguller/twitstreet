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
import com.twitstreet.config.ConfigMgr;
import com.twitstreet.db.data.User;
import com.twitstreet.session.UserMgr;
import com.twitstreet.twitter.TwitterProxy;
import com.twitstreet.twitter.TwitterProxyFactory;

public class DetectInvalidTokensTask implements Runnable {
	private static final long ONE_HOUR =60 * 60 * 1000;
	
	@Inject
	TwitterProxyFactory twitterProxyFactory = null;
	@Inject
	ConfigMgr configMgr;
	@Inject
	UserMgr userMgr;
	private static Logger logger = Logger.getLogger(DetectInvalidTokensTask.class);

	@Override
	public void run() {

		while (true) {
			long start = System.currentTimeMillis();

			try {
				List<User> userList = userMgr.getAllActive();
				logger.info("*****************DetectInvalidTokensTask*****************" );
				int i = 0;
				for (User user : userList) {
					TwitterProxy twitterProxy = twitterProxyFactory.create(user.getOauthToken(), user.getOauthTokenSecret());
					
					if(!twitterProxy.verifyUser()){
						userMgr.deleteUser(user.getId());
						i++;
					}
					
				}
				logger.info("Inactivated "+i+" users.");
			} catch (Throwable ex) {
				logger.error("Someone tried to kill our precious DetectInvalidTokensTask. He says: ", ex);
			}

			long elapsed = System.currentTimeMillis() - start;
			logger.info("DetectInvalidTokensTask completed in " + elapsed / 1000 + " seconds");
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
