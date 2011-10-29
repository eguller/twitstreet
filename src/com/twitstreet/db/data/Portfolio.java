package com.twitstreet.db.data;

import java.util.ArrayList;

public class Portfolio {
	User user;
	ArrayList<Stock> stockList;
	
	public Portfolio(User user, ArrayList<Stock> stockList){
		this.user = user;
		this.stockList = stockList;
	}
	
	public Portfolio(ArrayList<UserStock> userStock){
		if(userStock.size() > 0){
			this.user = userStock.get(0).getUser();
		}
		stockList = new ArrayList<Stock>(userStock.size());
		
		for(UserStock userStockDO : userStock){
			stockList.add(userStockDO.getStock());
		}
	}
}
