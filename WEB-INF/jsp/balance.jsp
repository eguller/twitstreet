<%@ page import="com.google.inject.Injector"%>
<%@ page import="com.google.inject.Guice"%>
<%@ page import="com.twitstreet.session.UserMgr"%>
<%@ page import="com.twitstreet.db.data.User"%>

<%
Injector inj = (Injector) pageContext.getServletContext().getAttribute(Injector.class.getName());
User sessionUser = (User)request.getSession().getAttribute(User.USER);
UserMgr userMgr = inj.getInstance(UserMgr.class);
User user = userMgr.getUserById(sessionUser.getId());

%>
<div id="balance">
	<input id="cash" type="hidden" value="3426" />
	<h3>Balance</h3>
	<table class="right_aligned">
		<tr>
			<td>Rank</td>
			<td>:</td>
			<td><%=user.getRank()%>.</td>
			<% if (user.getDirection() == 1) { %>
				<td><img src="../images/up.gif" /></td>
			<% } else { %>
				<td><img src="../images/down.gif" /></td>
			<% } %>
		</tr>
		<tr>
			<td>Cash</td>
			<td>:</td>
			<td colspan="2" id="cash_value"><%=user.getCash()%>$</td>
			
		</tr>
		<tr>
			<td>Portfolio</td>
			<td>:</td>
			<td colspan="2" id="portfolio_value"><%=user.getPortfolio()%>$</td>
		</tr>
		<tr>
			<td>Total</td>
			<td>:</td>
			<td colspan="2" id="total_value"><%=user.getCash() + user.getPortfolio()%>$</td>
		</tr>
	</table>
</div>