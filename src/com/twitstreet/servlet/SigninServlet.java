package com.twitstreet.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.RequestToken;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import com.twitstreet.base.Result;
import com.twitstreet.twitter.TwitterAuth;

@SuppressWarnings("serial")
@Singleton
public class SigninServlet extends HttpServlet {
	String consumerKey;
	String consumerSecret;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		Twitter twitter = new TwitterFactory().getInstance();
        req.getSession().setAttribute("twitter", twitter);
        try {
            StringBuffer callbackURL = req.getRequestURL();
            int index = callbackURL.lastIndexOf("/");
            callbackURL.replace(index, callbackURL.length(), "").append("/callback");
            twitter.setOAuthConsumer(getConsumerKey(), getConsumerSecret());
            RequestToken requestToken = twitter.getOAuthRequestToken(callbackURL.toString());
            req.getSession().setAttribute("requestToken", requestToken);
            resp.sendRedirect(requestToken.getAuthenticationURL());

        } catch (TwitterException e) {
            throw new ServletException(e);
        }
	}
	
	
	public String getConsumerKey() {
		return consumerKey;
	}
	@Inject
	public void setConsumerKey(@Named("com.twitstreet.meta.ConsumerKey")String consumerKey) {
		this.consumerKey = consumerKey;
	}
	public String getConsumerSecret() {
		return consumerSecret;
	}
	
	@Inject
	public void setConsumerSecret(@Named("com.twitstreet.meta.ConsumerSecret")String consumerSecret) {
		this.consumerSecret = consumerSecret;
	}
}
