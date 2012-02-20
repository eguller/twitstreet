<%@page import="com.twitstreet.servlet.GetUserServlet"%>
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

<%@page import="com.twitstreet.session.UserMgr"%>
<%
	Injector inj = (Injector) pageContext.getServletContext().getAttribute(Injector.class.getName());
	UserMgr userMgr = inj.getInstance(UserMgr.class);

	String getUserText = request.getAttribute(GetUserServlet.GET_USER_TEXT) == null ? "" : (String) request.getAttribute(GetUserServlet.GET_USER_TEXT);
	String getUserDisplay = request.getAttribute(GetUserServlet.GET_USER_DISPLAY) == null ? "" : (String) request.getAttribute(GetUserServlet.GET_USER_DISPLAY);

	User user = null;

	try {
		String userId = (String) request.getParameter(User.USER);
		user = userMgr.getUserById(Long.valueOf(userId));
	} catch (Exception ex) {

	}

	if (getUserText == null || getUserText.length() < 1) {

		getUserText = getUserDisplay;
	}
	if (getUserText == null || getUserText.length() < 1) {
		if (user != null) {
			getUserText = user.getUserName();
		}

	}
%>
<div id="get-user-div" class="main-div">

	<div id="userholder">

		<div>
			<input type="text" class="textbox" id="getUserTextboxId" value="<%=getUserText%>"
				name="getUserText" /> <input type="button" id="getUserButtonId"
				onclick="getUser($('#getUserTextboxId').val())" value="Search">
		</div>
		<input type="hidden" id="getUserTextHiddenId" value="<%=getUserText%>" /> 
		<input type="hidden" id="getUserIdHiddenId"
			value="<%=user == null ? "" : user.getId()%>" />
	</div>

	<script type="text/javascript">
		$("#getUserTextboxId").keyup(function(event) {
			if (event.keyCode == 13) {
				$("#getUserButtonId").click();
			}
		});
	</script>


</div>


