<%@ page import="com.twitstreet.localization.LocalizationUtil"%>
<%
	LocalizationUtil lutil = LocalizationUtil.getInstance();
	String lang = (String) request.getSession().getAttribute(
			LocalizationUtil.LANGUAGE);
%>
<div class="main-tabs" style="width: 260px;">
	<div class="title-bar">
		<a class="user-ranking-tab youarehere"
			onclick="showRankingTab('.user-ranking-tab','#user-ranking-content'); toprank();">
			<%=lutil.get("ranking.user", lang)%> </a> 
		<a class="group-ranking-tab"
			onclick="showRankingTab('.group-ranking-tab','#group-ranking-content'); toprank();">
			<%=lutil.get("ranking.group", lang)%> </a>
	</div>
</div>