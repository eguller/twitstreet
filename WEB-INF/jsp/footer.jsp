<%@ page import="com.twitstreet.localization.LocalizationUtil"%>

<%
	LocalizationUtil lutil = LocalizationUtil.getInstance();
	String lang = (String) request.getSession().getAttribute(
			LocalizationUtil.LANGUAGE);
%>
<div id="footer" class="footer-bar">
	<a href="https://github.com/bisanthe/twitstreet">Source</a> &#8226; <a
		href="https://github.com/bisanthe/twitstreet">Help</a> &#8226; <a
		href="https://github.com/bisanthe/twitstreet">Contact</a> &#8226; <a
		href="https://github.com/bisanthe/twitstreet">About us</a> 
</div>