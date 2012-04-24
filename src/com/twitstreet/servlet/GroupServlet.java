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
import com.twitstreet.session.UserMgr;

@SuppressWarnings("serial")
@Singleton
public class GroupServlet extends TwitStreetServlet {
	@Inject UserMgr userMgr;
	@Inject GroupMgr groupMgr;

	public void doGet(HttpServletRequest request, HttpServletResponse response)
	throws ServletException, IOException {
		response.setContentType("text/html;charset=utf-8");
		response.setHeader("Cache-Control","no-cache"); //HTTP 1.1
		response.setHeader("Pragma","no-cache"); //HTTP 1.0
		response.setDateHeader ("Expires", 0); //prevents caching at the proxy server
		
		loadUser(request);

		User user =(User) request.getAttribute(User.USER);
		


		String operation = "";
		
		operation = request.getParameter("op");
		if (operation != null && operation.length() > 0) {
			try {
				long groupId = Long.valueOf((String) request.getParameter(GroupDetailsServlet.GROUP_ID));
				long userId = -1;
				try{
					userId = Long.valueOf((String) request.getParameter(GroupDetailsServlet.USER_ID));
				}catch(Exception ex){
					
				}
			
				if (operation.equalsIgnoreCase("leave")) {
					groupMgr.removeUserFromGroup(user.getId(), groupId);
				} else if (operation.equalsIgnoreCase("join")) {

					groupMgr.addUserToGroup(user.getId(), groupId);

				} else if (operation.equalsIgnoreCase("delete")) {
					if (groupMgr.getGroup(groupId).getAdminId() == user.getId()) {

						groupMgr.deleteGroup(groupId);
					}
				}else if (operation.equalsIgnoreCase("disableentrance")) {
					if (groupMgr.getGroup(groupId).getAdminId() == user.getId()) {

						groupMgr.disableEntrance(groupId);
					}
				}else if (operation.equalsIgnoreCase("enableentrance")) {
					if (groupMgr.getGroup(groupId).getAdminId() == user.getId()) {

						groupMgr.enableEntrance(groupId);
					}
				}else if (operation.equalsIgnoreCase("removeuser")) {
					if (userId>-1 && groupMgr.getGroup(groupId).getAdminId() == user.getId()) {

						groupMgr.removeUserFromGroup(userId, groupId);

					}
				}else if (operation.equalsIgnoreCase("blockuser")) {
					if (groupMgr.getGroup(groupId).getAdminId() == user.getId()) {

						groupMgr.blockUserForGroup(userId, groupId);
					}
				}else if (operation.equalsIgnoreCase("unblockuser")) {
					if (userId>-1 && groupMgr.getGroup(groupId).getAdminId() == user.getId()) {

						groupMgr.unblockUserForGroup(userId, groupId);

					}
				}
			} catch (TwitstreetException e) {
				writeErrorIntoResponse(request, response, e);
			}
			return;
		}
		
		request.setAttribute(HomePageServlet.SELECTED_TAB_GROUP_BAR, "group-details-tab");
		//loadUserFromCookie(request);
		getServletContext().getRequestDispatcher("/WEB-INF/jsp/groupDetails.jsp").forward(request, response);

	}
	public void doPost(HttpServletRequest request, HttpServletResponse response)
	throws ServletException, IOException {
		doGet(request, response);
	}
}
