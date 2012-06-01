
<%@page import="com.twitstreet.servlet.GetUserServlet"%>
<%@page import="com.twitstreet.servlet.SeasonServlet"%>
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
	
	String getUserText = request.getAttribute(GetUserServlet.GET_USER_TEXT) == null ? "" : (String) request.getAttribute(GetUserServlet.GET_USER_TEXT);
	String getUserTextDisplay = request.getAttribute(GetUserServlet.GET_USER_DISPLAY) == null ? "" : (String) request.getAttribute(GetUserServlet.GET_USER_DISPLAY);
	
	
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
	
	<div class="flex-module clearfix ">

  <a target="_blank" class="profile-picture" href="https://si0.twimg.com/profile_images/1297462270/203076_731581199_1167659_n.jpg">
    <img class="avatar size128" alt="Hüseyin Tüfekçilerli" src="https://si0.twimg.com/profile_images/1297462270/203076_731581199_1167659_n_reasonably_small.jpg">
  </a>
  <div data-user-id="5816772" data-screen-name="huseyint" class="profile-card-inner js-actionable-user">
    <h1 class="fullname">
      Hüseyin Tüfekçilerli
      
    </h1>
    <h2 class="username">
      <span class="js-username"><span class="screen-name"><s>@</s>huseyint</span></span>
      
    </h2>
    <p class="bio ">All my twits are provided AS IS without warranty of any kind. Developing <a rel="nofollow" href="/#!/Netsparker" data-screen-name="Netsparker" class="  twitter-atreply pretty-link"><s>@</s><b>Netsparker</b></a> at Mavituna Security.</p>
    <p class="location-and-url">
      <span class="location">
        Istanbul, Türkiye
      </span>
          <span class="divider">·</span>
      <span class="url">
        <a href="http://about.me/huseyin" rel="me nofollow" target="_blank">
          http://about.me/huseyin
        </a>
      </span>
    </p>
  </div>
  <div class="profile-card-actions js-profile-card-actions">
    <div class="follow-btn-container">
      
        
    
    <div data-screen-name="huseyint" data-user-id="5816772" class="js-follow-combo follow-combo btn-group js-actionable-user not-following" data-dropdown="true">
  <a class="follow-btn btn js-combo-btn js-recommended-item">
    <div data-user-id="5816772" class="js-action-follow follow-text action-text">
      <i class="follow"></i>
      Follow
    </div>
    <div data-user-id="5816772" class="js-action-unfollow following-text action-text">
      Following
    </div>
    <div data-user-id="5816772" class="js-action-unfollow unfollow-text action-text">
      Unfollow
    </div>
    <div class="block-text action-text">
      Blocked
    </div>
    <div data-user-id="5816772" class="js-action-unblock unblock-text action-text">
      Unblock
    </div>
    <div data-user-id="5816772" class="js-action-unfollow pending-text action-text">
      Pending
    </div>
    <div data-user-id="5816772" class="js-action-unfollow cancel-req-text action-text">
      Cancel
    </div>
  </a>
</div>    </div>
    <ul class="stats js-mini-profile-stats">
   <li><a data-nav="profile" data-element-term="tweet_stats" href="/#!/huseyint">
      
        
            <strong>4,028</strong> Tweets
          
    </a></li>
    <li><a data-nav="following" data-element-term="following_stats" href="/#!/huseyint/following"><strong>339</strong> Following</a></li>
    <li><a data-nav="followers" data-element-term="follower_stats" href="/#!/huseyint/followers"><strong>332</strong> Followers</a></li>
</ul>
  </div>
</div>
	

	<div id="user-profile-menu" class="subheader" >

		<table class="datatbl">
			<tr>
				<td width="55" align="center" style="vertical-align: top" >
		
					<a style="float:left;vertical-align: top"  href="http://twitter.com/#!/<%=user.getUserName()%>"
						title="<%=lutil.get("twitter.link.tip", lang, user.getUserName())%>"
						target="_blank"><%=user.getUserName()%></a>
					
						
					<img class="twuser" width="128" height="128" 
					src="<%=user == null ? "" : user.getAvatarUrl()%>"
					id="dashboard-picture">
				</td>
				
							
				<td align="center"   style="vertical-align: top">
					<div style="text-align: left; vertical-align: top">
				
						<div style="float:left; overflow: hidden; width:280px">
						
							<table class="datatbl" style="text-align: center;">
								
								<tr>
									<td width="50%">
										<b><%=lutil.get("season.thisseason", lang) %></b>
									</td>
									<td width="50%">
										<b><%=lutil.get("season.alltime", lang) %></b>
									</td>
								</tr>
									
								<tr>
									<td width="50%">
										<%=user.getRank()%>.
									</td>
									<td width="50%">
										<%=user.getRankCumulative()%>.
									</td>
								</tr>
									
								<tr>
									<td width="50%">
										<%=Util.getNumberFormatted(user.getTotal(), true, true, false, false, false, false)%> 
										 <%= (user.getProfit()!=0)? "("+Util.getNumberFormatted(user.getProfit(), true, true, true, true, false, true)+")":"" %>
									</td>
									<td width="50%">
										<%=Util.getNumberFormatted(user.getValueCumulative(), true, true, false, false, false, false)%> 
										 
									</td>
								</tr>
						
									
							
							</table>		
						
					
					
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
												ArrayList<Stock> watchList = portfolioMgr.getUserWatchList(sessionUser.getId());
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
			
		<div id="userRankingHistoryId">	
		<% 
					request.setAttribute("selectedSeason", selectedSeason);
					request.setAttribute(SeasonServlet.SEASON_HISTORY_USER, user); 
					request.setAttribute("chartName",  "userHistory"+user.getId()); %>
							
			<jsp:include page="userRankingHistory.jsp" >
		
				<jsp:param value="500" name="width"/>
			</jsp:include>
		</div>
		<div>
			<h3><%=lutil.get("transactions.header", lang) %></h3>
			<div>
				<jsp:include page="userTransactionsContent.jsp">
					<jsp:param value="<%=user.getId()%>" name="user-id"/>
				</jsp:include>
			</div>
		</div>
		
		<%
			}
		%>
		
		
		
	</div>
	
	
	
	<% if(user == null && getUserText.length()>0) { %>
		<div id="searchnoresult"><p style="text-align: center;"><%=lutil.get("shared.noresults", lang) %></p></div>
	<% } %>
	
</div>
