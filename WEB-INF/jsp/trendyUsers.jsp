
<%@page import="java.util.HashMap"%>
<%@page import="java.util.HashSet"%>
<%@page import="com.twitstreet.twitter.SimpleTwitterUser"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.twitstreet.db.data.UserStock"%>
<%@page import="java.sql.SQLException"%>
<%@page import="com.twitstreet.market.StockMgr"%>
<%@ page import="com.google.inject.Injector"%>
<%@ page import="com.google.inject.Guice"%>
<%@ page import="com.twitstreet.db.data.User"%>
<%@ page import="com.twitstreet.db.data.Stock"%>
<%@ page import="com.twitstreet.util.Util"%>
<%@page import="com.twitstreet.db.data.Portfolio"%>
<%@page import="com.twitstreet.market.PortfolioMgr"%>
<%@page import="com.twitstreet.config.ConfigMgr"%>
<%@page import="com.twitstreet.session.UserMgr"%>
<%@ page import="com.twitstreet.servlet.HomePageServlet"%>

<%@ page import="com.twitstreet.servlet.HomePageServlet"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.Date"%>
<%@page import="java.util.LinkedHashMap"%>
<%@page import="com.twitstreet.db.data.StockHistoryData"%>
<%@page import="com.twitstreet.db.data.UserStock"%>
<%@page import="java.sql.SQLException"%>
<%@page import="com.twitstreet.market.StockMgr"%>
<%@ page import="com.google.inject.Injector"%>
<%@ page import="com.twitstreet.db.data.User"%>
<%@ page import="com.google.inject.Guice"%>
<%@ page import="com.twitstreet.util.Util"%>
<%@ page import="com.twitstreet.db.data.Portfolio"%>
<%@page import="com.twitstreet.config.ConfigMgr"%>
<%@ page import="com.twitstreet.market.PortfolioMgr"%>
<%@page import="com.twitstreet.session.UserMgr"%>
<%@ page import="com.twitstreet.db.data.UserStockDetail"%>
<%@ page import="java.util.List"%>
<%@ page import="com.twitstreet.util.Util"%>
<%@ page import="java.text.DecimalFormat"%>
<%@ page import="com.twitstreet.market.StockMgr"%>
<%@ page import="com.twitstreet.db.data.Stock"%>
<%@ page import="java.text.DecimalFormat"%>
<%@ page import="com.twitstreet.localization.LocalizationUtil" %>
<%@page import="com.twitstreet.util.GUIUtil"%>
	<%
	Injector inj = (Injector) pageContext.getServletContext().getAttribute(Injector.class.getName());
	StockMgr stockMgr = inj.getInstance(StockMgr.class);
	UserMgr userMgr = inj.getInstance(UserMgr.class);
	
	ArrayList<User> trendResults = userMgr.getTopGrossingUsers(6);

	LocalizationUtil lutil = LocalizationUtil.getInstance();
String lang = (String)request.getSession().getAttribute(LocalizationUtil.LANGUAGE);
	%>
<div id="trendy-stocks">
			
	<%
	if(trendResults.size()>0){
	

		
	%>
	
	<h3><%=lutil.get("topgrossingusers.header", lang) %></h3>
	<table class="datatbl" style="margin-top: 10px;">
		
		<%
			for (int i = 0; i < trendResults.size();) {
		%>
		<tr height="150">
			<%
				for (int j = 0; j < 1; j++) {
					if (i < trendResults.size()) {
					
						User user = trendResults.get(i);
					
			%>

					<td>
					
						<table>
							<tr>
								<td width="60">
									<img class="twuser" width="48" height="48" 
									src="<%=user.getPictureUrl()%>" />
								</td>
								<td width="170">
								<table class="datatbl2">
									<tr>									
										<td colspan="2">	
											<a href="#user-<%=user.getId()%>"  onclick="reloadIfHashIsMyHref(this)"  title="<%=lutil.get("user.details.tip", lang, user.getUserName())%>">
											<%=user.getUserName()%>
											</a> 
										
											
										 	
										</td>
									</tr>
									<tr>									
										<td colspan="2"  align="left">								       
											<%=Util.getNumberFormatted(user.getPortfolio()+user.getCash(), true, true, false, false, false, false)%>
										</td>
										
									</tr>
									<tr>									
 										<td colspan="2"  align="right">
											<%=Util.getNumberFormatted((double)user.getProfit(),false,true,true,true,false,true) %>
										</td>
									</tr>
									<tr>									
										<td colspan="2" align="right">
											<%=Util.getPercentageFormatted((double)user.getProfit()/(user.getCash()+user.getPortfolio()),false,true,true,true,false,true)%>
										</td>
									</tr>
								</table>
								
									
								</td>
								<td  width="30">
									&nbsp;
								</td>
								<td>
									<div id="trendy-user<%=user.getId()%>" style="width:230px; height:120px">
									
									<% request.setAttribute("chartUser", user); %>
										<jsp:include page="userTimeLineChart.jsp">										
											<jsp:param name="divId" value="#trendy-user"/>
											<jsp:param name="format" value="simple"/>							
										</jsp:include>
									
									</div>
								</td>
							</tr>
						</table>
						
<!-- 						<br> -->
						
<!-- 						<hr class="hr-pink-class"> -->
						
						
					</td>
					<%
						} else {
					%>
					<td></td>
					<%
						}
							i++;
			}
			%>
		</tr>
		<%
			}
		%>
	</table>
	<%
		
	}
	%>
</div>
		
			