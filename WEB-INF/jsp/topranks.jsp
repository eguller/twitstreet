<%@ page import="com.google.inject.Injector"%>
<%@ page import="com.google.inject.Guice"%>
<%@ page import="com.twitstreet.session.UserMgr"%>
<%@ page import="com.twitstreet.db.data.User"%>
<%@ page import="java.util.ArrayList"%>


<%
Injector inj = (Injector) pageContext.getServletContext().getAttribute(Injector.class.getName());
User sessionUser = (User)request.getSession().getAttribute(User.USER);
UserMgr userMgr = inj.getInstance(UserMgr.class);
ArrayList<User> userList = userMgr.getTopRank();

%>

<div id="topranks">
	<h3>Ranking</h3>
	<table class="datatbl" id="topranktable">
		<% for(int i = 0; i < userList.size(); i++){ 
				int rank = i + 1;
				User user = userList.get(i);
				int total = user.getCash() + user.getPortfolio();
				if( i == 0){ %>
					<tr>
	    		<% }else{ %>
	    			<tr class="odd">
	    		<% } %>
						<td><%=rank%>.</td>
						<td><%=user.getUserName() %></td>
						<td><%=total%></td>
						<% if (user.getDirection() == 1) { %>
							<td><img src="../images/up.gif" /></td>
						<% } else { %>
							<td><img src="../images/down.gif" /></td>
						<% } %>
					</tr>
		<% } %>
	</table>
</div>