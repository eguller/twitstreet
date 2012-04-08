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
