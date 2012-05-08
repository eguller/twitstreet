<%@page import="com.twitstreet.servlet.TopRankGroupServlet"%>
<%@page import="com.twitstreet.session.GroupMgr"%>
<%@page import="com.twitstreet.servlet.HomePageServlet"%>
<%@page import="com.twitstreet.db.data.Group"%>
<%@page import="com.twitstreet.servlet.PaginationDO"%>
<%@page import="com.twitstreet.servlet.TopRankServlet"%>
<%@ page import="com.google.inject.Injector"%>
<%@ page import="com.google.inject.Guice"%>
<%@ page import="com.twitstreet.session.UserMgr"%>
<%@ page import="com.twitstreet.db.data.User"%>
<%@ page import="java.util.ArrayList"%>
<%@ page import="com.twitstreet.util.Util"%>
<%@ page import="com.twitstreet.localization.LocalizationUtil" %>
	
<%
LocalizationUtil lutil = LocalizationUtil.getInstance();
String lang = (String)request.getSession().getAttribute(LocalizationUtil.LANGUAGE);

Injector inj = (Injector) pageContext.getServletContext().getAttribute(Injector.class.getName());
User sessionUser = (User)request.getAttribute(User.USER);
GroupMgr userMgr = inj.getInstance(GroupMgr.class);


String typeParam = request.getParameter("type");

typeParam = (typeParam==null)?"currentSeason":typeParam;
PaginationDO pdo = (PaginationDO) request.getAttribute("pdo");

if(pdo==null){
	pdo = new PaginationDO(1,userMgr.getGroupCount(),GroupMgr.GROUP_COUNT_PER_PAGE,"groupRanking","toprank",true);
}

ArrayList<Group> groupList = null;
if(typeParam.equalsIgnoreCase("allTime")){

	groupList = userMgr.getTopGroupsAllTime(pdo.getOffset(), pdo.getRecordPerPage());
}else{

	groupList = userMgr.getTopGroups(pdo.getOffset(), pdo.getRecordPerPage());
}
%>


<div id="topranks" class="main-div">
	<div id="group-ranking-content">
					
		<div align="center" style="padding: 3px;margin:5px;">
	
			<jsp:include page="toprankSelectSeason.jsp">
		
				<jsp:param name="type" value="<%=String.valueOf(typeParam) %>"></jsp:param>
		
			</jsp:include>			
				
			<%
				if (pdo.getItemCount()>pdo.getRecordPerPage()) {
					request.setAttribute("pdo", pdo);
			%>
				<jsp:include page="pagination.jsp" />	
			<%
				}
			%>			
		</div>
	
			
		<div id="topranks-loading-div">
			
		
			<table class="datatbl" id="topranktable" style="margin-bottom:10px">
					
				<%
					
					
				for(int i = 0; i < groupList.size(); i++) {
						
					int rankDisplay = pdo.getOffset()+ i + 1;
						Group group = groupList.get(i);
		
						double profitDiff = 0;
						if (i > 0) {
							Group prevUser = groupList.get(i - 1);
							profitDiff = group.getChangePerHour() - prevUser.getChangePerHour() ;
		
						}
						double total = group.getTotal();
						
						if(typeParam.equalsIgnoreCase("allTime")){
							total = group.getTotalAllTime();					
						}
						String clssNm = "odd";
						
						if (i % 2 == 1) {
							clssNm ="";
						}
					
						
				%>
				<tr class="<%=clssNm%>">
					
					<td class="rank-number">
						<%=rankDisplay%>.					
					</td>
					<td>
					
					</td>
					<td>
						<a href="#!group=<%=group.getId()%>"  onclick="reloadIfHashIsMyHref(this)" > 
							<%=group.getName()%>
						</a>
						<br> 
						<%=Util.getNumberFormatted(total, true, true, false, false, false, false)%> 
						
						<%
						if (group.getChangePerHour() != 0) {
						%> 
							<br> 
							<div align="right">
							
							<%
								String className = null; 
								String profitPerHour = Util.getNumberFormatted(group.getChangePerHour(), true, true, true, true, false, false);
	
								if (group.getChangePerHour() > 0) {
	
									if (profitDiff > 0) {
										className= "green-profit";
									} else {
										className= "gray-profit";										
									}
									
									out.write("<div class=\""+className+"\">" + profitPerHour + "</div>");
								}
								else if (group.getChangePerHour() < 0){
									out.write("<div class=\"red-profit\">" + profitPerHour + "</div>");
									
								}
							%>
						    </div>
						
						<%
						}
						%> 
					</td>
				</tr>
				
				<%}%>
			</table>
	

			
		
			 <div align="center" style="padding: 3px;margin-bottom:5px;">
		
				<jsp:include page="toprankSelectSeason.jsp">
			
					<jsp:param name="type" value="<%=String.valueOf(typeParam) %>"></jsp:param>
			
				</jsp:include>				
				<br>
					
				<%
					if (pdo.getItemCount()>pdo.getRecordPerPage()) {
						request.setAttribute("pdo", pdo);
				%>
					<jsp:include page="pagination.jsp" />	
				<%
					}
				
				request.setAttribute("pdo", pdo);
				%>			
			</div>
		</div>
		
	</div>

	
</div>
