package com.twitstreet.main;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.servlet.GuiceServletContextListener;
import com.google.inject.servlet.ServletModule;
import com.twitstreet.filter.AuthenticationFilter;
import com.twitstreet.servlet.*;


public class TSServletConfig extends GuiceServletContextListener {
	@Override
	protected Injector getInjector() {
		return Guice.createInjector(new TSModule(), new ServletModule() {
			@Override
			protected void configureServlets() {
				filter("/a/*").through(AuthenticationFilter.class);

				serve("/").with(HomePageServlet.class);
				serve("/signin").with(SigninServlet.class);
				serve("/signout").with(SignoutServlet.class);
				serve("/callback").with(CallBackServlet.class);

				serve("/a/getstock").with(StockQuoteServlet.class);
				serve("/a/buy").with(BuyServlet.class);
				serve("/a/sell").with(SellServlet.class);
			}
		});
	}
}
