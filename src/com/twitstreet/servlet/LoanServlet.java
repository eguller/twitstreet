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
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.twitstreet.db.data.User;
import com.twitstreet.session.UserMgr;

@Singleton
public class LoanServlet extends TwitStreetServlet {
	private static Logger logger = Logger.getLogger(SeasonServlet.class);
	private static final String RECEIVE_LOAN = "receive-loan";
	private static final String PAY_BACK = "pay-back";
	private static final String PAY_BACK_ALL = "pay-back-all";
	@Inject
	UserMgr userMgr;

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		doPost(request, response);
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		response.setContentType("text/html;charset=utf-8");
		response.setHeader("Cache-Control", "no-cache"); // HTTP 1.1
		response.setHeader("Pragma", "no-cache"); // HTTP 1.0
		response.setDateHeader("Expires", 0); // prevents caching at the proxy

		Long userId = null; // server
		if ((userId = (Long) request.getSession().getAttribute(User.USER_ID)) != null) {
			String amountStr = request.getParameter("amount");
			String action = request.getParameter("action");

			if (RECEIVE_LOAN.equals(action)) {
				if (amountStr != null && amountStr.length() > 0) {
					double amount = Double.parseDouble(amountStr);
					userMgr.receiveLoan(userId, amount);
				}
			} else if (PAY_BACK.equals(action)) {
				if (amountStr != null && amountStr.length() > 0) {
					double amount = Double.parseDouble(amountStr);
					userMgr.payLoanBack(userId, amount);
				}
			} else if (PAY_BACK_ALL.equals(action)) {
				userMgr.payAllLoanBack(userId);
			}
		}

		loadUser(request);
		try {
			getServletContext()
					.getRequestDispatcher("/WEB-INF/jsp/balance.jsp").forward(
							request, response);
		} catch (ServletException e) {
			logger.error("Servlet: Dispatch error", e);
		}
	}
}
