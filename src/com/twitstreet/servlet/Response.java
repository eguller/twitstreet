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

public class Response {
	boolean result = false;
	String resultCode = "";
	ArrayList<String> reasons = new ArrayList<String>();
	Object respObj = null;

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
	public Object getRespOjb() {
		return respObj;
	}
	public void setRespOjb(Object respOjb) {
		this.respObj = respOjb;
	}
	public String getResultCode() {
		return resultCode;
	}
	public Response resultCode(String resultCode) {
		this.resultCode = resultCode;
		return this;
	}
}
