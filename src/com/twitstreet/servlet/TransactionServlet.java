package com.twitstreet.servlet;

import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.twitstreet.market.TransactionMgr;

@Singleton
public class TransactionServlet extends HttpServlet {
	@Inject
	TransactionMgr transactionMgr;
	public void doGet(HttpServletRequest request, HttpServletResponse response)
	throws IOException {
		//request.getAtt
	}
	
	public void doPost(HttpServletRequest request, HttpServletResponse response)
	throws IOException {
		
	}
}
