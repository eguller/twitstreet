package com.twitstreet.twitter;

import java.util.ArrayList;

import com.twitstreet.db.data.Announcer;

import twitter4j.Twitter;


public interface AnnouncerMgr {
	public static final String TWITSTREET_GAME = "twitstreet_game";
	public Twitter random();
	public void loadAnnouncers();
	public void announceFromAnnouncer(String message);
	public void announceFromTwitStreetGame(String message);
	public ArrayList<Announcer> getAnnouncerDataList();
	public Announcer randomAnnouncerData();
	public void retweet();
	
}