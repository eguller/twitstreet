<%@page import="java.util.ArrayList"%>
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
						<td style="width:60px;">
							<a href="/">
								 <img height="50" width="50" alt="twitstreet.com" title="twitstreet.com"
											src="/images/twitstreet_logo_50.png">
							</a>
						</td>
						<td style="vertical-align: top">
							<div id="twitstreet-header" style="float: left; height:25px; ">
								<a id="home"
									style="vertical-align: top; line-height: 30px; height: 30px"
									href="/">TwitStreet
									</a>	
									<br>	
							<div id="twitstreet-share-main" style="float: left; clear:left">
									<%=GUIUtil.getInstance().getTwitterShareButton("",
										"twitter.share.main", lang)%>
								</div>
								<div id="twitstreet-share-main" style="float: left;">
									<%=GUIUtil.getInstance().getTwitterFollowButton(
										"twitstreet_game", lang)%>
								</div>
							</div>
								
					
							
							<div style="float: left;vertical-align: bottom">
								
							
							</div>
						</td>
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

	<div id="top_right_div" style="vertical-align: bottom">

		<%
			if (user == null) {
		%>
		<div id="loginbox" style="float:right">
			<a href="/signin"><img src="/images/twitter-small.png"></img> </a>
		</div>
		<%
			} else {
		%>
		<div id="logoutbox" style="float:right">

			<table>
				<tr>
					<td rowspan="2"><img class="twuser" width="48" height="48"
						src="<%=user.getPictureUrl()%>" />
					</td>
					<td><span id="username"><a
							href="#!user=<%=user.getId()%>"
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
		
		<div style="float:left;height:33px;width:30px;"></div>
			
			<div id="twitstreet-languages" style="font-size:6px; float:left;text-align: left">
													
						
				<select onchange="loadLanguage($(this).val())" style="font-size:11px;">
	
				<% 					
				ArrayList<String> languages = LocalizationUtil.getInstance().getLanguages();
				for(int i = 0; i < languages.size();i++) {	
					
			      			String selectLang = languages.get(i);
				%>
					<option <%=(selectLang.equalsIgnoreCase(lang))?"selected=\"selected\"":""%> value="<%=selectLang%>"><%= lutil.getLanguageLongName(selectLang) %></option>
					
				<%	
				}		
				%>
				</select>
			
			</div>
			
	</div>

</div>
