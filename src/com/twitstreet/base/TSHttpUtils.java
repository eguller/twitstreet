package com.twitstreet.base;

import java.util.HashMap;
import java.util.Map;

public class TSHttpUtils {
	public static Map<String,String> parseQueryParms(String query) {
		String[] params = query.split("&");  
	    Map<String, String> map = new HashMap<String, String>();  
	    for (String param : params) {
	    	int index = param.indexOf('=');
	    	if(index>0) {
	    		String key = param.substring(0, index);
	    		String value = param.substring(index+1);
		    	map.put(key,value);
	    	}
	    }
	    return map;
	}
}
