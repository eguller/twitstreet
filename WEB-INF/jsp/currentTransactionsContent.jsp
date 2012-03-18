<%@page import="java.util.LinkedList"%>
<%@ page import="com.twitstreet.localization.LocalizationUtil"%>
<%@page import="com.twitstreet.db.data.TransactionRecord"%>
<%@page import="com.twitstreet.market.TransactionMgr"%>
<%@ page import="com.google.inject.Injector"%>
<%@ page import="com.google.inject.Guice"%>
<%@ page import="com.twitstreet.session.UserMgr"%>
<%@ page import="com.twitstreet.db.data.User"%>
<%@ page import="java.util.List"%>
<%@ page import="com.twitstreet.util.Util"%>
<%
	Injector inj = (Injector) pageContext.getServletContext()
			.getAttribute(Injector.class.getName());
	TransactionMgr transactionMgr = inj
			.getInstance(TransactionMgr.class);
	List<TransactionRecord> transactionRecordList = transactionMgr
			.getCurrentTransactions();
	User user = (User) request.getAttribute(User.USER);
	LocalizationUtil lutil = LocalizationUtil.getInstance();
	String lang = (String) request.getSession().getAttribute(
			LocalizationUtil.LANGUAGE);
%>
<table class="datatbl" id="current-transactions-table">
	<%
		int i = 0;
		for (TransactionRecord transactionRecord : transactionRecordList) {
			i++;
	%>
	<%
		if (i % 2 == 0) {
	%>
	<tr>
		<%
			} else {
		%>
	
	<tr class="odd">
		<%
			}
		%>
		<td>
			<%
				String requestUrl = request.getRequestURL().toString();
					String operation = "";
					String spanClass = "";
					if (transactionRecord.getOperation() == TransactionRecord.BUY) {
						spanClass = "green";
						operation = "&#9664;";

					} else {
						spanClass = "red";
						operation = "&#9654;";
					}
			%> <a href="#!user=<%=transactionRecord.getUserId()%>"
			onclick="reloadIfHashIsMyHref(this)"
			title="<%=lutil.get("user.details.tip", lang,
						transactionRecord.getUserName())%>">
				<%=transactionRecord.getUserName()%> </a> <span class="<%=spanClass%>">
				<%=operation%></span> <%=Util.commaSep(transactionRecord.getAmount())%> <a
			href="#!stock=<%=transactionRecord.getStockId()%>"
			onclick="reloadIfHashIsMyHref(this)"
			title="<%=lutil.get("stock.details.tip", lang,
						transactionRecord.getStockName())%>">
				<%=transactionRecord.getStockName()%> </a></td>
				<td style="text-align: right;font-size: 9px; color: #777777;"><i><%=Util.dateDiff2String(transactionRecord.getDate()) %></i></td>
	</tr>
	<%
		}
	%>
</table>