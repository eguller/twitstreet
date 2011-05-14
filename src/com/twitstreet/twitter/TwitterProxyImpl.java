package com.twitstreet.twitter;

import java.util.Map;

import org.scribe.builder.ServiceBuilder;
import org.scribe.builder.api.TwitterApi;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Response;
import org.scribe.model.Token;
import org.scribe.model.Verb;
import org.scribe.model.Verifier;
import org.scribe.oauth.OAuthService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import twitter4j.User;
import twitter4j.conf.ConfigurationBuilder;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.twitstreet.base.Result;
import com.twitstreet.base.TSHttpUtils;

public class TwitterProxyImpl implements TwitterProxy {
	private static final String CONSUMER_KEY = "DfIgLzNr6zl8gcE4sVFFgQ";

	private static Logger logger = LoggerFactory.getLogger(TwitterProxyImpl.class);

	private final OAuthService service;

	private static final String baseUrl = "http://api.twitter.com";

	private final String consumerSecret;

	@Inject
	public TwitterProxyImpl(@Named("com.twitstreet.meta.ConsumerSecret") String consumerSecret) {

		this.consumerSecret = consumerSecret;

		service = new ServiceBuilder().provider(TwitterApi.class).apiKey(CONSUMER_KEY).callback(
				"http://twitstreet.com/callback").apiSecret(consumerSecret).build();
	}

	@Override
	public String[] getNewRequestTokenPair() {
		Token token = service.getRequestToken();
		return new String[] { token.getToken(), token.getSecret() };
	}

	@Override
	public String getAuthorizationUrl(String[] requestTokenPair) {
		Token token = new Token(requestTokenPair[0], requestTokenPair[1]);
		return service.getAuthorizationUrl(token);
	}

	@Override
	public String[] getAccessTokenPair(String[] requestTokenPair, String oauth_verifier) {
		Token token = new Token(requestTokenPair[0], requestTokenPair[1]);
		Verifier verifier = new Verifier(oauth_verifier);
		Token accessToken = service.getAccessToken(token, verifier);
		return new String[] { accessToken.getToken(), accessToken.getSecret() };
	}

	@Override
	public int getFollowerCount(String[] accessTokenPair, String screenName) {

		ConfigurationBuilder confbuilder = new ConfigurationBuilder();
		confbuilder.setOAuthAccessToken(accessTokenPair[0]).setOAuthAccessTokenSecret(accessTokenPair[1])
				.setOAuthConsumerKey(CONSUMER_KEY).setOAuthConsumerSecret(consumerSecret);

		Twitter twitter = new TwitterFactory(confbuilder.build()).getInstance();

		try {
			User user = twitter.showUser(screenName);
			return user.getFollowersCount();

		} catch (Exception e) {
			e.printStackTrace();
			return -1;
		}
	}

	@Override
	public Result<String[]> getAccessTokenWithBridge(String requestToken, String oauth_bridge_code) {
		Token token = new Token(requestToken, "");
		OAuthRequest request = new OAuthRequest(Verb.POST, "https://api.twitter.com/oauth/access_token");
		request.addBodyParameter("oauth_bridge_code", oauth_bridge_code);
		service.signRequest(token, request);
		Response response = request.send();
		logger.info("access token response({}) : {}", response.getCode(), response.getBody());

		if (response.getCode() == 200) {
			Map<String, String> parmMap = TSHttpUtils.parseQueryParms(response.getBody());
			String[] tokenPair = new String[] { parmMap.get("oauth_token"), parmMap.get("oauth_token_secret") };
			return Result.success(tokenPair);
		} else {
			return Result.fail(TwitterError.BridgeAuthFailed);
		}
	}

	@Override
	public String doGet(String[] accessTokenPair, String url) {
		OAuthRequest request = new OAuthRequest(Verb.GET, baseUrl + url);
		Token token = new Token(accessTokenPair[0], accessTokenPair[1]);
		service.signRequest(token, request);
		Response response = request.send();
		return response.getBody();
	}
}
