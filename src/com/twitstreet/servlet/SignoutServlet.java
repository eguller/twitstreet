package com.twitstreet.servlet;

import java.io.IOException;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.inject.Singleton;

@SuppressWarnings("serial")
@Singleton
public class SignoutServlet extends HttpServlet {
	
	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		request.getSession().invalidate();
		invalidateCookies(new String[]{CallBackServlet.COOKIE_ID, CallBackServlet.COOKIE_OAUTHTOKEN}, request, response);
		response.sendRedirect(request.getHeader("Referer"));
	}
	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		doPost(request, response);
	}
	
	private void invalidateCookies(String[] cookieNames, HttpServletRequest request, HttpServletResponse response){
		for(String cookieName : cookieNames){
			Cookie cookie = new Cookie(cookieName, "");
			cookie.setMaxAge(0);
			cookie.setPath("/");
			cookie.setDomain(request.getHeader("host"));
			response.addCookie(cookie);
		}
	}
}
