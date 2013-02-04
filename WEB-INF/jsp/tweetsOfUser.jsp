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
<%@page import="org.apache.log4j.Logger" %>
<%
	long start = 0;
	long end = 0;
	start = System.currentTimeMillis();
	Logger logger = Logger.getLogger(this.getClass());
%>
	<div id="tweets-of-user-section" style="display: none;">
	
	<%
			Injector inj = (Injector) pageContext.getServletContext().getAttribute(Injector.class.getName());
			StockMgr stockMgr = inj.getInstance(StockMgr.class);

			Stock stock = null;
			long stockId = -1;
			stock = (Stock) request.getAttribute(HomePageServlet.STOCK);

			if (stock != null) {
				stockId = stock.getId();
			} else {
				try {
					stockId = (Long) Long.parseLong(request.getParameter("stock"));
					stock=stockMgr.getStockById(stockId);
				} catch (Exception ex) {

				}
			}

			if (stock!=null) {
		%>
		<script>showTweetsOfUserInDiv('<%=stock.getName()%>',"tweets-of-user-section");</script>
		


	<%
			} 
		%>
			

		
		</div>
<%
end = System.currentTimeMillis();
logger.debug("tweetOfUser.jsp execution time: " + (end - start));
%>	
