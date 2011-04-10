<%@ page contentType="text/html; charset=ISO-8859-1"%>
<%@page import="com.tweetstreet.data.HomeQ1User"%>
<%
HomeQ1User q1user = (HomeQ1User)request.getAttribute("q1user");
%><!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
   <jsp:include page="inc.jsp"></jsp:include>
   <script src="http://platform.twitter.com/anywhere.js?id=DfIgLzNr6zl8gcE4sVFFgQ&v=1" type="text/javascript"></script>
</head>
<body>
	<jsp:include page="header.jsp"></jsp:include>
	<div id="container">
		<table width="100%" class="layout">
			<tr>
				<td class="layout">
					<div id="topleft">
						<jsp:include page="dashboard.jsp"></jsp:include>
					</div>
				</td>
				<td class="layout">
					<div id="topright">
						<jsp:include page="portfolio.jsp"></jsp:include>
					</div>
				</td>
			</tr>
			<tr>
				<td class="layout">
					<div id="downleft">
						<jsp:include page="latest-transactions.jsp"></jsp:include>
					</div>
				</td>
				<td  class="layout"><div id="downright">
						<jsp:include page="topusers.jsp"></jsp:include>
					</div>
				</td>
			</tr>
		</table>
	</div>
	<jsp:include page="footer.jsp"></jsp:include>
	<span id="tcp"></span>

	<script type="text/javascript" src="http://ajax.googleapis.com/ajax/libs/jquery/1.5.1/jquery.min.js"></script>
	<script type="text/javascript" src="/js/home.js"></script>
</body>
</html>
