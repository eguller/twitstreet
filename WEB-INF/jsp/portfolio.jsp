<%@page import="com.twitstreet.db.data.StockInPortfolio"%>
<%@page import="com.twitstreet.db.data.Stock"%>
<%@page import="com.twitstreet.db.data.Portfolio"%>
<%@page import="com.twitstreet.market.PortfolioMgr"%>
<%@ page import="com.google.inject.Injector"%>
<%@ page import="com.google.inject.Guice"%>
<%@ page import="com.twitstreet.session.UserMgr"%>
<%@ page import="com.twitstreet.db.data.User"%>
<%@ page import="java.util.ArrayList"%>
<%@ page import="com.twitstreet.util.Util" %>
<%@ page import="com.twitstreet.session.UserMgr" %>


<%
Injector inj = (Injector) pageContext.getServletContext().getAttribute(Injector.class.getName());
User sessionUser = (User)request.getSession().getAttribute(User.USER);
PortfolioMgr portfolioMgr = inj.getInstance(PortfolioMgr.class);
UserMgr userMgr = inj.getInstance(UserMgr.class);


if(sessionUser==null){
	return;
}
User user = userMgr.getUserById(sessionUser.getId());
Portfolio portfolio = null;
if(user != null){
	portfolio = portfolioMgr.getUserPortfolio(user);
}
if(portfolio==null){
	
	return;
	
}
%>
<div id="portfolio" class="main-div">
	<h3>Portfolio</h3>
	<table class="datatbl" id="portfolio-table">
	<% 
	if(portfolio.getStockInPortfolioList().size()>0){
	for(int i = 0; i < portfolio.getStockInPortfolioList().size();){ %>
		<tr>
			<% for(int j = 0; j < 1; j ++) { 
				if( i < portfolio.getStockInPortfolioList().size()){
					
					StockInPortfolio stock = portfolio.getStockInPortfolioList().get(i);
			%>
			
				<td>
					<table class="datatbl">
						<tr>
							<td>
								<img class="twuser" src="<%=stock.getPictureUrl()%>"/>
							</td>
							<td>
								<a href='javascript:void(0)' onclick='loadStock(<%=stock.getStockId() %>)' title="Loads <%=stock.getStockName() %>'s stock details"><% out.write(stock.getStockName()); %></a>
								<br>
								$<% out.write(Util.commaSep(stock.getAmount()) + "&nbsp;("+Util.getShareString(stock.getPercentage())+")"); %>
								<br>							
								<table class="portfolio-stock-tbl">
									<tr>
										<td align="left">
													<%
								
											double profit = 0;
											
											double amount =stock.getAmount();
											double capital = stock.getCapital();
											
											profit = amount - capital;
											if(profit > 0){
												out.write("<span class=\"green-light\">"+Util.getProfitString(profit) + "</span>"); 
											}
											else if(profit < 0){
												out.write("<span class=\"red-light\">"+Util.getProfitString(profit) + "</span>"); 
											}
											else {
												out.write("<span></span>"); 
											}
											%>
										</td>
										<td align="right">
												<%
												String profitPerHour = Util.getChangePerHourString(stock.getChangePerHour());
												if (stock.getChangePerHour() > 0) {
													out.write("<span class=\"green-profit\">" + profitPerHour + "</span>");
												}
												else if (stock.getChangePerHour() < 0){
													out.write("<span class=\"red-profit\">" +  profitPerHour  + "</span>");
												}
												%>
										</td>
									</tr>
								</table>
								
								
					
							</td>
							
						</tr>
					</table>
				</td>
				<% } else { %>
				<td></td>
			<% 
				}
				i++;
			} %>
		</tr>
	<% } 
	} else{
		
		out.write("<tr><td>"+Util.NO_RECORDS_FOUND_HTML+"</td></tr>");
		
	}
	%>
	</table>
</div>