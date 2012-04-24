<%@page import="com.twitstreet.servlet.PaginationDO"%>
<%@page import="com.twitstreet.servlet.StockDetailsServlet"%>
<%@page import="com.twitstreet.session.GroupMgr"%>
<%@page import="com.twitstreet.servlet.GetGroupServlet"%>
<%@page import="com.twitstreet.servlet.GroupDetailsServlet"%>
<%@page import="com.twitstreet.db.data.Group"%>
<%@ page import="com.twitstreet.servlet.HomePageServlet"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.Date"%>
<%@page import="java.util.LinkedHashMap"%>
<%@page import="com.twitstreet.db.data.StockHistoryData"%>
<%@page import="com.twitstreet.db.data.UserStock"%>
<%@page import="java.sql.SQLException"%>
<%@page import="com.twitstreet.market.StockMgr"%>
<%@ page import="com.google.inject.Injector"%>
<%@ page import="com.twitstreet.db.data.User"%>
<%@ page import="com.google.inject.Guice"%>
<%@ page import="com.twitstreet.util.Util"%>
<%@ page import="com.twitstreet.db.data.Portfolio"%>
<%@page import="com.twitstreet.config.ConfigMgr"%>
<%@ page import="com.twitstreet.market.PortfolioMgr"%>
<%@page import="com.twitstreet.session.UserMgr"%>
<%@ page import="com.twitstreet.db.data.UserStockDetail"%>
<%@ page import="java.util.List"%>
<%@ page import="com.twitstreet.util.Util"%>
<%@ page import="java.text.DecimalFormat"%>
<%@ page import="com.twitstreet.db.data.Stock"%>
<%@ page import="com.twitstreet.localization.LocalizationUtil" %>
<%@page import="com.twitstreet.util.GUIUtil"%>
<%

LocalizationUtil lutil = LocalizationUtil.getInstance();
String lang = (String)request.getSession().getAttribute(LocalizationUtil.LANGUAGE);

	Injector inj = (Injector) pageContext.getServletContext().getAttribute(Injector.class.getName());
	UserMgr userMgr = inj.getInstance(UserMgr.class);
	User sessionUser = (User) request.getAttribute(User.USER);

	
	GroupMgr groupMgr = inj.getInstance(GroupMgr.class);
	StockMgr stockMgr = inj.getInstance(StockMgr.class);

	ConfigMgr configMgr = inj.getInstance(ConfigMgr.class);

	Group group = null;

	group = (Group) request.getAttribute(HomePageServlet.GROUP);
	if (group == null) {
		try {
			group = groupMgr.getGroup(Long.valueOf((String) request.getParameter(GroupDetailsServlet.GROUP_ID)));
		} catch (Exception ex) {

		}
	}
	
	
	
	String getGroup = request.getAttribute(GetGroupServlet.GET_GROUP) == null ? "" : (String) request.getAttribute(GetGroupServlet.GET_GROUP);
	String getGroupDisplay = request.getAttribute(GetGroupServlet.GET_GROUP_DISPLAY) == null ? "" : (String) request.getAttribute(GetGroupServlet.GET_GROUP_DISPLAY);

