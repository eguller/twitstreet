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
									href="/">TwitStreet<img height="35" width="35" alt=""
									src="/images/twitstreet_logo_50.png">
								</a>
							</div>
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
		<% if(user != null){ %>
		<div>
			<p style="text-align: center">
				Intive your friends with this link, <br><b>http://www.twitstreet.com/?ref=<%=user.getId() %></b><br> win <span class="green">$<%=(int)(configMgr.getInitialMoney() * UserMgr.INVITE_MONEY_RATE)%></span> more
			</p>
		</div>
		<%} %>
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
