
<%@page import="com.twitstreet.servlet.HomePageServlet"%>
<%@page import="com.twitstreet.db.data.Stock"%>
<%@page import="com.twitstreet.db.data.User"%>
<%@page import="com.twitstreet.market.StockMgr"%>
<%@page import="com.google.inject.Injector"%>
<%
	Injector inj = (Injector) pageContext.getServletContext().getAttribute(Injector.class.getName());
	StockMgr stockMgr = inj.getInstance(StockMgr.class);
	User user = (User) request.getAttribute(User.USER);
	
	long id = -1;
	
	
	
	Stock stock = (Stock) request.getAttribute(HomePageServlet.STOCK);
	String quote = request.getAttribute(HomePageServlet.QUOTE) == null ? "" : (String) request.getAttribute(HomePageServlet.QUOTE);

%>

	
<div id="dashboard" class="main-div">

	<jsp:include page="getQuote.jsp" />
	
	<div id="stocks-screen">
	<%
	if(stock!=null || quote!=null && quote.length()>0){
	%>	
		<jsp:include page="stockDetails.jsp" />
	<%	
	}else{
	%>
		<jsp:include page="trendyStocks.jsp" />
	<%
	}
	%>	
	</div>

</div>
