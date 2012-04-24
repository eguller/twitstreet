<%@page import="com.twitstreet.servlet.PaginationDO"%>
<%@page import="com.twitstreet.session.GroupMgr"%>
<%@page import="com.twitstreet.db.data.Group"%>
<%@page import="com.twitstreet.db.data.RankingData"%>
<%@page import="com.twitstreet.servlet.SeasonServlet"%>
<%@page import="com.twitstreet.season.SeasonResult"%>
<%@page import="com.twitstreet.season.SeasonInfo"%>
<%@page import="com.twitstreet.season.SeasonMgr"%>
<%@page import="com.twitstreet.servlet.HomePageServlet"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.twitstreet.util.GUIUtil"%>
<%@page import="com.twitstreet.servlet.UserProfileServlet"%>
<%@page import="com.twitstreet.servlet.GetUserServlet"%>
<%@page import="com.twitstreet.db.data.StockInPortfolio"%>
<%@page import="com.twitstreet.session.UserMgr"%>
<%@page import="com.twitstreet.db.data.User"%>
<%@ page import="com.google.inject.Injector"%>
<%@ page import="com.google.inject.Guice"%>
<%@ page import="java.util.List"%>
<%@ page import="com.twitstreet.util.Util"%>
<%@ page import="com.twitstreet.market.PortfolioMgr"%>
<%@ page import="com.twitstreet.db.data.Portfolio"%>
<%@ page import="com.twitstreet.localization.LocalizationUtil" %>

<%
Injector inj = (Injector) pageContext.getServletContext().getAttribute(Injector.class.getName());

LocalizationUtil lutil = LocalizationUtil.getInstance();
String lang = (String) request.getSession().getAttribute(LocalizationUtil.LANGUAGE);
User user = (User) request.getAttribute(User.USER);


GroupMgr groupMgr= inj.getInstance(GroupMgr.class);
UserMgr userMgr= inj.getInstance(UserMgr.class);
SeasonMgr seasonMgr = inj.getInstance(SeasonMgr.class);
SeasonInfo curSeason = seasonMgr.getCurrentSeason();

String selectedTab = (String) request.getAttribute(HomePageServlet.SELECTED_TAB_GROUP_BAR);
%>
<div id="groups" class="main-div" >

	<jsp:include page="getGroup.jsp" />
	
	<table class="datatbl">
		<tr>
			<td>
				<jsp:include page="groupBar.jsp"></jsp:include>
			</td>
		</tr>
		<tr>
			<td>
				<div id="groups-screen">
					<div id="group-list-content">
						<%if(selectedTab.equalsIgnoreCase("group-list-tab")){						
  							PaginationDO pdo = new PaginationDO(1,groupMgr.getGroupCount(),GroupMgr.GROUP_COUNT_PER_PAGE, "all","loadGroupList",false);
							request.setAttribute("pdo", pdo);
							ArrayList<Group> groups = groupMgr.getAllGroups(pdo.getOffset(),pdo.getRecordPerPage());
							request.setAttribute("groupList", groups);		
							request.setAttribute("groupListName", "all");					
						%>
							<jsp:include page="groupList.jsp" />							
						<%}%>
						
					</div>
					<%	if(user!=null){%>
	
					<div id="my-groups-content">
							<%if(selectedTab.equalsIgnoreCase("my-groups-tab")){						
								PaginationDO pdo = new PaginationDO(1,groupMgr.getGroupCountForUser(user.getId()),GroupMgr.GROUP_COUNT_PER_PAGE, "my","loadMyGroups",false);
								request.setAttribute("pdo", pdo);
								ArrayList<Group> groups = groupMgr.getGroupsForUser(user.getId(),0,pdo.getRecordPerPage());
								request.setAttribute("groupList", groups);		
								request.setAttribute("groupListName", "my");					
							%>
								<jsp:include page="groupList.jsp" />							
							<%}%>
							
						</div>
					<%}%>
					<div id="group-details-content">
					<%if(selectedTab.equalsIgnoreCase("group-details-tab")){%>
						<jsp:include page="groupDetails.jsp" />
						
					<%	}%>
						
					</div>
				</div>
			</td>
		</tr>
	</table>
	
	
</div>