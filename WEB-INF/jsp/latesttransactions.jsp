<%@page import="java.util.LinkedList"%>
<%@page import="com.twitstreet.db.data.TransactionRecord"%>
<%@page import="com.twitstreet.market.TransactionMgr"%>
<%@ page import="com.google.inject.Injector"%>
<%@ page import="com.google.inject.Guice"%>
<%@ page import="com.twitstreet.session.UserMgr"%>
<%@ page import="com.twitstreet.db.data.User"%>
<%@ page import="java.util.ArrayList"%>

<%
Injector inj = (Injector) pageContext.getServletContext().getAttribute(Injector.class.getName());
TransactionMgr transactionMgr = inj.getInstance(TransactionMgr.class);
LinkedList<TransactionRecord> transactionRecordList = transactionMgr.getCurrentTransactions();

%>
<div id="currenttransactions">
	<h3>Current Transactions</h3>
	<table class="datatbl">
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
					<% if(transactionRecord.getTransactionAction() == TransactionRecord.BUY){  
						out.write("<a href=\"/user/" + transactionRecord.getUserName() + "\">"+transactionRecord.getUserName()+"</a> <span class=\"green\">bought</span> " + transactionRecord.getAmount() + " <a href=\"/stock/" + transactionRecord.getStockName() + "\">"+ transactionRecord.getStockName() +"</a>");
					}
					else{
						out.write(transactionRecord.getUserName() + " <span class=\"red\">sold</span> " + transactionRecord.getAmount() + " " + transactionRecord.getStockName());
					}
					%>
						
					
				</td>
			</tr>
		<% } %>
	</table>
</div>