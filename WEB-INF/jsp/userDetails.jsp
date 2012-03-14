<%@page import="com.twitstreet.util.GUIUtil"%>
<%@page import="com.twitstreet.servlet.UserProfileServlet"%>
<%@page import="com.twitstreet.db.data.StockInPortfolio"%>
<%@page import="com.twitstreet.session.UserMgr"%>
<%@page import="com.twitstreet.market.StockMgr"%>
<%@ page import="com.twitstreet.db.data.Stock"%>
<%@page import="com.twitstreet.db.data.User"%>
<%@ page import="com.google.inject.Injector"%>
<%@ page import="com.google.inject.Guice"%>
<%@ page import="java.util.List"%>
<%@ page import="com.twitstreet.util.Util"%>
<%@ page import="com.twitstreet.market.PortfolioMgr"%>
<%@ page import="com.twitstreet.db.data.Portfolio"%>
<%@page import="java.util.ArrayList"%>
<%@ page import="com.twitstreet.localization.LocalizationUtil" %>

<%
LocalizationUtil lutil = LocalizationUtil.getInstance();
String lang = (String)request.getSession().getAttribute(LocalizationUtil.LANGUAGE);

	Injector inj = (Injector) pageContext.getServletContext()
			.getAttribute(Injector.class.getName());
	UserMgr userMgr = inj.getInstance(UserMgr.class);
	PortfolioMgr portfolioMgr = inj.getInstance(PortfolioMgr.class);
	Portfolio portfolio = null;
	
	String parameterUser = request.getParameter(User.USER);
	User user = null;
	
	StockMgr stockMgr = inj.getInstance(StockMgr.class);
	User sessionUser = (User) request.getAttribute(User.USER);
	 
	user = (user == null) ? (User) request.getAttribute(UserProfileServlet.USER_PROFILE_USER) : user;
	user = (user == null && parameterUser != null) ? userMgr.getUserById(Long.valueOf(parameterUser)) : user;
	request.setAttribute(UserProfileServlet.USER_PROFILE_USER, user);
			
	
%>

			
<div id="userdetails" class="main-div">
	<%
	if (user != null) {
		
		portfolio = portfolioMgr.getUserPortfolio(user);
	%>
	<input id="hiddenUserDetailsUserId" type="hidden" value="<%=user.getId() %>"/>
	
	<table class="datatbl">
			<tr>
				<td>
					<div class="h3 big"  style="vertical-align: top">
						<a style="vertical-align: top"  href="http://twitter.com/#!/<%=user.getUserName()%>"
							title="<%=lutil.get("twitter.link.tip", lang, user.getUserName())%>"
						target="_blank"><%=user.getUserName()%></a>
						&nbsp;&nbsp;&nbsp;
						
						<%=GUIUtil.getInstance().getTwitterShareButton("#!user="+ user.getId(), "twitter.share.user", lang, user.getUserName())%>
						<%=GUIUtil.getInstance().getTwitterFollowButton(user.getUserName(), lang)%>
						
					</div>					
				</td>
				
			</tr>
			
		</table>
		
		
	<div id="user-profile-menu" class="subheader main-div" style="height:54px">

		<table class="datatbl">
			<tr>
				<td>
					<img class="twuser" width="48" height="48" 
					src="<%=user == null ? "" : user.getPictureUrl()%>"
					id="dashboard-picture"></td>
				<td>
					<table>
						<tr>
							
							<td colspan="2" align="left" style="padding: 2px">
							<%=user.getRank()%>.
							</td>
						</tr>
					
						<tr>
							
							<td align="left" style="padding: 2px">
								<%= Util.getRoundedMoneyString(user.getCash()+user.getPortfolio()) %>
							</td>
						
							<td align="right" style="padding: 2px">
							 	 <%= (user.getProfit()!=0)? " | "+Util.getNumberFormatted(user.getProfit(), true, true, true, true, false, true):"" %>
							</td>
						</tr>
						
					</table>
				</td>
				<td style="vertical-align: bottom;">
					<div class="tabs">
						<a id="user-status-tab" class="youarehere" onClick="showUserProfileTab('#user-status-tab','#userstatus');">
							<%=lutil.get("userdetails.portfolio", lang) %></a> 
					
						<a id="user-ranking-history-tab"
							onClick="showUserProfileTab('#user-ranking-history-tab','#user-trend-section');redrawUserRankingHistory();">
							<%=lutil.get("userdetails.history", lang)%> </a> 
						<a id="user-tweets-tab"
							onClick="showUserProfileTab('#user-tweets-tab','#usertweets');">
							<%=lutil.get("userdetails.tweets", lang)%> </a> 
			
					</div>
				</td>
			</tr>
		</table>
		
	</div>
	

	<div id="userstatus" class="main-div">
		
		<div id="userPortfolio" class="main-div">
			<h3>
				<%=lutil.get("userdetails.portfolio", lang) %>
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
							<tr onmouseover="$('.user-portfolio-item-watch-div-<%=stock.getStockId() %>').show()" onmouseout="$('.user-portfolio-item-watch-div-<%=stock.getStockId() %>').hide()">
								<td width="58px"><img class="twuser" width="48" height="48" 
									src="<%=stock.getPictureUrl()%>" />
								</td>
								<td>								
									<div style="width:170px">
										<a href='#!stock=<%=stock.getStockId()%>'  onclick="reloadIfHashIsMyHref(this)"  title="<%=lutil.get("stock.details.tip", lang, stock.getStockName())%>">
												<%=stock.getStockName()%>
										
											
										</a> (<%=Util.getShareString(stock.getPercentage())%>)
										
											<%=(stock.getStockLongName()!=null)?"<br><span class='gray-small'>"+stock.getStockLongName()+"</span>":""%>
						 
											<%
												if(sessionUser!=null){
											 %>
												<div class="user-portfolio-item-watch-div-<%=stock.getStockId() %>" style="display:none; float:right; ">
					
												<%
												ArrayList<Stock> watchList = stockMgr.getUserWatchList(sessionUser.getId());
												boolean beingWatched = watchList.contains(stock);
												 %>
												<a class="add-to-watch-list-link-<%=stock.getStockId() %>" style="<%out.write((beingWatched)?"display:none":""); %>" href="javascript:void(0)" onclick="addToWatchList(<%=stock.getStockId()%>)">
													<%=Util.getWatchListIcon(true,15,lutil.get("watchlist.add", lang))%>
													
												</a>	
												<a class="remove-from-watch-list-link-<%=stock.getStockId() %>" style="<%=(!beingWatched)?"display:none":"" %>" href="javascript:void(0)" onclick="removeFromWatchList(<%=stock.getStockId()%>)">
													<%=Util.getWatchListIcon(false,15,lutil.get("watchlist.remove", lang))%>
													
												</a>	
												</div>
											<%
												}
											 %>
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
				}else{%>
				<tr><td align="center">
				<%
					out.write(lutil.get("shared.empty", lang));
				 %>
				 </td></tr>
				 
				 
				 <%}
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