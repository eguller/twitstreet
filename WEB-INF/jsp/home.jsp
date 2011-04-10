<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<jsp:include page="inc.jsp"></jsp:include>
</head>
<body>
	<jsp:include page="header.jsp"></jsp:include>
	<jsp:include page="navigation.jsp"></jsp:include>
	<div id="content-container">
		<div id="content-container2">
			<div id="content-container3">
				<div id="content">
					<jsp:include page="dashboard.jsp"></jsp:include>
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
</body>
</html>