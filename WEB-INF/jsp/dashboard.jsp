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
<%
	HashMap<String, Object> requestObjects = new HashMap<String, Object>();

	User sessionUser = (User) request.getSession().getAttribute(User.USER);
	Injector inj = (Injector) pageContext.getServletContext().getAttribute(Injector.class.getName());
	StockMgr stockMgr = inj.getInstance(StockMgr.class);
	PortfolioMgr portfolioMgr = inj.getInstance(PortfolioMgr.class);
	ConfigMgr configMgr = inj.getInstance(ConfigMgr.class);
	UserMgr userMgr = inj.getInstance(UserMgr.class);
	User user = null;
	if (sessionUser != null) {
		user = userMgr.getUserById(sessionUser.getId());
	}

	DecimalFormat f = new DecimalFormat("##.00");
	String stockId = request.getParameter("stock");

	long id = -1;
	List<UserStockDetail> stockDetailList = null;
	Stock stock = null;
	
	stock = (Stock) request.getAttribute("stock");
	
	String quote = request.getAttribute(HomePageServlet.QUOTE) == null ? "" : (String) request.getAttribute(HomePageServlet.QUOTE);
	
	StockHistoryData shd = null;
	if (stock == null) {
		if(quote.length() > 0 ){
		stock = stockMgr.getStock(quote);
		 request.getParameterMap().put("stock",new String[]{String.valueOf(stock.getId())});
		
		}
	}else{
		 id=stock.getId();
	}

	
	
	try {
		if(stockId!=null){

			id = Long.parseLong(stockId);
		}
		stockDetailList = portfolioMgr.getStockDistribution(id);
		

		shd = stockMgr.getStockHistory(id);

	} catch (NumberFormatException nfe) {

	}

	
	requestObjects.put("stock", stock);
	requestObjects.put("quote", quote);

	request.setAttribute("requestObjects", requestObjects);
%>

<div id="dashboard">

	
	<jsp:include page="getQuote.jsp" />

	<%
		if (stock != null) {
	%>
	<br>
	<jsp:include page="stockDetails.jsp" />
	<%
		}
	%>
	


	<div id="userfound"
		<%if (stock != null && quote.length() > 0 && stock.getTotal() > 0) {
				out.write(" style=\"visibility: visible;\"");
			} else {
				out.write(" style=\"display: none;\"");
			}%>
			>
 <%
 	UserStock userStock = null;
 	if (user != null && stock != null) {
 		userStock = portfolioMgr.getStockInPortfolio(user.getId(), stock.getId());
//  		if (userStock == null) {
//  			out.write("You don't have any " + stock.getName());
//  		} else {
//  			int amount = (int) (userStock.getPercent() * stock.getTotal());
//  			String commaSep = Util.commaSep(amount);
//  			out.write("You have <b>" + commaSep + "</b> " + stock.getName());
//  		}

 		if (stock.getTotal() < configMgr.getMinFollower()) {
//  			out.write("<br>");
//  			out.write(stock.getName() + " has <b>" + stock.getTotal() + "</b> follower. <br>You cannot buy followers if total is less than <b>" + configMgr.getMinFollower() + "</b>");
 		}
 	}
 %> 
		
	</div>

		<input type="hidden" id="user-stock-val"
			value="<%=userStock == null ? "" : (int) (userStock.getPercent() * stock.getTotal())%>" />
		<input type="hidden" id="available-hidden"
			value="<%=stock == null ? "" : stock.getAvailable()%>" /> <input
			type="hidden" id="sold-hidden"
			value="<%=stock == null ? "" : stock.getSold()%>" /> <input
			type="hidden" id="total-hidden"
			value="<%=stock == null ? "" : stock.getTotal()%>" />


	<%
		if (quote.length()>0) {
	%>
		<div id="hasnofollowers"
		<%if (quote.length() > 0 && stock != null && stock.getTotal() == 0) {
				out.write(" style=\"visibility: visible\"");
			} else {
				out.write(" style=\"display: none\"");
			}%>>

		<%
			if (stock != null) {
		%>
				<div id="dashboard-message-field" style="margin-top: 6px;"
			class="field-white">
			<p style="margin-top: 10px; margin-bottom: 10px;">
			<%
				out.write(stock.getName() + " has 0 followers. Please try something else.");
			%>
			</p>
			</div>
		<%
			}
		%>
	</div>
	
		<div id="other-search-result">
			<%
				ArrayList<SimpleTwitterUser> searchResults = (ArrayList<SimpleTwitterUser>) request.getSession().getAttribute(HomePageServlet.OTHER_SEARCH_RESULTS);
			%>
			<%
				if (searchResults != null && searchResults.size() > 0) {
			%>
			<table class="datatbl" style="margin-top: 10px;">
				<tr class="thead">
					<td style="width: 33%; text-align: left; font-weight: bolder;" colspan="3">Other results for <%=quote%></td>
				</tr>
				<%
					for (int i = 0; i < searchResults.size();) {
				%>
				<tr>
					<%
						for (int j = 0; j < 3; j++) {
									if (i < searchResults.size()) {
					%>

					<td>
						<table>
							<tr>
								<td><img class="twuser"
									src="<%=searchResults.get(i).getPictureUrl()%>" />
								</td>
								<td><a href='/?stock=<%=searchResults.get(i).getId()%>'
									title="Loads <%=searchResults.get(i).getScreenName()%>'s stock details">
										<%
											out.write(searchResults.get(i).getScreenName());
										%> </a> <br>
										<%
											out.write(Util.commaSep(searchResults.get(i).getFollowerCount()));
										%>
								</td>

							</tr>
						</table>
					</td>
					<%
						} else {
					%>
					<td></td>
					<%
						}
									i++;
								}
					%>
				</tr>
				<%
					}
				%>
			</table>
			<%
				}
			%>
		</div>

	<%
		}
	%>
	
	

	
	
	<div id="searchnoresult"
		<%if (quote.length() > 0 && stock == null) {
				out.write(" style=\"visibility: visible\"");
			} else {
				out.write(" style=\"display: none\"");
			}%>>
		Search does not have result</div>



	</div>
