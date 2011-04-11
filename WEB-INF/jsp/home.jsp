<%@page contentType="text/html; charset=ISO-8859-1" isELIgnored="true"%>
<%@page import="com.tweetstreet.data.HomeData"%>
<%@taglib tagdir="/WEB-INF/tags" prefix="tag"%>
<%
	HomeData data = (HomeData)request.getAttribute("data");
%><!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head><tag:inc/></head>
<body>
	<jsp:include page="header.jsp"></jsp:include>
	<jsp:include page="navigation.jsp"></jsp:include>
	<div id="content-container">
		<div id="content-container2">
			<div id="content-container3">
				<div id="content">
					<jsp:include page="dashboard.jsp"></jsp:include>
					<tag:dashboard data="<%=data.dashboard %>"></tag:dashboard>
					<jsp:include page="portfolio.jsp"></jsp:include>
				</div>
				<div id="aside">
					<jsp:include page="topusers.jsp"></jsp:include>
					<jsp:include page="latest-transactions.jsp"></jsp:include>
				</div>
			</div>
		</div>
	</div>
	<div id="footer-container">
		<jsp:include page="footer.jsp"></jsp:include>
	</div>

	<script type="text/javascript" src="http://ajax.googleapis.com/ajax/libs/jquery/1.5.1/jquery.min.js"></script>
	<script type="text/javascript" src="/js/home.js"></script>
</body>
</html>
