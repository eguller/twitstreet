<%@page import="java.util.Date"%>
<%@page import="java.util.LinkedHashMap"%>
<%@page import="com.twitstreet.db.data.StockHistoryData"%>
<%@page import="com.twitstreet.db.data.UserStock"%>
<%@page import="java.sql.SQLException"%>
<%@page import="com.twitstreet.market.StockMgr"%>
<%@ page import="com.google.inject.Injector"%>
<%@ page import="com.google.inject.Guice"%>
<%@ page import="com.twitstreet.util.Util"%>
<%@ page import="com.twitstreet.db.data.Portfolio"%>
<%@ page import="com.twitstreet.market.PortfolioMgr"%>
<%@ page import="com.twitstreet.db.data.UserStockDetail"%>
<%@ page import="java.util.List"%>
<%@ page import="com.twitstreet.util.Util"%>
<%@ page import="java.text.DecimalFormat"%>
<%@ page import="com.twitstreet.market.StockMgr"%>
<%@ page import="com.twitstreet.db.data.Stock"%>

<%
	Injector inj = (Injector) pageContext.getServletContext()
	.getAttribute(Injector.class.getName());
	PortfolioMgr portfolioMgr = inj.getInstance(PortfolioMgr.class);
	StockMgr stockMgr = inj.getInstance(StockMgr.class);
	DecimalFormat f = new DecimalFormat("##.00");
	String stockId = request.getParameter("stock");
	long id = -1;
	List<UserStockDetail> stockDetailList = null;
	Stock stock = null;
	StockHistoryData shd = null;
	try {
		id = Long.parseLong(stockId);
		stockDetailList = portfolioMgr.getStockDistribution(id);
		stock = (Stock)request.getAttribute("stock");
		
		
		shd = stockMgr.getStockHistory(id);

	} 
	catch (NumberFormatException nfe) {
	
	
	}
