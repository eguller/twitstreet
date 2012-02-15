
<%@ page import="com.twitstreet.db.data.User"%>
<%
User user = (User)request.getAttribute(User.USER);
%>
<div id="topbar">
	<a id="home" href="/">TwitStreet</a>
	<% if(user == null){ %>
	<div id="loginbox">
		<a href="/signin"><img src="/images/twitter-small.png"></img>
		</a>
	</div>
	<% } else { %>
		<div id="logoutbox">
			<table>
				<tr>
					<td rowspan="2"><img class="twuser" src="<%= user.getPictureUrl() %>" /></td>
					<td>@<span id="username"><%= user.getUserName() %></span></td>
				</tr>
				<tr>
					<td><a href="/?signout=1">Sign out >></a></td>
				</tr>
			</table>
		</div>
	<% } %>
</div>
