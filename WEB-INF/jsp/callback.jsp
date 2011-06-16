<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="true" session="false"%>
<%@page import="com.twitstreet.session.SessionData" %>
<%
	String data = (String)request.getAttribute("data");
%>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<title>twitstreet.com</title>
	<link href="css/twitstreet.css" rel="stylesheet" type="text/css"></link>
	<script type="text/javascript">
		if(!window.opener.closed) {
			//TODO make this json
			window.opener.callback(<%=data%>);
		}
		window.close();
	</script>
</head>
<body>
</body>