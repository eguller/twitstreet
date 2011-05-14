package com.twitstreet.twitter;

import org.scribe.builder.ServiceBuilder;
import org.scribe.builder.api.TwitterApi;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Response;
import org.scribe.model.Token;
import org.scribe.model.Verb;
import org.scribe.model.Verifier;
import org.scribe.oauth.OAuthService;

import com.google.inject.Inject;
import com.google.inject.name.Named;

public class TwitterProxyImpl implements TwitterProxy {

	private final OAuthService service;

	private static final String baseUrl = "http://api.twitter.com";

	@Inject
	public TwitterProxyImpl(
			@Named("com.twitstreet.meta.ConsumerSecret") String consumerSecret) {

		service = new ServiceBuilder().provider(TwitterApi.class).apiKey(
				"DfIgLzNr6zl8gcE4sVFFgQ").callback(
				"http://twitstreet.com/callback").apiSecret(consumerSecret)
				.build();
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
	public String[] getAccessTokenPair(String[] requestTokenPair,
			String oauth_verifier) {
		Token token = new Token(requestTokenPair[0], requestTokenPair[1]);
		Verifier verifier = new Verifier(oauth_verifier);
		Token accessToken = service.getAccessToken(token, verifier);
		return new String[] { accessToken.getToken(), accessToken.getSecret() };
	}

	@Override
	public int getFollowerCount(String userId) {
		return 0;
	}
	
	public String[] getAccessTokenWithBridge(String oauth_bridge_code) {
		Token token = new Token("0/bdAAAAAAC/bchNAAAAAPilDAAAAAAAnHSUTrCTvXA/umTqGHQhya9yzS4=tnvmhTtsvU0Q6fUCyomNtEFl86cKtQx9GQLhqwL6I","");//service.getRequestToken();
		OAuthRequest request = new OAuthRequest(Verb.POST, "https://api.twitter.com/oauth/access_token");
		request.addBodyParameter("oauth_bridge_code", oauth_bridge_code);
		service.signRequest(token, request);
		Response response = request.send();
		return new String[]{ String.valueOf(response.getCode()), response.getBody() };
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
