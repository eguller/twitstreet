package com.twitstreet.db.init;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;


public interface DBScriptParser {
	public ArrayList<String> parseFile(File f) throws IOException;
	public ArrayList<String> replaceParameters(Map<String, String> parmMap,
			ArrayList<String> statements);
}
