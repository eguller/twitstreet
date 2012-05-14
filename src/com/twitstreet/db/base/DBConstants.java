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

package com.twitstreet.db.base;


public class DBConstants {
	
	static final String DRIVER = "com.mysql.jdbc.Driver";
	static final int VALIDATION_INTERVAL = 30000;
	static final int EVICTION_RUN_MILLIS = 30000;
	static final int MIN_EVICTABLE_IDLE_TIME = 30000;
	static final int MIN_IDLE = 10;
	static final int MAX_ACTIVE = 100;
	static final int INITIAL_SIZE = 10;
	static final String VALIDATION_QUERY = "SELECT 1";
	static final int MAX_WAIT = 10000;
	static final int ABANDONED_TIMEOUT = 60;
	
	public static String RECORD_ALREADY_EXISTS = "DB: Record already exists. - ";
	public static String QUERY_EXECUTION_SUCC = "DB: Query executed successfully - ";
	public static String QUERY_EXECUTION_FAIL ="DB: Query failed - ";
	public static String RESOURCES_NOT_CLOSED ="DB: Resources could not be closed properly";
}
