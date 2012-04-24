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

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.twitstreet.db.data.User;
import com.twitstreet.main.TwitstreetException;
import com.twitstreet.session.GroupMgr;

@SuppressWarnings("serial")
@Singleton
public class CreateGroupServlet extends TwitStreetServlet {
	public static String CREATED_GROUP_NAME = "createdGroupName";
	@Inject
	GroupMgr groupMgr;
	public static String NAME = "name";
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("text/html;charset=utf-8");
		response.setHeader("Cache-Control","no-cache"); //HTTP 1.1
		response.setHeader("Pragma","no-cache"); //HTTP 1.0
		response.setDateHeader ("Expires", 0); //prevents caching at the proxy server
		
		String groupNameStr = request.getParameter(NAME);

		loadUser(request);
		
		User user =(User) request.getAttribute(User.USER);
		
		if (groupNameStr == null|| groupNameStr.length()<1) {
		
			return;
		}
		String lang = getLanguage(request);
		
		long groupId = -1;
		try{
			groupId = groupMgr.createGroupForUser(groupNameStr,user.getId());
			response.getWriter().print(groupId);
			
		}catch(TwitstreetException ex){
			writeErrorIntoResponse(request, response, ex);
		
		}
		
	
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}
}
