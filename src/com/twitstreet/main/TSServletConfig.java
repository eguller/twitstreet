package com.twitstreet.main;

import java.io.File;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.servlet.GuiceServletContextListener;
import com.google.inject.servlet.ServletModule;
import com.twitstreet.filter.AuthenticationFilter;
import com.twitstreet.servlet.*;


public class TSServletConfig extends GuiceServletContextListener {
	public void contextInitialized(ServletContextEvent servletContextEvent)  
    {  
		//turnoff twitter4j logging
		System.setProperty ("twitter4j.loggerFactory", 
		"twitter4j.internal.logging.NullLoggerFactory"); 
		Twitstreet twitStreet = getInjector().getInstance(Twitstreet.class);
		ServletContext servletContext = servletContextEvent.getServletContext();
		twitStreet.setServletContext(servletContext);
		File f = new File(System.getProperty("user.home") + "/.twitstreet/twitstreet.properties");
		if (f.exists()) {
			twitStreet.initialize();
		}
    }   
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
				serve("/setup").with(SetupServlet.class);

				serve("/getquote").with(StockQuoteServlet.class);
				serve("/a/buy").with(BuyServlet.class);
				serve("/a/sell").with(SellServlet.class);
			}
		});
	}
}
