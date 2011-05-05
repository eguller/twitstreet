package com.twitstreet.main;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.servlet.GuiceServletContextListener;
import com.google.inject.servlet.ServletModule;
import com.twitstreet.filter.AuthenticationFilter;
import com.twitstreet.servlet.HomePageServlet;
import com.twitstreet.servlet.LogoutServlet;

public class TSServletConfig extends GuiceServletContextListener {

	@Override
	protected Injector getInjector() {
		return Guice.createInjector(new TSModule(), new ServletModule() {
			@Override
			protected void configureServlets() {
				serve("/").with(HomePageServlet.class);
				serve("/logout").with(LogoutServlet.class);
				filter("/p/*").through(AuthenticationFilter.class);
			}
		});
	}

}
