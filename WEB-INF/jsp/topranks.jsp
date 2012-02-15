<%@page import="com.twitstreet.servlet.TopRankServlet"%>
<%@ page import="com.google.inject.Injector"%>
<%@ page import="com.google.inject.Guice"%>
<%@ page import="com.twitstreet.session.UserMgr"%>
<%@ page import="com.twitstreet.db.data.User"%>
<%@ page import="java.util.ArrayList"%>
<%@ page import="com.twitstreet.util.Util"%>

<%
Injector inj = (Injector) pageContext.getServletContext().getAttribute(Injector.class.getName());
User sessionUser = (User)request.getAttribute(User.USER);
UserMgr userMgr = inj.getInstance(UserMgr.class);
//TODO : count will be cached
int userCount = userMgr.count();

String pageParam = request.getParameter("page");

if (pageParam == null || pageParam == ""){
	
	pageParam = (String) request.getSession().getAttribute(TopRankServlet.PAGE);
}
int currPage = 1;
if (pageParam != null && pageParam != "") {
	// get pageNumber from param
	try{
		
		currPage = Integer.parseInt(pageParam);
	}catch(Exception ex){		
		currPage = 1;		
	}
	if (currPage < 1) {
		currPage = 1;
	}
	// check again
	
}

request.getSession().setAttribute(TopRankServlet.PAGE,String.valueOf(currPage));

int pageCount = 1;
ArrayList<User> userList = userMgr.getTopRank(currPage);

userList = (userList==null)? new ArrayList<User>(): userList;

// if our users more than one page
int maxRank = userMgr.getRecordPerPage();
if (userCount > maxRank) {
	// we should add 1 because of integer conversion
	pageCount = (userCount / maxRank) + 1;
}
%>
<div id="topranks" class="main-div">
	<h3>Ranking</h3>
	<div id="topranks-loading-div">
		<jsp:include page="toprankPagination.jsp">
	
			<jsp:param name="currPage" value="<%=String.valueOf(currPage) %>"></jsp:param>
			<jsp:param name="pageCount" value="<%=String.valueOf(pageCount) %>"></jsp:param>
			<jsp:param name="userCount" value="<%=String.valueOf(userCount) %>"></jsp:param>
	
		</jsp:include>
	
	
		<table class="datatbl" id="topranktable">
				<%
			
			
			for(int i = 0; i < userList.size(); i++) {
					
					User user = userList.get(i);
	
					double profitDiff = 0;
					if (i > 0) {
						User prevUser = userList.get(i - 1);
						profitDiff = user.getProfit() - prevUser.getProfit();
	
					}
					double total = user.getCash() + user.getPortfolio();
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
					
					<td class="rank-number"><%=user.getRank()%>.</td>
					<td><img class="twuser" src="<%=user.getPictureUrl()%>" /></td>
					<td><a href="javascript:void(0)" onclick="loadUserProfile(<%=user.getId()%>)"
						title="<%=user.getUserName()%>&#39;s profile page."> <%=user.getUserName()%></a>
						<br> $<%=Util.commaSep(total)%> <%
									String className = null; 
									String profitPerHour = Util.getRoundedChangePerHourString(user.getProfitPerHour());
	
									if (user.getProfitPerHour() > 0) {
	
										if (profitDiff > 0) {
											className= "green-profit";
										} else {
											className= "gray-profit";										
										}
										
										out.write("<br><div class=\""+className+"\">" + profitPerHour + "</div>");
									}
									else if (user.getProfitPerHour() < 0){
										out.write("<br><div class=\"red-profit\">" + profitPerHour + "</div>");
										
									}
							%></td>
				</tr>
				<%
				}
			%>
			</table>
	
	
		<jsp:include page="toprankPagination.jsp">
	
			<jsp:param name="currPage" value="<%=String.valueOf(currPage) %>"></jsp:param>
			<jsp:param name="pageCount" value="<%=String.valueOf(pageCount) %>"></jsp:param>
			<jsp:param name="userCount" value="<%=String.valueOf(userCount) %>"></jsp:param>
	
		</jsp:include>
	</div>
</div>