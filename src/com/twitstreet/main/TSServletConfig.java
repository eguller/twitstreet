package com.twitstreet.main;

import java.io.File;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.servlet.GuiceServletContextListener;
import com.google.inject.servlet.ServletModule;
import com.twitstreet.servlet.BalanceServlet;
import com.twitstreet.servlet.BuyServlet;
import com.twitstreet.servlet.CallBackServlet;
import com.twitstreet.servlet.HomePageServlet;
import com.twitstreet.servlet.PortfolioServlet;
import com.twitstreet.servlet.SellServlet;
import com.twitstreet.servlet.SetupServlet;
import com.twitstreet.servlet.SigninServlet;
import com.twitstreet.servlet.SignoutServlet;
import com.twitstreet.servlet.StockQuoteServlet;
import com.twitstreet.servlet.TopRankServlet;
import com.twitstreet.servlet.TransactionServlet;


public class TSServletConfig extends GuiceServletContextListener {
	public void contextInitialized(ServletContextEvent servletContextEvent)  
    {  
		//turnoff twitter4j logging
		System.setProperty ("twitter4j.loggerFactory", 
		"twitter4j.internal.logging.NullLoggerFactory");
		Injector injector = getInjector();
		Twitstreet twitStreet = injector.getInstance(Twitstreet.class);
		ServletContext servletContext = servletContextEvent.getServletContext();
		twitStreet.setServletContext(servletContext);
		servletContext.setAttribute(Injector.class.getName(), injector);
		File f = new File(System.getProperty("user.home") + "/.twitstreet/twitstreet.properties");
		if (f.exists()) {
			twitStreet.initialize();
		}
		
		ReRankTask reRankTask = injector.getInstance(ReRankTask.class);
		StockUpdateTask updateFollowerCountTask = injector.getInstance(StockUpdateTask.class);
		
		Thread reRankThread = new Thread(reRankTask);
		reRankThread.setName("Re-Rank");
		reRankThread.start();
		
		Thread updateFollowerCountThread = new Thread (updateFollowerCountTask);
		updateFollowerCountThread.setName("Update Follower Count");
		updateFollowerCountThread.start();
    }   
	@Override
	protected Injector getInjector() {
		return Guice.createInjector(new TSModule(), new ServletModule() {
			@Override
			protected void configureServlets() {
				serve("/").with(HomePageServlet.class);
				serve("/signin").with(SigninServlet.class);
				serve("/signout").with(SignoutServlet.class);
				serve("/callback").with(CallBackServlet.class);
				serve("/setup").with(SetupServlet.class);

				serve("/getquote").with(StockQuoteServlet.class);
				serve("/a/buy").with(BuyServlet.class);
				serve("/a/sell").with(SellServlet.class);
				serve("/toprank").with(TopRankServlet.class);
				serve("/portfolio").with(PortfolioServlet.class);
				serve("/transaction").with(TransactionServlet.class);
				serve("/balance").with(BalanceServlet.class);
			}
		});
	}
}
