package com.twitstreet.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.inject.Singleton;
import com.twitstreet.db.data.User;

@Singleton
public class SignoutServlet extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6497112269192613257L;

	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		request.getSession().invalidate();
		request.removeAttribute(User.USER);
		response.sendRedirect(request.getContextPath() + "/");
	}
}
