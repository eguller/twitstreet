<%@page import="com.twitstreet.db.data.RankingData"%>
<%@page import="com.twitstreet.servlet.SeasonServlet"%>
<%@page import="com.twitstreet.season.SeasonResult"%>
<%@page import="com.twitstreet.season.SeasonInfo"%>
<%@page import="com.twitstreet.season.SeasonMgr"%>
<%@page import="com.twitstreet.servlet.HomePageServlet"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.twitstreet.util.GUIUtil"%>
<%@page import="com.twitstreet.servlet.UserProfileServlet"%>
<%@page import="com.twitstreet.servlet.GetUserServlet"%>
<%@page import="com.twitstreet.db.data.StockInPortfolio"%>
<%@page import="com.twitstreet.session.UserMgr"%>
<%@page import="com.twitstreet.db.data.User"%>
<%@ page import="com.google.inject.Injector"%>
<%@ page import="com.google.inject.Guice"%>
<%@ page import="java.util.List"%>
<%@ page import="com.twitstreet.util.Util"%>
<%@ page import="com.twitstreet.market.PortfolioMgr"%>
<%@ page import="com.twitstreet.db.data.Portfolio"%>
<%@ page import="com.twitstreet.localization.LocalizationUtil" %>

<%
Injector inj = (Injector) pageContext.getServletContext().getAttribute(Injector.class.getName());

LocalizationUtil lutil = LocalizationUtil.getInstance();
String lang = (String) request.getSession().getAttribute(LocalizationUtil.LANGUAGE);


UserMgr userMgr= inj.getInstance(UserMgr.class);
SeasonMgr seasonMgr = inj.getInstance(SeasonMgr.class);
SeasonInfo curSeason = seasonMgr.getCurrentSeason();

ArrayList<SeasonInfo> siList = seasonMgr.getAllSeasons();

int seasonId = curSeason.getId()-1 ;



try {
	seasonId = Integer.valueOf(request.getParameter(SeasonServlet.SEASON_ID));
} catch (Exception ex) {

}

SeasonInfo selectedSeason = seasonMgr.getSeasonInfo(seasonId);
SeasonResult sr = seasonMgr.getSeasonResult(seasonId);

%>
<div id="oldseasons" class="main-div" >
	<div align="center">
			<%=lutil.get("season", lang) %>
			<select onchange="reloadIfHashIs('#!seasonresults='+$(this).val());" style="font-size:11px;">
			<% 
			for(SeasonInfo si: siList){
				if(si.isActive() || si.getId()<2)continue;
		    %>	
				<option <%=(si.getId()==selectedSeason.getId())?"selected=\"selected\"":""%> value="<%=si.getId()%>"><%= si.getId() %></option>
								
			    <%		
			}
			%>
			</select>
			<div align="center"><%=selectedSeason.localizedSeasonTime(lang) %></div>	
		</div>
	<table class="datatbl">
		<tbody>
		
		
		<%
		int i =1;
		for(RankingData rd : sr.getRankingHistory()){
			i++;
			User seasonUser = (User)  userMgr.getUserById(rd.getUserId());
		%>
			<tr>
			
				<td style="width:55px;" valign="middle" class="rank-number"><%=rd.getRank()%>.
				</td>
				<td style="width:55px;">
					<img class="twuser" width="48" height="48" 
									src="<%=seasonUser.getPictureUrl()%>" />
				
				
				</td>
				<td>													
					<div align="left">
						<a href="#!user=<%=seasonUser.getId()%>"  onclick="reloadIfHashIsMyHref(this)"  title="<%=lutil.get("user.details.tip", lang, seasonUser.getUserName())%>">
							<%=seasonUser.getUserName()%>
						</a>
		 			</div>	
		 														
					<div align="left">				
						<%=Util.getNumberFormatted(rd.getTotal(), true, true, false, false, false, false)%>					
					</div>	
				</td>
			</tr>
			<tr height="210px">
				<td colspan="3">
					<div id="oldseasons-user<%=seasonUser.getId()%>" style="width:490px; height:210px">
						<% 
						request.setAttribute("selectedSeason", selectedSeason);
						request.setAttribute(SeasonServlet.SEASON_HISTORY_USER, seasonUser); 
						request.setAttribute("chartName",  "seasonUserHistory"+seasonUser.getId()); %>
														
						<jsp:include page="userRankingHistory.jsp"  >					
							<jsp:param value="490" name="width"/>
						</jsp:include>
								
									
					</div>

				
				</td>
			</tr>
			<tr height="40px">
				<td colspan="3">
					
				</td>
			</tr>
			
			
		<%	
		}
		%>
		
		
		</tbody>
	
	
	</table>
	
</div>