
			
	<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
	isELIgnored="true" session="false"%>
<%@ taglib uri='/WEB-INF/tlds/template.tld' prefix='template'%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN">
<html>
<head>
<template:get name="header"></template:get>
</head>
<body>

	<div id="container">
		<template:get name="topbar"></template:get>
		<div id="main">
			<div id="column_left">
				<div id="balance-container">
					<template:get name="balance"></template:get>
				</div>
				<div id="portfolio-container">
					<template:get name="portfolio"></template:get>
				</div>
				<div id="yourtransactions-container">
					<template:get name="yourtransactions"></template:get>
				</div>

				<div id="latesttransactions-container">
					<template:get name="latesttransactions"></template:get>
				</div>

				<template:get name="recentTweets"></template:get>

			</div>
			<div id="column_center">
				
				
				<template:get name="howtoplay"></template:get>
			
				<template:get name="mainmenu"></template:get>
				<template:get name="stocks"></template:get>
				<template:get name="users"></template:get>

				<script type="text/javascript">showTabMain('.stocks-tab','#stocks-container')</script>

			
			</div>
			<div id="column_right">
				<div id="topranks-container">
					<template:get name="topranks"></template:get>
				</div>
			</div>
			<!-- Don't remove spacer div. Solve an issue about container height -->
			<div class="spacer"></div>
		</div>
		<div id="footer">
			<template:get name="footer" />
		</div>
	</div>
</body>
</html>

	