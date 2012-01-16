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
	<table class="right_aligned datatbl">
		<tr>
			<td><b>Rank</b></td>
			<td style="padding-left: 2px; padding-right: 2px; text-align: center;">:</td>
			<td id="balance_rank"><%=user.getRank()%>. &nbsp;
				<% if (user.getDirection() == 1) { %>
				<td id="balance_direction"><img src="/images/up_small.png" /></td>
			<% } else { %>
				<td id="balance_direction" style="text-align: left"><img src="/images/down_small.png" /></td>
			<% } %>
			</td>

		</tr>
		<tr>
			<td><b>Cash</b></td>
			<td style="padding-left: 2px; padding-right: 2px; text-align: center;">:</td>
			<td colspan="2" id="cash_value" style="text-align: left">$<%=Util.commaSep(user.getCash())%></td>
			
		</tr>
		<tr>
			<td><b>Portfolio</b></td>
			<td style="padding-left: 2px; padding-right: 2px; text-align: center;">:</td>
			<td colspan="2" id="portfolio_value" style="text-align: left">$<%=Util.commaSep(user.getPortfolio())%></td>
		</tr>
		<tr>
			<td><b>Total</b></td>
			<td style="padding-left: 2px; padding-right: 2px; text-align: center;">:</td>
			<td colspan="2" id="total_value" style="text-align: left">$<%=Util.commaSep(user.getCash() + user.getPortfolio())%></td>
		</tr>
	</table>
</div>