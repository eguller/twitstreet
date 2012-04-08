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
