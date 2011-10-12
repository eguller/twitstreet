package com.twitstreet.servlet;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.twitstreet.db.init.DBSetup;
import com.twitstreet.main.Twitstreet;
import com.twitstreet.util.Util;

@Singleton
public class SetupServlet extends HttpServlet {
	@Inject
	Twitstreet twitStreet;
	@Inject DBSetup dbSetup;
	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		doPost(request, response);
	}

	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		if (!twitStreet.isInitialized()) {
			String dbIp = request.getParameter("dbIp");
			String dbPortStr = request.getParameter("dbPort");
			String dbAdmin = request.getParameter("dbAdmin");
			String dbAdminPassword = request.getParameter("dbAdminPassword");
			String dbName = request.getParameter("dbName");
			String admin = request.getParameter("admin");
			String adminPassword = request.getParameter("adminPassword");
			String consumerSecret = request.getParameter("consumerSecret");
			String consumerKey = request.getParameter("consumerKey");
			
			int dbPort = Integer.parseInt(dbPortStr);
			try {
				adminPassword = Util.MD5(adminPassword);
			} catch (NoSuchAlgorithmException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			try {
				dbSetup.openConnection(dbIp, dbPort, dbAdmin, dbAdminPassword);
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			try {
				dbSetup.createDatabase(dbName);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			try {
				dbSetup.createTables();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			try {
				dbSetup.dataFill(admin, adminPassword, consumerKey, consumerSecret);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			try {
				dbSetup.closeConnection();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
		}
		else{
			response.sendRedirect(response.encodeRedirectURL("/"));
		}
	}
}
