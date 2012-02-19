
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
	<%
	Injector inj = (Injector) pageContext.getServletContext().getAttribute(Injector.class.getName());
	StockMgr stockMgr = inj.getInstance(StockMgr.class);
	
	ArrayList<Stock> trendResults = stockMgr.getTrendyStocks();
	
	%>
<div id="trendy-stocks">
			
	<%
	if(trendResults.size()>0){
	

		
	%>
	
	<h3>Trendy Stocks</h3>
	<table class="datatbl" style="margin-top: 10px;">
		
		<%
			for (int i = 0; i < trendResults.size();) {
		%>
		<tr>
			<%
				for (int j = 0; j < 2; j++) {
					if (i < trendResults.size()) {
					
						Stock stock = trendResults.get(i);
						String className = "";
						className = (stock.getChangePerHour()<0)?"red-profit":"green-profit";
			%>

					<td>
						<table>
							<tr>
								<td width="60">
									<img class="twuser"
									src="<%=stock.getPictureUrl()%>" />
								</td>
								<td width="130">
								<table class="datatbl2">
									<tr>									
										<td>	
											<a href="javascript:void(0)" onclick="getQuote('<%=stock.getName()%>')"
																title="Loads <%=stock.getName()%>'s stock details">
											<%=stock.getName()%>
											</a> 
											<% if(stock.isVerified()){ %>
											<img src="images/verified.png" title="This twitter account is verified"/>
											<% } %>
										 	
										</td>
									</tr>
									<tr>									
										<td>								       
											<%=Util.commaSep(stock.getAvailable())%> / <%=Util.commaSep(stock.getTotal())%>
										</td>
									</tr>
<!-- 									<tr>									 -->
<%-- 										<td align="right" class="<%=className %>"> --%>
<%-- 											<%=Util.getChangePerHourString(stock.getChangePerHour())%> --%>
<!-- 										</td> -->
<!-- 									</tr> -->
									<tr>									
										<td align="right" class="<%=className %>">
											<%=Util.getPercentageChangePerHourString((double)stock.getChangePerHour()/stock.getTotal())%>
										</td>
									</tr>
								</table>
								
									
									<br>
								</td>
		
							</tr>
						</table>
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
		
			