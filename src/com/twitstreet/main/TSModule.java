package com.twitstreet.main;

import java.io.File;
import java.io.FileReader;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.jcache.JCache;

import com.google.inject.AbstractModule;
import com.google.inject.Inject;
import com.google.inject.Provides;
import com.google.inject.Scopes;
import com.google.inject.name.Named;
import com.google.inject.name.Names;
import com.twitstreet.db.base.ConnectionProvider;
import com.twitstreet.db.base.HibernateConnection;
import com.twitstreet.market.PortfolioMgr;
import com.twitstreet.market.PortfolioMgrImpl;
import com.twitstreet.market.StockMgr;
import com.twitstreet.market.StockMgrImpl;
import com.twitstreet.market.TransactionMgr;
import com.twitstreet.market.TransactionMgrImpl;
import com.twitstreet.session.UserMgr;
import com.twitstreet.session.UserMgrImpl;
import com.twitstreet.twitter.TwitterProxy;
import com.twitstreet.twitter.TwitterProxyImpl;

public class TSModule extends AbstractModule {
	@Inject Twitstreet twitStreet;
	@Override
	protected void configure() {
		//bindPropertiesFile(System.getProperty("user.home")
		//		+ "/.twitstreet/app.properties");
		bind(HibernateConnection.class).toProvider(ConnectionProvider.class)
				.in(Scopes.SINGLETON);
		bind(Twitstreet.class).to(TwitstreetImpl.class).in(Scopes.SINGLETON);
		bind(TwitterProxy.class).to(TwitterProxyImpl.class);
		bind(UserMgr.class).to(UserMgrImpl.class);
		bind(StockMgr.class).to(StockMgrImpl.class);
		bind(TransactionMgr.class).to(TransactionMgrImpl.class);
		bind(PortfolioMgr.class).to(PortfolioMgrImpl.class);
	}

	private void bindPropertiesFile(String propFileName) {
		try {
			Properties props = new Properties();
			props.load(new FileReader(propFileName));
			for (Entry<Object, Object> entry : props.entrySet()) {
				String name = (String) entry.getKey();
				String value = (String) entry.getValue();
				bindConstant().annotatedWith(Names.named(name)).to(value);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
