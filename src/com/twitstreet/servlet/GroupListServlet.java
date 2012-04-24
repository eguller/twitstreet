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
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.twitstreet.db.data.Group;
import com.twitstreet.db.data.Stock;
import com.twitstreet.db.data.User;
import com.twitstreet.market.StockMgr;
import com.twitstreet.session.GroupMgr;
import com.twitstreet.session.UserMgr;

@SuppressWarnings("serial")
@Singleton
public class GroupListServlet extends TwitStreetServlet {
	@Inject
	UserMgr userMgr;
	@Inject
	StockMgr stockMgr;
	@Inject
	GroupMgr groupMgr;

	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		response.setContentType("text/html;charset=utf-8");
		response.setHeader("Cache-Control", "no-cache"); // HTTP 1.1
		response.setHeader("Pragma", "no-cache"); // HTTP 1.0
		response.setDateHeader("Expires", 0); // prevents caching at the proxy
												// server

		loadUser(request);
		User user =(User) request.getAttribute(User.USER);
		
		int page = getPage(request);
	
		String type = "";
		PaginationDO pdo = null;
		ArrayList<Group> results = null;
		type = request.getParameter("type");
		if(type!=null&& type.equalsIgnoreCase("my")){
			pdo = new PaginationDO(page,groupMgr.getGroupCountForUser(user.getId()),GroupMgr.GROUP_COUNT_PER_PAGE, type,"loadMyGroups",false);
			results = groupMgr.getGroupsForUser(user.getId(),(page-1)*GroupMgr.GROUP_COUNT_PER_PAGE,GroupMgr.GROUP_COUNT_PER_PAGE);
		}else{
			type = "all";
			pdo = new PaginationDO(page,groupMgr.getGroupCount(),GroupMgr.GROUP_COUNT_PER_PAGE, type,"loadGroupList",false);
			results = groupMgr.getAllGroups((page-1)*GroupMgr.GROUP_COUNT_PER_PAGE,GroupMgr.GROUP_COUNT_PER_PAGE);
		}
	
		
		request.setAttribute("pdo", pdo);	
		request.setAttribute("groupList", results);
		request.setAttribute("groupListName", type);
		//loadUserFromCookie(request);
		try {
			getServletContext().getRequestDispatcher(
					"/WEB-INF/jsp/groupList.jsp").forward(request, response);
		} catch (ServletException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
