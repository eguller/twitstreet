<%@page import="com.twitstreet.servlet.GetGroupServlet"%>
<%@page import="java.util.HashMap"%>
<%@page import="com.twitstreet.twitter.SimpleTwitterUser"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.twitstreet.db.data.UserStock"%>
<%@page import="java.sql.SQLException"%>
<%@page import="com.twitstreet.market.StockMgr"%>
<%@ page import="com.google.inject.Injector"%>
<%@ page import="com.google.inject.Guice"%>
<%@ page import="com.twitstreet.db.data.User"%>
<%@ page import="com.twitstreet.db.data.Stock"%>
<%@ page import="com.twitstreet.util.Util"%>
<%@page import="com.twitstreet.db.data.Portfolio"%>
<%@page import="com.twitstreet.market.PortfolioMgr"%>
<%@page import="com.twitstreet.config.ConfigMgr"%>
<%@page import="com.twitstreet.session.UserMgr"%>
<%@ page import="com.twitstreet.servlet.HomePageServlet"%>
<%@ page import="com.twitstreet.localization.LocalizationUtil" %>


<%	LocalizationUtil lutil = LocalizationUtil.getInstance();
String lang = (String)request.getSession().getAttribute(LocalizationUtil.LANGUAGE);

	Injector inj = (Injector) pageContext.getServletContext().getAttribute(Injector.class.getName());
	UserMgr userMgr = inj.getInstance(UserMgr.class);

	String getGroupText = request.getAttribute(GetGroupServlet.GET_GROUP_TEXT) == null ? "" : (String) request.getAttribute(GetGroupServlet.GET_GROUP_TEXT);
	String getGroupDisplay = request.getAttribute(GetGroupServlet.GET_GROUP_DISPLAY) == null ? "" : (String) request.getAttribute(GetGroupServlet.GET_GROUP_DISPLAY);
	User user = (User) request.getAttribute(User.USER);
	

	if (getGroupText == null || getGroupText.length() < 1) {

		getGroupText = getGroupDisplay;
	}

%>
<div id="get-group-div" class="main-div">

	<div id="groupholder">

		<div>
			<input type="text" class="textbox" id="getGroupTextboxId" value="<%=getGroupText%>"
				name="getGroupText" /> <input type="button" id="getGroupButtonId"
				onclick=" reloadIfHashIs('#!searchgroup='+$('#getGroupTextboxId').val()); window.location = '#!searchgroup='+$('#getGroupTextboxId').val();" value="<%=lutil.get("shared.search", lang) %>">
				<script>
			
					jQuery('#getGroupTextboxId').click(function() {
						selectAllText(jQuery(this))
					});
					
				</script>
		</div>

	</div>

	<script type="text/javascript">
		$("#getGroupTextboxId").keyup(function(event) {
			if (event.keyCode == 13) {
				$("#getGroupButtonId").click();
			}
		});
	</script>


</div>


