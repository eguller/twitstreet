package com.twitstreet.twitter;

import java.security.MessageDigest;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;
import twitter4j.conf.ConfigurationBuilder;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import com.twitstreet.base.Result;

@Singleton
public class TwitterAuthImpl implements TwitterAuth {

	private static Logger logger = LoggerFactory
			.getLogger(TwitterAuthImpl.class);

	private final String consumerSecret;

	private final TwitterFactory twitterFactory;
	
	@Inject
	@Named("twitter.oauth.request")
	private final Map<String,String> requestCache = null;

	@Inject
	public TwitterAuthImpl(
			@Named("com.twitstreet.meta.ConsumerKey") String consumerKey,
			@Named("com.twitstreet.meta.ConsumerSecret") String consumerSecret) {

		ConfigurationBuilder confbuilder = new ConfigurationBuilder();
		confbuilder.setOAuthConsumerKey(consumerKey).setOAuthConsumerSecret(
				consumerSecret);
		twitterFactory = new TwitterFactory(confbuilder.build());

		this.consumerSecret = consumerSecret;
	}

	@Override
	public Result<String> getUserIdFromTACookie(String value) {
		// parse cookie "user_id:signature"
		int idx = value.indexOf(':');
		if (idx > 0) {
			String userid = value.substring(0, idx);
			String signature = value.substring(idx + 1, value.length());
			try {
				// SHA1.hexdigest(user_id + consumer_secret)
				MessageDigest md = MessageDigest.getInstance("SHA-1");
				md.update(userid.getBytes());
				md.update(consumerSecret.getBytes());
				byte[] digest = md.digest();

				StringBuilder hexdigest = new StringBuilder();
				for (int i = 0; i < digest.length; i++) {
					int digByte = digest[i] & 0xFF;
					if (digByte < 0x10) {
						hexdigest.append('0');
					}
					hexdigest.append(Integer.toHexString(digByte));
				}

				if (signature.equals(hexdigest.toString())) {
					return Result.success(userid);

				} else {
					return Result.fail(TwitterError.CookieCheckFailed);
				}

			} catch (Exception e) {
				return Result.fail(e);
			}

		} else {
			return Result.fail(TwitterError.CookieCheckFailed);
		}

	}

	/*
	 * Methods for getting OAuth 1.0a Access Token (REST API)
	 */
	@Override
	public Result<TwitterAccessData> getAccess(String requestTokenStr,
			String requestVerifier) {
		try {
			String requestTokenSecret = requestCache.remove(requestTokenStr);
			if(requestTokenSecret==null) {
				return Result.fail(TwitterError.Timeout);				
			}
			RequestToken requestToken = new RequestToken(requestTokenStr,
					requestTokenSecret);
			AccessToken accessToken = twitterFactory.getInstance()
					.getOAuthAccessToken(requestToken, requestVerifier);
			return Result.success(new TwitterAccessData(accessToken.getToken(),
					accessToken.getTokenSecret(), accessToken.getUserId(),
					accessToken.getScreenName()));

		} catch (Exception e) {
			return Result.fail(e);
		}
	}

	@Override
	public Result<String> getAuthenticationUrl(String callbackURL) {
		try {
			RequestToken token = twitterFactory.getInstance()
					.getOAuthRequestToken(callbackURL);
			requestCache.put(token.getToken(), token.getTokenSecret() );
			return Result.success(token.getAuthenticationURL());

		} catch (Exception e) {
			logger.info("OAuth failed", e);
			return Result.fail(e);
		}
	}

}
