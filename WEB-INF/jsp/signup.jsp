<%@page import="org.apache.log4j.Logger" %>
<%
	long start = 0;
	long end = 0;
	start = System.currentTimeMillis();
	Logger logger = Logger.getLogger(this.getClass());
%>
<div id="signup">
	<p align="center">
		<a href="/signin"><img src="/images/twitter-big.png"></img>
		</a>
	</p>
</div>
<%
end = System.currentTimeMillis();
logger.debug("signup.jsp execution time: " + (end - start));
%>