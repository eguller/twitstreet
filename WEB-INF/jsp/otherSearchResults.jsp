
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
	String quote = request.getAttribute(HomePageServlet.QUOTE) == null ? "" : (String) request.getAttribute(HomePageServlet.QUOTE);
	String quoteDisplay = request.getAttribute(HomePageServlet.QUOTE_DISPLAY) == null ? "" : (String) request.getAttribute(HomePageServlet.QUOTE_DISPLAY);
	
	if(quote.length()>0){
	%>
<div id="other-search-result">
			
			<%
				ArrayList<SimpleTwitterUser> searchResults = (ArrayList<SimpleTwitterUser>) request.getSession().getAttribute(HomePageServlet.OTHER_SEARCH_RESULTS);
		
				if (quote.length()>0 && searchResults != null && searchResults.size() > 0) {
			%>
			
			<h3>Other Results for "<%=quote%>"</h3>
			<table class="datatbl" style="margin-top: 10px;">
				
				<%
					for (int i = 0; i < searchResults.size();) {
				%>
				<tr>
					<%
						for (int j = 0; j < 3; j++) {
									if (i < searchResults.size()) {
					%>

					<td>
						<table>
							<tr>
								<td><img class="twuser"
									src="<%=searchResults.get(i).getPictureUrl()%>" />
								</td>
								<td><a href="javascript:void(0)" onclick="getQuote('<%=searchResults.get(i).getScreenName()%>')"
									title="Loads <%=searchResults.get(i).getScreenName()%>'s stock details">
										<%
											out.write(searchResults.get(i).getScreenName());
										%> </a> <br>
										<%
											out.write(Util.commaSep(searchResults.get(i).getFollowerCount()));
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
		
			<%
				}
			%>