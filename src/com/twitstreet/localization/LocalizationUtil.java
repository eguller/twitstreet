/**
	TwitStreet - Twitter Stock Market Game
    Copyright (C) 2012  Engin Guller (bisanth@gmail.com), Cagdas Ozek (cagdasozek@gmail.com)

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
	private HashMap<String, ResourceBundle> setOfErrorDictionaries = new HashMap<String, ResourceBundle>();

	String[] languages = {"tr","en"};
	
	
	public ArrayList<String> getLanguages(){
		
		
		return new ArrayList<String>(Arrays.asList(languages));
		
	}
	public String getLanguageLongName(String shortName){
		 
		return get("language", shortName);
		
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
			String errorFileStr = this.getClass().getPackage().getName()+".error_"+language;

			
			ResourceBundle propertiesFile = PropertyResourceBundle.getBundle(propFileStr);
			ResourceBundle errorPropFile = PropertyResourceBundle.getBundle(errorFileStr);
			
			setOfDictionaries.put(language, propertiesFile);
			setOfErrorDictionaries.put(language, errorPropFile);
			
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
	
	private String get(String key, String lang, Object[] params,  HashMap<String, ResourceBundle>  map){

		if(lang==null|| lang.length()==0){
			
			lang = DEFAULT_LANGUAGE;
			
		}
		
		String value ="";
		try {
			value = map.get(lang).getString(key);
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
	
	public String get(String key, String lang){
		
		return get(key, lang, null);
		
	}	
	public String get(String key, String lang, Object param){
		
		return get(key, lang, new Object[]{param});
	}	
	public String get(String key, String lang, Object[] params){
		
		return get(key, lang, params, setOfDictionaries);
		
	}	
	public static LocalizationUtil getInstance(){
		
		return instance;
		
		
	}
	public String getError(String key, String lang){
		
		return getError(key, lang, null);
		
	}	
	public String getError(String key, String lang, Object param){
		
		return getError(key, lang, new Object[]{param});
	}	
public String getError(String key, String lang, Object[] params){
		
		return get(key, lang, params, setOfErrorDictionaries);
		
	}	
}
