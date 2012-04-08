/**
	TwitStreet - Twitter Stock Market Game
    Copyright (C) 2012  Engin Guller (bisanth@gmail.com), Cagdas Ozek (cagdasozek@gmail.com)

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
**/

package com.twitstreet.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.RequestToken;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.twitstreet.config.ConfigMgr;
import com.twitstreet.db.data.User;
import com.twitstreet.session.UserMgrImpl;

@SuppressWarnings("serial")
@Singleton
public class SigninServlet extends TwitStreetServlet {
	private static Logger logger = Logger.getLogger(SigninServlet.class);
	@Inject
	ConfigMgr configMgr;

	@Override
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html;charset=utf-8");
		response.setHeader("Cache-Control","no-cache"); //HTTP 1.1
		response.setHeader("Pragma","no-cache"); //HTTP 1.0
		response.setDateHeader ("Expires", 0); //prevents caching at the proxy server
		

		Twitter twitter = new TwitterFactory().getInstance();
		try {
			StringBuffer callbackURL = request.getRequestURL();
			int index = callbackURL.lastIndexOf("/");
			callbackURL.replace(index, callbackURL.length(), "").append(
					"/callback");
			logger.debug("Callback url is: " + callbackURL.toString());
			twitter.setOAuthConsumer(configMgr.getConsumerKey(),
					configMgr.getConsumerSecret());
			logger.debug("Consumer Key: " + configMgr.getConsumerKey()
					+ ", Consumer Secret: " + configMgr.getConsumerSecret());
			RequestToken requestToken = twitter
					.getOAuthRequestToken(callbackURL.toString());
			request.getSession().setAttribute(CallBackServlet.REQUEST_TOKEN, requestToken);
			response.sendRedirect(requestToken.getAuthenticationURL());
			logger.debug("Redirect sent to authentication URL: "
					+ requestToken.getAuthenticationURL());

		} catch (TwitterException e) {
			throw new ServletException(e);
		}
	}
}
