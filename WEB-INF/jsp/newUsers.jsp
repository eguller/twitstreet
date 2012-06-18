
<%@page import="com.twitstreet.util.Util"%>
<%@page import="com.twitstreet.session.UserMgr"%>
<%@page import="com.twitstreet.market.StockMgr"%>
<%@page import="com.google.inject.Injector"%>
<%@page import="com.twitstreet.db.data.User"%>
<%@page import="java.util.ArrayList"%>
<%@ page import="com.twitstreet.localization.LocalizationUtil" %>
<%@page import="com.twitstreet.util.GUIUtil"%>
	<%
	Injector inj = (Injector) pageContext.getServletContext().getAttribute(Injector.class.getName());
	StockMgr stockMgr = inj.getInstance(StockMgr.class);
	UserMgr userMgr = inj.getInstance(UserMgr.class);
	
	ArrayList<User> newUsers = userMgr.getNewUsers(0 ,10 );

	LocalizationUtil lutil = LocalizationUtil.getInstance();
String lang = (String)request.getSession().getAttribute(LocalizationUtil.LANGUAGE);
	%>
<div id="new-users">
			
	<%
	if(newUsers.size()>0){
		
	%>

<table class="datatbl" id="new-users-table">
	<%
		int i = 0;
		for (User newUser : newUsers) {
			i++;
	%>
	<%
		if (i % 2 == 0) {
	%>
	<tr>
		<%
			} else {
		%>
	
	<tr class="odd">
		<%
			}
		%>
		<td align="center">		
			<img class="twuser" width="48" height="48"  src="<%=newUser.getPictureUrl()%>" />
			
		</td>
		<td align="left">		
			<a href="#!user=<%=newUser.getId()%>"  onclick="reloadIfHashIsMyHref(this); loadTitle('<%=lutil.get("user.bar.profile", lang, newUser.getUserName())%>');" title="<%=lutil.get("user.details.tip",lang, newUser.getUserName())%>"> <%=newUser.getUserName()%></a>
		</td>
		<td align="right">		
			<i><%=Util.dateDiff2String(newUser.getFirstLogin(),
						lang)%></i>
		</td>
		<td width="33%">		
			
		</td>
	</tr>
	<%
	}
	%>
	
</table>
	<%
	}
	%>
</div>
		
			