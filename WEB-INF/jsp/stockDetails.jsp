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
	try {
		id = Long.parseLong(stockId);
		stockDetailList = portfolioMgr.getStockDistribution(id);
		stock = (Stock)request.getAttribute("stock");

	} catch (NumberFormatException nfe) {
	}
%>
<div id="stockdetails">
	<h3>
		Stock Details <a style="float: right; vertical-align: bottom;"
			href="/">Go to Home &gt;&gt;</a>
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
						<td style="text-align: left;" id="dashboard-stock-follower-status"><a href="/?stock=<%=stock == null ? "" : stock.getId()%>" title="Goes to main page and loads <%=stock == null ? "" : stock.getName()%>'s stock details"><%=stock == null ? "" : stock.getName()%></a>'s
							follower status <a href="http://twitter.com/#!/<%=stock == null ? "" : stock.getName()%>" style="float: right" target="_blank">Twitter Page &gt;&gt;</a></td>
					</tr>
				</table></td>
		</tr>
		<tr><td colspan="3">&nbsp;</td></tr>
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
		<div id="dashboard-message-field"
		style="margin-top: 42px;" class="field-white">
			Stock distribution table shows who owns how much share of <br><a href="/?stock=<%=stock == null ? "" : stock.getId()%>"><%=stock == null ? "" : stock.getName()%></a>.
		</div>
	<table class="datatbl" style="margin-top: 10px;">
		<thead>
			<tr class="thead">
				<td style="width: 120px"><b>Stock Distribution</b>
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
			<td><img class='twuser' style="margin-top: 2px; margin-bottom: 2px;"
				src="<%=stockDetail.getUserPictureUrl()%>" />
			</td>
			<td><a href="/user?user=<%=stockDetail.getUserId()%>" title="Goes to <%=stockDetail.getUserName()%>'s user profile page"><%=stockDetail.getUserName()%></a>
			</td>
			<td>$<%=Util.commaSep((int) (stockDetail.getPercent() * stockDetail
								.getStockTotal()))%></td>
			<td><%=f.format(stockDetail.getPercent() * 100 > 100 ? 100 : stockDetail.getPercent() * 100)%>%</td>
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