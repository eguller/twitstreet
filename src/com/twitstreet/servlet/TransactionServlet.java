package com.twitstreet.servlet;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.twitstreet.cache.TransactionCache;
import com.twitstreet.db.data.TransactionRecord;
import com.twitstreet.db.data.User;
import com.twitstreet.market.TransactionMgr;

@SuppressWarnings("serial")
@Singleton
public class TransactionServlet extends HttpServlet {
	private static String USER_TRANSACTIONS = "user";
	@Inject
	TransactionMgr transactionMgr;
	@Inject TransactionCache transactionRecordCache;
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
		response.setHeader("Cache-Control","no-cache"); //HTTP 1.1
		response.setHeader("Pragma","no-cache"); //HTTP 1.0
		response.setDateHeader ("Expires", 0); //prevents caching at the proxy server
		
		List<TransactionRecord> transactionRecordList = null;
		String type = request.getParameter("type");
		if(USER_TRANSACTIONS.equals(type)){
			String userIdStr = (String) request.getAttribute("user");
			if(userIdStr == null){
				User user = (User)request.getSession(false).getAttribute(User.USER);
				if(user != null){
					transactionRecordList = transactionRecordCache.getUserTransactions(user.getId());
				}
			}
			else{
				transactionRecordList = transactionRecordCache.getUserTransactions(Long.parseLong(userIdStr));
			}
		}
		else{
			transactionRecordList = transactionRecordCache.getCurrentTransactions();
			
		}
		
		if(transactionRecordList != null){
			response.getWriter().write(gson.toJson(transactionRecordList));
		}
	}
}
