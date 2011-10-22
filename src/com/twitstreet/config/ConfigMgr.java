package com.twitstreet.config;

public interface ConfigMgr {
	public static final String CONSUMER_KEY = "consumerKey";
	public static final String CONSUMER_SECRET = "consumerSecret";
	public void load();
	public String get(String parm);
	public String getConsumerKey();
	public String getConsumerSecret();
}
