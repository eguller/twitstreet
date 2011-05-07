package com.twitstreet.main;

import javax.servlet.ServletContextEvent;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.servlet.GuiceServletContextListener;
import com.google.inject.servlet.ServletModule;
import com.twitstreet.filter.AuthenticationFilter;
import com.twitstreet.servlet.GetTwitUser;
import com.twitstreet.servlet.HomePageServlet;
import com.twitstreet.servlet.LogoutServlet;
import com.twitstreet.twitter.TwitterConnection;

public class TSServletConfig extends GuiceServletContextListener {

	private Injector injector;
	
	@Override
	protected Injector getInjector() {
		injector = Guice.createInjector(new TSModule(), new ServletModule() {
			@Override
			protected void configureServlets() {
				serve("/").with(HomePageServlet.class);
				serve("/logout").with(LogoutServlet.class);
				serve("/a/gettwituser").with(GetTwitUser.class);
				filter("/a/*").through(AuthenticationFilter.class);
			}
		});
		return injector;
	}
	
	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
	}

	@Override
	public void contextInitialized(ServletContextEvent event) {
		super.contextInitialized(event);
		injector.getInstance(TwitterConnection.class).init();
	}

}
