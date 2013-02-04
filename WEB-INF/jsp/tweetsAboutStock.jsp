<%@page import="com.twitstreet.localization.LocalizationUtil"%>
<%@page import="com.twitstreet.util.GUIUtil"%>
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
	<div id="tweets-about-stock-section" style="display: none;">
	
		<%
		LocalizationUtil lutil = LocalizationUtil.getInstance();
		String lang = (String)request.getSession().getAttribute(LocalizationUtil.LANGUAGE);

		Injector inj = (Injector) pageContext.getServletContext().getAttribute(Injector.class.getName());
		StockMgr stockMgr = inj.getInstance(StockMgr.class);

		Stock stock = null;
		stock = (Stock) request.getAttribute(HomePageServlet.STOCK);

		if (stock == null) {
			try {
				long stockId = (Long) Long.parseLong(request.getParameter("stock"));
				stock=stockMgr.getStockById(stockId);
			} catch (Exception ex) {

			}
		}

		if (stock!=null) {
		%>
	    <div class="field-white">
	    <br>
				<%=lutil.get("stockdetails.about.writeHere", lang,stock.getName())%>
		<br>	
		<br>
			</div>
		<div style="float:left; margin-top: 5px;">
				
			<div style="text-align:center">
				<%= GUIUtil.getInstance().getTwitterMentionButton(stock.getName(), lang) %>
			</div>
			<br>
			<div id="tweets-about-stock-container" style="height:350px;width: 240px;  overflow-y : hidden; overflow-x : hidden;" onmouseover="$(this).css('overflow-y', 'scroll')" onmouseout="$(this).css('overflow-y', 'hidden')">
				<script>showTweetsWithKeyWords('@<%=stock.getName()%>',"tweets-about-stock-container");</script>
			</div>
		
		</div>
		<div  style="float:left; margin-top: 5px;">
			<div style="text-align:center">
				<%= GUIUtil.getInstance().getTwitterHashButton(stock.getName()+"_twitstreet", lang, "http://www.twitstreet.com/#!stock="+stock.getId()) %>
			</div>
			<br>
		
			<div id="internal-tweets-about-stock-container" style="height:350px;width: 240px;overflow-y : hidden; overflow-x : hidden;" onmouseover="$(this).css('overflow-y', 'scroll')" onmouseout="$(this).css('overflow-y', 'hidden')">
				<script>showTweetsWithKeyWords('#<%=stock.getName()%>_twitstreet',"internal-tweets-about-stock-container");</script>
			</div>
		</div>
			
		<%
		} 
		%>
			

		
	</div>
<%
end = System.currentTimeMillis();
logger.debug("tweetAboutStocks.jsp execution time: " + (end - start));
%>	
