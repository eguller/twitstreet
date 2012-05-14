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
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.twitstreet.db.data.Stock;
import com.twitstreet.db.data.User;
import com.twitstreet.market.StockMgr;
import com.twitstreet.session.UserMgr;

@SuppressWarnings("serial")
@Singleton
public class TopRankServlet extends TwitStreetServlet {

	public static String TOPRANKS_USER_LIST = "topranksuserlist";
	public static String TOPRANKS_TYPE = "type";

	@Inject
	UserMgr userMgr;

	public static String PAGE = "toprankPage";
	public static String GROUP = "group";

	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		response.setContentType("text/html;charset=utf-8");
		response.setHeader("Cache-Control", "no-cache"); // HTTP 1.1
		response.setHeader("Pragma", "no-cache"); // HTTP 1.0
		response.setDateHeader("Expires", 0); // prevents caching at the proxy

		loadUser(request);
		int page = getPage(request);
		
		String type = request.getParameter(TOPRANKS_TYPE);		
		type = (type==null)?"currentSeason":type;
		
		long groupId = -1;		
		try{
			groupId = Long.parseLong(request.getParameter(GROUP));
		}catch(Exception ex){
			
		}
		PaginationDO pdo = null;
		ArrayList<User> userList = null;
		if(groupId==-1){
			pdo = new PaginationDO(page,userMgr.getUserCount(),UserMgr.MAX_TOPRANK_USER,"toprank","toprank",true);
			if(type.equalsIgnoreCase("currentSeason")){
				userList = userMgr.getTopRank(pdo.getOffset(), pdo.getRecordPerPage());
			}else{
				userList = userMgr.getTopRankAllTime(pdo.getOffset(),  pdo.getRecordPerPage());
			}			
		}else{
			pdo = new PaginationDO(page,userMgr.getUserCountForGroup(groupId),UserMgr.MAX_TOPRANK_USER,"toprank","toprank",true);
			if(type.equalsIgnoreCase("currentSeason")){
				userList = userMgr.getTopRankForGroup(groupId, pdo.getOffset(),  pdo.getRecordPerPage());
			}else{
				userList = userMgr.getTopRankAllTimeForGroup(groupId, pdo.getOffset(),  pdo.getRecordPerPage());
			}
			
		}
		request.setAttribute("pdo", pdo);
		request.setAttribute(TOPRANKS_USER_LIST, userList);
		try {
		
			getServletContext().getRequestDispatcher("/WEB-INF/jsp/userRanking.jsp").forward(request, response);

		} catch (ServletException e) {
			e.printStackTrace();
		}

	}

}
