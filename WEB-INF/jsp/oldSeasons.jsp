<%@page import="org.apache.log4j.Logger" %>
<%
	long start = 0;
	long end = 0;
	start = System.currentTimeMillis();
	Logger logger = Logger.getLogger(this.getClass());
%>
<div id="oldseasons-container">
<%-- 	<jsp:include page="oldSeasonsContent.jsp"></jsp:include> --%>
</div>
<%
end = System.currentTimeMillis();
logger.debug("oldSeasons.jsp execution time: " + (end - start));
%>	
