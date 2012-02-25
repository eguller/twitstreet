<%@ page import="com.google.inject.Injector" %>
<%@ page import="com.google.inject.Guice" %>
<%@page import="com.twitstreet.config.ConfigMgr"%>
<%
Injector inj = (Injector) pageContext.getServletContext().getAttribute(Injector.class.getName());

ConfigMgr configMgr = inj.getInstance(ConfigMgr.class);
%>

<title>
<%= request.getAttribute("title")%>
</title>

<meta http-equiv="content-type" content="text/html; charset=UTF-8">
<meta name="description" content="<%=request.getAttribute("meta-desc") %>" >

<script src="/js/jquery-1.6.4.min.js"></script>
<script src="/js/jquery-corner.js"></script>
<script src="/js/jquery.blockUI.js"></script>
<script src="/js/hashchange.js"></script>
<script type="text/javascript" src="https://www.google.com/jsapi"></script>
<script charset="utf-8" src="http://widgets.twimg.com/j/2/widget.js"></script>

<script src="/js/twitstreet.js"></script>
<script src="/js/ajax.js"></script>
<script src="/js/util.js"></script>
<script src="/js/stockTabs.js"></script>
<script src="/js/mainTabs.js"></script>
<script src="/js/userProfileTabs.js"></script>
<script src="/js/charts.js"></script>

<script language="javascript" type="text/javascript">
	$(function(){
	  
	  // Bind an event to window.onhashchange that, when the hash changes, gets the
	  // hash and adds the class "selected" to any matching nav link.
	  $(window).hashchange( function(){
	    var hash = location.hash;
	    
	    // Set the page title based on the hash.
	    var command =  hash.replace( /^#/, '' );
	    
	    var itemType = command.split('-')[0];
	   
	    
	    if(itemType == 'stock'){
	    	 var id = command.split('-')[1];
	    	loadStock(id);
	    	
	    }else if(itemType == 'user'){
	    	 var id = command.split('-')[1];
	    	loadUserProfile(id);
	    }
		else if(itemType == 'trendystocks'){
	    	
	    	loadTrendyStocks();
	    }else if(itemType == 'searchstock'){

	    	var searchString = command.split('-')[1];
	    	getQuote(searchString);
	    }
	    else if(itemType == 'searchuser'){

	    	var searchString = command.split('-')[1];
	    	getUser(searchString);
	    }
	    
	    
	 
	  })
	  
	  // Since the event is only triggered when the hash changes, we need to trigger
	  // the event now, to handle the hash the page may have loaded with.
	  $(window).hashchange();
	  
	});
	google.load('visualization', '1.0', {'packages':['corechart']});
	google.load('visualization', '1', {packages: ['annotatedtimeline']});
</script>

<link rel="stylesheet" type="text/css" href="/css/cssreset-min.css" />
<link rel="stylesheet" type="text/css" href="/css/twitstreet.css" />
<link rel="stylesheet" type="text/css" href="/css/rankingDropDown.css" />
<script type="text/javascript">

  var _gaq = _gaq || [];
  _gaq.push(['_setAccount', '<%=configMgr.getGaAccount()%>']);
  _gaq.push(['_setDomainName', 'twitstreet.com']);
  _gaq.push(['_trackPageview']);

  (function() {
    var ga = document.createElement('script'); ga.type = 'text/javascript'; ga.async = true;
    ga.src = ('https:' == document.location.protocol ? 'https://ssl' : 'http://www') + '.google-analytics.com/ga.js';
    var s = document.getElementsByTagName('script')[0]; s.parentNode.insertBefore(ga, s);
  })();

</script>

