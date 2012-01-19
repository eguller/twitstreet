package com.twitstreet.db.init;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;


import com.google.inject.Inject;
import com.twitstreet.main.Twitstreet;

public class DBSetupImpl implements DBSetup {
	private static final String CREATE_DB_FILE = "WEB-INF/db/setup/createDatabase.sql";
	private static final String CREATE_TABLES_FILE = "WEB-INF/db/setup/createTables.sql";
	private static final String DATA_FILL_FILE = "WEB-INF/db/setup/dataFill.sql";
	private static final String PROC_RERANK = "WEB-INF/db/setup/proc/rerank.sql";
	private static final String FUNC_PORTFOLIO_VALUE = "WEB-INF/db/setup/proc/portfolio_value.sql";
	private static final String FUNC_STOCK_SOLD = "WEB-INF/db/setup/proc/stock_sold.sql";
	
	private static final String DATABASENAME_KEY = "databasename";
	private static final String USERNAME = "username";
	private static final String PASSWORD = "password";
	private static final String CONSUMER_KEY = "consumerKey";
	private static final String CONSUMER_SECRET = "consumerSecret";

	Connection con = null;
	@Inject DBScriptParser dbScriptParser;
	@Inject Twitstreet twitstreet;

	@Override
	public void openConnection(String dbHost, int dbPort, String dbUser, String dbPassword) throws ClassNotFoundException, SQLException {
		Class.forName("com.mysql.jdbc.Driver");
		con = DriverManager.getConnection(
				"jdbc:mysql://"+dbHost+":"+dbPort + "/mysql", dbUser, dbPassword);
	}

	@Override
	public void closeConnection() throws SQLException {
		con.close();
	}

	@Override
	public void createDatabase(String databaseName) throws IOException, SQLException {
		ArrayList<String> statements = dbScriptParser.parseFile(new File(twitstreet.getServletContext().getRealPath(CREATE_DB_FILE)));
		HashMap<String, String> parmMap = new HashMap<String, String>();
		parmMap.put(DATABASENAME_KEY, databaseName);
		statements = dbScriptParser.replaceParameters(parmMap, statements);
		executeStatements(statements);
	}

	@Override
	public void createTables() throws IOException, SQLException {
		ArrayList<String> statements = dbScriptParser.parseFile(new File(twitstreet.getServletContext().getRealPath(CREATE_TABLES_FILE)));
		executeStatements(statements);
	}
	
	@Override
	public void executeScriptFiles() throws IOException, SQLException {
		// add all static sql scripts here
		ArrayList<String> scripts = new ArrayList<String>();
		scripts.add(PROC_RERANK);
		scripts.add(FUNC_PORTFOLIO_VALUE);
		scripts.add(FUNC_STOCK_SOLD);
		executeScripts(scripts);
	}
	

	@Override
	public void dataFill(String admin, String adminPassword, String consumerKey, String consumerSecret) throws SQLException, IOException {
		ArrayList<String> statements = dbScriptParser.parseFile(new File(twitstreet.getServletContext().getRealPath(DATA_FILL_FILE)));
		HashMap<String, String> parmMap = new HashMap<String, String>();
		parmMap.put(USERNAME, admin);
		parmMap.put(PASSWORD, adminPassword);
		parmMap.put(CONSUMER_KEY, consumerKey);
		parmMap.put(CONSUMER_SECRET, consumerSecret);
		statements = dbScriptParser.replaceParameters(parmMap, statements);
		executeStatements(statements);
	}
	
	private void executeScripts(ArrayList<String> scripts) throws IOException, SQLException {
		// run each script
		for (String script : scripts) {
			ScriptRunner scriptRunner = new ScriptRunner(con, false, true);
			String fullPath = this.twitstreet.getServletContext().getRealPath(script);
			scriptRunner.runScript(fullPath);
		}
	}
	
	private void executeStatements(ArrayList<String> statements) throws SQLException{
		Statement statement = con.createStatement();
		for(String statementStr : statements){
			statement.executeUpdate(statementStr);
		}
		
	}

}
