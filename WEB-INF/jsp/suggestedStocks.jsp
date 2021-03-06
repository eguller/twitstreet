
<%@page import="com.twitstreet.servlet.PaginationDO"%>
<%@page import="com.twitstreet.twitter.SimpleTwitterUser"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.sql.SQLException"%>

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
<%@ page import="com.twitstreet.util.GUIUtil"%>
<%@ page import="com.twitstreet.servlet.TopGrossingStocksServlet" %>
<%@ page import="com.twitstreet.servlet.TrendyStocksServlet" %>
<%@page import="org.apache.log4j.Logger" %>

<%
	long start = 0;
	long end = 0;
	start = System.currentTimeMillis();
	Logger logger = Logger.getLogger(this.getClass());
	
	LocalizationUtil lutil = LocalizationUtil.getInstance();
	String lang = (String) request.getSession().getAttribute(
			LocalizationUtil.LANGUAGE);

	Injector inj = (Injector) pageContext.getServletContext().getAttribute(Injector.class.getName());
	ArrayList<Stock> stockList = (ArrayList<Stock>) request.getAttribute("stockList");
	String stockListName = (String) request.getAttribute("stockListName");

	PortfolioMgr portfolioMgr = inj.getInstance(PortfolioMgr.class);
	long time = (new Date()).getTime();
	StockMgr stockMgr = inj.getInstance(StockMgr.class);
	User user = (User) request.getAttribute(User.USER);

	
PaginationDO pdo = (PaginationDO)request.getAttribute("pdo");


ArrayList<Stock> watchList = null;
if(user!=null){
	watchList = portfolioMgr.getUserWatchList(user.getId());
}

%>
<div id="stock-list-<%=stockListName%>">

	<%
		if (stockList!=null && stockList.size() > 0) {
	%>
	<%request.setAttribute("pdo", pdo); %>
<jsp:include page="pagination.jsp"/>
<%-- 	<h3><%=lutil.get("suggestedstocks.header", lang)%></h3> --%>
		<table class="datatbl" style="margin-top: 10px;">
			<%
				for (int i = 0; i < stockList.size();) {
			%>
			<tr height="150">
				<%
					for (int j = 0; j < 1; j++) {
								if (i < stockList.size()) {

									Stock stock = stockList.get(i);
				%>

				<td>
					<table>
						<tr>
							<td width="60"><img class="twuser" width="48" height="48"
								src="<%=stock.getPictureUrl()%>" />
							</td>
							<td width="170"
								onmouseover="$('.user-portfolio-item-watch-div-<%=stock.getId()%>').show()"
								onmouseout="$('.user-portfolio-item-watch-div-<%=stock.getId()%>').hide()">
								<table class="datatbl2">
									
									<tr>
										<td colspan="2"><a href="#!stock=<%=stock.getId()%>"
											onclick="reloadIfHashIsMyHref(this); loadTitle('<%=lutil.get("stock.bar.profile", lang, stock.getName())%>');"
											title="<%=lutil.get("stock.details.tip", lang, stock.getName())%>">
												<%=stock.getName()%> </a> <%
												 	if (stock.isVerified()) {
												 %> <%=GUIUtil.getInstance().getVerifiedIcon(lang)%> <%
												 	}
												 %> <%
												 	if (user != null) {
												 %>
											<div class="user-portfolio-item-watch-div-<%=stock.getId()%>"
												style="display: none; float: right;">

												<%
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
									<tr class="gray-small">
										<td>
											<%=(stock.getLongName()!=null)?stock.getLongName():""%>
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
										<td colspan="2" align="right">
										<%										
											if(stock.getTotal()!=0){
												out.write(Util.getPercentageFormatted(
														(double) stock.getChangePerHour()
														/ stock.getTotal(), false, true,
												true, true, false, true));
											}
										%></td>
									</tr>
								</table> 
							</td>
							<td width="30">&nbsp;</td>
							<td>
								<div id="<%=stockListName%>-stock-history-<%=stock.getId()%>"
									style="width: 230px; height: 120px">

									<%
										request.setAttribute("chartStock", stock);
										request.setAttribute("stockTimeLineDivId", "#"+stockListName+"-stock-history-"+stock.getId());
									%>
									<jsp:include page="stockTimeLineChart.jsp">
										<jsp:param name="format" value="simple,240" />							
										<jsp:param name="forMinutes" value="360"/>	
									</jsp:include>
								</div>
							</td>
						</tr>
					</table>
					
<!-- 							<br> -->
<!-- 						<hr class="hr-blue-class"> -->
						
						
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
		<%request.setAttribute("pdo", pdo); %>
<jsp:include page="pagination.jsp"/>
</div>
<%
end = System.currentTimeMillis();
logger.debug("suggestedStocks.jsp execution time: " + (end - start));
%>
