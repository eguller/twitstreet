package com.twitstreet.main;

import java.io.File;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;

import org.apache.log4j.Logger;

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
import com.twitstreet.servlet.StockDetailsServlet;
import com.twitstreet.servlet.StockQuoteServlet;
import com.twitstreet.servlet.TopRankServlet;
import com.twitstreet.servlet.TransactionServlet;
import com.twitstreet.servlet.UserProfileServlet;


public class TSServletConfig extends GuiceServletContextListener {
	private static Logger logger = Logger.getLogger(TSServletConfig.class);
	public void contextInitialized(ServletContextEvent servletContextEvent)  
    {  
		//turnoff twitter4j logging
		System.setProperty ("twitter4j.loggerFactory", 
		"twitter4j.internal.logging.NullLoggerFactory");
		Injector injector = getInjector();
		Twitstreet twitStreet = injector.getInstance(Twitstreet.class);
		ServletContext servletContext = servletContextEvent.getServletContext();
		twitStreet.setServletContext(servletContext);
		twitStreet.setInjector(injector);
		servletContext.setAttribute(Injector.class.getName(), injector);
		String fileLocation = System.getProperty("user.home") + "/.twitstreet/twitstreet.properties";
		File f = new File(fileLocation);
		logger.debug("Checking config file at: " + fileLocation);
		if (f.exists()) {
			twitStreet.initialize();
			logger.info(" Config file exist. Twitstreet initialization completed.");
		}
		else{
			logger.info(" Config does not exist at " + fileLocation);
		}
		

    }   
	@Override
	protected Injector getInjector() {
		return Guice.createInjector(new TSModule(), new ServletModule() {
			@Override
			protected void configureServlets() {
				serve("/").with(HomePageServlet.class);
				serve("/signin").with(SigninServlet.class);
				serve("/callback").with(CallBackServlet.class);
				serve("/setup").with(SetupServlet.class);
				serve("/getquote").with(StockQuoteServlet.class);
				serve("/a/buy").with(BuyServlet.class);
				serve("/a/sell").with(SellServlet.class);
				serve("/toprank").with(TopRankServlet.class);
				serve("/portfolio").with(PortfolioServlet.class);
				serve("/transaction").with(TransactionServlet.class);
				serve("/balance").with(BalanceServlet.class);
				serve("/stock").with(StockDetailsServlet.class);
				serve("/user").with(UserProfileServlet.class);
			}
		});
	}
}
