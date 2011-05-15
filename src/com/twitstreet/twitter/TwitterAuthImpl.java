package com.twitstreet.twitter;

import java.security.MessageDigest;
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

import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.twitstreet.base.Result;
import com.twitstreet.base.TSHttpUtils;

public class TwitterAuthImpl implements TwitterAuth {

	private static Logger logger = LoggerFactory.getLogger(TwitterAuthImpl.class);

	private final String consumerSecret;

	private final OAuthService service;

	@Inject
	public TwitterAuthImpl(@Named("com.twitstreet.meta.ConsumerKey") String consumerKey,
			@Named("com.twitstreet.meta.ConsumerSecret") String consumerSecret) {

		this.consumerSecret = consumerSecret;

		this.service = new ServiceBuilder().provider(TwitterApi.class).apiKey(consumerKey).callback(
				"http://twitstreet.com/callback").apiSecret(consumerSecret).build();
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

	@Override
	public Result<TwitterAccessData> getAccessDataWithBridge(String accessToken, String oauth_bridge_code) {
		Token token = new Token(accessToken, "");
		OAuthRequest request = new OAuthRequest(Verb.POST, "https://api.twitter.com/oauth/access_token");
		request.addBodyParameter("oauth_bridge_code", oauth_bridge_code);
		service.signRequest(token, request);
		Response response = request.send();
		logger.info("access token response({}) : {}", response.getCode(), response.getBody());

		if (response.getCode() == 200) {
			Map<String, String> parmMap = TSHttpUtils.parseQueryParms(response.getBody());
			String oauth_token = parmMap.get("oauth_token");
			String oauth_token_secret = parmMap.get("oauth_token_secret");
			String userId = parmMap.get("user_id");
			String screenName = parmMap.get("screen_name");
			
			if(oauth_token!=null && oauth_token_secret!=null && userId!=null && screenName!=null) {
				return Result.success( new TwitterAccessData(oauth_token, oauth_token_secret, userId, screenName) );
			}
			// else: fall through to fail result.

		}
		// else: fall through to fail result.
		
		return Result.fail(TwitterError.BridgeAuthFailed);
	}

	/*
	 * Methods for getting OAuth 1.0a Access Token (REST API)
	 */
	public String[] getNewRequestTokenPair() {
		Token token = service.getRequestToken();
		return new String[] { token.getToken(), token.getSecret() };
	}

	public String getAuthorizationUrl(String[] requestTokenPair) {
		Token token = new Token(requestTokenPair[0], requestTokenPair[1]);
		return service.getAuthorizationUrl(token);
	}

	public String[] getAccessTokenPair(String[] requestTokenPair, String oauth_verifier) {
		Token token = new Token(requestTokenPair[0], requestTokenPair[1]);
		Verifier verifier = new Verifier(oauth_verifier);
		Token accessToken = service.getAccessToken(token, verifier);
		return new String[] { accessToken.getToken(), accessToken.getSecret() };
	}

}
