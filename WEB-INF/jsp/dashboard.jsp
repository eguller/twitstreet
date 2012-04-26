
<%@page import="com.twitstreet.util.Util"%>
<%@page import="com.twitstreet.servlet.PaginationDO"%>
<%@page import="java.util.ArrayList"%>
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

	String selectedTab = (String) request.getAttribute(HomePageServlet.SELECTED_TAB_STOCK_BAR);
	  
	
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
					
					
					
						<%if(selectedTab.equalsIgnoreCase("suggested-stocks-tab")){			
							PaginationDO pdo = new PaginationDO(1,stockMgr.getSuggestedStockCount(),StockMgr.MAX_TRENDS_PER_PAGE,"suggested","loadSuggestedStocks", false);
							
							ArrayList<Stock> results = stockMgr.getSuggestedStocks(pdo.getOffset(),pdo.getRecordPerPage());
						
							request.setAttribute("stockList", results);		
							request.setAttribute("stockListName", "suggested");					
						%>
							
								
							<%request.setAttribute("pdo", pdo); %>					
							<jsp:include page="suggestedStocks.jsp" />	
							
			
						
				
										
						<%}%>
						
					</div>
					<div id="top-grossing-stocks-content">
						<%if(selectedTab.equalsIgnoreCase("top-grossing-stocks-tab")){		

							ArrayList<Stock> results = stockMgr.getTopGrossingStocks();
							request.setAttribute("stockList", results);		
							request.setAttribute("stockListName", "top-grossing");	
						%>
							<jsp:include page="suggestedStocks.jsp" />							
						<%}%>
						
					</div>
					<div id="stock-details-content">
					<%if(selectedTab.equalsIgnoreCase("stock-details-tab")){%>
						<jsp:include page="stockDetails.jsp" />
						
					<%	}%>
						
					</div>
				</div>
			</td>
		</tr>
	</table>


</div>
