package com.twitstreet.twitter;

public interface TwitterProxy {
	public int getFollowerCount(String userId);
	public String[] getAccessTokenPair(String bridgeCode);
}
