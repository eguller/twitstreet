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
