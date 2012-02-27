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
<%@ page import="com.twitstreet.localization.LocalizationUtil" %>
<%

LocalizationUtil lutil = LocalizationUtil.getInstance();
String lang = (String)request.getSession().getAttribute(LocalizationUtil.LANGUAGE);

	Injector inj = (Injector) pageContext.getServletContext().getAttribute(Injector.class.getName());
	UserMgr userMgr = inj.getInstance(UserMgr.class);
	User user = (User) request.getAttribute(User.USER);

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

	String quote = request.getAttribute(HomePageServlet.QUOTE) == null ? "" : (String) request.getAttribute(HomePageServlet.QUOTE);
	String quoteDisplay = request.getAttribute(HomePageServlet.QUOTE_DISPLAY) == null ? "" : (String) request.getAttribute(HomePageServlet.QUOTE_DISPLAY);

%>

	
<div id="stockdetails" class="main-div">
	
	
	
	<%
		if (stock != null) {
	%>
	
		<input id="hiddenStockDetailsStockId" type="hidden" value="<%=stock.getId() %>"/>
		<table class="datatbl">
			<tr>
				<td>
					<h3 >
						<a style="color:#000000" href="http://twitter.com/#!/<%=stock == null ? "" : stock.getName()%>"
							title="<%=stock == null ? "" : stock.getName()%>&#39;s twitter page"
							target="_blank"><%=stock == null ? "" : stock.getName()%></a>
							<% if(stock != null && stock.isVerified()){ %>
								<img src="images/verified.png" title="This twitter account is verified"/>
							<% } %>
					</h3>
				</td>
				<td>
					<div class="h3-right-top">
					
						<%
						ArrayList<Stock> watchList = stockMgr.getUserWatchList(user.getId());
						boolean beingWatched = watchList.contains(stock);
						 %>
						<a class="add-to-watch-list-link-<%=stock.getId() %>" style="<%out.write((beingWatched)?"display:none":""); %>" href="javascript:void(0)" onclick="addToWatchList(<%=stock.getId()%>)">
							<%=Util.getWatchListIcon(true,20,lutil.get("watchlist.add", lang))%>
							
						</a>	
						<a class="remove-from-watch-list-link-<%=stock.getId() %>" style="<%=(!beingWatched)?"display:none":"" %>" href="javascript:void(0)" onclick="removeFromWatchList(<%=stock.getId()%>)">
							<%=Util.getWatchListIcon(false,20,lutil.get("watchlist.remove", lang))%>
							
						</a>	
					</div>
				</td>
			</tr>
			
	</table>
		
			
			
	
	<div id="stock-details-menu" class="subheader">

			
		<table class="datatbl">
			<tr>
				<td>
					<img class="twuser"
					src="<%=stock == null ? "" : stock.getPictureUrl()%>"
					id="dashboard-picture">
					
<!-- 					<img src="/images/activity_indicator_32.gif" /> -->
															
					
					</td>
				<td>
					<% if(stock.isChangePerHourCalculated() && stock.getChangePerHour()!=0){ 
					
						
					%>
	
						<%=Util.getPercentageFormatted((double) stock.getChangePerHour() / stock.getTotal(), false, true, true, true, false, true)  %>
	
					<% }
					
					
					%>
					
					
			
											
						
				
					
				</td>
				<td style="vertical-align: bottom; width:330px">
					<div class="tabs">
						<a id="buy-sell-tab" class="youarehere" onClick="showBuySell();">
							<%=lutil.get("stockdetails.buysell", lang) %></a> <a id="stock-history-tab" onClick="showStockHistory();">
							<%=lutil.get("stockdetails.history", lang) %></a> <a id="stock-distribution-tab"
							onClick="showStockDistribution(<%=stock.getId()%>);">
							<%=lutil.get("stockdetails.distribution", lang) %> </a> <a id="tweets-of-user-tab"
							onClick="showTweetsOfUser();"> <%=lutil.get("stockdetails.tweets", lang) %> </a>
					</div>
				</td>
			</tr>
		</table>
		
	</div>
	
	<br>
	
	<div id="stock-details-screen">
		<div id="buy-sell-container">
			<jsp:include page="buySell.jsp" />
		</div>
		<jsp:include page="stockHistory.jsp" />
		<jsp:include page="stockDistribution.jsp" />

		<jsp:include page="tweetsOfUser.jsp" />

	</div>
	<%
		}
	%>

	<div id="hasnofollowers">

		<%
			if (quote.length() > 0 && stock != null && stock.getTotal() == 0) {
		%>
		<div id="dashboard-message-field" style="margin-top: 6px;"
			class="field-white">
			<p style="margin-top: 10px; margin-bottom: 10px;">
				<%
					out.write(stock.getName() + " has 0 followers. Please try something else.");
				%>
			</p>
		</div>
		<%
			}
		%>
	</div>


	

	<%
		if (quote.length() > 0){
			%>
			<jsp:include page="otherSearchResults.jsp" />
			
			
			<%
			if(stock == null) {
			%>	
			
				<div id="searchnoresult"><p><%=lutil.get("shared.noresults", lang) %></p></div>
		<%	}
		%>

			

	<%
		}
	%>

</div>


