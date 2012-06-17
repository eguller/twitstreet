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
<div id="fb-root"></div>

<script>(function(d, s, id) {
  var js, fjs = d.getElementsByTagName(s)[0];
  if (d.getElementById(id)) return;
  js = d.createElement(s); js.id = id;
  js.src = "//connect.facebook.net/en_US/all.js#xfbml=1&appId=429661103727162";
  fjs.parentNode.insertBefore(js, fjs);
}(document, 'script', 'facebook-jssdk'));</script>

<div id="main-menu">
	<div class="title-bar">
		<div class="main-tabs">
			<a class="stocks-tab"
				onclick="showTabMain('.stocks-tab'); loadTitle('TwitStreet - <%=lutil.get("stocks.title", lang)%>');"> <%=lutil.get("stocks", lang)%>
			</a> 
			<a class="users-tab" onclick="showTabMain('.users-tab'); loadTitle('TwitStreet - <%=lutil.get("users.title", lang)%>');"> <%=lutil.get("users", lang)%>
			</a>	 
			<a class="groups-tab" onclick="showTabMain('.groups-tab'); loadTitle('TwitStreet - <%=lutil.get("groups.title",lang)%>');"> <%=lutil.get("groups", lang)%>
			</a>	
			<a class="oldseasons-tab" onclick="showTabMain('.oldseasons-tab'); loadTitle('TwitStreet - <%=lutil.get("oldseasons.title", lang)%>');"> <%=lutil.get("season.oldseasons", lang)%>
			</a>
		</div>
		<div style="float: right; margin-top: 3px;">
			<div class="fb-like" data-href="http://www.twitstreet.com" data-layout="button_count" data-width="90" data-show-faces="false" data-font="trebuchet ms"></div>
		</div>
	</div>
	<jsp:include page="stocks.jsp"></jsp:include>
	<jsp:include page="users.jsp"></jsp:include>
	<jsp:include page="groups.jsp"></jsp:include>
	<jsp:include page="oldSeasons.jsp"></jsp:include>
</div>
