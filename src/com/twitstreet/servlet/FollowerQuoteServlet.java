package com.twitstreet.servlet;

import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.inject.Singleton;

@SuppressWarnings("serial")
@Singleton
public class FollowerQuoteServlet extends HttpServlet {
	private static final Gson gson = new Gson();
	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException{
		
	}
	
	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException{
		String twitterUser = (String)request.getAttribute("twitteruser");
		
	}
	
}
