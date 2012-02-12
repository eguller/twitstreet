package com.twitstreet.servlet;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
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
public class TopRankServlet extends HttpServlet{
	
	public static String TOPRANKS_USER_LIST = "topranksuserlist";

	@Inject UserMgr userMgr;
	@Inject
	private final Gson gson = null;
	
	public static String PAGE = "toprankPage";
	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		response.setContentType("application/json;charset=utf-8");
		response.setHeader("Cache-Control","no-cache"); //HTTP 1.1
		response.setHeader("Pragma","no-cache"); //HTTP 1.0
		response.setDateHeader ("Expires", 0); //prevents caching at the proxy server
	
			try {
				getServletContext().getRequestDispatcher(
						"/WEB-INF/jsp/topranks.jsp").forward(request, response);
			} catch (ServletException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
				
	//	response.getWriter().write(gson.toJson(userList));
	}
		
}
