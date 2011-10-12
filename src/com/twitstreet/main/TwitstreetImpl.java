package com.twitstreet.main;

import javax.servlet.ServletContext;

import com.google.inject.Singleton;


@SuppressWarnings("serial")
@Singleton
public class TwitstreetImpl implements Twitstreet {
	private boolean initialized = false;
	ServletContext servletContext;
	public TwitstreetImpl(){
		
	}
	
	@Override
	public void initialize() {
		initialized = true;
	}
	public boolean isInitialized(){
		return initialized;
	}

	@Override
	public ServletContext getServletContext() {
		return servletContext;
	}

	@Override
	public void setServletContext(ServletContext applicationPath) {
		this.servletContext = applicationPath;
	}
}
