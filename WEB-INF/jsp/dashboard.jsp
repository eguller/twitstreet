<%@page import="com.twitstreet.db.data.UserStock"%>
<%@page import="java.sql.SQLException" %>
<%@page import="com.twitstreet.servlet.StockQuoteServlet" %>
<%@page import="com.twitstreet.market.StockMgr" %>
<%@ page import="com.google.inject.Injector" %>
<%@ page import="com.google.inject.Guice" %>
<%@ page import="com.twitstreet.db.data.User" %>
<%@ page import="com.twitstreet.db.data.Stock" %>
<%@ page import="com.twitstreet.util.Util" %>
<%@page import="com.twitstreet.db.data.Portfolio"%>
<%@page import="com.twitstreet.market.PortfolioMgr"%>
<%@page import="com.twitstreet.config.ConfigMgr"%>

<%
User sessionUser = (User)request.getSession().getAttribute(User.USER);
Injector inj = (Injector) pageContext.getServletContext().getAttribute(Injector.class.getName());
StockMgr stockMgr = inj.getInstance(StockMgr.class);
PortfolioMgr portfolioMgr = inj.getInstance(PortfolioMgr.class);
ConfigMgr configMgr = inj.getInstance(ConfigMgr.class);
%>

<div id="dashboard">
	<h3>Dasboard</h3>
	<div id="quoteholder">
	    <%
	    	String quote = request.getSession().getAttribute(StockQuoteServlet.QUOTE) == null ? "" : (String)request.getSession().getAttribute(StockQuoteServlet.QUOTE); 
	    	Stock stock = null;
	    	boolean exceptionOccured = false;
	    	if(quote.length() > 0){
	    		try{
	    			stock = stockMgr.getStock(quote);
	    		}
	    		catch(SQLException ex){
	    			exceptionOccured = true;
	    		}
	    	}
	    %>
		<input type="text" class="textbox" id="quote" value="<%=quote%>" />
		<button class="button" onclick="getquote();" id="getquotebutton">Get quote</button>
		<input type="hidden" id="quote-hidden" />
		<input type="hidden" id="quote-id" />
	</div>
	<div id="userfound" 
		<% 
			if(stock != null && quote.length() > 0 && !exceptionOccured && stock.getTotal() > 0){
				out.write(" style=\"visibility: visible;\"");
			} 
			else{
				out.write(" style=\"display: none;\"");
			}
		%>
	>
		<p style="width: 100%; text-align: center"><span id="user-stock">
			<%
				if(sessionUser != null && stock != null){
					UserStock userStock =  portfolioMgr.getStockInPortfolio(sessionUser.getId(), stock.getId());
					if(userStock == null){
						out.write("You don't have any " + stock.getName());
					}
					else{
						int amount = (int)(userStock.getPercent() * stock.getTotal());
						String commaSep = Util.commaSep(amount);
						out.write("You have <b>" + commaSep + "</b> " + stock.getName());
					}
					
					if(stock.getTotal() < configMgr.getMinFollower()){
						out.write("<br>");
						out.write(stock.getName() + " has <b>" + stock.getTotal() + "</b> follower. <br>You cannot buy followers if total is less than <b>" + configMgr.getMinFollower() +"</b>");
					}
				}
			%>
		</span></p>
		<input type="hidden" id="user-stock-val"/>
		<table class="datatbl">
			<tr>
				<td colspan="3" >
					<%if(stock != null){%>
					<table width="100%">
						<tr>
							<td width="36px;">
								<img class="twuser" src="<%=stock.getPictureUrl()%>" id="dashboard-picture">
							</td>
							<td style="text-align: left;" id="dashboard-stock-follower-status"><%=stock.getName()%>'s follower status </td>
							<td style="text-align: right;"><a href="/stock/<%=stock.getName()%>">See All</a></td>
						</tr>
					</table>
					<% } %>
				</td>
			</tr>
			<tr>
				<td>Available</td>
				<td>Sold</td>
				<td>Total</td>
			</tr>
			<tr>
				<td id = "available">
					<%
					if(stock != null){
						out.write(Util.commaSep(stock.getAvailable()));
					}
					%>
				</td>
				<td id = "sold">
					<%
					if(stock != null){
						out.write(Util.commaSep(stock.getTotal() - stock.getAvailable()));
					}
					%>
				</td>
				<td id="total">
					<%
					if(stock != null){
						out.write(Util.commaSep(stock.getTotal()));
					}
					%>
				</td>
			</tr>
			<tr id="buy-links-row">
				<td colspan="3" id="buy-links"></td>
			</tr>
			<tr id="sell-links-row">
				<td colspan="3" id="sell-links"></td>
			</tr>
		</table>
	</div>
	<div id="searchnoresult"
	<%
		if(quote.length() > 0 && stock == null && !exceptionOccured){
			out.write(" style=\"visibility: visible\"");
		}
		else {
			out.write(" style=\"display: none\"");
		}
	%>
	
	>
		Search does not have result
	</div>
	
	<div id="hasnofollowers"
	<%
		if(quote.length() > 0 && stock != null && stock.getTotal() == 0 && !exceptionOccured){
			out.write(" style=\"visibility: visible\"");
		}
		else {
			out.write(" style=\"display: none\"");
		}
		%>
	>
		
		<% 
		if(stock != null){
			out.write(stock.getName() + " has 0 followers. Please try something else."); 
		}
		%> 
	</div>
	
	<div id="searchfailed"
	<%
	    if(exceptionOccured){
			out.write(" style=\"visibility: visible\"");
		}
		else {
			out.write(" style=\"display: none\"");
		}
	%>
	>
		Search failed.
	</div>
	
</div>

