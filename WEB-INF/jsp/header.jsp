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

<% if(configMgr.isDev()){ %>
<!-- Start CSS -->
<link rel="stylesheet" type="text/css" href="/css/jquery.countdown.css" />
<!-- <link rel="stylesheet" type="text/css" href="/css/jquery-ui-1.8.20.custom.css" /> -->
<link rel="stylesheet" type="text/css" href="/css/twitstreet.css" />
<!-- End CSS -->
<% } else{ %>
	<link rel="stylesheet" type="text/css" href="/css/all-min.css" />
<% } %>

<!-- Start GoogleAPI -->
<script type="text/javascript" src="https://www.google.com/jsapi"></script>
<script type="text/javascript">
	google.load("jquery", "1.7.1");
	google.load('visualization', '1.0', {'packages':['corechart']});
	google.load('visualization', '1', {packages: ['annotatedtimeline']});
</script>

<!--  Start of Twitter Widget -->
<script type="text/javascript" src="http://widgets.twimg.com/j/2/widget.js"></script>
<!--  End of Twitter Widget -->

<!-- End GoogleAPI -->
<% if(configMgr.isDev()){ %>
<!-- Start Tw-Static -->
<!-- If you are updating here please update file list under merge-static task in buil.xml file -->
<script type="text/javascript" src="/js/jquery-corner.js"></script>
<script type="text/javascript" src="/js/jquery.blockUI.js"></script>
<script type="text/javascript" src="/js/hashchange.js"></script>
<script type="text/javascript" src="/js/twitstreet.js"></script>
<script type="text/javascript" src="/js/ajax.js"></script>
<script type="text/javascript" src="/js/util.js"></script>
<script type="text/javascript" src="/js/stockTabs.js"></script>
<script type="text/javascript" src="/js/userTabs.js"></script>
<script type="text/javascript" src="/js/groupTabs.js"></script>
<script type="text/javascript" src="/js/stockDetailsTabs.js"></script>
<script type="text/javascript" src="/js/mainTabs.js"></script>
<script type="text/javascript" src="/js/userProfileTabs.js"></script>
<script type="text/javascript" src="/js/portfolioTab.js"></script>
<script type="text/javascript" src="/js/toprankTabs.js"></script>
<script type="text/javascript" src="/js/transactionTab.js"></script>
<script type="text/javascript" src="/js/charts.js"></script>
<script type="text/javascript" src="/js/jquery.countdown.min.js"></script>
<!-- <script type="text/javascript" src="/js/jquery-ui-1.8.20.custom.js"></script> -->
<!-- End Tw-Static -->
<% } else{ %>
<script type="text/javascript" src="/js/all-min.js"></script>
<% } %>
<%if(!lang.equalsIgnoreCase(LocalizationUtil.DEFAULT_LANGUAGE)){
%>	
<script type="text/javascript" src="/js/jquery.countdown-<%=lang%>.js"></script>
<%	
}%>
