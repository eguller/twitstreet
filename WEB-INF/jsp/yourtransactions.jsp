<%@page import="com.twitstreet.db.data.TransactionRecord"%>
<%@page import="com.twitstreet.market.TransactionMgr"%>
<%@ page import="com.google.inject.Injector"%>
<%@ page import="com.google.inject.Guice"%>
<%@ page import="com.twitstreet.session.UserMgr"%>
<%@ page import="com.twitstreet.db.data.User"%>
<%@ page import="java.util.ArrayList"%>

<%
Injector inj = (Injector) pageContext.getServletContext().getAttribute(Injector.class.getName());
User sessionUser = (User)request.getSession().getAttribute(User.USER);
TransactionMgr transactionMgr = inj.getInstance(TransactionMgr.class);
ArrayList<TransactionRecord> transactionRecordList = transactionMgr.queryTransactionRecord(sessionUser);

%>
<div id="yourtransactions">
	<h3>Your Transactions</h3>
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
						out.write("You <span class=\"green\">bought</span> " + transactionRecord.getAmount() + " " + transactionRecord.getStockName());
					}
					else{
						out.write("You <span class=\"red\">sold</span> " + transactionRecord.getAmount() + " " + transactionRecord.getStockName());
					}
					%>
						
					
				</td>
			</tr>
		<% } %>
	</table>
</div>