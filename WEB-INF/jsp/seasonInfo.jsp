<%@page import="com.twitstreet.season.SeasonResult"%>
<%@page import="com.twitstreet.servlet.SeasonServlet"%>
<%@page import="com.twitstreet.season.SeasonMgr"%>
<%@page import="com.twitstreet.season.SeasonInfo"%>
<%@page import="com.twitstreet.util.GUIUtil"%>
<%@ page import="com.google.inject.Injector"%>
<%@ page import="com.google.inject.Guice"%>
<%@ page import="com.twitstreet.session.UserMgr"%>
<%@ page import="com.twitstreet.db.data.User"%>
<%@ page import="com.twitstreet.localization.LocalizationUtil"%>
<%@ page import="com.twitstreet.util.Util"%>
<%@ page import="com.twitstreet.servlet.TwitStreetServlet"%>
<%@page import="org.apache.log4j.Logger" %>
<%
	long start = 0;
	long end = 0;
	start = System.currentTimeMillis();
	Logger logger = Logger.getLogger(this.getClass());
	
	Injector inj = (Injector) pageContext.getServletContext().getAttribute(Injector.class.getName());

	LocalizationUtil lutil = LocalizationUtil.getInstance();
	String lang = (String) request.getSession().getAttribute(LocalizationUtil.LANGUAGE);

	User user = (User) request.getAttribute(User.USER);
	SeasonMgr seasonMgr = inj.getInstance(SeasonMgr.class);

	int seasonId = -1;

	try {
		seasonId = Integer.valueOf(request.getParameter(SeasonServlet.SEASON_ID));
	} catch (Exception ex) {

	}

	SeasonInfo si = seasonMgr.getSeasonInfo(seasonId);
	SeasonResult sr = seasonMgr.getSeasonResult(seasonId);
%>

<div id="season_details" class="main-div">

	<h3><%=lutil.get("season", lang)%> <%=si.getId() %> (<%=si.localizedSeasonTime(lang)%>)</h3>

</div>
<%
end = System.currentTimeMillis();
logger.debug("seasonInfo.jsp execution time: " + (end - start));
%>
