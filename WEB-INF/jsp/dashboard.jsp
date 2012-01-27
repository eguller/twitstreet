<%@page import="java.util.ArrayList"%>
<%@page import="com.twitstreet.db.data.UserStock"%>
<%@page import="java.sql.SQLException"%>
<%@page import="com.twitstreet.servlet.StockQuoteServlet"%>
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
<%@ page  import="com.twitstreet.servlet.HomePageServlet" %>

<%
	User sessionUser = (User) request.getSession().getAttribute(
			User.USER);
	Injector inj = (Injector) pageContext.getServletContext()
			.getAttribute(Injector.class.getName());
	StockMgr stockMgr = inj.getInstance(StockMgr.class);
	PortfolioMgr portfolioMgr = inj.getInstance(PortfolioMgr.class);
	ConfigMgr configMgr = inj.getInstance(ConfigMgr.class);
	UserMgr userMgr = inj.getInstance(UserMgr.class);
	User user = null;
	if (sessionUser != null) {
		user = userMgr.getUserById(sessionUser.getId());
	}
%>

<div id="dashboard">
	<h3>Dashboard</h3>
	<div id="quoteholder">
		<%
			String quote = request.getSession().getAttribute(
					StockQuoteServlet.QUOTE) == null ? "" : (String) request
					.getSession().getAttribute(StockQuoteServlet.QUOTE);
			Stock stock = null;
			if (quote.length() > 0) {
				stock = (Stock) request.getAttribute(HomePageServlet.STOCK);
				if (stock == null) {
					stock = stockMgr.getStock(quote);
				}
			}
		%>
		<input type="text" class="textbox" id="quote" value="<%=quote%>" />
		<button class="button" onclick="getquote();" id="getquotebutton">Get
			quote</button>
		<input type="hidden" id="quote-hidden" value="<%=quote%>" /> <input
			type="hidden" id="quote-id"
			value="<%=stock == null ? "" : stock.getId()%>" />
	</div>
	<div id="userfound"
		<%if (stock != null && quote.length() > 0 && stock.getTotal() > 0) {
				out.write(" style=\"visibility: visible;\"");
			} else {
				out.write(" style=\"display: none;\"");
			}%>>
		<div id="dashboard-message-field" style="margin-top: 6px;"
			class="field-white">
			<p
				style="width: 100%; text-align: center; margin-top: 10px; margin-bottom: 10px; padding-top: 5px; padding-bottom: 5px;">
				<span id="user-stock"> <%
 	UserStock userStock = null;
 	if (user != null && stock != null) {
 		userStock = portfolioMgr.getStockInPortfolio(user.getId(),
 				stock.getId());
 		if (userStock == null) {
 			out.write("You don't have any " + stock.getName());
 		} else {
 			int amount = (int) (userStock.getPercent() * stock
 					.getTotal());
 			String commaSep = Util.commaSep(amount);
 			out.write("You have <b>" + commaSep + "</b> "
 					+ stock.getName());
 		}

 		if (stock.getTotal() < configMgr.getMinFollower()) {
 			out.write("<br>");
 			out.write(stock.getName()
 					+ " has <b>"
 					+ stock.getTotal()
 					+ "</b> follower. <br>You cannot buy followers if total is less than <b>"
 					+ configMgr.getMinFollower() + "</b>");
 		}
 	}
 %> </span>
			</p>
		</div>

		<input type="hidden" id="user-stock-val"
			value="<%=userStock == null ? ""
					: (int) (userStock.getPercent() * stock.getTotal())%>" />
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
								id="dashboard-picture">
							</td>
							<td style="text-align: left;"
								id="dashboard-stock-follower-status"><a
								href="/stock?stock=<%=stock == null ? "" : stock.getId()%>"
								title="<%=stock == null ? "" : stock.getName()%>&#39;s stock details page."><%=stock == null ? "" : stock.getName()%></a>'s
								follower status</td>
						</tr>
					</table>
				</td>
			</tr>
			<tr>
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
										double userTotalStock = (userStock.getPercent() * stock
												.getTotal());
										i = userTotalStock < 1 ? 0 : String.valueOf(
												(int) userTotalStock).length();
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
										if (i < buyValues.size()
												&& stock.getTotal() > configMgr.getMinFollower()) {
											out.write("<div class=\"field-green\" onclick=\"buy("
													+ stock.getId() + "," + buyValues.get(i)
													+ ");\">");
											out.write("Buy<br>");
											out.write(Util.commaSep(buyValues.get(i).intValue()));
											out.write("</div>");
										}
										out.write("</td>");
										out.write("<td>");
										if (i < sellValues.size()) {
											out.write("<div class=\"field-red\" onclick=\"sell("
													+ stock.getId() + "," + sellValues.get(i)
													+ ");\">");
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
	<div id="searchnoresult"
		<%if (quote.length() > 0 && stock == null) {
				out.write(" style=\"visibility: visible\"");
			} else {
				out.write(" style=\"display: none\"");
			}%>>
		Search does not have result</div>

	<div id="hasnofollowers"
		<%if (quote.length() > 0 && stock != null && stock.getTotal() == 0) {
				out.write(" style=\"visibility: visible\"");
			} else {
				out.write(" style=\"display: none\"");
			}%>>

		<%
			if (stock != null) {
				out.write(stock.getName()
						+ " has 0 followers. Please try something else.");
			}
		%>
	</div>
</div>

