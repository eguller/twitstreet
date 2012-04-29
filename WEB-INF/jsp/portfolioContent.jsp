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
<%@ page import="com.twitstreet.localization.LocalizationUtil"%>
<%@ page import="com.twitstreet.servlet.TwitStreetServlet"%>
<%@page import="com.twitstreet.util.GUIUtil"%>

<%
	Injector inj = (Injector) pageContext.getServletContext()
			.getAttribute(Injector.class.getName());
	User user = (User) request.getAttribute(User.USER);
	PortfolioMgr portfolioMgr = inj.getInstance(PortfolioMgr.class);

	LocalizationUtil lutil = LocalizationUtil.getInstance();
	String lang = (String) request.getSession().getAttribute(
			LocalizationUtil.LANGUAGE);
	Portfolio portfolio = null;
	if (user != null) {
		portfolio = portfolioMgr.getUserPortfolio(user);
	}
	if (portfolio == null) {

		return;

	}
%>
<table class="datatbl2" id="portfolio-table">
	<tbody>
		<%
			if (portfolio.getStockInPortfolioList().size() > 0) {
				for (int i = 0; i < portfolio.getStockInPortfolioList().size(); i++) {

					StockInPortfolio stock = portfolio
							.getStockInPortfolioList().get(i);
		%>
		<tr onmouseover="$('#portfolio-item-<%=stock.getStockId()%>').show()"
			onmouseout="$('#portfolio-item-<%=stock.getStockId()%>').hide()">
			<td width="58px"><img class="twuser" width="48" height="48"
				src="<%=stock.getPictureUrl()%>" />
			</td>
			<td>
				<table class="portfolio-stock-tbl">
					<tr>

						<td colspan="3" rowspan="1" height="20px" align="left">

							<div style="float: left">

								<a href='#!stock=<%=stock.getStockId()%>'
									onclick="reloadIfHashIsMyHref(this)"
									title="<%=lutil.get("stock.details.tip", lang,
							stock.getStockName())%>"><%=stock.getStockName()%>
								</a>
								<%
									if (stock != null && stock.isVerified()) {
								%>
								<%=GUIUtil.getInstance().getVerifiedIcon(lang)%>
								<%
									}
								%>
							</div>
							<div style="float: left"
								title="<%=lutil.get("portfolio.share.tip", lang)%>">
								&nbsp;(<%=Util.getShareString(stock.getPercentage())%>)
							</div>


							<div id="portfolio-item-<%=stock.getStockId()%>"
								style="display: none; float: right; text-align: right">
								<a href="javascript:void(0)" class="red-profit"
									onclick="sellAll(<%=stock.getStockId()%>)">
									<%=lutil.get("portfolio.sellall", lang)%> </a>
							</div></td>
					</tr>
					<tr class="gray-small">
							<td colspan="3">
							<%=(stock.getStockLongName()!=null)?stock.getStockLongName():""%>
						 </td>
					</tr>
					<tr>

						<td colspan="1" rowspan="1">
							<div title="<%=lutil.get("portfolio.value.tip", lang)%>">
								<b>$<%=Util.commaSep(stock.getAmount())%></b>
							</div>
						</td>
						<td colspan="1" rowspan="1">
							<%-- 								$<%=Util.commaSep(stock.getCapital())%> --%>
						</td>


						<td colspan="1" rowspan="1" align="right"
							title="<%=lutil.get("portfolio.speed.tip", lang)%>"><%=Util.getNumberFormatted(stock.getChangePerHour(),
							true, true, true, true, false, true)%></td>

					</tr>
					<tr>
						<td colspan="1" rowspan="1" align="left">
							<%
								double profit = 0;

										double amount = stock.getAmount();
										double capital = stock.getCapital();

										profit = amount - capital;
										if (profit != 0) {
							%>


							<div style="float: left"
								title="<%=lutil.get("portfolio.profit.tip", lang)%>">
								<%=Util.getNumberFormatted(profit, true, true,
								false, false, true, false)%>
							</div> <%
 	} else {
 %>
							<div style="float: left"></div> <%
 	}
 %>
						</td>
						<td colspan="1" rowspan="1"></td>
						<td colspan="1" rowspan="1" align="right">
							<%
								if (stock.isChangePerHourCalculated()
												&& stock.getChangePerHour() != 0 && stock.getTotal()!=0) {
							%> <%=Util.getPercentageFormatted(
								(double) stock.getTotalChangePerHour()
										/ stock.getTotal(), false, true, true,
								true, false, true)%> <%
 	}
 %>
						</td>
					</tr>
				</table></td>
		</tr>
		<%
			}
			} else {
		%>
		<tr>
			<td align="center"><%=lutil.get("shared.empty", lang)%></td>
		</tr>
		<%
			}
		%>
	</tbody>
</table>
