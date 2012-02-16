
<%@page import="java.util.HashMap"%>
<%@page import="java.util.HashSet"%>
<%@page import="com.twitstreet.twitter.SimpleTwitterUser"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.twitstreet.db.data.UserStock"%>
<%@page import="java.sql.SQLException"%>
<%@page import="com.twitstreet.market.StockMgr"%>
<%@ page import="com.google.inject.Injector"%>
<%@ page import="com.google.inject.Guice"%>
<%@ page import="com.twitstreet.db.data.User"%>
<%@ page import="com.twitstreet.db.data.Stock"%>
<%@ page import="com.twitstreet.util.Util"%>
<%@page import="com.twitstreet.db.data.Portfolio"%>
<%@page import="com.twitstreet.market.PortfolioMgr"%>
<%@page import="com.twitstreet.config.ConfigMgr"%>
<%@page import="com.twitstreet.session.UserMgr"%>
<%@ page import="com.twitstreet.servlet.HomePageServlet"%>

<%@ page import="com.twitstreet.servlet.HomePageServlet"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.Date"%>
<%@page import="java.util.LinkedHashMap"%>
<%@page import="com.twitstreet.db.data.RankingHistoryData"%>
<%@page import="com.twitstreet.db.data.RankingData"%>
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
	
<div id="user-trend-section" style="display: none;">
	
	<%
	Injector inj = (Injector) pageContext.getServletContext().getAttribute(Injector.class.getName());
	UserMgr userMgr = inj.getInstance(UserMgr.class);
	
	String parameterUser = request.getParameter(User.USER);
	User user = null;
	
	if(parameterUser!=null){
		user = userMgr.getUserById(Long.valueOf(parameterUser));		
	}
	
	RankingHistoryData rhd = null;
	
	if(user!=null){
		
		rhd = userMgr.getRankingHistoryForUser(user.getId());
		
		
	}
	
	if (rhd != null && rhd.getRankingHistory().size() > 0) {
		%>
	
	<h3>Asset History</h3>
	<div id="user-value-chart-div" style="height: 200px; width: 500px;"></div>
	<br>
<!-- 	<h3>Ranking History</h3> -->
<!-- 		<div id="user-trend-chart-div" style="height: 200px; width: 500px;"></div> -->
		<script type="text/javascript">
			var dateArray = new Array();
			var valueArray = new Array();
			var rankArray = new Array();
			var userName = '<%=user.getUserName()%>';
		<%
		double totalNow = user.getCash()+ user.getPortfolio();
		Date date = new Date();
		out.write("dateArray.push(new Date(" + date.getTime()+ "));\n");

		out.write("rankArray.push(" + user.getRank() + ");\n");
		
		out.write("valueArray.push("  + totalNow  + ");\n");
		
		for(RankingData rd : rhd.getRankingHistory()){
				
					
					out.write("dateArray.push(new Date(" + rd.getLastUpdate().getTime()+ "));\n");

					out.write("rankArray.push(" + rd.getRank() + ");\n");
					
					out.write("valueArray.push(" + rd.getTotal() + ");\n");
					
				
					
		}		
					
					%>
			//drawUserRankingHistory('#user-trend-chart-div', dateArray, rankArray, userName);
			drawUserValueHistory('#user-value-chart-div', dateArray, valueArray, userName);
		</script>

	

	<%
	}else if(user!=null){%>
	
	
		
		<div>
		<p>
		No history data available for <%=user.getUserName()%> yet.
		</p>
		</div>
	
	
	<%			
	}
	%>
		
</div>
	