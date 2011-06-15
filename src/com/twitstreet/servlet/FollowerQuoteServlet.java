package com.twitstreet.servlet;

import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Singleton;

@SuppressWarnings("serial")
@Singleton
public class FollowerQuoteServlet extends HttpServlet {
	private static Logger logger = LoggerFactory.getLogger(FollowerQuoteServlet.class);
	
	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException{
		
	}
	
	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException{
		String twitterUser = (String)request.getParameter("twitteruser");
		logger.info("user:{}", twitterUser);
	}
	
}
