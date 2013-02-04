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
<%@page import="java.util.Date"%>
<%@page import="java.util.LinkedHashMap"%>
<%@page import="com.twitstreet.db.data.StockHistoryData"%>
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
<%@ page import="com.twitstreet.util.Util"%>
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
	User user = (User) request.getAttribute(User.USER);
	
	String divId =(String) request.getAttribute("stockTimeLineDivId");
	
	String format = request.getParameter("format");
	

	String since = request.getParameter("since");
	
	String forMinutes = request.getParameter("forMinutes");
	
	Stock stock = (Stock) request.getAttribute("chartStock");

	LocalizationUtil lutil = LocalizationUtil.getInstance();
	
	String lang = (String)request.getSession().getAttribute(LocalizationUtil.LANGUAGE);
	%>


	<script type="text/javascript">
		var dateArrayStock<%=stock.getId()%> = new Array();
		var valueArrayStock<%=stock.getId()%> = new Array();
		var stockNameStock<%=stock.getId()%> =
		<%
	
	
			StockHistoryData shd = null;
			if(since!=null){
				Date sinceDate = Util.stringToDate(since);
						shd = stockMgr.getStockHistory(stock.getId(),sinceDate);
			}
			else if(forMinutes!=null){
				shd = stockMgr.getStockHistory(stock.getId(),Integer.valueOf(forMinutes));
			}else{
				shd = stockMgr.getStockHistory(stock.getId());
			}
			out.write("'" + shd.getName() + "';\n");

			LinkedHashMap<Date, Integer> dvm = shd.getDateValueMap();

			out.write("dateArrayStock"+stock.getId()+".push(new Date(" + stock.getLastUpdate().getTime() + "));\n");

			out.write("valueArrayStock"+stock.getId()+".push(" + stock.getTotal() + ");\n");

			for (Date date : dvm.keySet()) {
				out.write("dateArrayStock"+stock.getId()+".push(new Date(" + date.getTime() + "));\n");

				out.write("valueArrayStock"+stock.getId()+".push(" + dvm.get(date) + ");\n");
		}%>
		drawStockHistory('<%=divId%>', dateArrayStock<%=stock.getId()%>, valueArrayStock<%=stock.getId()%>,
				stockNameStock<%=stock.getId()%>,'<%=format%>');
	</script>
<%
end = System.currentTimeMillis();
logger.debug("stocksTimeLineChart.jsp execution time: " + (end - start));
%>
		

		
			