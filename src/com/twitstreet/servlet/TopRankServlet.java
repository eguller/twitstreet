package com.twitstreet.servlet;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.twitstreet.db.data.User;
import com.twitstreet.session.UserMgr;

@Singleton
public class TopRankServlet extends HttpServlet{
	@Inject UserMgr userMgr;
	@Inject
	private final Gson gson = null;
	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		response.setContentType("application/json;charset=utf-8");
		ArrayList<User> userList = userMgr.getTopRank();
		response.getWriter().write(gson.toJson(userList));
	}
}
