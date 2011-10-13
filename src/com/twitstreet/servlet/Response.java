package com.twitstreet.servlet;

import java.util.ArrayList;

public class Response {
	boolean result = false;
	ArrayList<String> reasons = new ArrayList<String>();
	private Response(){}
	private Response(boolean result){
		this.result = result;
	}
	public Response success(){
		this.result = true;
		return this;
	}
	public  Response fail(){
		this.result = false;
		return this;
	}
	
	public boolean isSuccess(){
		return result;
	}
	
	public Response reason(String reason){
		reasons.add(reason);
		return this;
	}
	public static Response create(){
		return new Response();
	}
}
