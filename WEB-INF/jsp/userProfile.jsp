<%@page import="com.twitstreet.session.UserMgr"%>
<%@page import="com.twitstreet.db.data.User"%>
<%@ page import="com.google.inject.Injector"%>
<%@ page import="com.google.inject.Guice"%>
<%@ page import="java.util.List"%>
<%@ page import="com.twitstreet.util.Util"%>
<%@ page import="com.twitstreet.market.PortfolioMgr"%>
<%@ page import="com.twitstreet.db.data.Portfolio"%>

<%
	Injector inj = (Injector) pageContext.getServletContext()
			.getAttribute(Injector.class.getName());
	UserMgr userMgr = inj.getInstance(UserMgr.class);
	PortfolioMgr portfolioMgr = inj.getInstance(PortfolioMgr.class);
	Portfolio portfolio = null;
	User user = (User)request.getAttribute("user");
	portfolio = portfolioMgr.getUserPortfolio(user);
%>
<%
	if (user != null) {
%>
<div id="userprofile">
	<h3><%=user.getUserName()%>'s Profile <a
			style="float: right; vertical-align: bottom;" href="/">Go to Home
			&gt;&gt;</a>
	</h3>

	<table class="datatbl">
		<tr>
			<td colspan="4">
				<table class="datatbl">
					<tr>
						<td width="36px;"><img class="twuser"
							src="<%=user == null ? "" : user.getPictureUrl()%>"
							id="dashboard-picture">
						</td>
						<td style="text-align: left;" id="dashboard-stock-follower-status"><a
							href="http://twitter.com/#!/<%=user == null ? "" : user.getUserName()%>" title="Goes to <%=user == null ? "" : user.getUserName()%>'s twitter page"
							target="_blank"><%=user == null ? "" : user.getUserName()%></a>'s
							follower status
						</td>
					</tr>
				</table>
			</td>
		</tr>
		<tr>
			<td colspan="4">&nbsp;</td>
		</tr>
		<tr class="thead">
			<td style="width: 20%; text-align: center; font-weight: bolder;">Rank</td>
			<td style="width: 20%; text-align: center; font-weight: bolder;">Cash</td>
			<td style="width: 20%; text-align: center; font-weight: bolder;">Portfolio</td>
			<td style="width: 20%; text-align: center; font-weight: bolder;">Total</td>
			<td style="width: 20%; text-align: center; font-weight: bolder;">Moves</td>
		</tr>
		<tr>
			<td style="width: 25%; text-align: center;"><%=user.getRank()%>.
			</td>
			<td style="width: 25%; text-align: center;">$<%=Util.commaSep(user.getCash())%>
			</td>
			<td style="width: 25%; text-align: center;">$<%=Util.commaSep(user.getPortfolio())%>
			</td>
			<td style="width: 25%; text-align: center;">$<%=Util.commaSep(user.getPortfolio() + user.getCash())%>
			</td>
			<td style="width: 25%; text-align: center;">
				<%
					if (user.getDirection() == 1) {
				%> <img alt=""
				style="margin-top: 1px;" src="/images/up.png" /> <%
 	} else {
 %> <img
				alt="" style="margin-top: 1px;" src="/images/down.png" /> <%
 	}
 %>
			</td>
		</tr>
	</table>

</div>
<div id="userportfolio" style="margin-top: 15px;">
	<h3><%=user.getUserName()%>'s Portfolio
	</h3>
	<table class="datatbl">
		<%
			for (int i = 0; i < portfolio.getStockInPortfolioList().size();) {
		%>
		<tr>
			<%
				for (int j = 0; j < 4; j++) {
							if (i < portfolio.getStockInPortfolioList().size()) {
			%>

			<td>
				<table>
					<tr>
						<td><img class="twuser"
							src="<%=portfolio.getStockInPortfolioList()
									.get(i).getPictureUrl()%>" />
						</td>
						<td><a
							href='/stock?stock=<%=portfolio.getStockInPortfolioList()
									.get(i).getStockId()%>' title="Goes to <%=portfolio.getStockInPortfolioList().get(i).getStockName()%>'s stock detail page">
								<%
									out.write(portfolio.getStockInPortfolioList()
															.get(i).getStockName());
								%>
						</a> <br> <%
 	out.write(Util.commaSep(portfolio
 							.getStockInPortfolioList().get(i)
 							.getAmount()));
 %>$
						</td>
					</tr>
				</table></td>
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
</div>
<%
	} else {
%>
<div id="userportfolio">
	<div id="dashboard-message-field" style="margin-top: 42px;"
		class="field-white">User not found!</div>
</div>

<%
	}
%>