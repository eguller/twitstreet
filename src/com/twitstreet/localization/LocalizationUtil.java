package com.twitstreet.localization;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

public class LocalizationUtil {

	public static String DEFAULT_LANGUAGE = "en";



	private static LocalizationUtil instance = new LocalizationUtil();



	public static String LANGUAGE = "lang";

	
	
	private HashMap<String, ResourceBundle> setOfDictionaries = new HashMap<String, ResourceBundle>();

	String[] languages = {"tr","en"};
	
	
	public ArrayList<String> getLanguages(){
		
		
		return new ArrayList<String>(Arrays.asList(languages));
		
	}
	public String getLanguageLongName(String shortName){
		 
		if("en".equalsIgnoreCase(shortName)){
			return "English";
		}else if("tr".equalsIgnoreCase(shortName)){
			return "Türkçe";
		}
		return "";
		
	}
	public boolean checkLanguageIsValid(String lang){
		
		for(String lan : languages){
			
			if(lan.equalsIgnoreCase(lang)){
				return true;
				
			}
			
			
		}
		return false;
		
	}
	
	private LocalizationUtil() {		
		
		for(String language : languages){
			
			String propFileStr = this.getClass().getPackage().getName()+"."+language;

			ResourceBundle propertiesFile = PropertyResourceBundle.getBundle(propFileStr);
			
			setOfDictionaries.put(language, propertiesFile);
			
		}
	
		
	}
	public ArrayList<String> getStartsWith(String key, String lang){
		
		ArrayList<String> valueList = new ArrayList<String>();

		if(lang==null|| lang.length()==0){
			
			lang = DEFAULT_LANGUAGE;
			
		}
		
	
		try {
			ArrayList<String> keyList = new ArrayList<String>();
			 Enumeration<String> strList = setOfDictionaries.get(lang).getKeys();
			 
			 while(strList.hasMoreElements()){
				 String aKey = strList.nextElement();
				 
				 if(aKey.startsWith(key)){
					 keyList.add(aKey);
					
				 }
				 
			 }
			 
			 Collections.sort(keyList);
			 
			 for(String sortedKey: keyList){
				 
				 valueList.add(setOfDictionaries.get(lang).getString(sortedKey));
			 }
			 
			 
			
		
		} catch (Exception ex) {

		}
		
		return valueList;
		
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
