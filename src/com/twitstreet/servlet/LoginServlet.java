package com.twitstreet.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.twitstreet.base.Result;
import com.twitstreet.twitter.TwitterAuth;

@SuppressWarnings("serial")
@Singleton
public class LoginServlet extends HttpServlet {
	
	@Inject
	private final TwitterAuth auth = null;
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		Result<String> authURL = auth.getAuthenticationUrl("http://twitstreet.com/callback");
		if(authURL.isSuccessful()) {
			resp.sendRedirect(authURL.getPayload());
		}
		else {
			PrintWriter out = resp.getWriter();
			out.println(authURL);
		}
	}
}
