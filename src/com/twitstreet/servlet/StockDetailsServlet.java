package com.twitstreet.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.twitstreet.db.data.User;

@SuppressWarnings("serial")
public class StockDetailsServlet extends HttpServlet {
	public void doGet(HttpServletRequest request, HttpServletResponse response)
	throws ServletException, IOException {
		response.setContentType("application/json;charset=utf-8");
		response.setHeader("Cache-Control","no-cache"); //HTTP 1.1
		response.setHeader("Pragma","no-cache"); //HTTP 1.0
		response.setDateHeader ("Expires", 0); //prevents caching at the proxy server
		
		User user = (User) request.getSession().getAttribute(User.USER);
		String pathInfo = request.getPathInfo();
		String stockIdStr = pathInfo == null || "/".equals(pathInfo) ? null : pathInfo.substring(1);
				
		if(stockIdStr == null){
			response.sendRedirect(response.encodeRedirectURL("/"));
			return;
		}
		
		if (user != null) {
			getServletContext().getRequestDispatcher(
			"/WEB-INF/jsp/stockAuth.jsp?stock="+stockIdStr).forward(request, response);
		}
		else{
			getServletContext().getRequestDispatcher(
			"/WEB-INF/jsp/stockUnAuth.jsp?stock="+stockIdStr).forward(request, response);
		}
	}
	public void doPost(HttpServletRequest request, HttpServletResponse response)
	throws ServletException, IOException {
		doGet(request, response);
	}
}
