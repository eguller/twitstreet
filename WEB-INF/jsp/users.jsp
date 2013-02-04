<%@page import="org.apache.log4j.Logger" %>
<%
	long start = 0;
	long end = 0;
	start = System.currentTimeMillis();
	Logger logger = Logger.getLogger(this.getClass());
%>
<div id="users-container">
	<jsp:include page="userProfile.jsp"></jsp:include>
</div>
<%
end = System.currentTimeMillis();
logger.debug("users.jsp execution time: " + (end - start));
%>