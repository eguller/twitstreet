
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

<%
	Injector inj = (Injector) pageContext.getServletContext().getAttribute(Injector.class.getName());
	UserMgr userMgr = inj.getInstance(UserMgr.class);
	User sessionUser = (User) request.getSession().getAttribute(User.USER);
	User user = null;
	if (sessionUser != null) {
		user = userMgr.getUserById(sessionUser.getId());
	}

	PortfolioMgr portfolioMgr = inj.getInstance(PortfolioMgr.class);
	StockMgr stockMgr = inj.getInstance(StockMgr.class);

	ConfigMgr configMgr = inj.getInstance(ConfigMgr.class);

	Stock stock = null;

	stock = (Stock) request.getAttribute(HomePageServlet.STOCK);

	UserStock userStock = null;
	if (user != null && stock != null) {
		userStock = portfolioMgr.getStockInPortfolio(user.getId(), stock.getId());

	}
%>

			<input type="hidden" id="user-stock-val"
				value="<%=userStock == null ? "" : (int) (userStock.getPercent() * stock.getTotal())%>" />
			<input type="hidden" id="available-hidden"
				value="<%=stock == null ? "" : stock.getAvailable()%>" /> <input
				type="hidden" id="sold-hidden"
				value="<%=stock == null ? "" : stock.getSold()%>" /> <input
				type="hidden" id="total-hidden"
				value="<%=stock == null ? "" : stock.getTotal()%>" />
					<input type="hidden" id="quote-id"
		value="<%=stock == null ? "" : stock.getId()%>" />
				

<div id="stockdetails" class="main-div">
	
	
		<%
		if (stock != null) {
		%>
		
	<div class="subheader">
		<h1>
			<a
				href="http://twitter.com/#!/<%=stock == null ? "" : stock.getName()%>"
				title="<%=stock == null ? "" : stock.getName()%>&#39;s twitter page"
				target="_blank"><%=stock == null ? "" : stock.getName()%></a>
		</h1>
		
		
		<div id="tabs">
			<a id="buy-sell-tab" class="youarehere" onClick="showBuySell();">
				Buy/Sell</a> 
			<a id="stock-history-tab"
				onClick="showStockHistory();"> 
				History</a> 
			<a id="stock-distribution-tab"
				onClick="showStockDistribution(<%=stock.getId()%>);">
				Distribution
				</a>
			<a id="tweets-of-user-tab"
				onClick="showTweetsOfUser();">
				Tweets
				</a>
		</div>
	</div>

	
	
	<jsp:include page="buySell.jsp" />
	<jsp:include page="stockHistory.jsp" />
	<jsp:include page="stockDistribution.jsp" />

	<jsp:include page="tweetsOfUser.jsp" />

	<script type="text/javascript">
		initStockTabs();
	</script>


	<%
		}
	%>



</div>


