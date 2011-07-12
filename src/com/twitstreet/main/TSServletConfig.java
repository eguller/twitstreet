package com.twitstreet.main;

import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.servlet.GuiceServletContextListener;
import com.google.inject.servlet.ServletModule;
import com.twitstreet.db.base.HibernateConnection;
import com.twitstreet.filter.AuthenticationFilter;
import com.twitstreet.filter.RequireAuthenticationFilter;
import com.twitstreet.market.StockMgr;
import com.twitstreet.servlet.*;
import com.twitstreet.session.SessionMgr;

import javax.servlet.ServletContextEvent;

public class TSServletConfig extends GuiceServletContextListener {
	@Override
	protected Injector getInjector() {
		return Guice.createInjector(new TSModule(), new ServletModule() {
			@Override
			protected void configureServlets() {
				filter("/a/*","/").through(AuthenticationFilter.class);
				filter("/a/*").through(RequireAuthenticationFilter.class);

				serve("/").with(HomePageServlet.class);
				serve("/login").with(LoginServlet.class);
				serve("/callback").with(CallbackServlet.class);
				serve("/logout").with(LogoutServlet.class);

				serve("/a/getstock").with(StockQuoteServlet.class);
				serve("/a/buy").with(BuyServlet.class);
				serve("/a/sell").with(SellServlet.class);
			}
		});
	}
}
