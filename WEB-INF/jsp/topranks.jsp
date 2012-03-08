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
	
}
else if(sessionUser!=null){
	currPage= userMgr.getPageOfRank(sessionUser.getRank());
	
}

request.getSession().setAttribute(TopRankServlet.PAGE,String.valueOf(currPage));

int pageCount = 1;
ArrayList<User> userList = userMgr.getTopRank(currPage);

userList = (userList==null)? new ArrayList<User>(): userList;

// if our users more than one page
int maxRank = userMgr.getRecordPerPage();
if (userCount > maxRank) {
	// we should add 1 because of integer conversion
	pageCount = (userCount / maxRank);
	if(userCount%maxRank!=0){
		pageCount++;
	}
}
%>


<div id="topranks" class="main-div">
	<div style="padding-top:0px; height:22px" class="h3">
	
		<div align="left" style="float: left;padding-top:4px">
			<%=lutil.get("ranking.header", lang) %>
		</div>
		<div align="right" style="float: right">
			<jsp:include page="toprankPagination.jsp">
		
				<jsp:param name="currPage" value="<%=String.valueOf(currPage) %>"></jsp:param>
				<jsp:param name="pageCount" value="<%=String.valueOf(pageCount) %>"></jsp:param>
				<jsp:param name="userCount" value="<%=String.valueOf(userCount) %>"></jsp:param>
		
			</jsp:include>				
		</div>
			
	</div>	

	<div id="topranks-loading-div">
		
	
		<table class="datatbl" id="topranktable" style="margin-bottom:10px">
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
					<td><img class="twuser" width="48" height="48"  src="<%=user.getPictureUrl()%>" /></td>
					<td><a href="#user-<%=user.getId()%>"  onclick="reloadIfHashIsMyHref(this)" title="<%=lutil.get("user.details.tip",lang, user.getUserName())%>"> <%=user.getUserName()%></a>
						<br> <%=Util.getNumberFormatted(total, true, true, false, false, false, false)%> <%
									String className = null; 
									String profitPerHour = Util.getRoundedProfitPerHourString(user.getProfit());
	
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
			%>
			</table>
	
		<div style="padding-top:0px; height:22px" class="h3" align="right">
			<jsp:include page="toprankPagination.jsp">
		
				<jsp:param name="currPage" value="<%=String.valueOf(currPage) %>"></jsp:param>
				<jsp:param name="pageCount" value="<%=String.valueOf(pageCount) %>"></jsp:param>
				<jsp:param name="userCount" value="<%=String.valueOf(userCount) %>"></jsp:param>
		
			</jsp:include>
		</div>
	</div>
</div>
