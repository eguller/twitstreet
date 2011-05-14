package com.twitstreet.servlet;

import java.io.IOException;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.twitstreet.base.Result;
import com.twitstreet.base.TSHttpUtils;
import com.twitstreet.twitter.TwitterProxy;

@SuppressWarnings("serial")
@Singleton
public class CallbackServlet extends HttpServlet {

	private static Logger logger = LoggerFactory.getLogger(CallbackServlet.class);

	@Inject
	private final TwitterProxy twitterProxy = null;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		getServletContext().getRequestDispatcher("/WEB-INF/jsp/callback.jsp").forward(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		Map<String, String> params = TSHttpUtils.parseQueryParms(req.getParameter("hash"));
		String requestToken = params.get("oauth_access_token");
		String oauthBridgeCode = params.get("oauth_bridge_code");
		Result<String[]> result = twitterProxy.getAccessTokenWithBridge(requestToken, oauthBridgeCode);
		
		// TODO save to DB
		if(result.isSuccessful()) {
			int count = twitterProxy.getFollowerCount(result.getPayload(), "tccankaya");
			logger.info("follower count: {}", count);
		}
		else {
			logger.info("auth failed: {}", result.getErrorCode());
		}
	}
}
