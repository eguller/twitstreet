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
User user = userMgr.getUserById(sessionUser.getId());
Portfolio portfolio = null;
if(user != null){
	portfolio = portfolioMgr.getUserPortfolio(user);
}
if(portfolio==null){
	
	return;
	
}
%>
<div id="portfolio" style="margin-top: 10px;">
	<h3>Portfolio</h3>
	<table class="datatbl" id="portfolio-table">
	<% 
	if(portfolio.getStockInPortfolioList().size()>0){
	for(int i = 0; i < portfolio.getStockInPortfolioList().size();){ %>
		<tr>
			<% for(int j = 0; j < 1; j ++) { 
				if( i < portfolio.getStockInPortfolioList().size()){
			%>
			
				<td>
					<table>
						<tr>
							<td>
								<img class="twuser" src="<%=portfolio.getStockInPortfolioList().get(i).getPictureUrl()%>"/>
							</td>
							<td>
								<a href='/?stock=<%=portfolio.getStockInPortfolioList().get(i).getStockId() %>' title="Loads <%=portfolio.getStockInPortfolioList().get(i).getStockName() %>'s stock details"><% out.write(portfolio.getStockInPortfolioList().get(i).getStockName()); %></a>
								<br>
								$<% out.write(Util.commaSep(portfolio.getStockInPortfolioList().get(i).getAmount())); %>
								<br><%
								
								double profit = 0;
								
								double amount = portfolio.getStockInPortfolioList().get(i).getAmount();
								double capital = portfolio.getStockInPortfolioList().get(i).getCapital();
								
								profit = amount - capital;
								if(profit > 0){
									out.write("<span class=\"green-light\">$"+Util.commaSep(profit) + "&nbsp; &#9650;</span>"); 
								}
								else if(profit < 0){
									out.write("<span class=\"red-light\">$"+Util.commaSep(profit) + "&nbsp; &#9660;</span>"); 
								}
								else {
									out.write("<span>$"+Util.commaSep(profit)+"</span>"); 
								}
								%>
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