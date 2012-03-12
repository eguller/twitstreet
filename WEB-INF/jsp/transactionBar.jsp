<%@ page import="com.twitstreet.localization.LocalizationUtil"%>
<%
	LocalizationUtil lutil = LocalizationUtil.getInstance();
	String lang = (String) request.getSession().getAttribute(
			LocalizationUtil.LANGUAGE);
%>
<div class="main-tabs">
	<a class="currenttransactions-tab youarehere"
		onclick="showTabPortfolio('.currenttransactions-tab','#currenttransactions-content'); loadCurrentTransactions(false);">
		<%=lutil.get("transaction.all", lang)%> </a> <a class="yourtransactions-tab"
		onclick="showTabPortfolio('.yourtransactions-tab','#yourtransactions-content'); loadUserTransactions(false,true);">
		<%=lutil.get("transaction.your", lang)%> </a>
</div>