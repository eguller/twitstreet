
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
<%@ page import="com.twitstreet.servlet.TwitStreetServlet" %>

<%
	Injector inj = (Injector) pageContext.getServletContext().getAttribute(Injector.class.getName());
	UserMgr userMgr = inj.getInstance(UserMgr.class);
	User user = (User) request.getAttribute(TwitStreetServlet.USER);

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

				

	<div id="buy-sell-section">
		<div id="userfound"
			<%if (stock != null && stock.getTotal() > 0) {
				out.write(" style=\"visibility: visible;\"");
			} else {
				out.write(" style=\"display: none;\"");
			}%>>



			<table class="datatbl" style="margin-top: 10px;"
				<%if (stock == null) {
				out.write("style='display: none;'");
			}%>>
				<tr>
					<td colspan="3">
						<div class="field-white">
							<table class="datatbl">
								<tr>
									<td width="36px;"><img class="twuser"
										src="<%=stock == null ? "" : stock.getPictureUrl()%>"
										id="dashboard-picture"></td>
							
									<td>

										<table class="datatbl">
											<tr>
												<!--class="thead"-->
												<td style="width: 25%; text-align: center;"><span
													class="green-light"><b>Available</b></span></td>
												<td style="width: 25%; text-align: center;"><span
													class="red-light"><b>Sold</b></span></td>
												<td style="width: 25%; text-align: center;"><b>Total</b></td>
												<td style="width: 25%; text-align: center;"></td>
											</tr>
											<tr>
												<td id="available" style="width: 25%; text-align: center;">
													<%
														if (stock != null) {
															out.write(Util.commaSep(stock.getAvailable()));
														}
													%>
												</td>
												<td id="sold" style="width: 25%; text-align: center;">
													<%
														if (stock != null) {
															out.write(Util.commaSep(stock.getTotal() - stock.getAvailable()));
														}
													%>
												</td>
												<td id="total" style="width: 25%; text-align: center;">
													<%
														if (stock != null) {
															out.write(Util.commaSep(stock.getTotal()));
														}
													%>
												</td>
											<td id="changePerHour"
												style="width: 25%; text-align: center;">
												<%
													if (stock != null && stock.getChangePerHour() != 0) {
														int cph = stock.getChangePerHour();
														String str = Util.getFollowerChangePerHourString(cph,stock.getTotal());
														if (stock.getChangePerHour() > 0) {

															out.write("<span class=\"green-profit\">" + str + "</span>");
														} else if (stock.getChangePerHour() < 0) {
															out.write("<span class=\"red-profit\">" + str + "</span>");

														}

													}
												%>


											</td>
										</tr>
										</table>

									</td>
								</tr>
							</table>
						</div>
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
	


