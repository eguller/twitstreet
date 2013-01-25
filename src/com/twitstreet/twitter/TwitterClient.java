package com.twitstreet.twitter;

import java.io.BufferedReader;
import java.io.EOFException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.zip.GZIPInputStream;

import javax.inject.Inject;

import org.apache.http.Header;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.log4j.Logger;

import com.twitstreet.db.data.Announcer;

public class TwitterClient {
	AnnouncerMgr announcerMgr;

	HttpClient client = null;
	private static boolean DETAILED_LOG = false;

	private static Logger logger = Logger.getLogger(TwitterClient.class);
	Announcer announcer = null;

	private void loadCredentials() {
		try {

			// Protocol easyhttps = new Protocol("https", new
			// EasySSLProtocolSocketFactory(), 443);
			// Protocol.registerProtocol("https", easyhttps);
			logger.info("loadCredentials - 1");
			HttpClient client = null;
			client = new DefaultHttpClient();
			client = WebClientDevWrapper.wrapClient(client);

			// client.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY,
			// getProxyAsHttpHost());

			HttpGet httpGet = new HttpGet("https://twitter.com");

			HttpResponse response = client.execute(httpGet);
			logger.info("loadCredentials - 2");
			Header[] headers = response.getAllHeaders();

			InputStream input = response.getEntity().getContent();

			InputStreamReader isr = new InputStreamReader(input);
			BufferedReader br = new BufferedReader(isr);

			String content = "";
			String line = null;
			while ((line = br.readLine()) != null) {
				content = content + "\n" + line;
			}
			logger.info("loadCredentials - 3");
			// System.out.println(content);

			String paut = Util.parse(content, " value=\"", "\"",
					"name=\"redirect_after_login\" value=\"/\">",
					"name=\"authenticity_token\"");
			logger.info("loadCredentials - 4, authToken: " + paut);
			// System.out.println(paut);

			HttpPost httpPost = new HttpPost("https://twitter.com/sessions");
			BasicNameValuePair[] params = {
					new BasicNameValuePair("session[username_or_email]",
							announcer.getName()),
					new BasicNameValuePair("session[password]",
							announcer.getPassword()),
					new BasicNameValuePair("authenticity_token", paut), };
			UrlEncodedFormEntity urlEncodedFormEntity = new UrlEncodedFormEntity(
					Arrays.asList(params));
			urlEncodedFormEntity.setContentEncoding(HTTP.UTF_8);
			httpPost.setEntity(urlEncodedFormEntity);

			response = client.execute(httpPost);
			Header[] cookieArray = response.getHeaders("Set-Cookie");
			String sessIdCk = "";
			String autIdCk = "";
			for (Header cookie : cookieArray) {
				if (cookie.getValue().contains("_twitter_sess")) {

					sessIdCk = cookie.getValue();
				}

			}
			String sesId = Util.parse(sessIdCk, "_twitter_sess=", ";", null,
					null);

			logger.info("loadCredentials -5, sesId: " + sesId);

			announcer.setSessionId(sesId);
			announcer.setAccessToken(paut);

		} catch (Exception ex) {
			logger.error("Error", ex);
		}

	}

	public TwitterClient(Announcer announcer, AnnouncerMgr announcerMgr) {

		this.announcer = announcer;
		this.announcerMgr = announcerMgr;
		loadCredentials();

	}

	public void unfollow(long userId) {

		ArrayList<BasicNameValuePair> nvps = new ArrayList<BasicNameValuePair>();
		nvps.add(new BasicNameValuePair("user_id", String.valueOf(userId)));
		post("https://twitter.com/i/user/unfollow", nvps);
	}

	public long tweet(String tweet) {

		ArrayList<BasicNameValuePair> nvps = new ArrayList<BasicNameValuePair>();
		nvps.add(new BasicNameValuePair("status", tweet));
		String resp = post("https://twitter.com/i/tweet/create", nvps);

		return getTweetId(resp);

	}

	public void favorite(long statusId) {

		ArrayList<BasicNameValuePair> nvps = new ArrayList<BasicNameValuePair>();
		nvps.add(new BasicNameValuePair("id", String.valueOf(statusId)));
		post("https://twitter.com/i/tweet/favorite", nvps);

	}

	private long getTweetId(String response) {

		String tweetIdStr = Util.parse(response, "\"", "\"", "data-tweet-id",
				null);
		tweetIdStr = tweetIdStr.replace("\\", "");
		return Long.valueOf(tweetIdStr);

	}

	public void follow(long userId) {

		ArrayList<BasicNameValuePair> nvps = new ArrayList<BasicNameValuePair>();
		nvps.add(new BasicNameValuePair("user_id", String.valueOf(userId)));
		post("https://twitter.com/i/user/follow", nvps);
	}

	public void retweet(long statusId) {

		ArrayList<BasicNameValuePair> nvps = new ArrayList<BasicNameValuePair>();
		nvps.add(new BasicNameValuePair("id", String.valueOf(statusId)));
		post("https://twitter.com/i/tweet/retweet", nvps);

	}

