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

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.twitstreet.db.data.Group;
import com.twitstreet.db.data.Stock;
import com.twitstreet.market.StockMgr;
import com.twitstreet.session.GroupMgr;

@SuppressWarnings("serial")
@Singleton
public class GroupDetailsServlet extends TwitStreetServlet {
	@Inject
	GroupMgr groupMgr;
	public static int USERS_PER_PAGE = 5  ;
	public static String GROUP_ID = "group";
	public static String USER_ID = "user";
	public static String GROUP = "group";
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("text/html;charset=utf-8");
		response.setHeader("Cache-Control","no-cache"); //HTTP 1.1
		response.setHeader("Pragma","no-cache"); //HTTP 1.0
		response.setDateHeader ("Expires", 0); //prevents caching at the proxy server
		
		String groupIdStr = request.getParameter(GROUP_ID);

		loadUser(request);
		int page = getPage(request);
		
		PaginationDO pdo = null;
		if (groupIdStr == null) {
			response.sendRedirect(response.encodeRedirectURL("/"));
			return;
		}
		Group group = groupMgr.getGroup(Long.parseLong(groupIdStr));

		if (group != null) {
//			request.setAttribute("title", "Stock details of " + group.getName());
//			request.setAttribute(
//					"meta-desc",
//					"This page show details of a "
//							+ group.getName()
//							+ " like available, sold and total number of a followers. Stock distribution shows who has how much "
//							+ group.getName() + ".");

			request.setAttribute(GetGroupServlet.GET_GROUP_DISPLAY, group.getName());
			request.setAttribute(GROUP, group);

			pdo = new PaginationDO(page, userMgr.getUserCountForGroup(group.getId()), USERS_PER_PAGE, "groupdetails", "loadGroupUsers", true);
			request.setAttribute("pdo", pdo);

		}


		
		request.setAttribute(HomePageServlet.SELECTED_TAB_GROUP_BAR, "group-details-tab");
		getServletContext().getRequestDispatcher("/WEB-INF/jsp/groupsContent.jsp")
				.forward(request, response);

	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}
}
