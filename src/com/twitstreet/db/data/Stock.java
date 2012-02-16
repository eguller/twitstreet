package com.twitstreet.db.data;

import java.util.Date;

public class Stock {
	long id;
	String name;
	int total;
	double sold;
	String pictureUrl;
	Date lastUpdate;
	int changePerHour;
	boolean verified;
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
	public double getSold() {
		return sold;
	}
	public void setSold(double sold) {
		this.sold = sold;
	}
	public int getAvailable(){
		return (int)(total * ( 1 - sold));
	}
	public String getPictureUrl() {
		return pictureUrl;
	}
	public void setPictureUrl(String pictureUrl) {
		this.pictureUrl = pictureUrl;
	}
	public Date getLastUpdate() {
		return lastUpdate;
	}
	public void setLastUpdate(Date lastUpdate) {
		this.lastUpdate = lastUpdate;
	}
	public int getChangePerHour() {
		return changePerHour;
	}
	public void setChangePerHour(int changePerHour) {
		this.changePerHour = changePerHour;
	}
	public boolean isVerified() {
		return verified;
	}
	public void setVerified(boolean verified) {
		this.verified = verified;
	}
}
