<%@ page import="com.google.inject.Injector"%>
<%@ page import="com.google.inject.Guice"%>
<%@ page import="com.twitstreet.session.UserMgr"%>
<%@ page import="com.twitstreet.db.data.User"%>
<%@ page import="java.util.ArrayList"%>
<%@ page import="com.twitstreet.util.Util" %>

<%
	Injector inj = (Injector) pageContext.getServletContext().getAttribute(Injector.class.getName());
	User sessionUser = (User) request.getSession().getAttribute(User.USER);
	UserMgr userMgr = inj.getInstance(UserMgr.class);
	ArrayList<User> userList = userMgr.getTopRank();
%>

<div id="topranks">
	<h3>Ranking</h3>
	<table class="datatbl" id="topranktable">
		<%
			for (int i = 0; i < userList.size(); i++) {
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
								String profitPerHour = "$" + (int) (user.getProfitPerHour()+1);

								if (user.getProfitPerHour() > 0) {

									profitPerHour = profitPerHour + "/h &#9650;";
									if (profitDiff > 0) {
										className= "green-profit";
										

									} else {
										className= "gray-profit";
										
									}
									
									out.write("<br><div class=\""+className+"\">" + profitPerHour + "</div>");
								}
								else if (user.getProfitPerHour() < 0){
									profitPerHour = profitPerHour + "/h &#9660;";
									out.write("<br><div class=\"red-profit\">" + profitPerHour + "</div>");
									
								}
						%>
						</td>
						<%
							if (user.getDirection() > 0) {
						%>
							<td><img src="/images/up.png" /></td>
						<%
							} else if (user.getDirection() < 0) {
						%>
							<td><img src="/images/down.png" /></td>
						<%
							} else {
						%>
							<td>
<!-- 							<img src="/images/nochange.png" /> -->
							</td>
						<%
							}
						%>
					</tr>
		<%
			}
		%>
	</table>
</div>