%>

	
<div id="groupdetails" class="main-div">
					
	
	
	<%
		if (group != null) {
			
			
			boolean userIsAdmin = false;

			boolean userIsMember = false;
			
			if(sessionUser!=null){
				userIsAdmin = group.getAdminId() == sessionUser.getId();
				userIsMember = groupMgr.userIsMemberOfGroup(sessionUser.getId(), group.getId());
			}
			
			PaginationDO pdo = (PaginationDO) request.getAttribute("pdo");
			if(pdo==null){
				pdo = new PaginationDO(1, userMgr.getUserCountForGroup(group.getId()),GroupDetailsServlet.USERS_PER_PAGE,"groupdetails","loadGroupUsers",true);
			}
			
			ArrayList<User> groupUsers = userMgr.getTopRankForGroup(group.getId(), pdo.getOffset(), pdo.getRecordPerPage());
	%>
	
			<input id="hiddenGroupDetailsGroupId" type="hidden" value="<%=group.getId() %>"/>
			
			<%
			if (userIsAdmin) {
			%>
			
				<div align="left">
				
				<%if(group.getStatus() == Group.STATUS_NEW_USER_DISABLED){ %>
					<%=lutil.get("group.entrancedisabled", lang) %>
					<input class="buttonMedium" type="button" value='<%=lutil.get("group.enableentrance", lang) %>' onclick='enableEntranceForGroup(<%=group.getId()%>)' />
				<%} else { %>
					<%=lutil.get("group.entranceenabled", lang) %>
					<input class="buttonMedium" type="button" value='<%=lutil.get("group.disableentrance", lang) %>' onclick='disableEntranceForGroup(<%=group.getId()%>)' />
				
				<%}%>
				
				</div>
				
				
			<%
			}else if (userIsMember) {
			%>
				<div align="right">
				
					<a class="red-light" href="javascript:void(0)" onclick="leaveGroup(<%=group.getId()%>)">
						<%=lutil.get("group.leave", lang) %>
					</a>
				</div>
			<%
			} else if (sessionUser!=null) {
			%>
				<div align="right">
				
					<a class="green-light" href="javascript:void(0)" onclick="joinGroup(<%=group.getId()%>)">
						<%=lutil.get("group.join", lang) %>
					</a>
				</div>
			<%
			}
			%>
			
			
			
			<%
			if (pdo.getItemCount()> pdo.getRecordPerPage()) {
				request.setAttribute("pdo", pdo);
			%>
				<jsp:include page="pagination.jsp" />	
			
			<%
			}
			%>
			<table class="datatbl">
			<%for(int i =0; i<groupUsers.size();i++){ 
				int rank = pdo.getOffset()+i+1;
				User user = groupUsers.get(i);
			%>
				<tr>
				
					<td class="rank-number">
					<%=rank%>.
					</td>
					<td>
						<img class="twuser" width="48" height="48"  src="<%=user.getPictureUrl()%>" />
						<% 
						if(userIsAdmin && sessionUser.getId() != user.getId()){
						%>
						<br>
						<a class="red-light" href="javascript:void(0)" onclick="removeUserFromGroup(<%=user.getId()%>,<%=group.getId()%>)">
						<%=lutil.get("group.removeuser", lang) %>
						</a>
						<br>
						<a class="red-light" href="javascript:void(0)" onclick="blockUserForGroup(<%=user.getId()%>,<%=group.getId()%>)">
						<%=lutil.get("group.banuser", lang) %>
						</a>
						<% 
						}
						%>				
					</td>
					<td>
						<div style="padding:15px;">
							<div align="center">
								<a href="#!user=<%=user.getId()%>"  onclick="reloadIfHashIsMyHref(this)" title="<%=lutil.get("user.details.tip",lang, user.getUserName())%>"> <%=user.getUserName()%></a>
							</div>						
							<br> 
							<div align="center">
								<%=Util.getNumberFormatted(user.getTotal(), true, true, false, false, false, false) %>
							</div>
							<br> 
							<div align="right">
								<%if(user.getProfit()!=0){ %>
									<%=Util.getNumberFormatted(user.getProfit(), true, true, true, true, false, true) %>
								<%}%>
							</div>				
						</div>
					</td>
					<td style="float:right">
						<div id="group-user<%=user.getId()%>" style="width:230px; height:120px">
						<% request.setAttribute("chartUser", user); %>
							<jsp:include page="userTimeLineChart.jsp">										
								<jsp:param name="divId" value="#group-user"/>
								<jsp:param name="format" value="simple"/>							
							</jsp:include>
						
						</div>
					</td>
				</tr>
			<% } %>


			</table>
		
			<%
			if (pdo.getItemCount()> pdo.getRecordPerPage()) {
				request.setAttribute("pdo", pdo);
			%>
				<jsp:include page="pagination.jsp" />	
			
			<%
			}
			%>
	<%
		}
	%>

	

	<%
		if (getGroup.length() > 0){
			%>
			<br>
			<jsp:include page="otherSearchResults.jsp" />
			
			
			<%
			if(group == null) {
			%>	
			
				<div id="searchnoresult"><p style="text-align: center;"><%=lutil.get("shared.noresults", lang) %></p></div>
		<%	}
		%>

			

	<%
		}
	%>

</div>


