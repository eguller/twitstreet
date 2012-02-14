<%@page import="com.twitstreet.db.data.StockInPortfolio"%>
<%@page import="com.twitstreet.session.UserMgr"%>
<%@page import="com.twitstreet.db.data.User"%>
<%@ page import="com.google.inject.Injector"%>
<%@ page import="com.google.inject.Guice"%>
<%@ page import="java.util.List"%>
<%@ page import="com.twitstreet.util.Util"%>
<%@ page import="com.twitstreet.market.PortfolioMgr"%>
<%@ page import="com.twitstreet.db.data.Portfolio"%>

<%
	Injector inj = (Injector) pageContext.getServletContext()
			.getAttribute(Injector.class.getName());
	UserMgr userMgr = inj.getInstance(UserMgr.class);
	PortfolioMgr portfolioMgr = inj.getInstance(PortfolioMgr.class);
	Portfolio portfolio = null;
	User user = (User)request.getAttribute("user");
	portfolio = portfolioMgr.getUserPortfolio(user);
%>
<%
	if (user != null) {

		String userIdStr = String.valueOf(user.getId());
%>
<div id="userprofile" class="main-div">
	<input id="userProfileUserId" type="hidden" value="<%=userIdStr%>"></input>
	<jsp:include page="getQuote.jsp" />


	<div id="userbalance" class="main-div">
		<h3><%=user.getUserName()%>'s Profile
		</h3>

		<table class="datatbl">
			<tr>
				<td colspan="4">
					<table class="datatbl">
						<tr>
							<td width="36px;"><img class="twuser"
								src="<%=user == null ? "" : user.getPictureUrl()%>"
								id="dashboard-picture">
							</td>
							<td style="text-align: left;" id="dashboard-stock-follower-status"><a
								href="http://twitter.com/#!/<%=user == null ? "" : user.getUserName()%>" title="<%=user == null ? "" : user.getUserName()%>&#39;s twitter page"
								target="_blank"><%=user == null ? "" : user.getUserName()%></a>'s status
							</td>
						</tr>
					</table>
				</td>
			</tr>
			<tr>
				<td colspan="4">&nbsp;</td>
			</tr>
			<tr class="thead">
				<td style="width: 10%; text-align: center; font-weight: bolder;">Rank</td>
				<td style="width: 20%; text-align: center; font-weight: bolder;">Cash</td>
				<td style="width: 25%; text-align: center; font-weight: bolder;">Portfolio</td>
				<td style="width: 25%; text-align: center; font-weight: bolder;">Total</td>
				
				<td style="width: 20%; text-align: center; font-weight: bolder;"></td>
			</tr>
			<tr>
		
				<td id="userProfileRank" style="width: 10%; text-align: center;"><%=user.getRank()%>.
				</td>
				<td id="userProfileCash" style="width: 20%; text-align: center;">$<%=Util.commaSep(user.getCash())%>
				</td>
				<td id="userProfilePortfolio" style="width: 25%; text-align: center;">$<%=Util.commaSep(user.getPortfolio())%>
		
				</td>
				<td id="userProfileTotal" style="width: 25%; text-align: center;">$<%=Util.commaSep(user.getPortfolio() + user.getCash())%>
				</td>
			    <td id="userProfileProfit" style="width: 20%; text-align: center;">
				
				<%
				if(user.getProfitPerHour()>0){
					out.write("<span class=\"green-profit\">" + Util.getRoundedChangePerHourString(user.getProfitPerHour())+ "</span>");			
				}
				else if(user.getProfitPerHour()<0){
					
					out.write("<span class=\"red-profit\">" + Util.getRoundedChangePerHourString(user.getProfitPerHour())+ "</span>");
				}
				
				%>
				
				
				</td>
		
			</tr>
		</table>

	</div>
	<div id="userportfolio" class="main-div">
		<h3><%=user.getUserName()%>'s Portfolio
		</h3>
		<table class="datatbl">
			<%
			if(portfolio.getStockInPortfolioList().size()>0){
				for (int i = 0; i < portfolio.getStockInPortfolioList().size();) {
			%>
			<tr>
				<%
					for (int j = 0; j < 2; j++) {
								if (i < portfolio.getStockInPortfolioList().size()) {
									StockInPortfolio stock = portfolio.getStockInPortfolioList().get(i);
				%>
	
				<td>
					<table>
						<tr>
							<td><img class="twuser"
								src="<%=stock.getPictureUrl()%>" />
							</td>
							<td><a href='javascript:void(0)' onclick='loadStock(<%=stock.getStockId()%>)' title="<%=stock.getStockName()%>&#39;s stock detail page">
									<%
										out.write(stock.getStockName());
									%>
							</a> <br> $<%
	 							out.write(Util.commaSep(stock.getAmount()) + "&nbsp;("+ Util.getShareString(stock.getPercentage())
								+")"); %>
	
	 <br>
									
										<table class="portfolio-stock-tbl">
										<tr>
											<td align="left">
												<%
									
									double profit = 0;
									
									double amount = stock.getAmount();
									double capital = stock.getCapital();
									
									profit = amount - capital;
									if(profit > 0){
										out.write("<span class=\"green-light\">"+Util.getProfitString(profit) + "</span>"); 
									}
									else if(profit < 0){
										out.write("<span class=\"red-light\">"+Util.getProfitString(profit) + "</span>"); 
									}
	// 								else {
	// 									out.write("<span>$"+Util.getProfitString(profit)+"</span>"); 
	// 								}
									%>
											</td>
											<td align="right">
													<%
													String profitPerHour =  Util.getChangePerHourString(stock.getChangePerHour());
													
													if (stock.getChangePerHour() > 0) {
					
														out.write("<span class=\"green-profit\">" + profitPerHour + "</span>");
													}
													else if (stock.getChangePerHour() < 0){
														out.write("<span class=\"red-profit\">" +  profitPerHour  + "</span>");
														
													}
													%>
											</td>
										</tr>
									</table>
							</td>
						</tr>
					</table></td>
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
			}else{
				out.write(Util.NO_RECORDS_FOUND_HTML);
			}
			%>
		</table>
	</div>
	<%
		} else {
	%>
	<div id="userportfolio">
		<div id="dashboard-message-field" style="margin-top: 42px;"
			class="field-white">User not found!</div>
	</div>
	
	<%
		}
	%>

	<div id="usertweets" class="main-div">
		<h3><%=user.getUserName()%>'s Tweets
		</h3>
		
		<div id="user-tweets-widget-container" class="main-div">
		<script>showTweetsOfUserInDiv('<%=user.getUserName()%>',"user-tweets-widget-container");</script>
		
	</div>
</div>