package com.twitstreet.twitter;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.util.Properties;
import java.util.Map.Entry;

import org.junit.Test;
import org.junit.runner.RunWith;

import atunit.AtUnit;
import atunit.Container;
import atunit.MockFramework;
import atunit.Unit;

import com.google.inject.AbstractModule;
import com.google.inject.Inject;
import com.google.inject.name.Names;
import com.twitstreet.base.Result;

@RunWith(AtUnit.class)
@MockFramework(MockFramework.Option.EASYMOCK)
@Container(Container.Option.GUICE)
public class TwitterAuthTest extends AbstractModule {

	@Inject
	@Unit
	private TwitterAuthImpl twitterAuth;

	// @Test
	public void testGetFollowerCount() {
		fail("Not yet implemented");
	}

	@Test
	public void authorize() throws Exception {
		String authorizationUrl = twitterAuth.getAuthenticationUrl("http://twitstreet.com/test").getPayload();
		System.out.println("Make sure you are signed out from twitter");
		System.out.println("go to: " + authorizationUrl + " and Approve twitstreet App.");
		
		System.out.println("What is the oauth_token in url?");
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String requestToken = br.readLine();
		
		System.out.println("What is the oauth_verifier in url?");
		br = new BufferedReader(new InputStreamReader(System.in));
        String oauth_verifier = br.readLine();

        Result<TwitterAccessData> result = twitterAuth.getAccess(requestToken, oauth_verifier);
        assertTrue(result.isSuccessful());
        TwitterAccessData access = result.getPayload();
        System.out.println("Access token/secret: " + access.getOauthToken() + " / " + access.getOauthTokenSecret());
        
        System.out.println("Trying to access an authorized method...");
        //String response = twitterAuth.doGet(accessTokenPair, "/1/account/verify_credentials.xml");
        //System.out.println("Got:\n" + response);
	}
	
	//@Test
	public void reCall() {
		//String[] accessTokenPair = new String[] { "14546643-tnvmhTtsvU0Q6fUCyomNtEFl86cKtQx9GQLhqwL6I", "7IX2WDTuP8HCAHs9vjitAF1ttveUkLlrTRKfGeZI" };
        //String response = twitterAuth.doGet(accessTokenPair, "/1/account/verify_credentials.xml");
        //System.out.println("Got:\n" + response);
	}

	@Override
	protected void configure() {
		bindPropertiesFile(System.getProperty("user.home")
				+ "/.twitstreet/app.properties");
	}

	private void bindPropertiesFile(String propFileName) {
		try {
			Properties props = new Properties();
			props.load(new FileReader(propFileName));
			for (Entry<Object, Object> entry : props.entrySet()) {
				String name = (String) entry.getKey();
				String value = (String) entry.getValue();
				bindConstant().annotatedWith(Names.named(name)).to(value);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
