<%@ page import="com.twitstreet.localization.LocalizationUtil"%>
<%@ page import="com.twitstreet.twitter.SimpleTwitterUser"%>
<%@ page import="com.twitstreet.servlet.HomePageServlet"%>
<%@ page import="java.util.Date"%>
<%@ page import="com.twitstreet.db.data.StockHistoryData"%>
<%@ page import="com.twitstreet.db.data.UserStock"%>
<%@ page import="com.google.inject.Injector"%>
<%@ page import="com.twitstreet.db.data.User"%>
<%@ page import="com.google.inject.Guice"%>
<%@ page import="com.twitstreet.db.data.Portfolio"%>
<%@ page import="com.twitstreet.config.ConfigMgr"%>
<%@ page import="com.twitstreet.market.PortfolioMgr"%>
<%@ page import="com.twitstreet.session.UserMgr"%>
<%@ page import="com.twitstreet.db.data.UserStockDetail"%>
<%@ page import="java.util.List"%>
<%@ page import="com.twitstreet.util.Util"%>
<%@ page import="java.text.DecimalFormat"%>
<%@ page import="com.twitstreet.market.StockMgr"%>
<%@ page import="com.twitstreet.db.data.Stock"%>
<%@ page import="java.text.DecimalFormat"%>

<div id="stock-share-section" style="display: none;" >


	<%
	LocalizationUtil lutil = LocalizationUtil.getInstance();
	String lang = (String)request.getSession().getAttribute(LocalizationUtil.LANGUAGE);

	Injector inj = (Injector) pageContext.getServletContext().getAttribute(Injector.class.getName());
	StockMgr stockMgr = inj.getInstance(StockMgr.class);
	
	Stock stock = null;
	long stockId = -1;
	stock = (Stock)request.getAttribute(HomePageServlet.STOCK);
	
	
	if(stock!=null){
		stockId = stock.getId();
	}
	else{
		try{
			stockId = (Long) Long.parseLong(request.getParameter("stock"));
			stock = stockMgr.getStockById(stockId);
		}catch(Exception ex){
			
			
			
		}
	}
	
	PortfolioMgr portfolioMgr = inj.getInstance(PortfolioMgr.class);
	
	List<UserStockDetail> stockDetailList = null;

	if(stockId>-1){
		DecimalFormat f = new DecimalFormat("##.00");

		
		stockDetailList = portfolioMgr.getStockDistribution(stockId);
	
		%>


	<div id="stock-shares-chart-div"></div>

	<script id="stockDistributionScript" type="text/javascript">
		drawStockDistributionDivId = '#stock-shares-chart-div';
		drawStockDistributionPercentArray = new Array();
		drawStockDistributionNameArray = new Array();
		drawStockDistributionStockName =
	<%out.write("'" + stock.getName() + "';");

				if (stock.getAvailable() > 0) {
					out.write("drawStockDistributionNameArray.push('"+lutil.get("stockdistribution.available", lang)+"');\n");

					out.write("drawStockDistributionPercentArray.push(" + stock.getAvailable() + ");\n");
				}
				for (UserStockDetail stockDetail : stockDetailList) {
					out.write("drawStockDistributionNameArray.push('" + stockDetail.getUserName() + "');\n");

					out.write("drawStockDistributionPercentArray.push(" + (int) (stockDetail.getStockTotal() * stockDetail.getPercent()) + ");\n");
				}%>
		drawStockDistribution(drawStockDistributionDivId, drawStockDistributionNameArray,
				drawStockDistributionPercentArray, drawStockDistributionStockName);
	</script>

	<%if (stockDetailList != null && stockDetailList.size()>0) {
				%>
	<!--STOCK DISTRIBUTION TABLE -->

<%-- 	<div class="h3" style="text-align: center"><%=lutil.get("stockdistribution.details", lang)  %></div> --%>
	<table class="datatbl" style="margin-top: 10px;">
		<thead>
			<tr class="thead">
				<td style="width: 120px">&nbsp;</td>
				<td><%=lutil.get("user", lang) %></td>
				<td><%=lutil.get("stockdistribution.share", lang) %></td>
				<td><%=lutil.get("stockdistribution.value", lang) %></td>
			</tr>
		</thead>


		<%
				if (stockDetailList != null) {
						int i = 0;
						for (UserStockDetail stockDetail : stockDetailList) {
							if (i % 2 == 0) {
			%>


		<tr class='odd'>
			<%
					} else {
				%>
		
		<tr>
			<%
					}
				%>
			<td><img class='twuser'
				style="margin-top: 2px; margin-bottom: 2px;" width="48px" height="48px"
				src="<%=stockDetail.getUserPictureUrl()%>" /></td>
			<td><a href="#!user=<%=stockDetail.getUserId()%>"  onclick="reloadIfHashIsMyHref(this)" title="<%= lutil.get("user.details.tip", lang, stockDetail.getUserName())%>"><%=stockDetail.getUserName()%></a>
			</td>
			<td><%=Util.getShareString(stockDetail.getPercent()) %></td>
			<td>$<%=Util.commaSep((int) (stockDetail.getPercent() * stockDetail.getStockTotal()))%></td>
		</tr>
		<%
				i++;
						}

					}
			%>
	</table>
	<%
					}
			%>

	<%
					}
			%>

</div>
