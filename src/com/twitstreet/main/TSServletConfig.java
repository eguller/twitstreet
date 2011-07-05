package com.twitstreet.main;

import javax.servlet.ServletContextEvent;

import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.servlet.GuiceServletContextListener;
import com.google.inject.servlet.ServletModule;
import com.twitstreet.db.base.HibernateConnection;
import com.twitstreet.filter.AuthenticationFilter;
import com.twitstreet.filter.RequireAuthenticationFilter;
import com.twitstreet.market.StockMgr;
import com.twitstreet.servlet.CallbackServlet;
import com.twitstreet.servlet.StockQuoteServlet;
import com.twitstreet.servlet.HomePageServlet;
import com.twitstreet.servlet.LoginServlet;
import com.twitstreet.servlet.LogoutServlet;

public class TSServletConfig extends GuiceServletContextListener {
	HibernateConnection connection;
	@Inject StockMgr stockMgr;
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

				serve("/a/gettwituser").with(StockQuoteServlet.class);
			}
		});
	}
	
	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
	}
	
	@Override
	public void contextInitialized(ServletContextEvent servletContextEvent){
		connection = getInjector().getInstance(HibernateConnection.class);  
        stockMgr = getInjector().getInstance(StockMgr.class);
		connection.connect();
        stockMgr.setConnection(connection);
	}

}
