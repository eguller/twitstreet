<%@page import="com.twitstreet.config.ConfigMgr"%>
<%@ page import="com.google.inject.Injector"%>
<%
	Injector inj = (Injector) pageContext.getServletContext().getAttribute(Injector.class.getName());
	 ConfigMgr configMgr = inj.getInstance(ConfigMgr.class);
%>
<script type="text/javascript">
  var _gaq = _gaq || [];
  _gaq.push(['_setAccount', '<%=configMgr.getGaAccount()%>' ]);
	_gaq.push([ '_setDomainName', 'twitstreet.com' ]);
	_gaq.push([ '_trackPageview' ]);

	(function() {
		var ga = document.createElement('script');
		ga.type = 'text/javascript';
		ga.async = true;
		ga.src = ('https:' == document.location.protocol ? 'https://ssl'
				: 'http://www')
				+ '.google-analytics.com/ga.js';
		var s = document.getElementsByTagName('script')[0];
		s.parentNode.insertBefore(ga, s);
	})();
</script>