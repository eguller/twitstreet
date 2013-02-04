<%@page import="org.apache.log4j.Logger" %>
<%
	long start = 0;
	long end = 0;
	start = System.currentTimeMillis();
	Logger logger = Logger.getLogger(this.getClass());
%>
<div id="groups-container">
	<jsp:include page="groupsContent.jsp"></jsp:include>
</div>
<%
end = System.currentTimeMillis();
logger.debug("groups.jsp execution time: " + (end - start));
%>
