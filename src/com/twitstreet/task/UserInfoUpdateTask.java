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