%>
<div id="stockdetails">
	<h3>
		Stock Details <a style="float: right; vertical-align: bottom;"
			href="/?stock=<%=stock == null ? "" : stock.getId()%>">Go to Home
			&gt;&gt;</a>
	</h3>
	<%
		if (id > -1) {
	%>

	<table class="datatbl">
		<tr>
			<td colspan="3">
				<table width="100%">
					<tr>
						<td width="36px;"><img class="twuser"
							src="<%=stock == null ? "" : stock.getPictureUrl()%>"
							id="dashboard-picture"></td>
						<td style="text-align: left;" id="dashboard-stock-follower-status"><a
							href="/?stock=<%=stock == null ? "" : stock.getId()%>"
							title="Goes to main page and loads <%=stock == null ? "" : stock.getName()%>&#39;s stock details"><%=stock == null ? "" : stock.getName()%></a>'s
							follower status <a
							href="http://twitter.com/#!/<%=stock == null ? "" : stock.getName()%>"
							style="float: right" target="_blank">Twitter Page &gt;&gt;</a></td>
					</tr>
				</table>
			</td>
		</tr>
		<tr>
			<td colspan="3">&nbsp;</td>
		</tr>
		<tr class="thead">
			<td style="width: 33%; text-align: center; font-weight: bolder;">Available</td>
			<td style="width: 33%; text-align: center; font-weight: bolder;">Sold</td>
			<td style="width: 33%; text-align: center; font-weight: bolder;">Total</td>
		</tr>
		<tr>
			<td id="available" style="width: 33%; text-align: center;">
				<%
					if (stock != null) {
															out.write(Util.commaSep(stock.getAvailable()));
														}
				%>
			</td>
			<td id="sold" style="width: 33%; text-align: center;">
				<%
					if (stock != null) {
															out.write(Util.commaSep(stock.getTotal()
																	- stock.getAvailable()));
														}
				%>
			</td>
			<td id="total" style="width: 33%; text-align: center;">
				<%
					if (stock != null) {
															out.write(Util.commaSep(stock.getTotal()));
														}
				%>
			</td>
		</tr>
	</table>

	<br> <br> <br>

	<div class="subheader">
		<h1><b>Charts</b></h1>
		<div id="tabs">
			<a id="stock-history-tab" class="youarehere"
				onClick="showStockHistory();">Stock History</a> <a
				id="stock-distribution-tab" onClick="showStockDistribution();">Stock
				Distribution</a>
		</div>
	</div>

	<div id="stock-trend-section">

		<%
			if(shd!=null && shd.getDateValueMap().size()>0){
		%>

		<div id="stock-trend-chart-div" style="height: 200px; width: 500px;"></div>
		<script type="text/javascript">
			var dateArray = new Array();
			var valueArray = new Array();
			var stockName =
		<%out.write("'"+shd.getName()+"';\n");
		
			LinkedHashMap<Date,Integer> dvm = shd.getDateValueMap();
			
			for (Date date: dvm.keySet()) {
				out.write("dateArray.push(new Date("+date.getTime()+"));\n");
				
				out.write("valueArray.push("+ dvm.get(date) +");\n");		
			}%>
			drawStockHistory('stock-trend-chart-div', dateArray, valueArray,
					stockName);
		</script>


		<%
			}
		%>

	</div>

	<!-- 		<div id="dashboard-message-field" -->
	<!-- 		style="margin-top: 42px;" class="field-white"> -->
	<%-- 			Stock distribution pie shows who owns how much share of <br><a href="/?stock=<%=stock == null ? "" : stock.getId()%>" title="Goes to main page and loads <%=stock == null ? "" : stock.getName()%>&#39;s stock details"><%=stock == null ? "" : stock.getName()%></a>. --%>
	<!-- 		</div> -->

	<div id="stock-share-section" style="display: none;">
		<div id="stock-shares-chart-div"></div>

		<script type="text/javascript">
			var percentArray = new Array();
			var nameArray = new Array();
			var stockName =
		<%out.write("'"+stock.getName()+"';");
	
	
		if(stock.getAvailable()>0){
			out.write("nameArray.push('Available');\n");
			
			out.write("percentArray.push("+ stock.getAvailable() +");\n");		
		}
		for (UserStockDetail stockDetail : stockDetailList) {
			out.write("nameArray.push('"+stockDetail.getUserName() +"');\n");
			
			out.write("percentArray.push("+ (int)(stockDetail.getStockTotal() * stockDetail.getPercent()) +");\n");		
		}%>
			drawStockDistribution('stock-shares-chart-div', nameArray,
					percentArray, stockName);
		</script>



		<table class="datatbl" style="margin-top: 10px;">
			<thead>
				<tr class="thead">
					<td style="width: 120px">
						<!-- 	<b>Stock Distribution</b> -->
					</td>
					<td>User Name</td>
					<td>Value</td>
					<td>Share</td>
				</tr>
			</thead>
			<%
				if (id > -1 && stockDetailList != null) {
						int i = 0;
						for (UserStockDetail stockDetail : stockDetailList) {
							if (i % 2 == 0) {
			%><tr class='odd'>
				<%
					} else {
				%>
			
			<tr>
				<%
					}
				%>
				<td><img class='twuser'
					style="margin-top: 2px; margin-bottom: 2px;"
					src="<%=stockDetail.getUserPictureUrl()%>" />
				</td>
				<td><a href="/user?user=<%=stockDetail.getUserId()%>"
					title="<%=stockDetail.getUserName()%>&#39;s user profile page"><%=stockDetail.getUserName()%></a>
				</td>
				<td>$<%=Util.commaSep((int) (stockDetail.getPercent() * stockDetail
								.getStockTotal()))%></td>
				<td><%=f.format(stockDetail.getPercent() * 100 > 100 ? 100
								: stockDetail.getPercent() * 100)%>%</td>
			</tr>
			<%
				i++;
						}
					}
			%>
		</table>


		<%
			} else {
		%>
		<div id="dashboard-message-field"
			style="margin-top: 6px; margin-bottom: 6px;" class="field-white">
			Stock not found</div>
		<%
			}
		%>
	</div>
</div>