package com.twitstreet.servlet;

import com.twitstreet.db.data.Stock;

public class MinFollowerCountResponse {
	Stock stock;
	int minFollowerCount;
	
	public MinFollowerCountResponse(Stock  stock, int minFollowerCount){
		this.stock = stock;
		this.minFollowerCount = minFollowerCount;
	}
	
	public Stock getStock() {
		return stock;
	}
	public void setStock(Stock stock) {
		this.stock = stock;
	}
	public int getMinFollowerCount() {
		return minFollowerCount;
	}
	public void setMinFollowerCount(int minFollowerCount) {
		this.minFollowerCount = minFollowerCount;
	}
	
}
