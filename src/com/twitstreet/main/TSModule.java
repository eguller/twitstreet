package com.twitstreet.main;

import com.google.inject.AbstractModule;
import com.google.inject.Inject;
import com.google.inject.Scopes;
import com.twitstreet.config.ConfigMgr;
import com.twitstreet.config.ConfigMgrImpl;
import com.twitstreet.db.base.DBMgr;
import com.twitstreet.db.base.DBMgrImpl;
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
import com.twitstreet.twitter.TwitterProxy;
import com.twitstreet.twitter.TwitterProxyImpl;

public class TSModule extends AbstractModule {
	@Inject Twitstreet twitStreet;
	@Override
	protected void configure() {
		bind(Twitstreet.class).to(TwitstreetImpl.class).in(Scopes.SINGLETON);
		bind(TwitterProxy.class).to(TwitterProxyImpl.class);
		bind(UserMgr.class).to(UserMgrImpl.class);
		bind(StockMgr.class).to(StockMgrImpl.class);
		bind(TransactionMgr.class).to(TransactionMgrImpl.class);
		bind(PortfolioMgr.class).to(PortfolioMgrImpl.class);
		bind(DBSetup.class).to(DBSetupImpl.class);
		bind(DBScriptParser.class).to(DBScriptParserImpl.class);
		bind(DBMgr.class).to(DBMgrImpl.class);
		bind(ConfigMgr.class).to(ConfigMgrImpl.class);
	}
}
