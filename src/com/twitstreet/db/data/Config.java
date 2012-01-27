package com.twitstreet.db.data;

public class Config {
	public static final String NONE = "";
	public static final String ID = "id";
	public static final String PARM = "parm";
	public static final String VAL = "val";
	
	long id;
	String parm;
	String val;
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getParm() {
		return parm;
	}
	public void setParm(String parm) {
		this.parm = parm;
	}
	public String getVal() {
		return val;
	}
	public void setVal(String val) {
		this.val = val;
	}

}
