
<%@page import="com.twitstreet.twitter.SimpleTwitterUser"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.twitstreet.db.data.UserStock"%>
<%@page import="java.sql.SQLException"%>
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
<%@ page import="com.twitstreet.servlet.HomePageServlet"%>

<%@ page import="com.twitstreet.servlet.HomePageServlet"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.Date"%>
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
<%@ page import="java.text.DecimalFormat"%>
<%@ page import="com.twitstreet.localization.LocalizationUtil"%>
<%@page import="com.twitstreet.util.GUIUtil"%>
<%
	Injector inj = (Injector) pageContext.getServletContext()
			.getAttribute(Injector.class.getName());
	StockMgr stockMgr = inj.getInstance(StockMgr.class);
	User user = (User) request.getAttribute(User.USER);

	ArrayList<Stock> trendResults = stockMgr.getTrendyStocks();

	LocalizationUtil lutil = LocalizationUtil.getInstance();
	String lang = (String) request.getSession().getAttribute(
			LocalizationUtil.LANGUAGE);
%>
<div id="trendy-stocks">

	<%
		if (trendResults.size() > 0) {
	%>

	<h3><%=lutil.get("trendystocks.header", lang)%></h3>
		<table class="datatbl" style="margin-top: 10px;">
			<%
				for (int i = 0; i < trendResults.size();) {
			%>
			<tr height="150">
				<%
					for (int j = 0; j < 1; j++) {
								if (i < trendResults.size()) {

									Stock stock = trendResults.get(i);
				%>

				<td>
					<table>
						<tr>
							<td width="60"><img class="twuser" width="48" height="48"
								src="<%=stock.getPictureUrl()%>" />
							</td>
							<td width="140"
								onmouseover="$('.user-portfolio-item-watch-div-<%=stock.getId()%>').show()"
								onmouseout="$('.user-portfolio-item-watch-div-<%=stock.getId()%>').hide()">
								<table class="datatbl2">
									<tr>
										<td colspan="2"><a href="#stock-<%=stock.getId()%>"
											onclick="reloadIfHashIsMyHref(this)"
											title="<%=lutil.get("stock.details.tip", lang,
									stock.getName())%>">
												<%=stock.getName()%> </a> <%
 	if (stock.isVerified()) {
 %> <%=GUIUtil.getInstance()
										.getVerifiedIcon(lang)%> <%
 	}
 %> <%
 	if (user != null) {
 %>
											<div class="user-portfolio-item-watch-div-<%=stock.getId()%>"
												style="display: none; float: right;">

												<%
													ArrayList<Stock> watchList = stockMgr
																				.getUserWatchList(user.getId());
																		boolean beingWatched = watchList
																				.contains(stock);
												%>
												<a class="add-to-watch-list-link-<%=stock.getId()%>"
													style="<%out.write((beingWatched) ? "display:none" : "");%>"
													href="javascript:void(0)"
													onclick="addToWatchList(<%=stock.getId()%>)"> <%=Util.getWatchListIcon(true, 15,
										lutil.get("watchlist.add", lang))%> </a> <a
													class="remove-from-watch-list-link-<%=stock.getId()%>"
													style="<%=(!beingWatched) ? "display:none" : ""%>"
													href="javascript:void(0)"
													onclick="removeFromWatchList(<%=stock.getId()%>)"> <%=Util.getWatchListIcon(false, 15,
										lutil.get("watchlist.remove", lang))%> </a>
											</div> <%
 	}
 %>
										</td>
									</tr>
									<tr>
										<td colspan="2" align="left"><%=Util.getNumberFormatted(
									stock.getAvailable(), false, true, false,
									false, false, false)%> / <%=Util.getNumberFormatted(stock.getTotal(),
									false, true, false, false, false, false)%></td>

									</tr>
									<tr>
										<td colspan="2" align="right"><%=Util.getNumberFormatted(
									(double) stock.getChangePerHour(), false,
									true, true, true, false, true)%></td>

									</tr>
									<tr>
										<td colspan="2" align="right"><%=Util.getPercentageFormatted(
									(double) stock.getChangePerHour()
											/ stock.getTotal(), false, true,
									true, true, false, true)%></td>
									</tr>
								</table> <br>
							</td>
							<td width="30">&nbsp;</td>
							<td>
								<div id="trendy-stock<%=stock.getId()%>"
									style="width: 260px; height: 120px">

									<%
										request.setAttribute("chartStock", stock);
									%>
									<jsp:include page="stockTimeLineChart.jsp">
										<jsp:param name="divId" value="#trendy-stock" />
										<jsp:param name="format" value="lastSixHours" />
									</jsp:include>
								</div>
							</td>
						</tr>
					</table>
				</td>
				<%
					} else {
				%>
				<td></td>
				<%
					}
								i++;
							}
				%>
			</tr>
			<%
				}
			%>
		</table>
	<%
		}
	%>
</div>

