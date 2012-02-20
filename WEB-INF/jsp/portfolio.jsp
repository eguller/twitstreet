<%@page import="com.twitstreet.db.data.StockInPortfolio"%>
<%@page import="com.twitstreet.db.data.Stock"%>
<%@page import="com.twitstreet.db.data.Portfolio"%>
<%@page import="com.twitstreet.market.PortfolioMgr"%>
<%@ page import="com.google.inject.Injector"%>
<%@ page import="com.google.inject.Guice"%>
<%@ page import="com.twitstreet.session.UserMgr"%>
<%@ page import="com.twitstreet.db.data.User"%>
<%@ page import="java.util.ArrayList"%>
<%@ page import="com.twitstreet.util.Util"%>
<%@ page import="com.twitstreet.session.UserMgr"%>
<%@ page import="com.twitstreet.servlet.TwitStreetServlet"%>


<%
	Injector inj = (Injector) pageContext.getServletContext()
			.getAttribute(Injector.class.getName());
	User user = (User) request.getAttribute(User.USER);
	PortfolioMgr portfolioMgr = inj.getInstance(PortfolioMgr.class);

	Portfolio portfolio = null;
	if (user != null) {
		portfolio = portfolioMgr.getUserPortfolio(user);
	}
	if (portfolio == null) {

		return;

	}
%>
<div id="portfolio-container">

	<div id="portfolio" class="main-div">
		<h3>Portfolio</h3>
		<table class="datatbl2" id="portfolio-table">
			<tbody>
				<%
					if (portfolio.getStockInPortfolioList().size() > 0) {
						for (int i = 0; i < portfolio.getStockInPortfolioList().size(); i++) {
	
							StockInPortfolio stock = portfolio
									.getStockInPortfolioList().get(i);
				%>
				<tr>
					<td width="58px"><img class="twuser"
						src="<%=stock.getPictureUrl()%>" /></td>
					<td>
						<table class="portfolio-stock-tbl">
							<tr>
	
								<td colspan="3" rowspan="1" height="20px" align="left">
	
									<div style="float: left">
	
										<a href='javascript:void(0)'
											onclick='loadStock(<%=stock.getStockId()%>)'
											title="Loads <%=stock.getStockName()%>'s stock details"><%=stock.getStockName()%>
										</a>
										<% if(stock != null && stock.isVerified()){ %>
											<img src="images/verified.png" title="This twitter account is verified"/>
										<% } %>
									</div>
									<div style="float: left"
										title="The ratio of your share to the whole stock">
										&nbsp;(<%=Util.getShareString(stock.getPercentage())%>)
									</div></td>
	
							</tr>
	
							<tr>
	
								<td colspan="1" rowspan="1">
									<div title="The cash equivalent of your share">
										<b>$<%=Util.commaSep(stock.getAmount())%></b>
									</div></td>
								<td colspan="1" rowspan="1">
									<%-- 								$<%=Util.commaSep(stock.getCapital())%> --%></td>
								<td colspan="1" rowspan="1" align="right">
									<%
										double profit = 0;
	
												double amount = stock.getAmount();
												double capital = stock.getCapital();
	
												profit = amount - capital;
												if (profit > 0) {
													out.write("<div title=\"Your profit from this stock\">"
															+ Util.getProfitString(profit) + "</div>");
												} else if (profit < 0) {
													out.write("<div title=\"Your loss from this stock\">"
															+ Util.getProfitString(profit) + "</div>");
												} else {
													out.write("<div style=\"float:left;\"></div>");
												}
									%>
								</td>
	
							</tr>
							<tr>
								<td colspan="1" rowspan="1" align="left"></td>
								<td colspan="1" rowspan="1"></td>
								<td colspan="1" rowspan="1">
									<%
										String profitPerHour = Util.getChangePerHourString(stock
														.getChangePerHour());
												if (stock.getChangePerHour() > 0) {
													out.write("<div title=\"Your estimated profit from this stock for the next hour\" style=\"float:left;\" class=\"green-profit\">"
															+ profitPerHour + "</div");
												} else if (stock.getChangePerHour() < 0) {
													out.write("<div title=\"Your estimated loss from this stock for the next hour\" style=\"float:left;\" class=\"red-profit\">"
															+ profitPerHour + "</div");
												} else {
													out.write("<div style=\"float:left;\">&nbsp;<div>");
	
												}
									%>
								</td>
							</tr>
						</table></td>
				</tr>
				<%
					}
					} else {
						out.write("<tr><td>" + Util.NO_RECORDS_FOUND_HTML
								+ "</td></tr>");
					}
				%>
			</tbody>
		</table>
	</div>
</div>