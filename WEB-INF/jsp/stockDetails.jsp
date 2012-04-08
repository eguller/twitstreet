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
<%@page import="com.twitstreet.util.GUIUtil"%>
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
	

	String quote = request.getAttribute(HomePageServlet.QUOTE) == null ? "" : (String) request.getAttribute(HomePageServlet.QUOTE);
	String quoteDisplay = request.getAttribute(HomePageServlet.QUOTE_DISPLAY) == null ? "" : (String) request.getAttribute(HomePageServlet.QUOTE_DISPLAY);

%>

	
<div id="stockdetails" class="main-div">
					
	
	
	<%
		if (stock != null) {
	%>
	
	<input id="hiddenStockDetailsStockId" type="hidden" value="<%=stock.getId() %>"/>
	
	<div id="stock-details-menu" class="subheader">
		<%
		if(stock.isSuspended()){
	
			%>	
			<div class="field-white red" style="margin-bottom:10px">
				<b><%=lutil.get("stock.suspended", lang) %></b>
			</div>
			<%	
		}
		
		%>
	
		<table class="datatbl">
			
			<tr>
				<td width="55" style="vertical-align: top">
					<img class="twuser" width="48" height="48" 
					src="<%=stock == null ? "" : stock.getPictureUrl()%>"
					id="dashboard-picture">
		
				</td>
				<td style="vertical-align: top">
					<div style="text-align: left; vertical-align: top">

						<div style="float:left; overflow: hidden">
							
							<a  style="float:left; vertical-align: top; " href="http://twitter.com/#!/<%=stock.getName()%>"
							title="<%=lutil.get("twitter.link.tip", lang, stock.getName())%>" target="_blank">
							<%=(stock.getLongName()!=null)?stock.getLongName():""%></a> 
							 
								
							
							<%if(stock.isVerified()){ %>
								<div style="float:left">
									<%=GUIUtil.getInstance().getVerifiedIcon(lang) %>
								</div>
							<% } %>
								
								
							<%
							if(user!=null){
							 %>
								<div style="float:left; margin-left:5px" class="user-portfolio-item-watch-div-<%=stock.getId() %>">
									<%
									ArrayList<Stock> watchList = portfolioMgr.getUserWatchList(user.getId());
									boolean beingWatched = watchList.contains(stock);
									 %>
									<a class="add-to-watch-list-link-<%=stock.getId() %>" style="<%out.write((beingWatched)?"display:none":""); %>" href="javascript:void(0)" onclick="addToWatchList(<%=stock.getId()%>)">
										<%=Util.getWatchListIcon(true,15,lutil.get("watchlist.add", lang))%>
										
									</a>	
									<a class="remove-from-watch-list-link-<%=stock.getId() %>" style="<%=(!beingWatched)?"display:none":"" %>" href="javascript:void(0)" onclick="removeFromWatchList(<%=stock.getId()%>)">
										<%=Util.getWatchListIcon(false,15,lutil.get("watchlist.remove", lang))%>
										
									</a>	
								</div>
							 <%
							}
							 %>	
							
						
						</div>
						<div style="float:right">
							<div style="float:right">
								<%=GUIUtil.getInstance().getTwitterShareButton("#!stock="+ stock.getId(), "twitter.share.stock", lang, stock.getName())%>
								<%=GUIUtil.getInstance().getTwitterFollowButton(stock.getName(), lang)%>
							</div>
						</div>
					</div>					
					<br>					
					<div style="float:left" class="gray-small"><%=(stock.getDescription()!=null)?stock.getDescription():""%></div>
					<br>
					<div style="float:right">
						<% if(user.getLocation() != null){ %>
							<a href="http://maps.google.com/maps?q=<%=stock.getLocation() %>" target="_blank"><%=stock.getLocation()%></a>
						<% } %>
					</div>
				</td>
			</tr>	
			
		</table>
		
		<table class="datatbl">
			<tr>
				<td style="font-size: 15px">
							
					
					<% if(stock.isChangePerHourCalculated() && stock.getChangePerHour()!=0 && stock.getTotal() !=0 ){ 
					%>
						<%=Util.getPercentageFormatted((double) stock.getChangePerHour() / stock.getTotal(), false, true, true, true, false, true)  %>
					<% }
					%>	
					
							
				</td>
				<td style="float:right;vertical-align: bottom; width:370px">
					<div class="tabs">
							
						<a id="buy-sell-tab" class="youarehere" onClick="showBuySell();">
							<%=lutil.get("stockdetails.buysell", lang) %></a> 
							
						<a id="tweets-of-user-tab"
							onClick="showTweetsOfUser();"> <%=lutil.get("stockdetails.tweets", lang) %> </a>
						<a id="tweets-about-stock-tab"
							onClick="showTweetsAboutStock();">?</a>
						<a id="stock-history-tab" onClick="showStockHistory();">
							<%=lutil.get("stockdetails.history", lang) %></a> 
							
						<a id="stock-distribution-tab"
							onClick="showStockDistribution(<%=stock.getId()%>);">
							<%=lutil.get("stockdetails.distribution", lang) %> </a> 
							
							
					</div>
				</td>
			</tr>
		</table>
		
	</div>
	
	<br>
	
	<div id="stock-details-screen" class="main-div">
		<div id="buy-sell-container">
			<jsp:include page="buySell.jsp" />
		</div>
		<jsp:include page="stockHistory.jsp" />
		<jsp:include page="stockDistribution.jsp" />

		<jsp:include page="tweetsOfUser.jsp" />
		<jsp:include page="tweetsAboutStock.jsp" />

	</div>
	<%
		}
	%>

	
		<%
			if (quote.length() > 0 && stock != null && stock.getTotal() == 0) {
		%>
		<div id="hasnofollowers">
		
		<div id="dashboard-message-field" style="margin-top: 6px;"
			class="field-white">
			<p style="margin-top: 10px; margin-bottom: 10px;">
				<%
					out.write(stock.getName() + " has 0 followers. Please try something else.");
				%>
			</p>
		</div>
			</div>
		
		<%
			}
		%>


	

	<%
		if (quote.length() > 0){
			%>
			<br>
			<jsp:include page="otherSearchResults.jsp" />
			
			
			<%
			if(stock == null) {
			%>	
			
				<div id="searchnoresult"><p style="text-align: center;"><%=lutil.get("shared.noresults", lang) %></p></div>
		<%	}
		%>

			

	<%
		}
	%>

</div>


