package com.twitstreet.servlet;

import java.util.ArrayList;

import com.twitstreet.db.data.Stock;
import com.twitstreet.twitter.SimpleTwitterUser;

public class QuoteResponse {
	Stock stock;
	double percentage;
	ArrayList<SimpleTwitterUser> searchResultList;
	public QuoteResponse(Stock stock, double percentage, ArrayList<SimpleTwitterUser> searchResultList){
		this.stock = stock;
		this.percentage = percentage;
		this.searchResultList = searchResultList;
	}

	public Stock getStock() {
		return stock;
	}
	public void setStock(Stock stock) {
		this.stock = stock;
	}
	public double getPercentage() {
		return percentage;
	}
	public void setPercentage(double percentage) {
		this.percentage = percentage;
	}
}
