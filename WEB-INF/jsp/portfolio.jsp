<%@page import="org.apache.log4j.Logger" %>
<%
	long start = 0;
	long end = 0;
	start = System.currentTimeMillis();
	Logger logger = Logger.getLogger(this.getClass());
%>	
<div id="portfolio" class="main-div">
	<table class="datatbl">
		<tr>
			<td>
				<jsp:include page="portfolioBar.jsp"></jsp:include>
			</td>
		</tr>
		<tr>
			<td>
				<div id="portfolio-content">
					<jsp:include page="portfolioContent.jsp"></jsp:include>
				</div>
			</td>
		</tr>
	</table>
</div>
<%
end = System.currentTimeMillis();
logger.debug("portfolio.jsp execution time: " + (end - start));
%>