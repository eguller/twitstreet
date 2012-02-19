<%@ page import="com.twitstreet.db.data.User"%>
<%
	User user = (User) request.getAttribute(User.USER);
%>
<div id="topbar">

	
	<div id="top_left_div">
		<a id="home" href="/">TwitStreet</a>
	</div>


	<div id="top_center_div" class="main-tabs">
				
					<a id="stocks-tab" class="youarehere"
						onclick="showTabMain('#stocks-tab','#stocks-container');">
						Stocks</a></h3>
						
					<a id="users-tab"
						onclick="showTabMain('#users-tab','#users-container');"> Users</a>
						
				</div>
	
	<div id="top_right_div">
		<%
			if (user == null) {
		%>
		<div id="loginbox">
			<a href="/signin"><img src="/images/twitter-small.png"></img>
			</a>
		</div>
		<%
			} else {
		%>
		<div id="logoutbox">
			<table>
				<tr>
					<td rowspan="2"><img class="twuser"
						src="<%=user.getPictureUrl()%>" /></td>
					<td>@<span id="username"><%=user.getUserName()%></span></td>
				</tr>
				<tr>
					<td><a href="/?signout=1">Sign out >></a></td>
				</tr>
			</table>
		</div>
		<%
			}
		%>
	</div>
	
</div>
