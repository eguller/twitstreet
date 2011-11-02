package com.twitstreet.config;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.twitstreet.db.base.DBMgr;

public class ConfigMgrProvider implements Provider<ConfigMgr> {
	private final Provider<DBMgr> dbMgrProvider;
	@Inject public ConfigMgrProvider(Provider<DBMgr> dbMgrProvider) {
		this.dbMgrProvider = dbMgrProvider;
	}
	@Override
	public ConfigMgr get() {
		return new ConfigMgrImpl(dbMgrProvider.get());
	}

}
