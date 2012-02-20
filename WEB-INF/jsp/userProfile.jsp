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

<%
	Injector inj = (Injector) pageContext.getServletContext().getAttribute(Injector.class.getName());
	UserMgr userMgr = inj.getInstance(UserMgr.class);
	PortfolioMgr portfolioMgr = inj.getInstance(PortfolioMgr.class);
	Portfolio portfolio = null;

	User user = null;
	user = (user == null) ? (User) request.getAttribute(GetUserServlet.GET_USER) : user;

	String parameterUser = request.getParameter(User.USER);
	user = (user == null && parameterUser != null) ? userMgr.getUserById(Long.valueOf(parameterUser)) : user;
	
	request.setAttribute(UserProfileServlet.USER_PROFILE_USER, user);

	String getUserText = request.getAttribute(GetUserServlet.GET_USER_TEXT) == null ? "" : (String) request.getAttribute(GetUserServlet.GET_USER_TEXT);
	String getUserTextDisplay = request.getAttribute(GetUserServlet.GET_USER_DISPLAY) == null ? "" : (String) request.getAttribute(GetUserServlet.GET_USER_DISPLAY);

%>
<div id="userprofile" class="main-div" > 
	<jsp:include page="getUser.jsp" />
	
	<%
		if (user != null) {
			portfolio = portfolioMgr.getUserPortfolio(user);
			String userIdStr = String.valueOf(user.getId());
			
	%>
	
			<h3>
				<a style="color:#000000"  href="http://twitter.com/#!/<%=user == null ? "" : user.getUserName()%>"
					title="<%=user == null ? "" : user.getUserName()%>&#39;s twitter page"
					target="_blank"><%=user == null ? "" : user.getUserName()%></a>
				
			</h3>
			<jsp:include page="userDetails.jsp" />
			
			<script type="text/javascript">initUserProfileTabs()</script>
	
			<jsp:include page="getUserOtherSearchResults.jsp" />



	<%
		} else if (getUserText.length() > 0) {
	%>	
			<div id="searchusernoresult"><p>No results found.</p></div>
	<%
		}
	%>
	
	
	
	
</div>