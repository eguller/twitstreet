<%@ page import="com.google.inject.Injector"%>
<%@ page import="com.google.inject.Guice"%>
<%@ page import="com.twitstreet.session.UserMgr"%>
<%@ page import="com.twitstreet.db.data.User"%>
<%@ page import="java.util.ArrayList"%>
<%@ page import="com.twitstreet.util.Util" %>

<%
Injector inj = (Injector) pageContext.getServletContext().getAttribute(Injector.class.getName());
User sessionUser = (User)request.getSession().getAttribute(User.USER);
UserMgr userMgr = inj.getInstance(UserMgr.class);
ArrayList<User> userList = userMgr.getTopRank(1);
//TODO : count will be cached
int userCount = userMgr.count();
int pageCount = 1;

// if our users more than one page
if (userCount > UserMgr.MAX_RANK) {
	// we should add 1 because of integer conversion
	pageCount = (userCount / UserMgr.MAX_RANK) + 1;
}
%>
<div id="topranks">
	<h3>Ranking</h3>
		<div id="tnt_pagination">
		<!-- <span class="disabled_tnt_pagination">Prev</span> -->
		<%
			for(int i = 1; i <= pageCount; i++) {
				
				int start = i*UserMgr.MAX_RANK+1;
				int stop = (i+1)*UserMgr.MAX_RANK;
				
				if(stop>userCount){
					
					stop = userCount;
				}
				
				if (i == 1) {
					%>
					<a class="active_tnt_link" onclick="retrievePage($(this))"><%=i%></a>
					<%
				} else {
					%>
					<a href="javascript:void(0)" onclick="retrievePage($(this))"><%=i%></a>
					<%
				}
			}
		%>
			<!--<a href="#forward">Next</a>-->
	</div>
	<table class="datatbl" id="topranktable">
		<%
		
		
		for(int i = 0; i < userList.size(); i++) {
				int rank = i + 1;
				User user = userList.get(i);

				double profitDiff = 0;
				if (i > 0) {
					User prevUser = userList.get(i - 1);
					profitDiff = user.getProfit() - prevUser.getProfit();

				}
				double total = user.getCash() + user.getPortfolio();
				if (i % 2 == 1) {
		%>
					<tr>
	    		<%
	    			} else {
	    		%>
	    			<tr class="odd">
	    		<%
	    			}
	    		%>
						<td class="rank-number"><%=rank%>.</td>
						<td><img class="twuser" src="<%=user.getPictureUrl()%>"/></td>
						<td><a href="/user?user=<%=user.getId()%>" title="<%=user.getUserName()%>&#39;s profile page.">
						<%=user.getUserName()%></a> 
						<br> $<%=Util.commaSep(total)%>
						
						<%
								String className = null; 
								String profitPerHour = "$" ;

								if (user.getProfitPerHour() > 0) {

									profitPerHour = profitPerHour + (int) (user.getProfitPerHour()+1)+"/h &#9650;";
									if (profitDiff > 0) {
										className= "green-profit";
										

									} else {
										className= "gray-profit";
										
									}
									
									out.write("<br><div class=\""+className+"\">" + profitPerHour + "</div>");
								}
								else if (user.getProfitPerHour() < 0){
									profitPerHour = profitPerHour + (int) (user.getProfitPerHour()-1)+"/h &#9660;";
									out.write("<br><div class=\"red-profit\">" + profitPerHour + "</div>");
									
								}
						%>
						</td>			
					</tr>
		<%
			}
		%>
	</table>

</div>