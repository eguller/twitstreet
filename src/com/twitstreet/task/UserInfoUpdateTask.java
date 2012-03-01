package com.twitstreet.task;

import java.beans.ConstructorProperties;
import java.util.List;

import org.apache.log4j.Logger;

import com.google.inject.Inject;
import com.twitstreet.config.ConfigMgr;
import com.twitstreet.db.data.User;
import com.twitstreet.session.UserMgr;
import com.twitstreet.session.UserMgrImpl;
import com.twitstreet.twitter.TwitterProxy;
import com.twitstreet.twitter.TwitterProxyFactory;

public class UserInfoUpdateTask implements Runnable {
	private static final long ONE_DAY = 24 * 60 * 60 * 1000;
	@Inject
	TwitterProxyFactory twitterProxyFactory = null;
	@Inject
	ConfigMgr configMgr;
	@Inject
	UserMgr userMgr;
	private static Logger logger = Logger.getLogger(UserMgrImpl.class);

	@Override
	public void run() {

		while (true) {
			long start = System.currentTimeMillis();
			List<User> userList = userMgr.getUserListByServerId(configMgr
					.getServerId());
			for (User user : userList) {
				TwitterProxy twitterProxy = twitterProxyFactory.create(
						user.getOauthToken(), user.getOauthTokenSecret());
				twitter4j.User twitterUser = twitterProxy.getTwUser(user
						.getId());
				if(twitterUser != null){
					System.out.println(user.getUserName() + ", " + twitterUser.getScreenName() + ", " + twitterUser.getProfileImageURL().toExternalForm());
					user.setUserName(twitterUser.getScreenName());
					user.setPictureUrl(twitterUser.getProfileImageURL()
						.toExternalForm());
					userMgr.updateTwitterData(user);
				}
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
