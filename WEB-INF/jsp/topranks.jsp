<%@page import="org.apache.log4j.Logger" %>
<%
	long start = 0;
	long end = 0;
	start = System.currentTimeMillis();
	Logger logger = Logger.getLogger(this.getClass());
%>
<div id="topranks-container" class="main-div">
	<table class="datatbl">
		<tr>
			<td>
				<jsp:include page="topranksBar.jsp"></jsp:include>
			</td>
		</tr>
		<tr>
			<td>
				<div id="topranks-content">
					<jsp:include page="topranksContent.jsp"></jsp:include>
				</div>
			</td>
		</tr>
	</table>
</div>
<%
end = System.currentTimeMillis();
logger.debug("topranks.jsp execution time: " + (end - start));
%>