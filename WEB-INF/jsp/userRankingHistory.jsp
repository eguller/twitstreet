<%@page import="java.util.ArrayList"%>
<%@page import="com.twitstreet.localization.LocalizationUtil"%>
<%@ page import="com.google.inject.Injector"%>
<%@ page import="com.twitstreet.db.data.User"%>
<%@page import="com.twitstreet.session.UserMgr"%>
<%@ page import="com.twitstreet.servlet.UserProfileServlet"%>
<%@ page import="com.twitstreet.main.*"%>

<%@page import="java.util.Date"%>
<%@page import="com.twitstreet.db.data.RankingHistoryData"%>
<%@page import="com.twitstreet.db.data.RankingData"%>
<%@ page import="com.google.inject.Injector"%>
<%@ page import="com.twitstreet.db.data.User"%>
<%@page import="com.twitstreet.session.UserMgr"%>
<%@ page import="java.util.List"%>
	
<div id="userRankingHistoryId">
	<%
	LocalizationUtil lutil = LocalizationUtil.getInstance();
	String lang = (String)request.getSession().getAttribute(LocalizationUtil.LANGUAGE);

	Injector inj = (Injector) pageContext.getServletContext().getAttribute(Injector.class.getName());
	UserMgr userMgr = inj.getInstance(UserMgr.class);
	Twitstreet twitstreet = inj.getInstance(Twitstreet.class);
	String parameterUser = request.getParameter(User.USER);
	User user = null;
	user = (user == null) ? (User) request.getAttribute(UserProfileServlet.USER_PROFILE_USER) : user;
	user = (user == null && parameterUser != null) ? userMgr.getUserById(Long.valueOf(parameterUser)) : user;
	request.setAttribute(UserProfileServlet.USER_PROFILE_USER, user);
	RankingHistoryData rhd = null;
	
	ArrayList<SeasonInfo> siList = twitstreet.getAllSeasons();
	SeasonInfo selectedSeason = (SeasonInfo) request.getAttribute("selectedSeason");
	SeasonInfo currentSeason = twitstreet.getCurrentSeason();
	selectedSeason = (selectedSeason!=null)? selectedSeason:twitstreet.getCurrentSeason();
	int id = -1;
	if(selectedSeason!=null){
		id = selectedSeason.getId();
	}
	
	
	
	if(user!=null && selectedSeason!=null){
		rhd = userMgr.getRankingHistoryForUser(user.getId(),selectedSeason.getStartTime(),selectedSeason.getEndTime());
	}
	
	if (rhd != null && rhd.getRankingHistory().size() > 0) {
		if(selectedSeason.getId() == currentSeason.getId()){
			RankingData rd = new RankingData();
	
			double totalNow = user.getCash()+ user.getPortfolio();
			Date date = new Date();
			
			rd.setCash(user.getCash());
			rd.setPortfolio(user.getPortfolio());
			rd.setLastUpdate(date);
			rd.setRank(user.getRank());
			rd.setTotal(totalNow);

			rhd.getRankingHistory().add(rd);
		}
		%>
		
	
		
		<div id="user-value-chart-div" style="height: 200px; width: 500px;"></div>
		<br>
		<script type="text/javascript">
			var dateArray = new Array();
			var valueArray = new Array();
			var rankArray = new Array();
			var userName = '<%=user.getUserName()%>';
			
			//Global variable
			rankTitle = '<%=lutil.get("balance.rank", lang)%>';
		<%
		for(RankingData rd : rhd.getRankingHistory()){
					out.write("dateArray.push(new Date(" + rd.getLastUpdate().getTime()+ "));\n");
					out.write("rankArray.push(" + rd.getRank() + ");\n");
					out.write("valueArray.push(" + rd.getTotal() + ");\n");
		}		
					%>
			drawUserValueHistory('#user-value-chart-div', dateArray, valueArray,rankArray, userName);
		</script>
	<%
	}else if(user!=null){%>
		<div align="center">
		<p>
		<%=lutil.get("user.noHistory", lang,user.getUserName()) %>
		</p>
		</div>
	<%			
	}
	%>
	<div>
		<h3><%=lutil.get("transactions.header", lang) %></h3>
		<div>
			<jsp:include page="userTransactionsContent.jsp">
				<jsp:param value="<%=user.getId()%>" name="user-id"/>
			</jsp:include>
		</div>
	</div>
</div>