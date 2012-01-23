package com.twitstreet.servlet;

import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.twitstreet.db.data.User;
import com.twitstreet.session.UserMgr;

@SuppressWarnings("serial")
@Singleton
public class BalanceServlet extends HttpServlet {
	@Inject UserMgr userMgr;
	@Inject private final Gson gson = null;
	public void doGet(HttpServletRequest request, HttpServletResponse response)
	throws IOException {
		doPost(request, response);
	}
	
	public void doPost(HttpServletRequest request, HttpServletResponse response)
	throws IOException {
		response.setContentType("application/json;charset=utf-8");
		response.setHeader("Cache-Control","no-cache"); //HTTP 1.1
		response.setHeader("Pragma","no-cache"); //HTTP 1.0
		response.setDateHeader ("Expires", 0); //prevents caching at the proxy server
		
		User user = request.getSession(false) == null ? null : (User) request.getSession(false).getAttribute(User.USER);
		if(user != null){
			User userFromDB = userMgr.getUserById(user.getId());
			response.getWriter().write(gson.toJson(new BalanceResponse(userFromDB)));
		}
	}
}
