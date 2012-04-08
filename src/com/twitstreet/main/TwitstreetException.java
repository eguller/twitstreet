package com.twitstreet.main;

import com.twitstreet.localization.LocalizationUtil;

public class TwitstreetException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	String className;
	String method;
	int number;
	Object[] parameters=null;
	
	
	public TwitstreetException(String className, String method, int number){
		
		this.className = className;
		this.method = method;
		this.number = number;
		
	}
	public TwitstreetException(String className, String method, int number,Object[] parameters){
		
		this.className = className;
		this.method = method;
		this.number = number;
		this.parameters = parameters;
		
	}
	public String getLocalizedMessage(String lang){
		
		String errorKey = className+"."+method+"."+number;
		
		return LocalizationUtil.getInstance().getError(errorKey,lang,parameters);
	
		
	}
}
