<%@ page import="com.twitstreet.db.data.User"%>
<%@ page import="com.twitstreet.localization.LocalizationUtil" %>
<%
LocalizationUtil lutil = LocalizationUtil.getInstance();
String lang = (String)request.getSession().getAttribute(LocalizationUtil.LANGUAGE);
	User user = (User) request.getAttribute(User.USER);
%>
<div id="topbar">

	
	<div id="top_left_div">
		<a id="home" href="/">TwitStreet</a>
		<br>
		<a href="javascript:void(0)"  onclick="loadLanguage('en')" >English</a>
		<a href="javascript:void(0)" onclick="loadLanguage('tr')" >Türkçe</a>
	</div>


	<div id="top_center_div">
				

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
					<td><span id="username"><a href="#user-<%=user.getId()%>"
						 onclick="reloadIfHashIsMyHref(this)" title="<%=lutil.get("user.details.tip", lang, user.getUserName()) %>"> <%=user.getUserName()%></a></span></td>
				</tr>
				<tr>
					<td><a onclick="signout();" href="javascript:void(0)"><%=lutil.get("signout", lang)%> &gt;&gt;</a></td>
				</tr>
			</table>
		</div>
		<%
			}
		%>
	</div>
	
</div>
