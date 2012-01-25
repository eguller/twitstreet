package com.twitstreet.main;

import com.google.inject.AbstractModule;
import com.google.inject.Inject;
import com.google.inject.Scopes;
import com.google.inject.assistedinject.FactoryModuleBuilder;
import com.twitstreet.cache.TransactionCache;
import com.twitstreet.cache.TransactionCacheImpl;
import com.twitstreet.config.ConfigMgr;
import com.twitstreet.config.ConfigMgrProvider;
import com.twitstreet.db.base.DBMgr;
import com.twitstreet.db.base.DBMgrProvider;
import com.twitstreet.db.init.DBScriptParser;
import com.twitstreet.db.init.DBScriptParserImpl;
import com.twitstreet.db.init.DBSetup;
import com.twitstreet.db.init.DBSetupImpl;
import com.twitstreet.market.PortfolioMgr;
import com.twitstreet.market.PortfolioMgrImpl;
import com.twitstreet.market.StockMgr;
import com.twitstreet.market.StockMgrImpl;
import com.twitstreet.market.TransactionMgr;
import com.twitstreet.market.TransactionMgrImpl;
import com.twitstreet.session.UserMgr;
import com.twitstreet.session.UserMgrImpl;
import com.twitstreet.task.AsyncQuery;
import com.twitstreet.task.AsyncQueryTask;
import com.twitstreet.task.ReRankTask;
import com.twitstreet.task.StockUpdateTask;
import com.twitstreet.twitter.TwitterProxy;
import com.twitstreet.twitter.TwitterProxyFactory;
import com.twitstreet.twitter.TwitterProxyImpl;

public class TSModule extends AbstractModule {
	@Inject Twitstreet twitStreet;
	@Override
	protected void configure() {
		bind(Twitstreet.class).to(TwitstreetImpl.class).in(Scopes.SINGLETON);
		bind(UserMgr.class).to(UserMgrImpl.class);
		bind(StockMgr.class).to(StockMgrImpl.class);
		bind(PortfolioMgr.class).to(PortfolioMgrImpl.class);
		bind(DBSetup.class).to(DBSetupImpl.class);
		bind(DBScriptParser.class).to(DBScriptParserImpl.class);
		bind(DBMgr.class).toProvider(DBMgrProvider.class).in(Scopes.SINGLETON);
		bind(ConfigMgr.class).toProvider(ConfigMgrProvider.class).in(Scopes.SINGLETON);
		bind(ReRankTask.class).in(Scopes.SINGLETON);
		bind(StockUpdateTask.class).in(Scopes.SINGLETON);
		//bind(AsyncQueryTask.class).in(Scopes.SINGLETON);
		bind(TransactionMgr.class).to(TransactionMgrImpl.class).in(Scopes.SINGLETON);
		bind(AsyncQuery.class).to(AsyncQueryTask.class).in(Scopes.SINGLETON);
		bind(TransactionCache.class).to(TransactionCacheImpl.class).in(Scopes.SINGLETON);
		install(new FactoryModuleBuilder()
	     .implement(TwitterProxy.class, TwitterProxyImpl.class)
	     .build(TwitterProxyFactory.class));
	}
}
