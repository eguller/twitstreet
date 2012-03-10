package com.twitstreet.config;


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
}
