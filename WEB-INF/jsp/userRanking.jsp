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
<%@page import="org.apache.log4j.Logger" %>
<%
	long start = 0;
	long end = 0;
	start = System.currentTimeMillis();
	Logger logger = Logger.getLogger(this.getClass());
	
LocalizationUtil lutil = LocalizationUtil.getInstance();
String lang = (String)request.getSession().getAttribute(LocalizationUtil.LANGUAGE);

Injector inj = (Injector) pageContext.getServletContext().getAttribute(Injector.class.getName());
User sessionUser = (User)request.getAttribute(User.USER);
UserMgr userMgr = inj.getInstance(UserMgr.class);
GroupMgr groupMgr = inj.getInstance(GroupMgr.class);

String typeParam = request.getParameter("type");
String groupParam = request.getParameter(TopRankServlet.GROUP);

typeParam = (typeParam==null)?"currentSeason":typeParam;
groupParam = (groupParam==null)?"":groupParam;
long selectedGroupId = -1;

if(groupParam.length()>0){
	try{
		selectedGroupId = Long.parseLong(groupParam);
	}catch(Exception ex){
		
	}
}

PaginationDO pdo = (PaginationDO) request.getAttribute("pdo");
if(pdo==null){
	pdo = new PaginationDO(1,userMgr.getUserCount(),UserMgr.MAX_TOPRANK_USER,"toprank","toprank",true);
}

request.getSession().setAttribute(TopRankServlet.PAGE,String.valueOf(pdo.getCurrPage()));

ArrayList<User> userList = (ArrayList<User>) request.getAttribute(TopRankServlet.TOPRANKS_USER_LIST);

userList = (userList==null)? userMgr.getTopRank(pdo.getOffset(), pdo.getRecordPerPage()): userList;

ArrayList<Group> userGroups = null;
if(sessionUser!=null) {
	
	userGroups = groupMgr.getGroupsForUser(sessionUser.getId());
}
%>


<div id="topranks" class="main-div">
	<div id="user-ranking-content">
		<div align="center" style="margin-bottom:5px;">
		
		
			<table class="datatbl">
				<tr>
					<td colspan="3" width="100%">
						<table class="datatbl">
							<tr>
								<td width="<%=(userGroups!=null && userGroups.size()>0)?"50%":"66%"%>">
									<div style="float:right;margin: 5px">
										<jsp:include page="toprankSelectSeason.jsp">
									
											<jsp:param name="type" value="<%=String.valueOf(typeParam) %>"></jsp:param>
									
										</jsp:include>		
									</div>
								</td>
								<td width="<%=(userGroups!=null && userGroups.size()>0)?"50%":"33%"%>">								
									<div style="float:left;margin: 5px">	
										<% 			
									       
										if(userGroups!=null && userGroups.size()>0){
										%>
										<select  class="topRankSelectGroup" style="font-size: 10px" onchange="toprank(null,null,null,$(this).val())">
									
											<option <%=(selectedGroupId<0)?"selected=\"selected\"":""%> value="-1"><%=lutil.get("group.overall", lang)%></option>
										
										
										<%		
											for(Group group : userGroups){
										%>
												<option <%=(selectedGroupId == group.getId())?"selected=\"selected\"":""%> value="<%=group.getId()%>"><%=group.getName()%></option>
										<%		
											}
										%>
										</select>		
											
										<%		
										}
										%>
									</div>
								</td>
							</tr>
						</table>
					</td>
				</tr>
				
				
				<tr>
					
					<td colspan="2" width="66%">
						
					
						<div style="float:right;margin: 5px">
							<%
							if (pdo.getItemCount()> pdo.getRecordPerPage()) {
								request.setAttribute("pdo", pdo);
							%>
								<jsp:include page="pagination.jsp" />	
							
							<%
							}
							%>
						</div>
					
					
					
					
					
					</td>
					<td width="33%">
						<div style="float:right;margin: 5px">	<%
							if (sessionUser!=null && pdo.getItemCount()> pdo.getRecordPerPage()) {
								int rankOfUser = -1;
								
								if(selectedGroupId<0){
									if(typeParam.equalsIgnoreCase("currentSeason")){
										rankOfUser = sessionUser.getRank();	
									}else{
										rankOfUser = sessionUser.getRankCumulative();
									}	
								}else{					
									if(typeParam.equalsIgnoreCase("currentSeason")){
		
										rankOfUser = groupMgr.getRankOfUserForGroup(sessionUser.getId(), selectedGroupId);
									}else{
										rankOfUser = groupMgr.getAllTimeRankOfUserForGroup(sessionUser.getId(), selectedGroupId);
									}	
								}
								
								int pageOfUser = Util.getPageOfRank(rankOfUser, pdo.getRecordPerPage());
								
							%>
								<a href="javascript:void(0)" onclick="toprank(<%=pageOfUser%>); "> <%=lutil.get("ranking.showme", lang) %></a>
							
							<%
							}
							%>
							
						</div>
					</td>
				<tr>
			</table>			
		</div>
	
			
			
	
		<table class="datatbl" id="topranktable" style="margin-bottom:10px">
				<%
			
			
			for(int i = 0; i < userList.size(); i++) {
					
					int rankDisplay =  pdo.getOffset() + i + 1;
					User user = userList.get(i);
	
					double profitDiff = 0;
					if (i > 0) {
						User prevUser = userList.get(i - 1);
						profitDiff = user.getProfit() - prevUser.getProfit();
	
					}
					double total = user.getTotal();
					
					if(typeParam.equalsIgnoreCase("allTime")){
						total = user.getValueCumulative();						
					}
					String clssNm = "odd";
					
					if (i % 2 == 1) {
						clssNm ="";
					}
					
					String style = "";
					if(sessionUser!=null && sessionUser.getId() == user.getId()){
						
						style = "border:solid 3px #DDD";
						
					}
					
			%>
				
			<tr style="<%=style%>" class="<%=clssNm%>">
				
				<td class="rank-number"><%=rankDisplay%>.</td>
				<td><img class="twuser" width="48" height="48"  src="<%=user.getPictureUrl()%>" /></td>
				<td><a href="#!user=<%=user.getId()%>"  onclick="reloadIfHashIsMyHref(this); loadTitle('<%=lutil.get("user.bar.profile", lang, user.getUserName())%>');" title="<%=lutil.get("user.details.tip",lang, user.getUserName())%>"> <%=user.getUserName()%></a>
					<br> <%=Util.getNumberFormatted(total, true, true, false, false, false, false)%> <%
								String className = null; 
								String profitPerHour = Util.getNumberFormatted(user.getProfit(), true, true, true, true, false, false);

								if (user.getProfit() > 0) {

									if (profitDiff > 0) {
										className= "green-profit";
									} else {
										className= "gray-profit";										
									}
									
									out.write("<br><div class=\""+className+"\">" + profitPerHour + "</div>");
								}
								else if (user.getProfit() < 0){
									out.write("<br><div class=\"red-profit\">" + profitPerHour + "</div>");
									
								}
						%></td>
			</tr>
			<%
			}
		request.setAttribute("pdo", null);
		%>
		</table>
	</div>
</div>
<%
end = System.currentTimeMillis();
logger.debug("userRanking.jsp execution time: " + (end - start));
%>
