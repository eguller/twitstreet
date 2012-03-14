<%@ page import="com.google.inject.Injector" %>
<%@ page import="com.google.inject.Guice" %>
<%@page import="com.twitstreet.config.ConfigMgr"%>
<%
Injector inj = (Injector) pageContext.getServletContext().getAttribute(Injector.class.getName());

ConfigMgr configMgr = inj.getInstance(ConfigMgr.class);
%>
<link rel="shortcut icon" type="image/x-icon" href="/images/twitstreet_logo_50.png">
<title>
<%= request.getAttribute("title")%>
</title>
<meta http-equiv="content-type" content="text/html; charset=UTF-8">
<meta name="description" content="<%=request.getAttribute("meta-desc") %>" >
<meta property="og:image" content="http://twitstreet.com/images/TwitStreet_logo1.png"/> 
<link rel="stylesheet" type="text/css" href="/css/twitstreet.css" />
<script type="text/javascript" src="https://www.google.com/jsapi"></script>
<script type="text/javascript">
	google.load("jquery", "1.7.1");
	google.load('visualization', '1.0', {'packages':['corechart']});
	google.load('visualization', '1', {packages: ['annotatedtimeline']});
</script>
<script type="text/javascript" src="/js/charts.js"></script>