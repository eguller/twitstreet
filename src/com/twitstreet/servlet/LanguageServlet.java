package com.twitstreet.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.twitstreet.localization.LocalizationUtil;
import com.twitstreet.session.UserMgr;

@SuppressWarnings("serial")
@Singleton
public class LanguageServlet extends TwitStreetServlet {


	@Inject
	UserMgr userMgr;

	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		response.setContentType("text/html;charset=utf-8");
		response.setHeader("Cache-Control", "no-cache"); // HTTP 1.1
		response.setHeader("Pragma", "no-cache"); // HTTP 1.0
		response.setDateHeader("Expires", 0); // prevents caching at the proxy
												// server

		loadUser(request);
		
		response.getWriter().write(LocalizationUtil.getInstance().get("language.load.message", getLanguage(request)));

	}

}
