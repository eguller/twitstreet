<%@page import="com.twitstreet.localization.LocalizationUtil"%>
<%@ page import="com.google.inject.Injector"%>
<%@ page import="com.twitstreet.db.data.User"%>
<%@page import="com.twitstreet.session.UserMgr"%>
<%@ page import="com.twitstreet.servlet.UserProfileServlet"%>

<%@page import="java.util.Date"%>
<%@page import="com.twitstreet.db.data.RankingHistoryData"%>
<%@page import="com.twitstreet.db.data.RankingData"%>
<%@ page import="com.google.inject.Injector"%>
<%@ page import="com.twitstreet.db.data.User"%>
<%@page import="com.twitstreet.session.UserMgr"%>
<%@ page import="java.util.List"%>
	
<div id="user-trend-section">
	<%
	LocalizationUtil lutil = LocalizationUtil.getInstance();
	String lang = (String)request.getSession().getAttribute(LocalizationUtil.LANGUAGE);

	Injector inj = (Injector) pageContext.getServletContext().getAttribute(Injector.class.getName());
	UserMgr userMgr = inj.getInstance(UserMgr.class);
	String parameterUser = request.getParameter(User.USER);
	User user = null;
	user = (user == null) ? (User) request.getAttribute(UserProfileServlet.USER_PROFILE_USER) : user;
	user = (user == null && parameterUser != null) ? userMgr.getUserById(Long.valueOf(parameterUser)) : user;
	request.setAttribute(UserProfileServlet.USER_PROFILE_USER, user);
	RankingHistoryData rhd = null;
	if(user!=null){
		rhd = userMgr.getRankingHistoryForUser(user.getId(),null);
	}
	
	if (rhd != null && rhd.getRankingHistory().size() > 0) {
		%>
	
<!-- 	<h3>Asset History</h3> -->
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
			drawUserValueHistory('#user-value-chart-div', dateArray, valueArray, userName);
		</script>
	<%
	}else if(user!=null){%>
		<div>
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