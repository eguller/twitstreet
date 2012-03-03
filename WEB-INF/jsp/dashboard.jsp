<%@page import="java.util.HashMap"%>
<%@page import="java.util.HashSet"%>
<%@page import="com.twitstreet.twitter.SimpleTwitterUser"%>
<%@page import="com.twitstreet.db.data.UserStock"%>
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
<%@ page import="com.twitstreet.db.data.User"%>
<%@ page import="com.twitstreet.servlet.HomePageServlet"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.Date"%>
<%@page import="java.util.LinkedHashMap"%>
<%@page import="com.twitstreet.db.data.StockHistoryData"%>
<%@page import="com.twitstreet.db.data.UserStock"%>
<%@page import="java.sql.SQLException"%>
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
	PortfolioMgr portfolioMgr = inj.getInstance(PortfolioMgr.class);
	ConfigMgr configMgr = inj.getInstance(ConfigMgr.class);
	UserMgr userMgr = inj.getInstance(UserMgr.class);
	User user = (User) request.getAttribute(User.USER);
	
	DecimalFormat f = new DecimalFormat("##.00");
	
	long id = -1;
	
	
	
	Stock stock = (Stock) request.getAttribute(HomePageServlet.STOCK);
	String quote = request.getAttribute(HomePageServlet.QUOTE) == null ? "" : (String) request.getAttribute(HomePageServlet.QUOTE);
	String quoteDisplay = request.getAttribute(HomePageServlet.QUOTE_DISPLAY) == null ? "" : (String) request.getAttribute(HomePageServlet.QUOTE_DISPLAY);

	StockHistoryData shd = null;
	List<UserStockDetail> stockDetailList = null;

	if(stock!=null){
		stockDetailList = portfolioMgr.getStockDistribution(stock.getId());

		shd = stockMgr.getStockHistory(stock.getId());
		
		
	}
	

%>

	
<div id="dashboard" class="main-div">

				
	<jsp:include page="getQuote.jsp" />


<div id="stocks-screen">
<%
if(stock!=null){
	
	%>
	
	<jsp:include page="stockDetails.jsp" />
<%	
}else{
%>
	<jsp:include page="trendyStocks.jsp" />

<%

}%>	
</div>

	


</div>
