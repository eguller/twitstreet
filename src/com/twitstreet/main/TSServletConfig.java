package com.twitstreet.main;

import javax.servlet.ServletContextEvent;

import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.servlet.GuiceServletContextListener;
import com.google.inject.servlet.ServletModule;
import com.twitstreet.filter.AuthenticationFilter;
import com.twitstreet.servlet.FollowerQuoteServlet;
import com.twitstreet.servlet.HomePageServlet;
import com.twitstreet.servlet.LogoutServlet;
import com.twitstreet.twitter.api.TwitterConnection;

public class TSServletConfig extends GuiceServletContextListener {

	TwitterConnection twitterConnection = getInjector().getInstance(TwitterConnection.class);
	
	@Override
	protected Injector getInjector() {
		return Guice.createInjector(new TSModule(), new ServletModule() {
			@Override
			protected void configureServlets() {
				serve("/").with(HomePageServlet.class);
				serve("/logout").with(LogoutServlet.class);
				serve("/a/gettwituser").with(FollowerQuoteServlet.class);
				filter("/a/*").through(AuthenticationFilter.class);
			}
		});
	}
	
	@Override
	public void contextDestroyed(ServletContextEvent arg0) {

	}

	@Override
	public void contextInitialized(ServletContextEvent arg0) {
		twitterConnection.init();
	}

}
