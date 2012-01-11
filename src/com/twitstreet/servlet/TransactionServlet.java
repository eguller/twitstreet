package com.twitstreet.servlet;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.twitstreet.db.data.TransactionRecord;
import com.twitstreet.db.data.User;
import com.twitstreet.market.TransactionMgr;

@Singleton
public class TransactionServlet extends HttpServlet {
	private static String USER_TRANSACTIONS = "user";
	@Inject
	TransactionMgr transactionMgr;
	@Inject
	private final Gson gson = null;
	public void doGet(HttpServletRequest request, HttpServletResponse response)
	throws IOException {
		response.setContentType("application/json;charset=utf-8");
		doPost(request, response);
	}
	
	public void doPost(HttpServletRequest request, HttpServletResponse response)
	throws IOException {
		response.setContentType("application/json;charset=utf-8");
		List<TransactionRecord> transactionRecordList = null;
		String type = request.getParameter("type");
		if(USER_TRANSACTIONS.equals(type)){
			String userIdStr = (String) request.getAttribute("user");
			if(userIdStr == null){
				User user = (User)request.getSession(false).getAttribute(User.USER);
				if(user != null){
					transactionRecordList = transactionMgr.queryTransactionRecord(user.getId());
				}
			}
			else{
				transactionRecordList = transactionMgr.queryTransactionRecord(Long.parseLong(userIdStr));
			}
		}
		else{
			transactionRecordList = transactionMgr.getCurrentTransactions();
			
		}
		
		if(transactionRecordList != null){
			response.getWriter().write(gson.toJson(transactionRecordList));
		}
	}
}
