package com.twitstreet.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.twitstreet.base.Result;
import com.twitstreet.data.CallbackData;
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
	private final Gson gson = null;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String requestToken = req.getParameter("oauth_token");
		String requestVerifier = req.getParameter("oauth_verifier");

		if(requestToken==null || requestVerifier==null) {
			PrintWriter out = resp.getWriter();
			out.println("Null param");
			return;
		}
		
		Result<TwitterAccessData> result = twitterAuth.getAccess(requestToken, requestVerifier);
		
		if(result.isSuccessful()) {
			TwitterAccessData accessData = result.getPayload();
			Result<SessionData> registerResult = sessionMgr.register(accessData);
			
			if(registerResult.isSuccessful()) {
				String sessionKey = sessionMgr.getKey().toString();
				SessionData sessionData = registerResult.getPayload();
				req.getSession(true).setAttribute(sessionKey, sessionData );
				
				CallbackData data = new CallbackData();
				accessData = sessionData.getTwitterAccessData();
				data.userId = accessData.getUserIdStr();
				data.screenName = accessData.getScreenName();
				req.setAttribute("data", gson.toJson(data));
				getServletContext().getRequestDispatcher("/WEB-INF/jsp/callback.jsp").forward(req, resp);
				return;
			}
			else {
				logger.info("register failed: {}", registerResult.getErrorCode());
				// Fall through to error
			}
		}
		else {
			logger.info("auth failed: {}", result.getErrorCode());
			// Fall through to error
		}
		// Fall through to error
		PrintWriter out = resp.getWriter();
		out.println(result);
	}
}
