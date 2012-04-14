<%@page import="com.twitstreet.util.GUIUtil"%>
<%@ page import="com.twitstreet.localization.LocalizationUtil"%>

<%
	LocalizationUtil lutil = LocalizationUtil.getInstance();
	String lang = (String) request.getSession().getAttribute(
			LocalizationUtil.LANGUAGE);
%>
<div id="footer-content" class="footer-bar">
    <a target="_blank" href="http://www.facebook.com/pages/twitstreet/222932487786315"><%=lutil.get("footer.facebook", lang) %></a>  &#8226; 
	<a target="_blank" href="https://github.com/bisanthe/twitstreet"><%=lutil.get("footer.source", lang) %></a> &#8226;
	<a href="https://github.com/bisanthe/twitstreet"><%=lutil.get("footer.help", lang) %></a> &#8226; 
	<a href="https://twitter.com/intent/tweet?screen_name=twitstreet_game"><%=lutil.get("footer.contact", lang) %></a> &#8226;
	<a href="https://github.com/bisanthe/twitstreet"><%=lutil.get("footer.aboutus", lang) %></a>
	
<%-- 	<div><%=GUIUtil.getInstance().getTwitterMentionButton("twitstreet_game", lang) %></div>  --%>
</div>