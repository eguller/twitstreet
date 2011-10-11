package com.twitstreet.main;

import com.google.inject.Singleton;


@SuppressWarnings("serial")
@Singleton
public class TwitstreetImpl implements Twitstreet {
	private boolean initialized = false;
	@Override
	public void initialize() {
		initialized = true;
	}
	public boolean isInitialized(){
		return initialized;
	}
}
