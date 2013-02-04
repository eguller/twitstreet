<%@ page import="com.twitstreet.localization.LocalizationUtil"%>
<%@ page import="com.twitstreet.db.data.User"%>
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
		<a class="currenttransactions-tab youarehere"
			onclick="showTabTransactions('.currenttransactions-tab','#currenttransactions-content'); loadCurrentTransactions(false);">
			<%=lutil.get("transaction.all", lang)%> </a>
		<%
			if (request.getSession().getAttribute(User.USER_ID) != null) {
		%>
		<a class="yourtransactions-tab"
			onclick="showTabTransactions('.yourtransactions-tab','#yourtransactions-content'); loadUserTransactions(false,false);">
			<%=lutil.get("transaction.your", lang)%> </a>
		<%
			}
		%>
	</div>
</div>
<%
end = System.currentTimeMillis();
logger.debug("transactionBar.jsp execution time: " + (end - start));
%>