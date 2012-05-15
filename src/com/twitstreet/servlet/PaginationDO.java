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

package com.twitstreet.servlet;

import com.twitstreet.util.Util;

public class PaginationDO {

	public PaginationDO(int currPage, int itemCount, int recordPerPage, String name, String onChangeFunction, boolean showInterval) {
		super();
		this.currPage = currPage;
		this.itemCount = itemCount;
		this.recordPerPage = recordPerPage;
		this.name=name;
		this.onChangeFunction = onChangeFunction;
		this.showInterval = showInterval;

		int offset = (this.currPage-1) * this.recordPerPage;		
		
		if(offset>=this.itemCount){
			this.currPage = (this.itemCount / this.recordPerPage)+1;
			if(this.itemCount%this.recordPerPage == 0 && this.currPage>1){
				this.currPage--;
			}
		}
		
	}
	public int getOffset(){
		
		return (this.currPage-1) * this.recordPerPage;
	}
	public int getPageCount(){
		return Util.getPageCount(itemCount, recordPerPage);
	}
	public int getItemCount() {
		return itemCount;
	}
	public void setItemCount(int itemCount) {
		this.itemCount = itemCount;
	}
	public int getRecordPerPage() {
		return recordPerPage;
	}
	public void setRecordPerPage(int recordPerPage) {
		this.recordPerPage = recordPerPage;
	}


	public String getOnChangeFunction() {
		return onChangeFunction;
	}
	public void setOnChangeFunction(String onChangeFunction) {
		this.onChangeFunction = onChangeFunction;
	}
	private int currPage;
	private int itemCount;
	private int recordPerPage;
	private String name;
	private String onChangeFunction;
	private boolean showInterval;
	public int getCurrPage() {
		return currPage;
	}
	public void setCurrPage(int currPage) {
		this.currPage = currPage;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public boolean isShowInterval() {
		return showInterval;
	}
	public void setShowInterval(boolean showInterval) {
		this.showInterval = showInterval;
	}


}
