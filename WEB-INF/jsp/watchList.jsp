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
<%@ page import="com.twitstreet.util.Util"%>
<%@ page import="com.twitstreet.session.UserMgr"%>
<%@ page import="com.twitstreet.servlet.TwitStreetServlet"%>
<%@ page import="java.util.*"%>


<%
	Injector inj = (Injector) pageContext.getServletContext()
			.getAttribute(Injector.class.getName());
	User user = (User) request.getAttribute(User.USER);
	StockMgr stockMgr = inj.getInstance(StockMgr.class);
	

	 
	

	ArrayList<Stock> watchList = stockMgr.getUserWatchList(user.getId());

%>
<div id="user-watch-list" class="main-div">
<h3>Watch List</h3>
<%
				if (watchList!=null) {
			%>

	
	<table class="datatbl2" id="watch-list-table">
		<tbody>
			<%
				if (watchList.size() > 0) {
					for (int i = 0; i < watchList.size(); i++) {

						Stock stock = watchList.get(i);
			%>
			<tr  onmouseover="$('#watch-item-<%=stock.getId()%>').show()" onmouseout="$('#watch-item-<%=stock.getId()%>').hide()">
				<td width="58px"><img class="twuser"
					src="<%=stock.getPictureUrl()%>" /></td>
				<td>
					<table class="portfolio-stock-tbl">
						<tr>

							<td colspan="2" rowspan="1" height="20px" align="left">

								<div style="float: left">

									<a href='javascript:void(0)'
										onclick='loadStock(<%=stock.getId()%>)'
										title="Loads <%=stock.getName()%>'s stock details"><%=stock.getName()%>
									</a>
									<% if(stock != null && stock.isVerified()){ %>
										<img src="images/verified.png" title="This twitter account is verified"/>
									<% } %>
								</div>
							</td>
							<td align="right">
								<div id="watch-item-<%=stock.getId()%>" style="display:none">
									<a href="javascript:void(0)" onclick="removeFromWatchList(<%=stock.getId()%>)"> Remove</a>
								</div>
							</td>

						</tr>

						<tr>

							<td colspan="1" rowspan="1">
								<%=Util.commaSep(stock.getAvailable()) %> / <%=Util.commaSep(stock.getTotal()) %>
							
								</td>
							<td colspan="1" rowspan="1">
								<%-- 								$<%=Util.commaSep(stock.getCapital())%> --%></td>
							<td colspan="1" rowspan="1" align="right">
								<% if(stock.isChangePerHourCalculated()){ %>
									<span class="<%=(stock.getChangePerHour()>=0)?"green-profit":"red-profit" %>">
									<%=(stock.getChangePerHour()!=0)? Util.getRoundedChangePerHourString(stock.getChangePerHour()):"&nbsp;" %>
								</span>
									
								<% } else{
									Date date = new Date();
									
									int minuteLeft = 15 - (int)(date.getTime() - stock.getLastUpdate().getTime()) / (60 * 1000);
									
									minuteLeft=(minuteLeft<1)?1:minuteLeft;
									String activityMessage = "Calculating the trend of the stock for the next hour.\nIt will be ready in "+minuteLeft+" minute"+((minuteLeft>1)?"s.":".");
								%>
								
								
									<img alt="<%=activityMessage %>" title="<%=activityMessage %>" src="/images/activity_indicator_16.gif"/>
								<% }
								%>
							
							</td>

						</tr>
						<tr>
							<td colspan="1" rowspan="1" align="left"></td>
							<td colspan="1" rowspan="1"></td>
							<td colspan="1" rowspan="1" align="right">
								<% if(stock.isChangePerHourCalculated()){
									
									
									
									%>
									<span class="<%=(stock.getChangePerHour()>=0)?"green-profit":"red-profit" %>">
										<%=(stock.getChangePerHour()!=0)? Util.getPercentageChangePerHourString((double)stock.getChangePerHour()/stock.getTotal()):"&nbsp;" %>
								
									</span>
								<% }else{%>
									
										&nbsp;
									<% }
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
	<%
		}
			%>
</div>
