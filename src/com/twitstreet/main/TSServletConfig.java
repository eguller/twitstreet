/**
	TwitStreet - Twitter Stock Market Game
    Copyright (C) 2012  Engin Guller (bisanth@gmail.com), Cagdas Ozek (cagdasozek@gmail.com)

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
**/

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
import com.twitstreet.servlet.CreateGroupServlet;
import com.twitstreet.servlet.GetGroupServlet;
import com.twitstreet.servlet.GetQuoteServlet;
import com.twitstreet.servlet.GetUserServlet;
import com.twitstreet.servlet.GroupDetailsServlet;
import com.twitstreet.servlet.GroupListServlet;
import com.twitstreet.servlet.GroupServlet;
import com.twitstreet.servlet.HomePageServlet;
import com.twitstreet.servlet.LanguageServlet;
import com.twitstreet.servlet.LoanServlet;
import com.twitstreet.servlet.NewUsersServlet;
import com.twitstreet.servlet.PortfolioServlet;
import com.twitstreet.servlet.SeasonServlet;
import com.twitstreet.servlet.SellServlet;
import com.twitstreet.servlet.SigninServlet;
import com.twitstreet.servlet.SignoutServlet;
import com.twitstreet.servlet.StockDetailsServlet;
import com.twitstreet.servlet.StockDistributionServlet;
import com.twitstreet.servlet.TopGrossingStocksServlet;
import com.twitstreet.servlet.TopRankGroupServlet;
import com.twitstreet.servlet.TopRankServlet;
import com.twitstreet.servlet.TransactionServlet;
import com.twitstreet.servlet.TrendyStocksServlet;
import com.twitstreet.servlet.TrendyUsersServlet;
import com.twitstreet.servlet.UserHistoryServlet;
import com.twitstreet.servlet.UserProfileServlet;
import com.twitstreet.servlet.WatchListServlet;


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
				//serve("/getQuote").with(CallBackServlet.class);				
				serve("/a/buy").with(BuyServlet.class);
				serve("/a/sell").with(SellServlet.class);
				serve("/toprank").with(TopRankServlet.class);
				serve("/toprankgroup").with(TopRankGroupServlet.class);
				serve("/getquote").with(GetQuoteServlet.class);
				serve("/getuser").with(GetUserServlet.class);
				serve("/stockdistribution").with(StockDistributionServlet.class);
				serve("/userhistory").with(UserHistoryServlet.class);
				serve("/portfolio").with(PortfolioServlet.class);
				serve("/transaction").with(TransactionServlet.class);
				serve("/balance").with(BalanceServlet.class);
				serve("/stock").with(StockDetailsServlet.class);
				serve("/user").with(UserProfileServlet.class);
				serve("/topgrossingstocks").with(TopGrossingStocksServlet.class);
				serve("/suggestedstocks").with(TrendyStocksServlet.class);
				serve("/trendyusers").with(TrendyUsersServlet.class);
				serve("/newusers").with(NewUsersServlet.class);
				serve("/season").with(SeasonServlet.class);
				serve("/getgroup").with(GetGroupServlet.class);
				serve("/groupdetails").with(GroupDetailsServlet.class);
				serve("/group").with(GroupServlet.class);
				serve("/grouplist").with(GroupListServlet.class);
				serve("/creategroup").with(CreateGroupServlet.class);
				serve("/loan").with(LoanServlet.class);

				serve("/lang").with(LanguageServlet.class);
				
				serve("/watchlist").with(WatchListServlet.class);
				serve("/signout").with(SignoutServlet.class);
			}
		});
	}
}
