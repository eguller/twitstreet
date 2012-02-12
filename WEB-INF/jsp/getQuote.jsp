<%@page import="java.util.HashMap"%>
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

<%
	Stock stock = (Stock) request.getAttribute(HomePageServlet.STOCK);
	String quote = request.getAttribute(HomePageServlet.QUOTE) == null ? "" : (String) request.getAttribute(HomePageServlet.QUOTE);
	String quoteDisplay = request.getAttribute(HomePageServlet.QUOTE_DISPLAY) == null ? "" : (String) request.getAttribute(HomePageServlet.QUOTE_DISPLAY);

	if (quote == null || quote.length() < 1) {

		quote = quoteDisplay;
	}
%>
	<h3>Dashboard</h3>
	<div id="quoteholder" class="main-div">
	
		<form action="/" method="post" accept-charset="UTF-8">
			<input type="text" class="textbox" id="quote" value="<%=quote%>" name="quote" />
			<input type="submit" id="getQuoteButton" value="Get Quote">
		</form>
		<input type="hidden" id="quote-hidden" value="<%=quote%>" /> <input
			type="hidden" id="quote-id"
			value="<%=stock == null ? "" : stock.getId()%>" />
	</div>
	
	
	
	
	
	