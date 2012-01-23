package com.twitstreet.config;

public interface ConfigMgr {
	public static final String CONSUMER_KEY = "consumerKey";
	public static final String CONSUMER_SECRET = "consumerSecret";
	public static final String MIN_FOLLOWER = "minFollower";
	public static final String GA_ACCOUNT = "gaAccount";
	public void load();
	public String get(String parm);
	public String getConsumerKey();
	public String getConsumerSecret();
	public int getMinFollower();
	public String getGaAccount();
	public double getInitialMoney();
}
