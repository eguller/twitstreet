package com.twitstreet.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.twitstreet.session.SessionMgr;

@Singleton
public class AuthenticationFilter implements Filter {
	private static Logger logger = LoggerFactory.getLogger(AuthenticationFilter.class);

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
		}

		chain.doFilter(req, resp);
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {
	}

}
