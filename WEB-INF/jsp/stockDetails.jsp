
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
	DecimalFormat f = new DecimalFormat("##.00");

	ConfigMgr configMgr = inj.getInstance(ConfigMgr.class);

	long id = -1;
	List<UserStockDetail> stockDetailList = null;
	Stock stock = null;
	StockHistoryData shd = null;
	try {
		stock = (Stock) request.getAttribute("stock");
		id = stock.getId();
		stockDetailList = portfolioMgr.getStockDistribution(stock.getId());
	
		shd = stockMgr.getStockHistory(stock.getId());

	} catch (NumberFormatException nfe) {

	}
	
 	UserStock userStock = null;
		if (user != null && stock != null) {
			userStock = portfolioMgr.getStockInPortfolio(user.getId(), stock.getId());
		
		}
		
%>

<div id="stockdetails">
<!-- 	<h3> -->
<!-- 		Stock Details <a style="float: right; vertical-align: bottom;" -->
<%-- 			href="/?stock=<%=stock == null ? "" : stock.getId()%>">Go to Home --%>
<!-- 			&gt;&gt;</a> -->
<!-- 	</h3> -->
	<%
		if (id > -1) {
	%>

	<input type="hidden" id="quote-id" value="<%=stock == null ? "" : stock.getId()%>" />
	



	<div class="subheader">
		<h1><b>	<%=stock.getName()%></b></h1>
		<div id="tabs">
			<a id="buy-sell-tab"  class="youarehere" onClick="showBuySell();">
				Buy/Sell</a>

			<a id="stock-history-tab" onClick="showStockHistory();">
			History</a>
		
			<a id="stock-distribution-tab" onClick="showStockDistribution();">
				Distribution</a>

		</div>
	</div>
		
	<div id="buy-sell-section">
		<div id="userfound"
			<%if (stock != null && stock.getTotal() > 0) {
						out.write(" style=\"visibility: visible;\"");
					} else {
						out.write(" style=\"display: none;\"");
					}%>>
		

			<input type="hidden" id="user-stock-val"
				value="<%=userStock == null ? "" : (int) (userStock.getPercent() * stock.getTotal())%>" />
			<input type="hidden" id="available-hidden"
				value="<%=stock == null ? "" : stock.getAvailable()%>" /> <input
				type="hidden" id="sold-hidden"
				value="<%=stock == null ? "" : stock.getSold()%>" /> <input
				type="hidden" id="total-hidden"
				value="<%=stock == null ? "" : stock.getTotal()%>" />

			<table class="datatbl" style="margin-top: 10px;"
				<%if (stock == null) {
						out.write("style='display: none;'");
					}%>>
				<tr>
					<td colspan="3">
						<table width="100%">
							<tr>
								<td width="36px;"><img class="twuser"
									src="<%=stock == null ? "" : stock.getPictureUrl()%>"
									id="dashboard-picture"></td>
								<td style="text-align: left;"
									id="dashboard-stock-follower-status"><a
									href="/?stock=<%=stock == null ? "" : stock.getId()%>"
									title="<%=stock == null ? "" : stock.getName()%>&#39;s stock details page."><%=stock == null ? "" : stock.getName()%></a>'s
									follower status</td>
							</tr>
						</table>
					</td>
				</tr>
				<tr class="thead">
					<td style="width: 33%; text-align: center;">Available</td>
					<td style="width: 33%; text-align: center;">Sold</td>
					<td style="width: 33%; text-align: center;">Total</td>
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
										out.write(Util.commaSep(stock.getTotal() - stock.getAvailable()));
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
				<tr>
					<td colspan="3" style="text-align: center; padding-top: 10px;">

						<div id="dashboard-message-field" style="margin-top: 6px;"
							class="field-white">
							<p
								style="width: 100%; text-align: center; margin-top: 10px; margin-bottom: 10px; padding-top: 5px; padding-bottom: 5px;">
								<span id="user-stock"> <%

 			if (userStock == null) {
					out.write("You don't have any " + stock.getName());
				} else {
					int amount = (int) (userStock.getPercent() * stock.getTotal());
					String commaSep = Util.commaSep(amount);
					out.write("You have <b>" + commaSep + "</b> " + stock.getName());
				}

				if (stock.getTotal() < configMgr.getMinFollower()) {
					out.write("<br>");
					out.write(stock.getName() + " has <b>" + stock.getTotal() + "</b> follower. <br>You cannot buy followers if total is less than <b>" + configMgr.getMinFollower() + "</b>");
				}
 %>

								</span>
							</p>
						</div>

					</td>



				</tr>
				
				<%if (stock == null) {
						out.write("style='display: none;'");
					}%>
				<tr>
					<td colspan="3" style="text-align: center; padding-top: 10px;">
						Twitstreet gets 1% commission on every sale!</td>
				</tr>
				<tr id="buy-links-row">
					<td colspan="3" id="buy-links">
						<div id="buy-sell-div">
							<table class="buy-sell-table">
								<%
									if (user != null && stock != null) {
												ArrayList<Double> buyValues = new ArrayList<Double>();
												ArrayList<Double> sellValues = new ArrayList<Double>();
												double totalCash = user.getCash();
												int available = stock.getAvailable();
												int min = (int) Math.min(totalCash, available);

												int i = min == 0 ? 0 : String.valueOf((int) min).length();
												if ((int) Math.pow(10, i - 1) != min && min > 0) {
													buyValues.add(Math.floor(min));
												}

												for (; i > 0; i--) {
													buyValues.add(Math.pow(10, i - 1));
												}

												if (userStock != null) {
													double userTotalStock = (userStock.getPercent() * stock.getTotal());
													i = userTotalStock < 1 ? 0 : String.valueOf((int) userTotalStock).length();
													if (userTotalStock != Math.pow(10, i - 1)) {
														sellValues.add(Math.floor(userTotalStock));
													}
													for (; i > 0; i--) {
														sellValues.add(Math.pow(10, i - 1));
													}
												}
												i = 0;
												while (true) {
													out.write("<tr>");
													out.write("<td>");
													if (i < buyValues.size() && stock.getTotal() > configMgr.getMinFollower()) {
														out.write("<div class=\"field-green\" onclick=\"buy(" + stock.getId() + "," + buyValues.get(i) + ");\">");
														out.write("Buy<br>");
														out.write(Util.commaSep(buyValues.get(i).intValue()));
														out.write("</div>");
													}
													out.write("</td>");
													out.write("<td>");
													if (i < sellValues.size()) {
														out.write("<div class=\"field-red\" onclick=\"sell(" + stock.getId() + "," + sellValues.get(i) + ");\">");
														out.write("Sell<br>");
														out.write(Util.commaSep(sellValues.get(i).intValue()));
														out.write("</div>");
													}
													out.write("</td>");
													out.write("</tr>");
													i++;
													if (i > buyValues.size() && i > sellValues.size()) {
														break;
													}
												}
											}
								%>
							</table>
						</div>
					</td>
				</tr>
			</table>
		</div>
	</div>
		<%
			if (shd != null && shd.getDateValueMap().size() > 1) {
		%>
	<div id="stock-trend-section" style="display: none;">

	

		<div id="stock-trend-chart-div" style="height: 200px; width: 500px;"></div>
		<script type="text/javascript">
			var dateArray = new Array();
			var valueArray = new Array();
			var stockName =
		<%out.write("'" + shd.getName() + "';\n");

						LinkedHashMap<Date, Integer> dvm = shd.getDateValueMap();

						for (Date date : dvm.keySet()) {
							out.write("dateArray.push(new Date(" + date.getTime() + "));\n");

							out.write("valueArray.push(" + dvm.get(date) + ");\n");
						}%>
			drawStockHistory('stock-trend-chart-div', dateArray, valueArray,
					stockName);
		</script>

	</div>

		<%
			}
		%>


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
		<%out.write("'" + stock.getName() + "';");

					if (stock.getAvailable() > 0) {
						out.write("nameArray.push('Available');\n");

						out.write("percentArray.push(" + stock.getAvailable() + ");\n");
					}
					for (UserStockDetail stockDetail : stockDetailList) {
						out.write("nameArray.push('" + stockDetail.getUserName() + "');\n");

						out.write("percentArray.push(" + (int) (stockDetail.getStockTotal() * stockDetail.getPercent()) + ");\n");
					}%>
			drawStockDistribution('stock-shares-chart-div', nameArray,
					percentArray, stockName);
		</script>


		<!--STOCK DISTRIBUTION TABLE -->

		<table class="datatbl" style="margin-top: 10px;">
			<thead>
				<tr class="thead">
					<td style="width: 120px"><b>Stock Distribution</b></td>
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
					style="margin-top: 2px; margin-bottom: 2px;"
					src="<%=stockDetail.getUserPictureUrl()%>" /></td>
				<td><a href="/user?user=<%=stockDetail.getUserId()%>"
					title="<%=stockDetail.getUserName()%>&#39;s user profile page"><%=stockDetail.getUserName()%></a>
				</td>
				<td>$<%=Util.commaSep((int) (stockDetail.getPercent() * stockDetail.getStockTotal()))%></td>
				<td><%=f.format(stockDetail.getPercent() * 100 > 100 ? 100 : stockDetail.getPercent() * 100)%>%</td>
			</tr>
			<%
				i++;
							}

						}
			%>
		</table>
	</div>

		

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