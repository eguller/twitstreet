package com.twitstreet.main;

import java.io.FileReader;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.jcache.JCache;

import com.google.inject.AbstractModule;
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
import com.twitstreet.session.SessionMgr;
import com.twitstreet.session.SessionMgrImpl;
import com.twitstreet.twitter.TwitterAuth;
import com.twitstreet.twitter.TwitterAuthImpl;
import com.twitstreet.twitter.TwitterProxy;
import com.twitstreet.twitter.TwitterProxyImpl;

public class TSModule extends AbstractModule {

	@Override
	protected void configure() {
		bindPropertiesFile(System.getProperty("user.home")+"/.twitstreet/app.properties");
		bind(HibernateConnection.class).toProvider(ConnectionProvider.class).in(Scopes.SINGLETON);  
		bind(TwitterProxy.class).to(TwitterProxyImpl.class);
		bind(TwitterAuth.class).to(TwitterAuthImpl.class);
		bind(SessionMgr.class).to(SessionMgrImpl.class);
		bind(StockMgr.class).to(StockMgrImpl.class);
		bind(TransactionMgr.class).to(TransactionMgrImpl.class);
		bind(PortfolioMgr.class).to(PortfolioMgrImpl.class);
	}

	private void bindPropertiesFile(String propFileName) {
		try {
			Properties props = new Properties();
			props.load(new FileReader(propFileName));
			for(Entry<Object, Object> entry:props.entrySet()) {
				String name = (String) entry.getKey();
				String value = (String) entry.getValue();
				bindConstant().annotatedWith(Names.named(name)).to(value);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@SuppressWarnings("unchecked")
	@Provides
	@Named("twitter.oauth.request")
	Map<String,String> getTwitterOauthRequestMap() {
		CacheManager cacheManager = CacheManager.create();
		Cache cache = cacheManager.getCache("twitter.oauth.request");
		return new JCache(cache);
	}
	
	//TODO: cache provider
}
