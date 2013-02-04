<%@page import="org.apache.log4j.Logger" %>
<%
	long start = 0;
	long end = 0;
	start = System.currentTimeMillis();
	Logger logger = Logger.getLogger(this.getClass());
%>
<div id="stocks-container">
	<jsp:include page="dashboard.jsp"></jsp:include>
</div>
<%
end = System.currentTimeMillis();
logger.debug("stocks.jsp execution time: " + (end - start));
%>