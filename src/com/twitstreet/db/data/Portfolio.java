package com.twitstreet.db.data;

import java.util.ArrayList;

public class Portfolio {
	UserDO user;
	ArrayList<StockDO> stockList;
	
	public Portfolio(UserDO user, ArrayList<StockDO> stockList){
		this.user = user;
		this.stockList = stockList;
	}
	
	public Portfolio(ArrayList<UserStockDO> userStock){
		if(userStock.size() > 0){
			this.user = userStock.get(0).getUser();
		}
		stockList = new ArrayList<StockDO>(userStock.size());
		
		for(UserStockDO userStockDO : userStock){
			stockList.add(userStockDO.getStock());
		}
	}
}
