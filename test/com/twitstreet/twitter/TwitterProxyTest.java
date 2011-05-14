package com.twitstreet.twitter;

import static org.junit.Assert.fail;

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
public class TwitterProxyTest extends AbstractModule {

	@Inject
	@Unit
	private TwitterProxyImpl twitterProxy;

	// @Test
	public void testGetFollowerCount() {
		fail("Not yet implemented");
	}

	//@Test
	public void authorize() throws Exception {
		String[] requestTokenPair = twitterProxy.getNewRequestTokenPair();
		String authorizationUrl = twitterProxy.getAuthorizationUrl(requestTokenPair);
		System.out.println("go to: " + authorizationUrl + " and Approve twitstreet App.");
		
		System.out.println("What is the oauth_verifier in url?");
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String oauth_verifier = br.readLine();

        String[] accessTokenPair = twitterProxy.getAccessTokenPair(requestTokenPair, oauth_verifier);
        System.out.println("Access token/secret: " + accessTokenPair[0] + " / " + accessTokenPair[1]);
        
        System.out.println("Trying to access an authorized method...");
        String response = twitterProxy.doGet(accessTokenPair, "/1/account/verify_credentials.xml");
        System.out.println("Got:\n" + response);
	}
	
	//@Test
	public void reCall() {
		String[] accessTokenPair = new String[] { "14546643-tnvmhTtsvU0Q6fUCyomNtEFl86cKtQx9GQLhqwL6I", "7IX2WDTuP8HCAHs9vjitAF1ttveUkLlrTRKfGeZI" };
        String response = twitterProxy.doGet(accessTokenPair, "/1/account/verify_credentials.xml");
        System.out.println("Got:\n" + response);
	}
	
	@Test
	public void bridge() {
		String requestToken = "0/bdAAAAAAC/bchNAAAAAPilDAAAAAAAnHSUTrCTvXA/umTqGHQhya9yzS4=tnvmhTtsvU0Q6fUCyomNtEFl86cKtQx9GQLhqwL6I";
		String bridgeCode = "EsYLB6Bisd1wqPR0LosYvgY88pxA239NhVhgkk8Zok";
		Result<String[]> result = twitterProxy.getAccessTokenWithBridge(requestToken, bridgeCode);
		String[] accessTokenPair = result.getPayload();
		System.out.println("Got: " + accessTokenPair[0]);
		System.out.println(accessTokenPair[1]);
        //String response = twitterProxy.doGet(accessTokenPair, "/1/account/verify_credentials.xml");
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
