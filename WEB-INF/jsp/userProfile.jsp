<%@page import="com.twitstreet.servlet.HomePageServlet"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.twitstreet.util.GUIUtil"%>
<%@page import="com.twitstreet.servlet.UserProfileServlet"%>
<%@page import="com.twitstreet.servlet.GetUserServlet"%>
<%@page import="com.twitstreet.db.data.StockInPortfolio"%>
<%@page import="com.twitstreet.session.UserMgr"%>
<%@page import="com.twitstreet.db.data.User"%>
<%@ page import="com.google.inject.Injector"%>
<%@ page import="com.google.inject.Guice"%>
<%@ page import="java.util.List"%>
<%@ page import="com.twitstreet.util.Util"%>
<%@ page import="com.twitstreet.market.PortfolioMgr"%>
<%@ page import="com.twitstreet.db.data.Portfolio"%>
<%@ page import="com.twitstreet.localization.LocalizationUtil"%>
<%@page import="org.apache.log4j.Logger" %>
<%
	long start = 0;
	long end = 0;
	start = System.currentTimeMillis();
	Logger logger = Logger.getLogger(this.getClass());
	
	LocalizationUtil lutil = LocalizationUtil.getInstance();
	String lang = (String) request.getSession().getAttribute(
			LocalizationUtil.LANGUAGE);

	Injector inj = (Injector) pageContext.getServletContext()
			.getAttribute(Injector.class.getName());
	UserMgr userMgr = inj.getInstance(UserMgr.class);
	PortfolioMgr portfolioMgr = inj.getInstance(PortfolioMgr.class);
	Portfolio portfolio = null;

	User user = null;
	user = (user == null) ? (User) request
			.getAttribute(GetUserServlet.GET_USER) : user;

	String parameterUser = request.getParameter(User.USER);
	user = (user == null && parameterUser != null) ? userMgr
			.getUserById(Long.valueOf(parameterUser)) : user;

	request.setAttribute(UserProfileServlet.USER_PROFILE_USER, user);

	String getUserText = request
			.getAttribute(GetUserServlet.GET_USER_TEXT) == null
			? ""
			: (String) request
					.getAttribute(GetUserServlet.GET_USER_TEXT);
	String getUserTextDisplay = request
			.getAttribute(GetUserServlet.GET_USER_DISPLAY) == null
			? ""
			: (String) request
					.getAttribute(GetUserServlet.GET_USER_DISPLAY);

	String selectedTab = (String) request
			.getAttribute(HomePageServlet.SELECTED_TAB_USER_BAR);
%>
<div id="userprofile" class="main-div">
	<jsp:include page="getUser.jsp" />
	<table class="datatbl">
		<tr>
			<td><jsp:include page="userBar.jsp"></jsp:include></td>
		</tr>
		<tr>
			<td>
				<div id="users-screen">

					<div id="top-grossing-users-content">
						<%
							if (selectedTab.equalsIgnoreCase("top-grossing-users-tab")) {
						%>
						<jsp:include page="trendyUsers.jsp" />
						<%
							}
						%>

					</div>
					<div id="user-details-content">
						<%
							if (selectedTab.equalsIgnoreCase("user-details-tab")) {
						%>
						<jsp:include page="userDetails.jsp" />
						<br>
						<jsp:include page="getUserOtherSearchResults.jsp" />
						<script type="text/javascript">
							initUserProfileTabs()
						</script>
						<%
							}
						%>
					</div>
					<div id="new-users-content">
						<%
							if (selectedTab.equalsIgnoreCase("new-users-tab")) {
						%>
						<jsp:include page="newUsers.jsp" />
						<%
							}
						%>
					</div>
				</div></td>
		</tr>
	</table>

</div>
<%
end = System.currentTimeMillis();
logger.debug("userProfile.jsp execution time: " + (end - start));
%>