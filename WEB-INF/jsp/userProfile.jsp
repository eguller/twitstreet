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
<input id="userProfileUserId" type="hidden" value="<%=userIdStr%>"></input>
<div id="userprofile" class="main-div">
	<h3><%=user.getUserName()%>'s Profile <a
			style="float: right; vertical-align: bottom;" href="/">Go to Home
			&gt;&gt;</a>
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
				out.write("<span class=\"green-profit\">$" + Util.commaSep(user.getProfitPerHour())+"/h &#9650" + "</span>");			
			}
			else if(user.getProfitPerHour()<0){
				
				out.write("<span class=\"red-profit\">$" + Util.commaSep(user.getProfitPerHour())+"/h &#9660" + "</span>");
			}
			
			%>
			
			
			</td>
	
		</tr>
	</table>

</div>
<div id="userportfolio" style="margin-top: 15px;">
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
							src="<%=portfolio.getStockInPortfolioList()
									.get(i).getPictureUrl()%>" />
						</td>
						<td><a
							href='/?stock=<%=portfolio.getStockInPortfolioList()
									.get(i).getStockId()%>' title="<%=portfolio.getStockInPortfolioList().get(i).getStockName()%>&#39;s stock detail page">
								<%
									out.write(portfolio.getStockInPortfolioList()
															.get(i).getStockName());
								%>
						</a> <br> $<%
 							out.write(Util.commaSep(portfolio
			 							.getStockInPortfolioList().get(i)
			 							.getAmount()) + "&nbsp;("+													
										Util.commaSep(
												(100*(Util.roundDouble(stock.getPercentage(),4))
														))
							+"%)"); %>

 <br>
								
									<table class="portfolio-stock-tbl">
									<tr>
										<td align="left">
											<%
								
								double profit = 0;
								
								double amount = portfolio.getStockInPortfolioList().get(i).getAmount();
								double capital = portfolio.getStockInPortfolioList().get(i).getCapital();
								
								profit = amount - capital;
								if(profit > 0){
									out.write("<span class=\"green-light\">$"+Util.commaSep(profit) + "&nbsp; &#9650;</span>"); 
								}
								else if(profit < 0){
									out.write("<span class=\"red-light\">$"+Util.commaSep(profit) + "&nbsp; &#9660;</span>"); 
								}
								else {
									out.write("<span>$"+Util.commaSep(profit)+"</span>"); 
								}
								%>
										</td>
										<td align="right">
												<%
												String profitPerHour = "$";
												profitPerHour = profitPerHour +  Util.roundDouble(stock.getChangePerHour(),2);
												
												if (stock.getChangePerHour() > 0) {
				
													profitPerHour = profitPerHour + "/h &#9650;";
													
													out.write("<span class=\"green-profit\">" + profitPerHour + "</span>");
												}
												else if (user.getProfitPerHour() < 0){
													profitPerHour = profitPerHour + "/h &#9660;";
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