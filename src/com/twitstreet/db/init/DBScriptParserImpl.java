package com.twitstreet.db.init;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

import com.google.inject.Singleton;

@Singleton
public class DBScriptParserImpl implements DBScriptParser {
	public ArrayList<String> parseFile(File f) throws IOException {
		ArrayList<String> statements = new ArrayList<String>();
		FileReader fr = new FileReader(f);
		BufferedReader br = new BufferedReader(fr);
		String line = null;
		StringBuilder builder = new StringBuilder();
		while ((line = br.readLine()) != null) {
			if (!line.trim().startsWith("--")) {
				builder.append(line);
				if (line.endsWith(";")) {
					statements.add(builder.toString());
					builder = new StringBuilder();
				}
			}
		}
		return statements;
	}

	@Override
	public ArrayList<String> replaceParameters(Map<String, String> parmMap,
			ArrayList<String> statements) {
		ArrayList<String> statementList = new ArrayList<String>();
		for (String statement : statements) {
			for (String key : parmMap.keySet()) {
				statement = statement.replace("{"+key+"}", parmMap.get(key));
			}
			statementList.add(statement);
		}
		return statementList;
	}
}
