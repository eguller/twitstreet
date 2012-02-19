<%@page import="com.twitstreet.servlet.UserProfileServlet"%>
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
	
	String parameterUser = request.getParameter(User.USER);
	User user = null;
	
	user = (user == null) ? (User) request.getAttribute(UserProfileServlet.USER_PROFILE_USER) : user;
	user = (user == null && parameterUser != null) ? userMgr.getUserById(Long.valueOf(parameterUser)) : user;
	request.setAttribute(UserProfileServlet.USER_PROFILE_USER, user);
			
	
%>

<div id="userdetails">
	<%
	if (user != null) {
		
		portfolio = portfolioMgr.getUserPortfolio(user);
	%>
	<div id="user-profile-menu" class="subheader">

		<table class="datatbl">
			<tr>
				<td>
					<img class="twuser"
					src="<%=user == null ? "" : user.getPictureUrl()%>"
					id="dashboard-picture"></td>
				<td>
					<h1>
						<a href="http://twitter.com/#!/<%=user == null ? "" : user.getUserName()%>"
							title="<%=user == null ? "" : user.getUserName()%>&#39;s twitter page"
							target="_blank"><%=user == null ? "" : user.getUserName()%></a>
					</h1>
				</td>
				<td style="vertical-align: bottom;">
					<div class="tabs">
						<a id="user-status-tab" class="youarehere" onClick="showUserProfileTab('#user-status-tab','#userstatus');">
							Balance & Portfolio</a> 
					
						<a id="user-ranking-history-tab"
							onClick="showUserProfileTab('#user-ranking-history-tab','#user-trend-section');redrawUserRankingHistory();">
							History </a> 
						<a id="user-tweets-tab"
							onClick="showUserProfileTab('#user-tweets-tab','#usertweets');">
							Tweets </a> 
			
					</div>
				</td>
			</tr>
		</table>
		
	</div>
	

	<div id="userstatus" class="main-div">
		<div id="userbalance" class="main-div">
			<h3>Balance
			</h3>
<!-- 			<div class="field-white"> -->
				<table class="datatbl">

					<tr>
						<td style="width: 15%; text-align: center; font-weight: bolder;">Rank</td>
						<td style="width: 25%; text-align: center; font-weight: bolder;">Cash</td>
						<td style="width: 30%; text-align: center; font-weight: bolder;">Portfolio</td>
						<td style="width: 30%; text-align: center; font-weight: bolder;">Total</td>
						
<!-- 						<td style="width: 20%; text-align: center; font-weight: bolder;"></td> -->
					</tr>
					<tr>
				
						<td id="userProfileRank" style="width: 15%; text-align: center;"><%=user.getRank()%>.
						</td>
						<td id="userProfileCash" style="width: 25%; text-align: center;">$<%=Util.commaSep(user.getCash())%>
						</td>
						<td id="userProfilePortfolio" style="width: 30%; text-align: center;">$<%=Util.commaSep(user.getPortfolio())%>
				
						</td>
						<td id="userProfileTotal" style="width: 30%; text-align: center;">$<%=Util.commaSep(user.getPortfolio() + user.getCash())%>
						
						&nbsp;	<% 
						if(user.getProfit()>0){
							out.write("<span class=\"green-profit\">" + Util.getRoundedChangePerHourString(user.getProfit())+ "</span>");			
						}
						else if(user.getProfit()<0){
							
							out.write("<span class=\"red-profit\">" + Util.getRoundedChangePerHourString(user.getProfit())+ "</span>");
						}
						
						%>
						</td>
<%--				    <td id="userProfileProfit" style="width: 20%; text-align: center;">
						
 					
						
						
						</td>--%>
				
					</tr>
				</table>
<!-- 			</div> -->
		</div>
		
		<div id="userPortfolio" class="main-div">
			<h3>
			Portfolio
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
						<table class="datatbl">
							<tr>
								<td width="58px"><img class="twuser"
									src="<%=stock.getPictureUrl()%>" />
								</td>
								<td><a href='javascript:void(0)' onclick='loadStock(<%=stock.getStockId()%>)' title="<%=stock.getStockName()%>&#39;s stock detail page">
										<%
											out.write(stock.getStockName());
										%>
								</a> (<%=Util.getShareString(stock.getPercentage())%>)<br> $<%
		 							out.write(Util.commaSep(stock.getAmount())); %>
		
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
												<td align="left">
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
				}else{
					out.write(Util.NO_RECORDS_FOUND_HTML);
				}
				%>
			</table>
		</div>
	</div>

	<div id="usertweets" class="main-div"">
	
		
		<div id="user-tweets-widget-container" class="main-div">
		<script>showTweetsOfUserInDiv('<%=user.getUserName()%>',"user-tweets-widget-container");</script>
		</div>
	</div>
	
	<jsp:include page="userRankingHistory.jsp" />
	<%
		}
	%>
	
</div>