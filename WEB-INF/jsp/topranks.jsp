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

%>
<div id="topranks">
	<h3>Ranking</h3>
	<table class="datatbl" id="topranktable">
		<%
		int pageCount = 1;
		
		// if our users more than one page
		if (userCount > UserMgr.MAX_RANK) {
			// we should add 1 because of integer conversion
			pageCount = (userCount / UserMgr.MAX_RANK) + 1;
		}
		
		for(int i = 0; i < userList.size(); i++){ 
				int rank = i + 1;
				User user = userList.get(i);
				double total = user.getCash() + user.getPortfolio();
				if( i%2 == 0){ %>
					<tr>
	    		<% }else{ %>
	    			<tr class="odd">
	    		<% } %>
						<td class="rank-number"><%=rank%>.</td>
						<td><img class="twuser" src="<%=user.getPictureUrl()%>"/></td>
						<td><a href="/user?user=<%=user.getId() %>" title="<%=user.getUserName() %>&#39;s profile page."><%=user.getUserName() %></a> <br> $<%=Util.commaSep(total)%></td>
						<% if (user.getDirection() > 0) { %>
							<td><img src="/images/up.png" /></td>
						<% } else if(user.getDirection() < 0)  { %>
							<td><img src="/images/down.png" /></td>
						<% } else   { %>
							<td>
<!-- 							<img src="/images/nochange.png" /> -->
							</td>
						<% }%>
					</tr>
		<% } %>
	</table>
	<div id="tnt_pagination">
		<!-- <span class="disabled_tnt_pagination">Prev</span> -->
		<%
			for(int i = 1; i <= pageCount; i++) {
				if (i == 1) {
					%>
					<a class="active_tnt_link" onclick="retrievePage($(this))">1</a>
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
</div>