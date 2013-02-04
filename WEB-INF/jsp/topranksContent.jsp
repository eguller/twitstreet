<%@page import="com.twitstreet.servlet.HomePageServlet"%>
<%@page import="com.twitstreet.db.data.Group"%>
<%@page import="com.twitstreet.servlet.PaginationDO"%>
<%@page import="com.twitstreet.servlet.TopRankServlet"%>
<%@ page import="com.google.inject.Injector"%>
<%@ page import="com.google.inject.Guice"%>
<%@ page import="com.twitstreet.session.UserMgr"%>
<%@ page import="com.twitstreet.db.data.User"%>
<%@ page import="java.util.ArrayList"%>
<%@ page import="com.twitstreet.util.Util"%>
<%@ page import="com.twitstreet.localization.LocalizationUtil" %>
<%@page import="org.apache.log4j.Logger" %>
<%
	long start = 0;
	long end = 0;
	start = System.currentTimeMillis();
	Logger logger = Logger.getLogger(this.getClass());
	
LocalizationUtil lutil = LocalizationUtil.getInstance();
String lang = (String)request.getSession().getAttribute(LocalizationUtil.LANGUAGE);

Injector inj = (Injector) pageContext.getServletContext().getAttribute(Injector.class.getName());
User sessionUser = (User)request.getAttribute(User.USER);



%>

<div id="topranks" class="main-div">


	<div id="topranks-screen">
		<jsp:include page="userRanking.jsp"></jsp:include>
	</div>



</div>

<%
end = System.currentTimeMillis();
logger.debug("toprankContent.jsp execution time: " + (end - start));
%>
