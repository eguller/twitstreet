<%@page import="com.twitstreet.localization.LocalizationUtil"%>
<%@ page import="com.twitstreet.servlet.TopRankServlet"%>
<%@ page import="com.google.inject.Injector"%>
<%@ page import="com.google.inject.Guice"%>
<%@ page import="com.twitstreet.session.UserMgr"%>
<%@ page import="com.twitstreet.db.data.User"%>
<%@ page import="java.util.ArrayList"%>
<%@ page import="com.twitstreet.util.Util"%>
<%@page import="org.apache.log4j.Logger" %>
<%
	long start = 0;
	long end = 0;
	start = System.currentTimeMillis();
	Logger logger = Logger.getLogger(this.getClass());
	
	LocalizationUtil lutil = LocalizationUtil.getInstance();
	String lang = (String)request.getSession().getAttribute(LocalizationUtil.LANGUAGE);
	int pageSelectorPerRow = 4;

	Injector inj = (Injector) pageContext.getServletContext()
			.getAttribute(Injector.class.getName());

	UserMgr userMgr = inj.getInstance(UserMgr.class);

	String typeParam = request.getParameter("type");


%>

<!-- <div class="h3" style="width: 100%; padding-right:5px;margin-bottom: 10px;" align="right"> -->
<div>
	
	<select  class="topRankSelectSeason" style="font-size: 10px" onchange="toprank(null,$(this).val())">
	
		<% 					
					
			
	       
					
		%>
			<option <%=(typeParam.equals("currentSeason"))?"selected=\"selected\"":""%> value="currentSeason"><%=lutil.get("season.thisseason", lang)%></option>
			<option <%=(typeParam.equals("allTime"))?"selected=\"selected\"":""%> value="allTime"><%=lutil.get("season.alltime", lang)%></option>
			
		<%	
				
		%>
	</select>

</div>
<%
end = System.currentTimeMillis();
logger.debug("toprankSelectSeason.jsp execution time: " + (end - start));
%>
