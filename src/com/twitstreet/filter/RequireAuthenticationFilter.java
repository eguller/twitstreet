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

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.twitstreet.session.SessionMgr;

@Singleton
public class RequireAuthenticationFilter implements Filter {

	@Inject
	private final SessionMgr sessionMgr = null;

	@Override
	public void destroy() {
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException,
			ServletException {
		
		HttpServletRequest req = ((HttpServletRequest) request);

		HttpSession session = req.getSession(false);
		
		String sessionKey = sessionMgr.getKey().toString();

		if(session==null || session.getAttribute(sessionKey)==null) {
			((HttpServletResponse)response).addHeader("REQUIRES_AUTH", "1");
		}

	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {
	}

}
