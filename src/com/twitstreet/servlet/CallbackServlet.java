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
import com.google.inject.name.Named;
import com.twitstreet.base.Result;
import com.twitstreet.base.TSHttpUtils;
import com.twitstreet.session.SessionData;
import com.twitstreet.session.SessionMgr;
import com.twitstreet.twitter.TwitterAccessData;
import com.twitstreet.twitter.TwitterAuth;

@SuppressWarnings("serial")
@Singleton
public class CallbackServlet extends HttpServlet {

	private static Logger logger = LoggerFactory.getLogger(CallbackServlet.class);

	@Inject
	private final SessionMgr sessionMgr = null;

	@Inject
	private final TwitterAuth twitterAuth = null;
	
	@Inject
	@Named("com.twitstreet.meta.ConsumerKey") 
	private final String consumerKey = null;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.setAttribute("taApiKey", consumerKey);
		getServletContext().getRequestDispatcher("/WEB-INF/jsp/callback.jsp").forward(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String hash = req.getParameter("hash");
		logger.info("hash: {}", hash);
		if(hash==null) return;

		Map<String, String> params = TSHttpUtils.parseQueryParms(hash);
		String requestToken = params.get("oauth_access_token");
		String oauthBridgeCode = params.get("oauth_bridge_code");
		Result<TwitterAccessData> result = twitterAuth.getAccessDataWithBridge(requestToken, oauthBridgeCode);
		
		if(result.isSuccessful()) {
			TwitterAccessData accessData = result.getPayload();
			Result<SessionData> registerResult = sessionMgr.register(accessData);
			
			if(registerResult.isSuccessful()) {
				String sessionKey = sessionMgr.getKey().toString();
				req.getSession(true).setAttribute(sessionKey, registerResult.getPayload());
			}
			else {
				logger.info("register failed: {}", registerResult.getErrorCode());
			}
		}
		else {
			logger.info("auth failed: {}", result.getErrorCode());
			//TODO display error on browser
		}
	}
}
