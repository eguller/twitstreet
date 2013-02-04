<%@page import="com.twitstreet.util.GUIUtil"%>
<%@ page import="com.twitstreet.localization.LocalizationUtil" %>
<%@page import="org.apache.log4j.Logger" %>
<%
	long start = 0;
	long end = 0;
	start = System.currentTimeMillis();
	Logger logger = Logger.getLogger(this.getClass());
	
LocalizationUtil lutil = LocalizationUtil.getInstance();
String lang = (String)request.getSession().getAttribute(LocalizationUtil.LANGUAGE);
%>



<div id="recent-tweets">
<h3>
<%= lutil.get("tweetsaboutus", lang) %>
</h3>


<div  style="margin-top: 5px; height:350px; overflow-y : hidden; overflow-x : hidden;" onmouseover="$(this).css('overflow-y', 'scroll')" onmouseout="$(this).css('overflow-y', 'hidden')">
	<script charset="utf-8" src="http://widgets.twimg.com/j/2/widget.js"></script>
	<script>
		new TWTR.Widget({
			version : 2,
			type : 'search',
			search: '@twitstreet_game OR #twitstreet OR from:twitstreet_game',
			rpp : 20,
			interval : 6000,
			width : 250,
			height : 1500,
			theme : {
				shell : {
					background : '#f2f2f2',
					color : '#000000'
				},
				tweets : {
					background : '#ffffff',
					color : '#000000',
					links : '#4183c4'
				}
			},
			features : {
			    scrollbar: false,
			    loop: true,
			    live: true,
			    hashtags: true,
			    timestamp: true,
			    avatars: true,
			    toptweets: true,
				behavior : 'all'
			}
		}).render().start();
	</script>
	</div>
	<br>
	<div style="text-align:center">
	<%= GUIUtil.getInstance().getTwitterHashButton("twitstreet", lang) %>
</div>
	
</div>
<%
end = System.currentTimeMillis();
logger.debug("recentTweets.jsp execution time: " + (end - start));
%>