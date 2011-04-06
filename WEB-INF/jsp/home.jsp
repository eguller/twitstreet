<%@ page contentType="text/html; charset=ISO-8859-1"%>
<%@page import="com.tweetstreet.data.HomeQ1User"%>
<%
HomeQ1User q1user = (HomeQ1User)request.getAttribute("q1user");
%><!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Twitstreet</title>
<script src="http://platform.twitter.com/anywhere.js?id=DfIgLzNr6zl8gcE4sVFFgQ&v=1" type="text/javascript"></script>
</head>
<body>
<span id="tcp"></span>
<div></div>
<script type="text/javascript" src="http://ajax.googleapis.com/ajax/libs/jquery/1.5.1/jquery.min.js"></script>
<script type="text/javascript" src="/js/home.js"></script>
</body>
</html>