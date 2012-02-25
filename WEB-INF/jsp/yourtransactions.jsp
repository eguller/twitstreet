<%@page import="com.twitstreet.db.data.TransactionRecord"%>
<%@page import="com.twitstreet.market.TransactionMgr"%>
<%@ page import="com.google.inject.Injector"%>
<%@ page import="com.google.inject.Guice"%>
<%@ page import="com.twitstreet.session.UserMgr"%>
<%@ page import="com.twitstreet.db.data.User"%>
<%@ page import="java.util.List"%>
<%@ page import="com.twitstreet.util.Util" %>
<%@ page import="com.twitstreet.servlet.TwitStreetServlet" %>

<%
Injector inj = (Injector) pageContext.getServletContext().getAttribute(Injector.class.getName());
User sessionUser = (User)request.getAttribute(User.USER);
TransactionMgr transactionMgr = inj.getInstance(TransactionMgr.class);
List<TransactionRecord> transactionRecordList = transactionMgr.queryTransactionRecord(sessionUser.getId());

%>

	<div id="yourtransactions" class="main-div">
		<h3>Your Transactions</h3>
		<table class="datatbl" id="your-transactions-table">
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
								out.write("You <span class=\"green\">bought</span> " + Util.commaSep(transactionRecord.getAmount()) + " <a href='#stock-"+transactionRecord.getStockId()+"' title=\""+transactionRecord.getStockName()+"&#39;s stock details page.\">"+transactionRecord.getStockName()+"</a>");
							}
							else{
								out.write("You <span class=\"green\">bought</span> " + Util.commaSep( transactionRecord.getAmount()) + " <a href='#stock-"+transactionRecord.getStockId()+"' title=\""+transactionRecord.getStockName()+"&#39;s stock details page.\">"+transactionRecord.getStockName()+"</a>");
							}
						}
						else{
							if(requestUrl != null && (requestUrl.endsWith("homeAuth.jsp") || requestUrl.endsWith("homeUnAuth.jsp"))){
								out.write("You <span class=\"red\">sold</span> " + Util.commaSep(transactionRecord.getAmount()) + " <a href='#stock-"+transactionRecord.getStockId()+"' title=\""+transactionRecord.getStockName()+"&#39;s stock details page.\">"+transactionRecord.getStockName()+"</a>");
							}
							else{
								out.write("You<a href='#stock-"+transactionRecord.getStockId()+"'  <a href='#stock-"+transactionRecord.getStockId()+"' title=\""+transactionRecord.getStockName()+"&#39;s stock details page.\">"+transactionRecord.getStockName()+"</a>");
							}
						}
						%>
					</td>
				</tr>
			<% } %>
		</table>
	</div>