	public long reply(String message, long statusId) {
		ArrayList<BasicNameValuePair> nvps = new ArrayList<BasicNameValuePair>();
		nvps.add(new BasicNameValuePair("status", message));
		nvps.add(new BasicNameValuePair("in_reply_to_status_id", String
				.valueOf(statusId)));
		String resp = post("https://twitter.com/i/tweet/create", nvps);
		return getTweetId(resp);
	}

	private void setHeaders(HttpRequestBase httpReq) {
		httpReq.setHeader("Host", "api.twitter.com");
		httpReq.setHeader("User-Agent",
				"Mozilla/5.0 (Windows NT 6.1; rv:13.0) Gecko/20100101 Firefox/13.0.1");
		httpReq.setHeader("Accept",
				"application/json, text/javascript, */*; q=0.01");
		httpReq.setHeader("Accept-Language", "en-us,en;q=0.5");
		httpReq.setHeader("Accept-Encoding", "gzip, deflate");
		httpReq.setHeader("Connection", "keep-alive");
		httpReq.setHeader("Content-Type",
				"application/x-www-form-urlencoded; charset=UTF-8");
		httpReq.setHeader("X-PHX", "true");
		httpReq.setHeader("X-Requested-With", "XMLHttpRequest");
		httpReq.setHeader("Referer", "https://api.twitter.com/receiver.html");

		httpReq.setHeader("Cookie", "_twitter_sess=" + announcer.getSessionId()
				+ "; ");

		httpReq.setHeader("Pragma", "no-cache");
		httpReq.setHeader("Cache-Control", "no-cache");
	}

	private String post(String url, ArrayList<BasicNameValuePair> parameters) {
		return post(url, parameters, true);

	}

	private String post(String url, ArrayList<BasicNameValuePair> parameters,
			boolean auth) {
		return method("post", url, parameters, 1);
	}

	private String get(String url, ArrayList<BasicNameValuePair> parameters) {
		return method("get", url, parameters, 1);
	}

	public boolean isUserSuspended(String screenName) {

		String resp = "";
		try {
			resp = get("https://www.twitter.com/" + screenName, null);
		} catch (Exception e) {
			logger.error(e);
		}

		return resp.contains("Sorry, that user is suspended.")
				|| resp.contains("Your account is suspended");
	}

	int MAX_TRY = 5;

	private String method(String method, String url,
			ArrayList<BasicNameValuePair> parameters, int tryNumber) {
		HttpClient client = null;

		String responseContent = "";
		try {
			client = new DefaultHttpClient();

			client = WebClientDevWrapper.wrapClient(client);

			ArrayList<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();

			params.add(new BasicNameValuePair("authenticity_token", announcer
					.getAccessToken()));

			if (Util.isListValid(parameters)) {
				params.addAll(parameters);
			}

			HttpRequestBase httpRequest = null;
			if (method.equalsIgnoreCase("post")) {
				httpRequest = new HttpPost(url);

				UrlEncodedFormEntity urlEncodedFormEntity = new UrlEncodedFormEntity(
						params);
				urlEncodedFormEntity.setContentEncoding(HTTP.UTF_8);
				((HttpPost) httpRequest).setEntity(urlEncodedFormEntity);
			} else {
				String paramString = "";
				if (Util.isListValid(parameters)) {
					params.addAll(parameters);
					paramString = URLEncodedUtils.format(parameters, "utf-8");
				}

				httpRequest = new HttpGet(url + "?" + paramString);
			}
			setHeaders(httpRequest);

			HttpResponse response = client.execute(httpRequest);

			InputStream input = response.getEntity().getContent();
			GZIPInputStream gzip = new GZIPInputStream(input);
			InputStreamReader isr = new InputStreamReader(gzip);
			BufferedReader br = new BufferedReader(isr);

			String line = null;
			while ((line = br.readLine()) != null) {
				responseContent = responseContent + "\n" + line;
			}

			client.getConnectionManager().shutdown();
		} catch (EOFException ex) {
			logger.error("EOF exception", ex);
		} catch (Exception ex) {
			logger.error("Exception", ex);
		}

		if (responseContent.contains("You are being")
				&& responseContent.contains("redirected")
				&& tryNumber <= MAX_TRY) {

			logger.info("loading credentials again...");
			logger.info("old credentials:" + announcer.getAccessToken() + "\n"
					+ announcer.getSessionId());
			loadCredentials();
			logger.info("new credentials:" + announcer.getAccessToken() + "\n"
					+ announcer.getSessionId());
			responseContent = method(method, url, parameters, tryNumber + 1);
		} else if (responseContent
				.contains("Your account may not be allowed to perform this action.")) {
			throw new RuntimeException(
					"Your account may not be allowed to perform this action.");
		} else if (responseContent.contains("Your account is suspended")) {
			announcerMgr.setAnnouncerSuspended(this.announcer.getId());
		}
		return responseContent;
	}

	public Announcer getAnnouncer() {
		return announcer;
	}

}
