
<%@page import="com.twitstreet.servlet.PaginationDO"%>
<%@page import="com.twitstreet.servlet.CreateGroupServlet"%>
<%@page import="com.twitstreet.session.GroupMgr"%>
<%@page import="com.twitstreet.db.data.Group"%>
<%@page import="com.twitstreet.twitter.SimpleTwitterUser"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.sql.SQLException"%>

<%@ page import="com.twitstreet.servlet.HomePageServlet"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.Date"%>
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
<%@ page import="java.text.DecimalFormat"%>
<%@ page import="com.twitstreet.localization.LocalizationUtil"%>
<%@page import="com.twitstreet.util.GUIUtil"%>
<%
	LocalizationUtil lutil = LocalizationUtil.getInstance();
	String lang = (String) request.getSession().getAttribute(
	LocalizationUtil.LANGUAGE);

	Injector inj = (Injector) pageContext.getServletContext().getAttribute(Injector.class.getName());
	ArrayList<Group> groupList = (ArrayList<Group>) request.getAttribute("groupList");
	String groupListName = (String) request.getAttribute("groupListName");

	PortfolioMgr portfolioMgr = inj.getInstance(PortfolioMgr.class);
	GroupMgr groupMgr = inj.getInstance(GroupMgr.class);
	long time = (new Date()).getTime();
	StockMgr stockMgr = inj.getInstance(StockMgr.class);
	User user = (User) request.getAttribute(User.USER);
	
	PaginationDO pdo = (PaginationDO) request.getAttribute("pdo");
	
%>
<div id="group-list-<%=groupListName%>">


	<div class="main-div">
	
		
		<div>
			<input type="text" id="<%=groupListName%>_createGroupTextboxId" class="textbox textboxBig"  value="" onclick="selectAllText(jQuery(this));" /> 
			<input type="button" id="<%=groupListName%>_createGroupButtonId" class="buttonBig" value="<%=lutil.get("group.create", lang) %>" onclick="createGroup($('#<%=groupListName%>_createGroupTextboxId').val())">
			
			
			<script type="text/javascript">
				$('#<%=groupListName%>_createGroupTextboxId').keyup(function(event) {
					if (event.keyCode == 13) {
						$("#<%=groupListName%>_createGroupButtonId").click();
					}
				});
			</script>
		</div>
		
	
	
	
	</div>

	<%
		if (groupList!=null && groupList.size() > 0) {
		
	%>

<%-- 	<h3><%=lutil.get("suggestedstocks.header", lang)%></h3> --%>

	
	
	
	<%
		if (pdo.getItemCount()>pdo.getRecordPerPage()) {
			request.setAttribute("pdo", pdo);
	%>
		<jsp:include page="pagination.jsp" />	
	<%

		}
	%>
		<table class="datatbl" style="margin-top: 10px;">
		
				<tr height="40px">
					<td>
						
					</td>
					<td width="100px">
						
					</td>
					<td align="center">
					
					</td>
					<td align="center">
					
					</td>
					<td align="center">
						<b><%=lutil.get("group.usercount", lang) %></b>
					</td>
					<td align="center">
						<b><%=lutil.get("group.admin", lang) %></b>	
					</td>
					
					<td>
						
					</td>
					<td align="center">
						<b><%=lutil.get("group.invite", lang) %></b>	
					</td>
				</tr>
			<%
				for (int i = 0; i < groupList.size();i++) {
					
					Group group = groupList.get(i);
			%>
				<tr height="50px" onmouseover="$('.groupList-<%=groupListName%>-<%=group.getId()%>').show()"
					onmouseout="$('.groupList-<%=groupListName%>-<%=group.getId()%>').hide()">
					<td>
						<%=group.getRank() %>. 
					</td>
					<td>
						<a href="#!group=<%=group.getId()%>" onclick="reloadIfHashIsMyHref(this); loadTitle('<%=lutil.get("groups.details.title", lang, group.getName())%>');"><%=group.getName() %></a>
						<div>
							<div style="float:left">
								<%=Util.getNumberFormatted(group.getTotal(), true, true, false, false, false, false)%>
							</div>
							<div style="float:right">
								<%if(group.getChangePerHour()!=0){ %>
								<%=Util.getNumberFormatted( group.getChangePerHour(), true, true, true, true, false, true)%>
								<%}%>
							</div>
						</div>
					</td>
					<td align="center">
						
					</td>
					<td align="center">
						
						
					</td>
					<td align="center">
						<%=group.getUserCount()%>
					</td>
					
					<td align="center">
					<a href="#!user=<%=group.getAdminId()%>"  onclick="reloadIfHashIsMyHref(this); loadTitle('<%=lutil.get("user.bar.profile", lang, group.getAdminName())%>');" title="<%=lutil.get("user.details.tip",lang,group.getAdminName())%>"> <%=group.getAdminName()%></a>
					</td>
					<td align="center">
				<% 
				if(user!=null){
				%>
				
				
				
						<% 
						if(group.getAdminId()==user.getId()){
						%>
						
						
						<b><a style="color:black;" href="javascript:void(0)" onclick="deleteGroup(<%=group.getId()%>,'<%=lutil.get("group.delete.confirm", lang, group.getName())%>')">
							<%=lutil.get("group.delete", lang) %>
						</a></b>
						
						<% 
						}else{
						%>
						
						
							<% 
							if(group.getStatus()!=Group.STATUS_NEW_USER_DISABLED){
								if(groupMgr.userIsMemberOfGroup(user.getId(), group.getId())){
										
								%>
								<a class="red-light" href="javascript:void(0)" onclick="leaveGroup(<%=group.getId()%>)">
								<%=lutil.get("group.leave", lang) %>
								</a>
								
								<% 
								}else{
								%>
								<a class="green-light" href="javascript:void(0)" onclick="joinGroup(<%=group.getId()%>)">
								<%=lutil.get("group.join", lang) %>
								</a>
								<% 
								}
							}
							%>

						<% 
						}
						%>
				
				
				<% 
				}
				%>		
						</td>		
					<td align="center">
						<%=GUIUtil.getInstance().getTwitterShareButton("#!joingroup="+group.getId(),"group.inviteText", lang, group.getName()) %>
					</td>
				</tr>
			<%
			}
			%>
		</table>
	
	
	<%
		if (pdo.getItemCount()>pdo.getRecordPerPage()) {
			request.setAttribute("pdo", pdo);
	%>
		<jsp:include page="pagination.jsp" />	
	<%
		}
	%>
	<%
		}
	
	
	request.setAttribute("pdo", null);
	%>
</div>

