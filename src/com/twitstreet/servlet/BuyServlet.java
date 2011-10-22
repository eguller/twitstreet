package com.twitstreet.servlet;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.twitstreet.market.TransactionMgr;
import com.twitstreet.session.UserMgr;

@SuppressWarnings("serial")
@Singleton
public class BuyServlet extends HttpServlet {
	@Inject TransactionMgr transactionMgr;
	@Inject UserMgr sessionMgr;
	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		doPost(request, response);
	}
	
	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		HttpSession session = request.getSession(false);
		String stock = request.getParameter("stock");
		String amount = request.getParameter("amount");
		try {
			transactionMgr.buy("buyer", stock, Integer.parseInt(amount));
		} catch (NumberFormatException e) {
			// TODO Wrong stock amount inform user
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Database operation failed inform user
			e.printStackTrace();
		}
	}
}
