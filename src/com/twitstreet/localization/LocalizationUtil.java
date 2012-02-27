package com.twitstreet.localization;

import java.util.HashMap;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

public class LocalizationUtil {

	private static String DEFAULT_LANGUAGE = "en";



	private static LocalizationUtil instance = new LocalizationUtil();



	public static String LANGUAGE = "lang";

	
	
	private HashMap<String, ResourceBundle> setOfDictionaries = new HashMap<String, ResourceBundle>();

	String[] languages = {"tr","en"};
	
	private LocalizationUtil() {		
		
		for(String language : languages){
			
			String propFileStr = this.getClass().getPackage().getName()+"."+language;

			ResourceBundle propertiesFile = PropertyResourceBundle.getBundle(propFileStr);
			
			setOfDictionaries.put(language, propertiesFile);
			
		}
	
		
	}
	
	public String get(String key, String lang){
		
		return get(key, lang, null);
		
	}	
	public String get(String key, String lang, Object param){
		
		return get(key, lang, new Object[]{param});
	}	
	public String get(String key, String lang, Object[] params){
		
		
		
		
		if(lang==null|| lang.length()==0){
			
			lang = DEFAULT_LANGUAGE;
			
		}
		
		String value ="";
		try {
			value = setOfDictionaries.get(lang).getString(key);
		} catch (Exception ex) {

		}
		if (params != null && params.length > 0) {
			int i = 0;
			for (Object obj : params) {

				String iStr = "{" + i + "}";

				value = value.replace(iStr, obj.toString());

				i++;

			}
		}
		
		return value;
		
	}	
	public static LocalizationUtil getInstance(){
		
		return instance;
		
		
	}
	
}
