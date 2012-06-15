<%@page import="com.twitstreet.session.UserMgr"%>
<%@page import="com.google.inject.Injector"%>
<%@page import="com.twitstreet.servlet.GetUserServlet"%>
<%@page import="com.twitstreet.servlet.GetQuoteServlet"%>
<%@page import="com.twitstreet.servlet.HomePageServlet"%>
<%@ page import="com.twitstreet.localization.LocalizationUtil"%>
<%@ page import="com.twitstreet.db.data.User"%>
<%

	LocalizationUtil lutil = LocalizationUtil.getInstance();
	String lang = (String)request.getSession().getAttribute(LocalizationUtil.LANGUAGE);

	Injector inj = (Injector) pageContext.getServletContext().getAttribute(Injector.class.getName());
	UserMgr userMgr = inj.getInstance(UserMgr.class);
	User user = null;
	user = (user == null) ? (User) request.getAttribute(GetUserServlet.GET_USER) : user;

	String parameterUser = request.getParameter(User.USER);
	user = (user == null && parameterUser != null) ? userMgr.getUserById(Long.valueOf(parameterUser)) : user;

	String selectedTab = (String) request.getAttribute(HomePageServlet.SELECTED_TAB_USER_BAR);
%>
<div class="main-tabs" style="width: 100%;">
	<div class="title-bar">
	
			<a class="top-grossing-users-tab <%=(!selectedTab.equalsIgnoreCase("top-grossing-users-tab"))?"": "youarehere"%>"
			onclick="reloadIfHashIs('#!topgrossingusers'); loadTitle('TwitStreet - <%=lutil.get("topgrossingusers.title", lang)%>');">
			<%=lutil.get("topgrossingusers.header", lang)%> </a>
			
			<a class="new-users-tab <%=(!selectedTab.equalsIgnoreCase("new-users-tab"))?"": "youarehere"%>"
			onclick="reloadIfHashIs('#!newusers'); loadTitle('TwitStreet - <%=lutil.get("newusers.title", lang)%>');">
			<%=lutil.get("newusers.header", lang)%> </a>
			
<%if(user!=null){ %>
			<a class="user-details-tab <%=(!selectedTab.equalsIgnoreCase("user-details-tab"))?"": "youarehere"%>"
			onclick="reloadIfHashIs('#!user=<%=user.getId()%>'); loadTitle('TwitStreet - <%= user.getUserName()%> <%=lutil.get("user.bar.profile", lang)%>'')">
			 <%=user.getUserName()%> 
					<%-- <%=lutil.get("userdetails", lang)%> --%>
		</a> 
<%} %>
	</div>
</div>
