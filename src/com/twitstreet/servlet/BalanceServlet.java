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
		User user = request.getSession(false) == null ? null : (User) request.getSession(false).getAttribute(User.USER);
		if(user != null){
			User userFromDB = userMgr.getUserById(user.getId());
			response.getWriter().write(gson.toJson(new BalanceResponse(userFromDB)));
		}
	}
}
