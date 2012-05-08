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

import com.google.inject.AbstractModule;
import com.google.inject.Inject;
import com.google.inject.Scopes;
import com.google.inject.assistedinject.FactoryModuleBuilder;
import com.twitstreet.config.ConfigMgr;
import com.twitstreet.config.ConfigMgrProvider;
import com.twitstreet.db.base.DBMgr;
import com.twitstreet.db.base.DBMgrProvider;
import com.twitstreet.db.init.DBScriptParser;
import com.twitstreet.db.init.DBScriptParserImpl;
import com.twitstreet.db.init.DBSetup;
import com.twitstreet.db.init.DBSetupImpl;
import com.twitstreet.mail.MailMgr;
import com.twitstreet.mail.MailMgrImpl;
import com.twitstreet.market.PortfolioMgr;
import com.twitstreet.market.PortfolioMgrImpl;
import com.twitstreet.market.StockMgr;
import com.twitstreet.market.StockMgrImpl;
import com.twitstreet.market.TransactionMgr;
import com.twitstreet.market.TransactionMgrImpl;
import com.twitstreet.season.SeasonMgr;
import com.twitstreet.season.SeasonMgrImpl;
import com.twitstreet.session.GroupMgr;
import com.twitstreet.session.GroupMgrImpl;
import com.twitstreet.session.UserMgr;
import com.twitstreet.session.UserMgrImpl;
import com.twitstreet.task.StockUpdateTask;
import com.twitstreet.task.TruncateHistoryTask;
import com.twitstreet.task.TruncateLogsTask;
import com.twitstreet.twitter.AdsListenerMgr;
import com.twitstreet.twitter.AdsListenerMgrImpl;
import com.twitstreet.twitter.AnnouncerMgr;
import com.twitstreet.twitter.AnnouncerMgrImpl;
import com.twitstreet.twitter.FollowBackMgr;
import com.twitstreet.twitter.FollowBackMgrImpl;
import com.twitstreet.twitter.TwitstreetAnnouncer;
import com.twitstreet.twitter.TwitstreetAnnouncerImpl;
import com.twitstreet.twitter.TwitterProxy;
import com.twitstreet.twitter.TwitterProxyFactory;
import com.twitstreet.twitter.TwitterProxyImpl;
import com.twitstreet.twitter.Welcome2ListenerMgr;
import com.twitstreet.twitter.Welcome2ListenerMgrImpl;

public class TSModule extends AbstractModule {
	@Inject Twitstreet twitStreet;
	@Override
	protected void configure() {
	
		

		bind(MailMgr.class).to(MailMgrImpl.class);
		bind(TruncateLogsTask.class).in(Scopes.SINGLETON);
		bind(TwitstreetAnnouncer.class).to(TwitstreetAnnouncerImpl.class).in(Scopes.SINGLETON);
		bind(GroupMgr.class).to(GroupMgrImpl.class).in(Scopes.SINGLETON);
		bind(Twitstreet.class).to(TwitstreetImpl.class).in(Scopes.SINGLETON);
		bind(UserMgr.class).to(UserMgrImpl.class);
		bind(StockMgr.class).to(StockMgrImpl.class);
		bind(PortfolioMgr.class).to(PortfolioMgrImpl.class);
		bind(DBSetup.class).to(DBSetupImpl.class);
		bind(DBScriptParser.class).to(DBScriptParserImpl.class);
		bind(DBMgr.class).toProvider(DBMgrProvider.class).in(Scopes.SINGLETON);
		bind(ConfigMgr.class).toProvider(ConfigMgrProvider.class).in(Scopes.SINGLETON);
		bind(StockUpdateTask.class).in(Scopes.SINGLETON);
		bind(TransactionMgr.class).to(TransactionMgrImpl.class).in(Scopes.SINGLETON);
		bind(AdsListenerMgr.class).to(AdsListenerMgrImpl.class).in(Scopes.SINGLETON);
		bind(AnnouncerMgr.class).to(AnnouncerMgrImpl.class).in(Scopes.SINGLETON);
		bind(Welcome2ListenerMgr.class).to(Welcome2ListenerMgrImpl.class).in(Scopes.SINGLETON);
		bind(SeasonMgr.class).to(SeasonMgrImpl.class).in(Scopes.SINGLETON);
		bind(FollowBackMgr.class).to(FollowBackMgrImpl.class).in(Scopes.SINGLETON);
		bind(TruncateHistoryTask.class).in(Scopes.SINGLETON);
		install(new FactoryModuleBuilder()
	     .implement(TwitterProxy.class, TwitterProxyImpl.class)
	     .build(TwitterProxyFactory.class));
	}
}
