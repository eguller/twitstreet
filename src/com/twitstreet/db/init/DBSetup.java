package com.twitstreet.db.init;

import java.io.IOException;
import java.sql.SQLException;

public interface DBSetup {
	public void openConnection(String dbIp, int dbPort, String dbUser, String dbPassword) throws ClassNotFoundException, SQLException;
	public void createDatabase(String databaseName) throws IOException, SQLException;
	public void createTables() throws IOException, SQLException;
	public void dataFill(String admin,String password, String consumerKey, String consumerSecret) throws SQLException, IOException;
	public void closeConnection() throws SQLException;
	public void executeScriptFiles() throws IOException, SQLException;
}
