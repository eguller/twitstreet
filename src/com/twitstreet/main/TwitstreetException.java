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
