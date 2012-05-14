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

package com.twitstreet.db.init;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import com.google.inject.Inject;
import com.twitstreet.main.Twitstreet;

public class DBSetupImpl implements DBSetup {
	private static final String CREATE_DB_FILE = "WEB-INF/db/setup/createDatabase.sql";
	private static final String CREATE_TABLES_FILE = "WEB-INF/db/setup/createTables.sql";
	private static final String DATA_FILL_FILE = "WEB-INF/db/setup/dataFill.sql";
	private static final String PROC_RERANK = "WEB-INF/db/setup/proc/rerank.sql";
	private static final String PROC_RESET_GAME = "WEB-INF/db/setup/proc/reset_game.sql";
	private static final String FUNC_PORTFOLIO_VALUE = "WEB-INF/db/setup/proc/portfolio_value.sql";
	private static final String FUNC_STOCK_SOLD = "WEB-INF/db/setup/proc/stock_sold.sql";
	private static final String FUNC_USER_PROFIT = "WEB-INF/db/setup/proc/user_profit.sql";
	private static final String FUNC_USER_STOCK_PROFIT = "WEB-INF/db/setup/proc/user_stock_profit.sql";
	private static final String FUNC_GET_STOCK_TREND_FOR_X_MINUTES = "WEB-INF/db/setup/proc/get_stock_trend_for_x_minutes.sql";
	
	
	public static String[] SCRIPT_FILES_ARRAY = {PROC_RERANK, PROC_RESET_GAME, FUNC_PORTFOLIO_VALUE,FUNC_STOCK_SOLD,FUNC_USER_PROFIT,FUNC_USER_STOCK_PROFIT,FUNC_GET_STOCK_TREND_FOR_X_MINUTES};
	public static ArrayList<String> SCRIPT_FILES = new ArrayList<String>(Arrays.asList(SCRIPT_FILES_ARRAY));
	
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

		executeScripts(SCRIPT_FILES);
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
