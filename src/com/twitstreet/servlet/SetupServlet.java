package com.twitstreet.servlet;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.Properties;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.twitstreet.db.init.DBSetup;
import com.twitstreet.main.Twitstreet;
import com.twitstreet.util.Util;

@SuppressWarnings("serial")
@Singleton
public class SetupServlet extends HttpServlet {
	@Inject
	Twitstreet twitStreet;
	@Inject
	DBSetup dbSetup;
	private static final Gson gson = new Gson();

	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		doPost(request, response);
	}

	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		response.setContentType("application/json;charset=utf-8");
		response.setHeader("Cache-Control","no-cache"); //HTTP 1.1
		response.setHeader("Pragma","no-cache"); //HTTP 1.0
		response.setDateHeader ("Expires", 0); //prevents caching at the proxy server
		
		Response resp = Response.create().success();
		if (!twitStreet.isInitialized()) {
			Properties properties = new Properties();
			String dbHost = request.getParameter("dbHost");
			String dbPortStr = request.getParameter("dbPort");
			String dbAdmin = request.getParameter("dbAdmin");
			String dbAdminPassword = request.getParameter("dbAdminPassword");
			String dbName = request.getParameter("dbName");
			String admin = request.getParameter("admin");
			String adminPassword = request.getParameter("adminPassword");
			String consumerSecret = request.getParameter("consumerSecret");
			String consumerKey = request.getParameter("consumerKey");

			int dbPort = Integer.parseInt(dbPortStr);
			if (resp.isSuccess()) {
				try {
					adminPassword = Util.MD5(adminPassword);
				} catch (NoSuchAlgorithmException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
					resp.fail().reason(e1.getMessage());
				}
			}

			if (resp.isSuccess()) {
				try {
					dbSetup.openConnection(dbHost, dbPort, dbAdmin,
							dbAdminPassword);
					properties.put(Twitstreet.DB_HOST, dbHost);
					properties.put(Twitstreet.DB_PORT, dbPortStr);
					properties.put(Twitstreet.DB_ADMIN, dbAdmin);
					properties.put(Twitstreet.DB_PASSWORD, dbAdminPassword);
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
					resp.fail().reason(e.getMessage());
				} catch (SQLException e) {
					e.printStackTrace();
					resp.fail().reason(e.getMessage());
				}
			}

			if (resp.isSuccess()) {
				try {
					dbSetup.createDatabase(dbName);
					properties.put(Twitstreet.DATABASE, dbName);
				} catch (SQLException e) {
					e.printStackTrace();
					resp.fail().reason(e.getMessage());
				}
			}

			if (resp.isSuccess()) {
				try {
					dbSetup.createTables();
				} catch (SQLException e) {
					e.printStackTrace();
					resp.fail().reason(e.getMessage());
				}
			}

			if (resp.isSuccess()) {
				try {
					dbSetup.dataFill(admin, adminPassword, consumerKey,
							consumerSecret);
				} catch (SQLException e) {
					e.printStackTrace();
					resp.fail().reason(e.getMessage());
				}
			}
			
			if (resp.isSuccess()) {
				try {
					dbSetup.executeScriptFiles();
				} catch (SQLException e) {
					e.printStackTrace();
					resp.fail().reason(e.getMessage());
				}
			}

			if (resp.isSuccess()) {
				try {
					dbSetup.closeConnection();
				} catch (SQLException e) {
					e.printStackTrace();
					resp.fail().reason(e.getMessage());
				}
			}

			if (resp.isSuccess()) {
				try {
					File twitStreetFile =  new File(
							Twitstreet.TWITSTREET_PROPERTIES);
					twitStreetFile.getParentFile().mkdirs();
					properties.store(new FileOutputStream(twitStreetFile),
							Twitstreet.TWITSTREET_PROPERTIES);
				} catch (IOException e) {
					e.printStackTrace();
					resp.fail().reason(e.getMessage());
				}
			}
			response.getWriter().write(gson.toJson(resp));
			twitStreet.initialize();
		} else {
			response.sendRedirect(response.encodeRedirectURL("/"));
		}
	}
}
