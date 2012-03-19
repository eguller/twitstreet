<%@page import="com.twitstreet.localization.LocalizationUtil"%>
<%@page import="com.twitstreet.servlet.HomePageServlet"%>
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
	
	<div id="stock-trend-section" style="display: none;">
	
	<%
	LocalizationUtil lutil = LocalizationUtil.getInstance();
	String lang = (String)request.getSession().getAttribute(LocalizationUtil.LANGUAGE);

	
			Injector inj = (Injector) pageContext.getServletContext().getAttribute(Injector.class.getName());
			StockMgr stockMgr = inj.getInstance(StockMgr.class);

			Stock stock = (Stock) request.getAttribute(HomePageServlet.STOCK);
			StockHistoryData shd = null;

			if (stock != null) {

				shd = stockMgr.getStockHistory(stock.getId());

			}

			if (shd != null && shd.getDateValueMap().size() > 1) {
		%>
	


		<div id="detail-stock-<%=stock.getId()%>" style="height: 200px; width: 500px;">
			<% request.setAttribute("chartStock", stock);

			request.setAttribute("stockTimeLineDivId", "#detail-stock-"+stock.getId());
			
			%>
			<jsp:include page="stockTimeLineChart.jsp"/>
		</div>
	<br>
<div>
		<h3><%=lutil.get("transactions.header", lang) %></h3>
		<div>
			<jsp:include page="stockTransactionsContent.jsp">
				<jsp:param value="<%=stock.getId()%>" name="stock-id"/>
			</jsp:include>
		</div>
	</div>
	<%
			} else if (stock != null) {
		%>



	<div class="field-white">
	
			<b><%=lutil.get("stock.noHistory", lang,stock.getName()) %>
			</b>
	
	</div>


	<%
		}
	%>
		
		</div>
	
