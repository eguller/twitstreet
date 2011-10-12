package com.twitstreet.main;

import javax.servlet.ServletContext;

public interface Twitstreet {
	public void initialize();
	public boolean isInitialized();
	public ServletContext getServletContext();
	public void setServletContext(ServletContext servletContext);
}
