package com.twitstreet.db.base;

import com.google.inject.Provider;

public class DBMgrProvider implements Provider<DBMgr> {

	@Override
	public DBMgr get() {
		return new DBMgrImpl();
	}

}
