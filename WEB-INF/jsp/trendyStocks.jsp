
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
				for (int j = 0; j < 3; j++) {
							if (i < trendResults.size()) {
			%>

			<td>
				<table>
					<tr>
						<td><img class="twuser"
							src="<%=trendResults.get(i).getPictureUrl()%>" />
						</td>
						<td><a href="javascript:void(0)" onclick="getQuote('<%=trendResults.get(i).getName()%>')"
							title="Loads <%=trendResults.get(i).getName()%>'s stock details">
								<%
									out.write(trendResults.get(i).getName());
								%> </a> 
								<% if(trendResults.get(i).isVerified()){ %>
									<img src="images/verified.png" title="This twitter account is verified"/>
									<% } %>
								<br>
								<%
									out.write(Util.commaSep(trendResults.get(i).getTotal()));
								%>
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
		
			