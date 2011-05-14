package com.twitstreet.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.twitstreet.base.Result;
import com.twitstreet.session.SessionMgr;
import com.twitstreet.twitter.TwitterAnywhere;

@Singleton
public class AuthenticationFilter implements Filter {
	private static Logger logger = LoggerFactory.getLogger(AuthenticationFilter.class);

	@Inject
	private final TwitterAnywhere twitterAnywhere = null;

	@Inject
	private final SessionMgr sessionMgr = null;

	@Override
	public void destroy() {

	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException,
			ServletException {

		HttpServletRequest req = ((HttpServletRequest) request);
		HttpServletResponse resp = ((HttpServletResponse) response);

		HttpSession session = req.getSession(false);

		String sessionKey = sessionMgr.getKey().toString();

		if (session != null) {
			if (session.getAttribute(sessionKey) == null) {
				session.invalidate();
				logger.warn("Invalidating empty session: {}", session.getId());
			}

		} else {
			Cookie taCookie = getCookie(req.getCookies(), "twitter_anywhere_identity");

			if (taCookie != null) {
				Result<String> useridResult = twitterAnywhere.getUserIdFromTACookie(taCookie.getValue());

				if (useridResult.isSuccessful()) {
					Result<?> sessionResult = sessionMgr.start(useridResult.getPayload());

					if (sessionResult.isSuccessful()) {
						req.getSession(true).setAttribute(sessionKey, sessionResult.getPayload());

					} else {
						logger.warn("Failed to create session for userid: {}", useridResult.getPayload());
						taCookie.setMaxAge(0);
						resp.addCookie(taCookie);
					}

				} else {
					logger.warn("Invalid cookie: {}", taCookie.getValue());
					taCookie.setMaxAge(0);
					resp.addCookie(taCookie);
				}
			}
		}

		chain.doFilter(request, response);
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {
	}

	private Cookie getCookie(Cookie[] cookies, String name) {
		if (cookies != null) {
			for (Cookie cookie : cookies) {
				if (name.equals(cookie.getName())) {
					return cookie;
				}
			}
		}
		return null;
	}
}
