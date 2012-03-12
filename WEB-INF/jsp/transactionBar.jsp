<%@ page import="com.twitstreet.localization.LocalizationUtil"%>
<%
	LocalizationUtil lutil = LocalizationUtil.getInstance();
	String lang = (String) request.getSession().getAttribute(
			LocalizationUtil.LANGUAGE);
%>
<div class="main-tabs">
	<a class="currenttransactions-tab youarehere"
		onclick="showTabTransactions('.currenttransactions-tab','#currenttransactions-content'); loadCurrentTransactions(false);">
		<%=lutil.get("transaction.all", lang)%> </a> <a class="yourtransactions-tab"
		onclick="showTabTransactions('.yourtransactions-tab','#yourtransactions-content'); loadUserTransactions(false,false);">
		<%=lutil.get("transaction.your", lang)%> </a>
</div>