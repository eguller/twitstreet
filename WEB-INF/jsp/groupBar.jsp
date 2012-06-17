<%@page import="com.twitstreet.db.data.User"%>
<%@page import="com.twitstreet.servlet.GroupDetailsServlet"%>
<%@page import="com.twitstreet.session.GroupMgr"%>
<%@page import="com.twitstreet.session.UserMgr"%>
<%@page import="com.google.inject.Injector"%>
<%@page import="com.twitstreet.servlet.GetGroupServlet"%>
<%@page import="com.twitstreet.servlet.GetQuoteServlet"%>
<%@page import="com.twitstreet.servlet.HomePageServlet"%>
<%@ page import="com.twitstreet.localization.LocalizationUtil"%>
<%@ page import="com.twitstreet.db.data.Group"%>
<%

	LocalizationUtil lutil = LocalizationUtil.getInstance();
	String lang = (String)request.getSession().getAttribute(LocalizationUtil.LANGUAGE);

	Injector inj = (Injector) pageContext.getServletContext().getAttribute(Injector.class.getName());
	GroupMgr userMgr = inj.getInstance(GroupMgr.class);
	Group group = null;
	group = (group == null) ? (Group) request.getAttribute(GroupDetailsServlet.GROUP) : group;
// 	group = (group == null) ? (Group) request.getAttribute(GetGroupServlet.GET_GROUP) : group;
	User user = (User) request.getAttribute(User.USER);

	
	String selectedTab = (String) request.getAttribute(HomePageServlet.SELECTED_TAB_GROUP_BAR);
%>
<div class="main-tabs" style="width: 100%;">
	<div class="title-bar">
	
			<a class="group-list-tab <%=(!selectedTab.equalsIgnoreCase("group-list-tab"))?"": "youarehere"%>"
			onclick="reloadIfHashIs('#!grouplist'); loadTitle('TwitStreet - <%=lutil.get("groups.list.title",lang)%>');">
			<%=lutil.get("grouplist.header", lang)%> </a>
<%	if(user!=null){%>
			
			<a class="my-groups-tab <%=(!selectedTab.equalsIgnoreCase("my-groups-tab"))?"": "youarehere"%>"
			onclick="reloadIfHashIs('#!mygroups'); loadTitle('TwitStreet - <%=lutil.get("groups.mygroups.title",lang)%>');">
			<%=lutil.get("group.mygroups", lang)%> </a>
<% }%>
<%if(group!=null){ %>
			<a class="group-details-tab <%=(!selectedTab.equalsIgnoreCase("group-details-tab"))?"": "youarehere"%>"
			onclick="reloadIfHashIs('#!group=<%=group.getId()%>'); loadTitle('TwitStreet - <%=group.getName()%> <%=lutil.get("groups.details.title", lang)%>');">
			 <%=group.getName()%> 
					<%-- <%=lutil.get("groupdetails", lang)%> --%>
		</a> 
<%} %>
	</div>
</div>
