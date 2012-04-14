package com.twitstreet.twitter;

import twitter4j.Twitter;


public interface AnnouncerMgr {
	public static final String TWITSTREET_GAME = "twitstreet_game";
	public Twitter random();
	public void loadAnnouncers();
	public void announceFromAnnouncer(String message);
	public void announceFromTwitStreetGame(String message);
}
