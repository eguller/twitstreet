<%@ page import="com.twitstreet.localization.LocalizationUtil"%>
<%
	LocalizationUtil lutil = LocalizationUtil.getInstance();
	String lang = (String) request.getSession().getAttribute(
			LocalizationUtil.LANGUAGE);
%>
<div class="main-tabs">
	<a class="portfolio-tab youarehere"
		onclick="showTabPortfolio('.portfolio-tab','#portfolio-content'); loadPortfolio(false);">
		<%=lutil.get("portfolio.header", lang)%> </a> <a class="watchlist-tab"
		onclick="showTabPortfolio('.watchlist-tab','#watchlist-content'); loadWatchList(false);">
		<%=lutil.get("watchlist.header", lang)%> </a>
</div>