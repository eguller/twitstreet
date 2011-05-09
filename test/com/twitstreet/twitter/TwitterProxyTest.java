package com.twitstreet.twitter;

import static org.junit.Assert.*;

import java.io.FileReader;
import java.util.Properties;
import java.util.Map.Entry;

import org.apache.http.client.methods.HttpGet;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.scribe.builder.ServiceBuilder;
import org.scribe.builder.api.TwitterApi;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Response;
import org.scribe.model.Verb;
import org.scribe.oauth.OAuthService;

import com.google.inject.AbstractModule;
import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.google.inject.name.Names;

import atunit.AtUnit;
import atunit.Container;
import atunit.MockFramework;
import atunit.Unit;

@RunWith(AtUnit.class)
@MockFramework(MockFramework.Option.EASYMOCK)
@Container(Container.Option.GUICE)
public class TwitterProxyTest extends AbstractModule {

	@Inject
	@Unit
	TwitterProxyImpl twitterProxy;

	OAuthService service;
	
	@Inject
	@Named("com.twitstreet.meta.ConsumerSecret")
	private final String consumerSecret = null;
	
	@Before
	public void setUp() throws Exception {
		service = new ServiceBuilder()
        .provider(TwitterApi.class)
        .apiKey("DfIgLzNr6zl8gcE4sVFFgQ")
        .apiSecret(consumerSecret)
        .build();
	}
	
	@Test
	public void testGetFollowerCount() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetAccessTokenPair() {
		OAuthRequest request = new OAuthRequest(Verb.POST, "https://api.twitter.com/oauth/access_token");
		request.addOAuthParameter("oauth_bridge_code", "afbry8OZ8xNA8yPPPGU92qdr3lAKFM5q3VtcVGAYo");
		service.signRequest(null, request); // the access token from step 4
		Response response = request.send();
		System.out.println(response.getBody());
	}

	@Override
	protected void configure() {
		bindPropertiesFile(System.getProperty("user.home")+"/.twitstreet/app.properties");
	}
	
	private void bindPropertiesFile(String propFileName) {
		try {
			Properties props = new Properties();
			props.load(new FileReader(propFileName));
			for(Entry<Object, Object> entry:props.entrySet()) {
				String name = (String) entry.getKey();
				String value = (String) entry.getValue();
				bindConstant().annotatedWith(Names.named(name)).to(value);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
