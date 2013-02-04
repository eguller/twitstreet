
<%@page import="com.twitstreet.season.SeasonInfo"%>
<%@page import="com.twitstreet.season.SeasonMgr"%>
<%@page import="java.util.Calendar"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.text.DateFormat"%>
<%@page import="com.twitstreet.db.data.RankingData"%>
<%@page import="com.twitstreet.db.data.RankingHistoryData"%>
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
<%@page import="com.twitstreet.db.data.StockHistoryData"%>
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
<%@ page import="com.twitstreet.localization.LocalizationUtil" %>
<%@page import="com.twitstreet.util.GUIUtil"%>
<%@page import="org.apache.log4j.Logger" %>
<%
	long start = 0;
	long end = 0;
	start = System.currentTimeMillis();
	Logger logger = Logger.getLogger(this.getClass());
	
	Injector inj = (Injector) pageContext.getServletContext().getAttribute(Injector.class.getName());
	StockMgr stockMgr = inj.getInstance(StockMgr.class);
	UserMgr userMgr = inj.getInstance(UserMgr.class);
	SeasonMgr seasonMgr = inj.getInstance(SeasonMgr.class);
	User user = (User) request.getAttribute(User.USER);
	
	String divId = request.getParameter("divId");
	String format = request.getParameter("format");
	User chartUser = (User ) request.getAttribute("chartUser");
	
	LocalizationUtil lutil = LocalizationUtil.getInstance();
	String lang = (String)request.getSession().getAttribute(LocalizationUtil.LANGUAGE);
	%>


	<script type="text/javascript">
		var dateArrayStock<%=chartUser.getId()%> = new Array();
		var valueArrayStock<%=chartUser.getId()%> = new Array();
		var stockNameStock<%=chartUser.getId()%> = '<%=chartUser.getUserName()%>' ;
		<%
	
	
			RankingHistoryData shd = null;
		
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		Calendar cal = Calendar.getInstance();
		
		cal.add(Calendar.HOUR_OF_DAY, -6);
		
		Date date = cal.getTime();
		SeasonInfo currentSeason = seasonMgr.getCurrentSeason();
		if(date.getTime() < currentSeason.getStartTime().getTime() ){
			date.setTime(currentSeason.getStartTime().getTime());
		}
	
			shd = userMgr.getRankingHistoryForUser(chartUser.getId(),dateFormat.format(date),null);

			out.write("dateArrayStock"+chartUser.getId()+".push(new Date());\n");

			out.write("valueArrayStock"+chartUser.getId()+".push(" + ((double) chartUser.getPortfolio()+chartUser.getCash()) + ");\n");

			for (RankingData rd : shd.getRankingHistory()) {
				out.write("dateArrayStock"+chartUser.getId()+".push(new Date(" + rd.getLastUpdate().getTime() + "));\n");

				out.write("valueArrayStock"+chartUser.getId()+".push(" + rd.getTotal() + ");\n");
		}%>
		drawStockHistory('<%=divId+chartUser.getId()%>', dateArrayStock<%=chartUser.getId()%>, valueArrayStock<%=chartUser.getId() %>,
				stockNameStock<%=chartUser.getId()%>,'<%=format%>', 'F2469C');
	</script>
<%
end = System.currentTimeMillis();
logger.debug("userTimeLineChart.jsp execution time: " + (end - start));
%>
		

		
			