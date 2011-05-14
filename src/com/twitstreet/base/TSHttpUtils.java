package com.twitstreet.base;

import java.util.HashMap;
import java.util.Map;

public class TSHttpUtils {
	public static Map<String,String> parseQueryParms(String query) {
		String[] params = query.split("&");  
	    Map<String, String> map = new HashMap<String, String>();  
	    for (String param : params) {
	    	String[] pair = param.split("=");
	    	map.put(pair[0],pair[1]);
	    }
	    return map;
	}
}
