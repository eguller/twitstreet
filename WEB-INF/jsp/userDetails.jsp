
<%@page import="com.twitstreet.main.Twitstreet"%>
<%@page import="com.twitstreet.season.SeasonInfo"%>
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
<%@ page import="java.util.ArrayList"%>
<%@ page import="com.twitstreet.localization.LocalizationUtil" %>
<%@ page import="com.twitstreet.season.SeasonMgr" %>

<%
LocalizationUtil lutil = LocalizationUtil.getInstance();
String lang = (String)request.getSession().getAttribute(LocalizationUtil.LANGUAGE);

	Injector inj = (Injector) pageContext.getServletContext()
			.getAttribute(Injector.class.getName());
	UserMgr userMgr = inj.getInstance(UserMgr.class);
	PortfolioMgr portfolioMgr = inj.getInstance(PortfolioMgr.class);
	Portfolio portfolio = null;

	SeasonMgr seasonMgr = inj.getInstance(SeasonMgr.class);
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
	

	<div id="user-profile-menu" class="subheader" >

		<table class="datatbl">
			<tr>
				<td width="55" style="vertical-align: top" >
					<img class="twuser" width="48" height="48" 
					src="<%=user == null ? "" : user.getPictureUrl()%>"
					id="dashboard-picture">
				</td>
				
							
				<td  style="vertical-align: top">
					<div style="text-align: left; vertical-align: top">
				
						<div style="float:left; overflow: hidden">
						
							<a style="float:left;vertical-align: top"  href="http://twitter.com/#!/<%=user.getUserName()%>"
								title="<%=lutil.get("twitter.link.tip", lang, user.getUserName())%>"
								target="_blank"><%=user.getUserName()%></a>
							
						
									
							<br>
							<%=user.getRank()%>.
							<br>
					
					
						
							<%= Util.getRoundedMoneyString(user.getCash()+user.getPortfolio()) %>
						 	 <%= (user.getProfit()!=0)? " | "+Util.getNumberFormatted(user.getProfit(), true, true, true, true, false, true):"" %>
							<br>
					
					
						</div>
						<div style="float:right">
								<div style="float:right">
									<%=GUIUtil.getInstance().getTwitterShareButton("#!user="+ user.getId(), "twitter.share.user", lang, user.getUserName())%>
									<%=GUIUtil.getInstance().getTwitterFollowButton(user.getUserName(), lang)%>
								</div>
							</div>
					</div>
					<br>					
					<div style="float:left" class="gray-small"><%=(user.getDescription()!=null)?user.getDescription():""%></div>
					<br>
					<div style="float:right">
						<% if(user.getLocation() != null){ %>
							<a href="http://maps.google.com/maps?q=<%=user.getLocation() %>" target="_blank"><%=user.getLocation()%></a>
						<% } %>
					</div>
				</td>
				
			</tr>
		</table>
		<table class="datatbl">
			<tr>
				<td style="font-size: 15px">
							
					
					<% if(user.isProfitCalculated() && user.getProfit()!=0){ 
					%>
						<%=Util.getPercentageFormatted((double) user.getProfit() / (user.getCash()+user.getPortfolio()), false, true, true, true, false, true)  %>
					<% }
					%>	
					
							
				</td>
				<td style="vertical-align: bottom;">
					<div class="tabs">
						<a id="user-status-tab" class="youarehere" onClick="showUserProfileTab('#user-status-tab','#userstatus');">
							<%=lutil.get("userdetails.portfolio", lang) %></a> 
					
						<a id="user-history-tab"
							onClick="showUserProfileTab('#user-history-tab','#user-trend-section');redrawUserRankingHistory();">
							<%=lutil.get("userdetails.history", lang)%> </a> 
						<a id="user-tweets-tab"
							onClick="showUserProfileTab('#user-tweets-tab','#usertweets');">
							<%=lutil.get("userdetails.tweets", lang)%> </a> 
			
					</div>
				</td>
			</tr>
		</table>
	</div>
	
	<br>
					

	<div id="userstatus" class="main-div">
		
		<div id="userPortfolio" class="main-div">
<!-- 			<h3> -->
<%-- 				<%=lutil.get("userdetails.portfolio", lang) %> --%>
<!-- 			</h3> -->
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
	

	<div id="user-trend-section">	
		<%
	
		ArrayList<SeasonInfo> siList = seasonMgr.getAllSeasons();
		SeasonInfo selectedSeason = seasonMgr.getCurrentSeason();
	
		
		%>
		<div align="center">
			<%=lutil.get("season", lang) %>
			<select onchange="loadUserHistory(<%=user.getId() %>,$(this).val())" style="font-size:11px;">
			<% 
			for(SeasonInfo si: siList){
		    %>	
				<option <%=(si.getId()==selectedSeason.getId())?"selected=\"selected\"":""%> value="<%=si.getId()%>"><%= si.getId() %></option>
								
			    <%		
			}
			%>
			</select>	
		</div>
			
			
		<jsp:include page="userRankingHistory.jsp" />
			
		
		<%
			}
		%>
	</div>
</div>
