<%@page import="org.apache.log4j.Logger" %>
<%
	long start = 0;
	long end = 0;
	start = System.currentTimeMillis();
	Logger logger = Logger.getLogger(this.getClass());
%>
<div id="transactions" class="main-div">
	<table class="datatbl">
		<tr>
			<td>
				<jsp:include page="transactionBar.jsp"></jsp:include>
			</td>
		</tr>
		<tr>
			<td>
				<div id="transactions-content">
					<jsp:include page="currentTransactionsContent.jsp"></jsp:include>
				</div>
			</td>
		</tr>
	</table>
</div>
<%
end = System.currentTimeMillis();
logger.debug("transactions.jsp execution time: " + (end - start));
%>