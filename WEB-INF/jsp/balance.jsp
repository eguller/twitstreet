<%@ page import="com.google.inject.Injector"%>
<%@ page import="com.google.inject.Guice"%>
<%@ page import="com.twitstreet.session.UserMgr"%>
<%@ page import="com.twitstreet.db.data.User"%>
<%@ page import="com.twitstreet.util.Util" %>

<%
Injector inj = (Injector) pageContext.getServletContext().getAttribute(Injector.class.getName());
User sessionUser = (User)request.getSession().getAttribute(User.USER);
UserMgr userMgr = inj.getInstance(UserMgr.class);
User user = userMgr.getUserById(sessionUser.getId());

%>
<div id="balance">
	<input id="cash-hidden" type="hidden" value="<%=user.getCash()%>" />
	<h3>Balance</h3>
	<table class="right_aligned">
		<tr>
			<td><b>Rank</b></td>
			<td>:</td>
			<td id="balance_rank"><%=user.getRank()%>.</td>
			<% if (user.getDirection() == 1) { %>
				<td id="balance_direction"><img src="/images/up.png" /></td>
			<% } else { %>
				<td id="balance_direction"><img src="/images/down.png" /></td>
			<% } %>
		</tr>
		<tr>
			<td><b>Cash</b></td>
			<td>:</td>
			<td colspan="2" id="cash_value">$<%=Util.commaSep(user.getCash())%></td>
			
		</tr>
		<tr>
			<td><b>Portfolio</b></td>
			<td>:</td>
			<td colspan="2" id="portfolio_value">$<%=Util.commaSep(user.getPortfolio())%></td>
		</tr>
		<tr>
			<td><b>Total</b></td>
			<td>:</td>
			<td colspan="2" id="total_value">$<%=Util.commaSep(user.getCash() + user.getPortfolio())%></td>
		</tr>
	</table>
</div>