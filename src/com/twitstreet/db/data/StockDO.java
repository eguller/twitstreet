package com.twitstreet.db.data;

public class StockDO {
	long id;
	String name;
	int total;
	double percentSold;
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getTotal() {
		return total;
	}
	public void setTotal(int total) {
		this.total = total;
	}
	public double getPercentSold() {
		return percentSold;
	}
	public void setPercentSold(double percenSold) {
		this.percentSold = percenSold;
	}
	public int getAvailable(){
		return (int)(total * ( 1 - percentSold / 100));
	}
}
