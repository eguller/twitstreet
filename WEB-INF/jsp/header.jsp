<%@page import="com.twitstreet.localization.LocalizationUtil"%>
<%@ page import="com.google.inject.Injector" %>
<%@ page import="com.google.inject.Guice" %>
<%@page import="com.twitstreet.config.ConfigMgr"%>
<%
LocalizationUtil lutil = LocalizationUtil.getInstance();
String lang = (String)request.getSession().getAttribute(LocalizationUtil.LANGUAGE);
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
<link rel="stylesheet" type="text/css" href="/css/all-min.css" />
<script type="text/javascript" src="https://www.google.com/jsapi"></script>
<script type="text/javascript">
	google.load("jquery", "1.7.1");
	google.load('visualization', '1.0', {'packages':['corechart']});
	google.load('visualization', '1', {packages: ['annotatedtimeline']});
</script>
<style type="text/css">
@import "/css/all-min.css";
</style>
<script type="text/javascript" src="/js/all-min.js"></script>
<script type="text/javascript" src="/js/jquery.countdown.min.js"></script>
<%if(!lang.equalsIgnoreCase(LocalizationUtil.DEFAULT_LANGUAGE)){
%>	
<script type="text/javascript" src="/js/jquery.countdown-<%=lang%>.js"></script>
<%	
}%>