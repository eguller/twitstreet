
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
	if (stock == null) {
		try {
			stock = stockMgr.getStockById(Long.valueOf((String) request.getParameter(HomePageServlet.STOCK)));
		} catch (Exception ex) {

		}
	}
	UserStock userStock = null;
	if (user != null && stock != null) {
		userStock = portfolioMgr.getStockInPortfolio(user.getId(), stock.getId());

	}
%>

	
<div id="stockdetails" class="main-div">
	
	
				
	
		<%
										if (stock != null) {
									%>
		
	<div id="stock-details-menu" class="subheader">

			
		<table class="datatbl">
			<tr>
				<td>
					<img class="twuser"
					src="<%=stock == null ? "" : stock.getPictureUrl()%>"
					id="dashboard-picture"></td>
				<td>
					<h1>
						<a href="http://twitter.com/#!/<%=stock == null ? "" : stock.getName()%>"
							title="<%=stock == null ? "" : stock.getName()%>&#39;s twitter page"
							target="_blank"><%=stock == null ? "" : stock.getName()%></a>
					</h1>
				</td>
				<td style="vertical-align: bottom;">
					<div id="tabs">
						<a id="buy-sell-tab" class="youarehere" onClick="showBuySell();">
							Buy/Sell</a> <a id="stock-history-tab" onClick="showStockHistory();">
							History</a> <a id="stock-distribution-tab"
							onClick="showStockDistribution(<%=stock.getId()%>);">
							Distribution </a> <a id="tweets-of-user-tab"
							onClick="showTweetsOfUser();"> Tweets </a>
					</div>
				</td>
			</tr>
		</table>
		
	</div>
<br>
	<div id="stock-details-screen">

		<jsp:include page="buySell.jsp" />
		<jsp:include page="stockHistory.jsp" />
		<jsp:include page="stockDistribution.jsp" />

		<jsp:include page="tweetsOfUser.jsp" />

	</div>
	<%
		}
	%>



</div>


