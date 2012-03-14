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
			</a> <a class="users-tab" onclick="showTabMain('.users-tab');"> <%=lutil.get("users", lang)%>
			</a>
		</div>

		<div class="h3-right-top">
			<div class="stocks-tab-right-content"
				style="display: none; text-align: right; float: right; text-size: 20px; height: 20px">
				<a id="trendy-stocks-id" href="#!suggestedstocks"
					onclick="reloadIfHashIsMyHref(this)"> <%=lutil.get("suggestedstocks.header", lang)%>
				</a>
			</div>
			<div class="users-tab-right-content"
				style="display: none; text-align: right; float: right; text-size: 20px; height: 20px">
				<a id="trendy-users-id" href="#!topgrossingusers"
					onclick="reloadIfHashIsMyHref(this)"> <%=lutil.get("topgrossingusers.header", lang)%>
				</a>
			</div>
		</div>
		<div style="clear: both"></div>
	</div>
	<jsp:include page="stocks.jsp"></jsp:include>
	<jsp:include page="users.jsp"></jsp:include>
</div>