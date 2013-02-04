/**
	TwitStreet - Twitter Stock Market Game
    Copyright (C) 2012  Engin Guller (bisanthe@gmail.com), Cagdas Ozek (cagdasozek@gmail.com)

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


package com.twitstreet.market;

import java.util.List;

import com.twitstreet.db.data.Stock;
import com.twitstreet.db.data.TransactionRecord;
import com.twitstreet.db.data.User;

public interface TransactionMgr {
	public static final int CURRENT_TRANSACTION_LIMIT = 10;
	public static final int BUY = 1;
	public static final int SELL = 0;
	public void recordTransaction(User user, Stock stock, int amount, int operation);
	public List<TransactionRecord> getCurrentTransactions();
	public List<TransactionRecord> queryTransactionRecord(long userId);
	public List<TransactionRecord> queryTransactionRecordByStock(long stockId);
	public List<TransactionRecord> getCurrentTransactionsFromDb();
	
}
