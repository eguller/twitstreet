<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="true" session="false"%>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<title>twitstreet.com</title>
	<link href="css/twitstreet.css" rel="stylesheet" type="text/css"></link>
	<script type="text/javascript" src="http://ajax.googleapis.com/ajax/libs/jquery/1.5.1/jquery.min.js"></script>
	<script type="text/javascript">
		$.post( "/callback", {hash : window.location.hash.substring(1)}, function() {
			$.getScript("http://platform.twitter.com/anywhere.js?id=<%=request.getAttribute("taApiKey")%>&v=1");
		} );
	</script>
</head>
<body>
</body>