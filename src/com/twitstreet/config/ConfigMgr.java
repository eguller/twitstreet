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
package com.twitstreet.config;

import java.util.Arrays;
import java.util.HashSet;


public interface ConfigMgr {
	public static final String CONSUMER_KEY = "consumerKey";
	public static final String CONSUMER_SECRET = "consumerSecret";
	public static final String MIN_FOLLOWER = "minFollower";
	public static final String GA_ACCOUNT = "gaAccount";
	public static final String COMISSION_TRESHOLD = "comissionTreshold";
	public static final String SERVER_COUNT = "server-count";
	public static final String SERVER_ID = "server-id";
	public static final String INITIAL_MONEY = "initialMoney";

	public static  String ANNOUNCER_CONSUMER_KEY = "announcerConsumerKey";
	public static  String ANNOUNCER_CONSUMER_SECRET = "announcerConsumerSecret";
	public static  String ANNOUNCER_ACCESS_TOKEN = "announcerAccessToken";
	public static  String ANNOUNCER_ACCESS_SECRET = "announcerAccessSecret";
	
	public static String DEFAULT_SERVER_COUNT = "1";
	
	//currently just one server is master and its id is 0.
	//replace {0} with {0,7,3,2,...} as new master servers are added
	public static HashSet<Integer> masterIdSet = new HashSet<Integer>(Arrays.asList(new Integer[]{0}));
	public static final String STAGE = "stage";
	
	public static final String DEV = "dev";
	public static final String PROD = "prod";
		
	
	public void load();
	public String get(String parm);
	public String getConsumerKey();
	public String getConsumerSecret();
	public String getAnnouncerConsumerKey();
	public String getAnnouncerConsumerSecret();
	public String getAnnouncerAccessToken();
	public String getAnnouncerAccessSecret();
	public int getMinFollower();
	public String getGaAccount();
	public double getInitialMoney();
	public int getComissionTreshold();
	public int getServerCount();
	public void setServerCount(int serverCount);
	public int getServerId();
	public void setServerId(int serverId);
	public boolean isDev();
	public void setDev(boolean dev);
	public boolean isMaster();
}
