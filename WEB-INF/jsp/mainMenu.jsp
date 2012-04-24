<%@page import="com.twitstreet.db.data.Stock"%>
<%@page import="com.twitstreet.servlet.HomePageServlet"%>
<%@page import="com.twitstreet.db.data.User"%>
<%@page import="com.twitstreet.servlet.GetUserServlet"%>
<%@ page import="com.twitstreet.localization.LocalizationUtil"%>
<%
	LocalizationUtil lutil = LocalizationUtil.getInstance();
	String lang = (String) request.getSession().getAttribute(
			LocalizationUtil.LANGUAGE);
%>
<div id="main-menu">
	<div class="title-bar">
		<div class="main-tabs">
			<a class="stocks-tab youarehere"
				onclick="showTabMain('.stocks-tab');"> <%=lutil.get("stocks", lang)%>
			</a> 
			<a class="users-tab" onclick="showTabMain('.users-tab');"> <%=lutil.get("users", lang)%>
			</a>	 
			<a class="groups-tab" onclick="showTabMain('.groups-tab');"> <%=lutil.get("groups", lang)%>
			</a>	
			<a class="oldseasons-tab" onclick="showTabMain('.oldseasons-tab');"> <%=lutil.get("season.oldseasons", lang)%>
			</a>
		</div>

	</div>
	<jsp:include page="stocks.jsp"></jsp:include>
	<jsp:include page="users.jsp"></jsp:include>
	<jsp:include page="groups.jsp"></jsp:include>
	<jsp:include page="oldSeasons.jsp"></jsp:include>
</div>