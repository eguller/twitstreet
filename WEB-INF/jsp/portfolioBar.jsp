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
%>
<div class="main-tabs" style="width: 260px;">
	<div class="title-bar">
		<a class="portfolio-tab youarehere"
			onclick="showTabPortfolio('.portfolio-tab','#portfolio-content'); loadPortfolio(false);">
			<%=lutil.get("portfolio.header", lang)%> </a> <a class="watchlist-tab"
			onclick="showTabPortfolio('.watchlist-tab','#watchlist-content'); loadWatchList(false);">
			<%=lutil.get("watchlist.header", lang)%> </a>
	</div>
</div>
<%
end = System.currentTimeMillis();
logger.debug("portfolioBar.jsp execution time: " + (end - start));
%>