package com.twitstreet.servlet;

import com.twitstreet.db.data.Stock;

public class QuoteResponse {
	Stock stock;
	double percentage;
	public QuoteResponse(Stock stock, double percentage){
		this.stock = stock;
		this.percentage = percentage;
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
