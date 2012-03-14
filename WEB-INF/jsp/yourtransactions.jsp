<%@page import="com.twitstreet.db.data.TransactionRecord"%>
<%@ page import="com.twitstreet.localization.LocalizationUtil" %>
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
LocalizationUtil lutil = LocalizationUtil.getInstance();
String lang = (String)request.getSession().getAttribute(LocalizationUtil.LANGUAGE);

%>

	<div id="yourtransactions" class="main-div">
		<h3><%=lutil.get("yourtransactions.header",lang) %></h3>
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
						String operation = "";
						String spanClass = "";
						if(transactionRecord.getOperation() == TransactionRecord.BUY){  
							spanClass = "green";
							operation = "&#9664;";											
							
						}
						else{
							spanClass = "red";
							operation = "&#9654;";	
						}
						%>

						<span class="<%=spanClass%>"> <%=operation%></span>
						<%=Util.commaSep(transactionRecord.getAmount())%>
						<a href="#!stock=<%=transactionRecord.getStockId()%>" title="<%=lutil.get("stock.details.tip", lang,transactionRecord.getStockName())%>">
							<%= transactionRecord.getStockName()%>
						</a>
						
					</td>
				</tr>
			<% } %>
		</table>
	</div>
