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

import javax.servlet.ServletContext;

import com.google.inject.Injector;

public interface Twitstreet {
	public static final String PROPERTIES_COMMENT = "Twitstreet properties";
	public static final String DB_HOST = "dbhost";
	public static final String DB_PORT = "dbport";
	public static final String DB_ADMIN = "dbadmin";
	public static final String DB_PASSWORD = "dbpassword";
	public static final String DATABASE = "database";
	public static final String MAIL_DEALER = "mailDealer";
	public static final String MAIL_DEALER_PWD = "mailDealerPassword";
	public static final String MAIL_RECIPIENTS = "mailRecipients";
	public static final String TWITSTREET_PROPERTIES = System.getProperty("user.home") + "/.twitstreet/twitstreet.properties";
	
	 
	public void initialize();
	public boolean isInitialized();
	public ServletContext getServletContext();
	public void setServletContext(ServletContext servletContext);
	public void setInjector(Injector injector);
}
