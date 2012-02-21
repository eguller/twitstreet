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

<div id="userdetails" class="main-div">
	<%
	if (user != null) {
		
		portfolio = portfolioMgr.getUserPortfolio(user);
	%>
	<div id="user-profile-menu" class="subheader main-div" style="height:54px">

		<table class="datatbl">
			<tr>
				<td>
					<img class="twuser"
					src="<%=user == null ? "" : user.getPictureUrl()%>"
					id="dashboard-picture"></td>
				<td>
					<table>
						<tr>
							
							<td align="left" style="padding: 2px">
							<%=user.getRank()%>.
							</td>
						</tr>
					
						<tr>
							
							<td align="left" style="padding: 2px">
								<%= Util.getRoundedMoneyString(user.getCash()+user.getPortfolio()) %> <%= (user.getProfit()!=0)? Util.getNumberFormatted(user.getProfit(), true, true, true, true, false, true):"" %>
							</td>
						</tr>
						
					</table>
				</td>
				<td style="vertical-align: bottom;">
					<div class="tabs">
						<a id="user-status-tab" class="youarehere" onClick="showUserProfileTab('#user-status-tab','#userstatus');">
							Portfolio</a> 
					
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
								<td>								
									<div style="width:170px">
										<a href='javascript:void(0)' onclick='loadStock(<%=stock.getStockId()%>)' title="<%=stock.getStockName()%>&#39;s stock detail page">
												<%=stock.getStockName()%>
										</a> (<%=Util.getShareString(stock.getPercentage())%>)
										<br> 
											<%=Util.getNumberFormatted(stock.getAmount(), true, true, false, false, false, false)%>
		
										<br>
								
										<table class="portfolio-stock-tbl">
											<tr>
												<td align="left">
													<%=Util.getNumberFormatted(stock.getAmount()-stock.getCapital(), true, true, false, false, true, false)%>
												</td>
												<td align="right">
														<%=Util.getNumberFormatted(stock.getChangePerHour(), true, true, true, true, false, true)%>
												</td>
											</tr>
										</table>
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