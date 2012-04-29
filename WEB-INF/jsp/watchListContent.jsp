<%@ page import="com.twitstreet.localization.LocalizationUtil"%>
<%@page import="com.twitstreet.db.data.StockInPortfolio"%>
<%@page import="com.twitstreet.db.data.Stock"%>
<%@page import="com.twitstreet.db.data.Portfolio"%>
<%@page import="com.twitstreet.market.PortfolioMgr"%>
<%@page import="com.twitstreet.market.StockMgr"%>
<%@ page import="com.google.inject.Injector"%>
<%@ page import="com.google.inject.Guice"%>
<%@ page import="com.twitstreet.session.UserMgr"%>
<%@ page import="com.twitstreet.db.data.User"%>
<%@ page import="java.util.ArrayList"%>
<%@ page import="com.twitstreet.util.GUIUtil"%>
<%@ page import="com.twitstreet.util.Util"%>
<%@ page import="com.twitstreet.session.UserMgr"%>
<%@ page import="com.twitstreet.servlet.TwitStreetServlet"%>
<%@ page import="java.util.*"%>
<%@page import="com.twitstreet.util.GUIUtil"%>


<%
	Injector inj = (Injector) pageContext.getServletContext().getAttribute(Injector.class.getName());
	User user = (User) request.getAttribute(User.USER);
	StockMgr stockMgr = inj.getInstance(StockMgr.class);

	LocalizationUtil lutil = LocalizationUtil.getInstance();
	String lang = (String) request.getSession().getAttribute(LocalizationUtil.LANGUAGE);
	PortfolioMgr portfolioMgr = inj.getInstance(PortfolioMgr.class);
	ArrayList<Stock> watchList = portfolioMgr.getUserWatchList(user.getId());
%>
<%
	if (watchList != null) {
%>

<table class="datatbl2" id="watch-list-table">
	<tbody>
		<%
			if (watchList.size() > 0) {
					for (int i = 0; i < watchList.size(); i++) {

						Stock stock = watchList.get(i);
		%>
		<tr onmouseover="$('#watch-item-<%=stock.getId()%>').show()"
			onmouseout="$('#watch-item-<%=stock.getId()%>').hide()">
			<td width="58px"><img class="twuser" width="48" height="48"
				src="<%=stock.getPictureUrl()%>" /></td>
			<td>
				<table class="portfolio-stock-tbl">
					<tr>

						<td colspan="2" rowspan="1" height="20px" align="left">

							<div style="float: left">

								<a href='#!stock=<%=stock.getId()%>'
									onclick="reloadIfHashIsMyHref(this)"
									title="<%=lutil.get("stock.details.tip", lang, stock.getName())%>"><%=stock.getName()%>
								</a>
								<%
									if (stock != null && stock.isVerified()) {
								%>
								<%=GUIUtil.getInstance().getVerifiedIcon(lang)%>
								<%
									}
								%>
							</div>
						</td>

						<td align="right">
							<div id="watch-item-<%=stock.getId()%>" style="display: none">
								<a class="red-profit" href="javascript:void(0)"
									title="<%=lutil.get("watchlist.remove", lang, stock.getName())%>"
									onclick="removeFromWatchList(<%=stock.getId()%>)"> <%=lutil.get("shared.remove", lang)%></a>
							</div>
						</td>

					</tr>
					<tr class="gray-small">
						<td colspan="3"><%=(stock.getLongName() != null) ? stock.getLongName() : ""%>
						</td>
					</tr>
					<tr>

						<td colspan="1" rowspan="1"><%=Util.commaSep(stock.getAvailable())%>
							/ <%=Util.commaSep(stock.getTotal())%></td>
						<td colspan="1" rowspan="1">
							<%-- 								$<%=Util.commaSep(stock.getCapital())%> --%>
						</td>
						<td colspan="1" rowspan="1" align="right">
							<%
								if (stock.isChangePerHourCalculated()) {
							%> <%=(stock.getChangePerHour() != 0) ? Util.getNumberFormatted(stock.getChangePerHour(), false, true, true, true, false, true) : "&nbsp;"%> <%
 	} else {
 %> <%=GUIUtil.getInstance().getSpeedCalculation(stock, lang)%> <%
 	}
 %>
						</td>

					</tr>
					<tr>
						<td colspan="1" rowspan="1" align="left"></td>
						<td colspan="1" rowspan="1"></td>
						<td colspan="1" rowspan="1" align="right">
							<%
								if (stock.isChangePerHourCalculated() && stock.getTotal() !=0 ) {
							%> <%=Util.getPercentageFormatted((double) stock.getChangePerHour() / stock.getTotal(), false, true, true, true, false, true)%> <%
 	} else {
 %> &nbsp; <%
 	}
 %>
						</td>
					</tr>
				</table>
			</td>
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
<%
	}
%>