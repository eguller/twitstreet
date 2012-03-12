<%@page import="com.twitstreet.session.UserMgr"%>
<%@page import="com.twitstreet.config.ConfigMgr"%>
<%@page import="com.twitstreet.util.GUIUtil"%>
<%@ page import="com.twitstreet.db.data.User"%>
<%@ page import="com.twitstreet.localization.LocalizationUtil"%>
<%@ page import="com.google.inject.Injector"%>
<%
	Injector inj = (Injector) pageContext.getServletContext()
			.getAttribute(Injector.class.getName());
	ConfigMgr configMgr = inj.getInstance(ConfigMgr.class);
	LocalizationUtil lutil = LocalizationUtil.getInstance();
	String lang = (String) request.getSession().getAttribute(
			LocalizationUtil.LANGUAGE);
	User user = (User) request.getAttribute(User.USER);
%>
<div id="topbar" style="overflow: hidden">


	<div id="top_left_div">

		<div id="twitstreet-header-container">
			<table class="datatbl2">
				<tbody>
					<tr>

						<td colspan=3 style="vertical-align: top">
							<div id="twitstreet-header" style="float: left">
								<a id="home"
									style="vertical-align: top; line-height: 30px; height: 30px"
									href="/">TwitStreet
								</a>
								<img height="35" width="35" alt="twitstreet.com" title="twitstreet.com"
								src="/images/twitstreet_logo_50.png" onclick="http://www.twitstreet.com/">
							</div>
						</td>
					</tr>

					<tr>

						<td style="float: left; margin-right: 5px">
							<div id="twitstreet-share-main" style="float: left">
								<%=GUIUtil.getInstance().getTwitterShareButton("",
									"twitter.share.main", lang)%>
							</div></td>
						<td style="float: left; margin-right: 5px">
							<div id="twitstreet-share-main" style="float: left">
								<%=GUIUtil.getInstance().getTwitterFollowButton(
									"twitstreet_game", lang)%>
							</div></td>

						<td style="float: right; margin-left: 5px">
							<div id="twitstreet-languages" style="text-align: right">
								<%
									if (!"en".equalsIgnoreCase(lang)) {
								%>
								<a href="javascript:void(0)" onclick="loadLanguage('en')">English</a>

								<%
									} else {
								%>
								<span>English</span>
								<%
									}
								%>

								<%
									if (!"tr".equalsIgnoreCase(lang)) {
								%>
								<a href="javascript:void(0)" onclick="loadLanguage('tr')">Türkçe</a>

								<%
									} else {
								%>
								<span>Türkçe</span>
								<%
									}
								%>

							</div></td>
					</tr>
				</tbody>

			</table>



		</div>


	</div>


	<div id="top_center_div">
		<div align="center">
		
			<h1><b><%=lutil.get("season.info", lang) %></b></h1>
		
			<h1><b><%=lutil.get("season.time", lang) %></b></h1>
			
		</div>
	</div>

	<div id="top_right_div">
		<%
			if (user == null) {
		%>
		<div id="loginbox">
			<a href="/signin"><img src="/images/twitter-small.png"></img> </a>
		</div>
		<%
			} else {
		%>
		<div id="logoutbox">
			<table>
				<tr>
					<td rowspan="2"><img class="twuser" width="48" height="48"
						src="<%=user.getPictureUrl()%>" />
					</td>
					<td><span id="username"><a
							href="#user-<%=user.getId()%>"
							onclick="reloadIfHashIsMyHref(this)"
							title="<%=lutil.get("user.details.tip", lang,
						user.getUserName())%>">
								<%=user.getUserName()%></a>
					</span>
					</td>
				</tr>
				<tr>
					<td><a onclick="signout();" href="javascript:void(0)"><%=lutil.get("signout", lang)%>
							&gt;&gt;</a>
					</td>
				</tr>
			</table>
		</div>
		<%
			}
		%>
	</div>

</div>
