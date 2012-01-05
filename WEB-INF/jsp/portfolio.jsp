<%@page import="com.twitstreet.db.data.Portfolio"%>
<%@page import="com.twitstreet.market.PortfolioMgr"%>
<%@ page import="com.google.inject.Injector"%>
<%@ page import="com.google.inject.Guice"%>
<%@ page import="com.twitstreet.session.UserMgr"%>
<%@ page import="com.twitstreet.db.data.User"%>
<%@ page import="java.util.ArrayList"%>


<%
Injector inj = (Injector) pageContext.getServletContext().getAttribute(Injector.class.getName());
User sessionUser = (User)request.getSession().getAttribute(User.USER);
PortfolioMgr portfolioMgr = inj.getInstance(PortfolioMgr.class);
Portfolio portfolio = portfolioMgr.getUserPortfolio(sessionUser.getId());

%>
<div id="portfolio">
	<h3>Portfolio</h3>
	<table class="datatbl">
	<% for(int i = 0; i < portfolio.getStockInPortfolioList().size();){ %>
		<tr>
			<% for(int j = 0; j < 4; j ++) { 
				if( i < portfolio.getStockInPortfolioList().size()){
			%>
			
				<td>
					<table>
						<tr>
							<td>
								<img src="<%=portfolio.getStockInPortfolioList().get(i).getPictureUrl()%>"/>
							</td>
							<td>
								<% out.write(portfolio.getStockInPortfolioList().get(i).getStockName()); %>
								<br>
								<% out.write(Integer.toString(portfolio.getStockInPortfolioList().get(i).getAmount())); %>$
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
	<% } %>
	</table>
</div>