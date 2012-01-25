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

%>
<div id="portfolio" style="margin-top: 10px;">
	<h3>Portfolio</h3>
	<table class="datatbl" id="portfolio-table">
	<% for(int i = 0; i < portfolio.getStockInPortfolioList().size();){ %>
		<tr>
			<% for(int j = 0; j < 4; j ++) { 
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