<%@page import="com.twitstreet.util.GUIUtil"%>
<%@ page import="com.twitstreet.db.data.User"%>
<%@ page import="com.twitstreet.localization.LocalizationUtil" %>
<%
LocalizationUtil lutil = LocalizationUtil.getInstance();
String lang = (String)request.getSession().getAttribute(LocalizationUtil.LANGUAGE);
	User user = (User) request.getAttribute(User.USER);
%>
<div id="topbar">

	
	<div id="top_left_div">
	
		<div id="twitstreet-header-container" style="width:150px">
			<div id="twitstreet-header"  style="float:left">
				<a id="home" href="/">TwitStreet</a>
			</div>
			<div id="twitstreet-share-main" style="float:right">
				<%=GUIUtil.getInstance().getTwitterShareButton("", "twitter.share.main",lang)%>
			</div>
		</div>
	
	<br>
	<br>
	
		<div id="twitstreet-languages" >
		
			<a href="javascript:void(0)"  onclick="loadLanguage('en')" >English</a>
			<a href="javascript:void(0)" onclick="loadLanguage('tr')" >Türkçe</a>
		</div>
	
	
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
					<td rowspan="2"><img class="twuser" width="48" height="48" 
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
