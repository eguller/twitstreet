package com.twitstreet.twitter;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Properties;

import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;

/**
 * @author eguller
 *
 */
public class TwitterConnectionImpl implements TwitterConnection {
	ArrayList<Twitter> twitterFeedList = new ArrayList<Twitter>();

	@Override
	public void init() {
		loadTwitterFeedAccounts();
	}

	/* (non-Javadoc)
	 * @see com.twitstreet.twitter.TwitterConnection#getTwitter()
	 */
	@Override
	public Twitter getTwitter() {
		return twitterFeedList.get((int) (twitterFeedList.size() * Math.random()));
	}

	/**
	 * Read $HOME/.twitstreet/app.properties file
	 *
	 */
	private void loadTwitterFeedAccounts() {
		String propFile = System.getProperty("user.home")
				+ "/.twitstreet/app.properties";
		Properties props = new Properties();

		try {
			props.load(new FileReader(propFile));
			createTwitterFeeds(props);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Read token, tokenSecret, consumerKey and consumerSecret values
	 * from properties and creates a new Twitter object for each key.
	 * @param props - properties file to load token, consumerKeys etc.
	 */
	private void createTwitterFeeds(Properties props) {
		for (int i = 1; props.getProperty("twitstreet" + i) != null; i++) {

			String token = props.getProperty("twitstreet" + i + ".token");
			String tokenSecret = props.getProperty("twitstreet" + i
					+ ".tokenSecret");
			String consumerKey = props.getProperty("twitstreet" + i
					+ ".consumerKey");
			String consumerSecret = props.getProperty("twitstreet" + i
					+ ".consumerSecret");


			ConfigurationBuilder confbuilder = new ConfigurationBuilder();
			confbuilder.setOAuthAccessToken(token)
					.setOAuthAccessTokenSecret(tokenSecret)
					.setOAuthConsumerKey(consumerKey)
					.setOAuthConsumerSecret(consumerSecret);
			Twitter twitter = new TwitterFactory(confbuilder.build())
					.getInstance();

			twitterFeedList.add(twitter);
		}
	}

}
