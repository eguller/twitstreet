/**
	TwitStreet - Twitter Stock Market Game
    Copyright (C) 2012  Engin Guller (bisanthe@gmail.com), Cagdas Ozek (cagdasozek@gmail.com)

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
**/

package com.twitstreet.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.twitstreet.db.data.User;
import com.twitstreet.market.TransactionMgr;

@SuppressWarnings("serial")
@Singleton
public class TransactionServlet extends TwitStreetServlet {
	private static String USER_TRANSACTIONS = "user";
	@Inject
	TransactionMgr transactionMgr;
	@Inject
	private final Gson gson = null;

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		doPost(request, response);
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		response.setContentType("text/html;charset=utf-8");
		response.setHeader("Cache-Control","no-cache"); //HTTP 1.1
		response.setHeader("Pragma","no-cache"); //HTTP 1.0
		response.setDateHeader ("Expires", 0); //prevents caching at the proxy server
		
		String type = request.getParameter("type");
		
		loadUser(request);
		//loadUserFromCookie(request);
		try {

			if (USER_TRANSACTIONS.equals(type)) {
				long userId = (Long)request.getSession().getAttribute(User.USER_ID);
				getServletContext().getRequestDispatcher("/WEB-INF/jsp/userTransactionsContent.jsp?user-id="+userId).forward(request, response);

			} else {

				getServletContext().getRequestDispatcher("/WEB-INF/jsp/currentTransactionsContent.jsp").forward(request, response);

			}

		} catch (ServletException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
