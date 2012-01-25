<%@page import="java.util.LinkedList"%>
<%@page import="com.twitstreet.db.data.TransactionRecord"%>
<%@page import="com.twitstreet.market.TransactionMgr"%>
<%@ page import="com.google.inject.Injector"%>
<%@ page import="com.google.inject.Guice"%>
<%@ page import="com.twitstreet.session.UserMgr"%>
<%@ page import="com.twitstreet.db.data.User"%>
<%@ page import="java.util.List"%>

<%
Injector inj = (Injector) pageContext.getServletContext().getAttribute(Injector.class.getName());
TransactionMgr transactionMgr = inj.getInstance(TransactionMgr.class);
List<TransactionRecord> transactionRecordList = transactionMgr.getCurrentTransactions();

%>
<div id="currenttransactions">
	<h3>Current Transactions</h3>
	<table class="datatbl" id="current-transactions-table">
		<% 
		int i = 0;
		for(TransactionRecord transactionRecord : transactionRecordList){ 
		i++;
		%>
			<%	if( i%2 == 0){ %>
				<tr>
	    	<% }else{ %>
	    		<tr class="odd">
	    	<% } %>
				<td>
					<% 
					String requestUrl = request.getRequestURL().toString();
					
					if(transactionRecord.getOperation() == TransactionRecord.BUY){  
						if(requestUrl != null && (requestUrl.endsWith("homeAuth.jsp") || requestUrl.endsWith("homeUnAuth.jsp"))){
							out.write("<a href=\"/user?user=" + transactionRecord.getUserId() + "\" title=\"Goes to "+transactionRecord.getUserName()+"'s profile page.\">"+transactionRecord.getUserName()+"</a> <span class=\"green\">bought</span> " + transactionRecord.getAmount() + " <a href='/?stock="+transactionRecord.getStockId()+"' title=\"Goes to "+transactionRecord.getStockName()+"'s stock details page.\">"+ transactionRecord.getStockName() +"</a>");
						}
						else{
							out.write("<a href=\"/user?user=" + transactionRecord.getUserId() + "\" title=\"Goes to "+transactionRecord.getUserName()+"'s profile page.\">"+transactionRecord.getUserName()+"</a> <span class=\"green\">bought</span> " + transactionRecord.getAmount() + " <a href='/?stock="+transactionRecord.getStockId()+"' title=\"Goes to "+transactionRecord.getStockName()+"'s stock details page.\">" + transactionRecord.getStockName() +"</a>");
						}
					}
					else{
						if(requestUrl != null && (requestUrl.endsWith("homeAuth.jsp") || requestUrl.endsWith("homeUnAuth.jsp"))){
							out.write("<a href=\"/user?user=" + transactionRecord.getUserId() + "\" title=\"Goes to "+transactionRecord.getUserName()+"'s profile page.\">"+transactionRecord.getUserName()+"</a> <span class=\"red\">sold</span> " + transactionRecord.getAmount() + " <a href='/?stock="+transactionRecord.getStockId()+"' title=\"Goes to "+transactionRecord.getStockName()+"'s stock details page.\">"+ transactionRecord.getStockName() +"</a>");
						}else{
							out.write("<a href=\"/user?user=" + transactionRecord.getUserId() + "\" title=\"Goes to "+transactionRecord.getUserName()+"'s profile page.\">"+transactionRecord.getUserName()+"</a> <span class=\"red\">sold</span> " + transactionRecord.getAmount() + " <a href='/stock/"+transactionRecord.getStockId()+"' title=\"Goes to "+transactionRecord.getStockName()+"'s stock details page.\">" + transactionRecord.getStockName() +"</a>");
						}
					}
					%>
						
					
				</td>
			</tr>
		<% } %>
	</table>
</div>