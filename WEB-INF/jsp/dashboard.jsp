
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
	boolean showSuggestedStocks = false;
	
	try{
		showSuggestedStocks = (boolean) (request.getAttribute(HomePageServlet.SUGGESTED_STOCKS).toString().length()>0);
	}catch(Exception ex){
		showSuggestedStocks = stock==null;
	}
	
%>

	
<div id="dashboard" class="main-div">

	<jsp:include page="getQuote.jsp" />
	
	
	
		<table class="datatbl">
		<tr>
			<td>
				<jsp:include page="stockBar.jsp"></jsp:include>
			</td>
		</tr>
		<tr>
			<td>
				<div id="stocks-screen">
					<div id="suggested-stocks-content">
						<%if(showSuggestedStocks){%>
							<jsp:include page="trendyStocks.jsp" />
							
						<%}%>
						
					</div>
					<div id="stock-details-content">
					<%if(stock!=null){%>
						<jsp:include page="stockDetails.jsp" />
						
					<%	}%>
						
					</div>
				</div>
			</td>
		</tr>
	</table>


</div>
