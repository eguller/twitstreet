<%@ page import="com.google.inject.Injector"%>
<%@ page import="com.google.inject.Guice"%>
<%@ page import="com.twitstreet.session.UserMgr"%>
<%@ page import="com.twitstreet.db.data.User"%>
<%@ page import="com.twitstreet.util.Util" %>
<%@ page import="com.twitstreet.servlet.TwitStreetServlet" %>

<%
Injector inj = (Injector) pageContext.getServletContext().getAttribute(Injector.class.getName());
User user = (User)request.getAttribute(User.USER);
%>
<div id="balance" class="main-div">
	<input id="cash-hidden" type="hidden" value="<%=user.getCash()%>" />
	<h3>Balance</h3>
	<table class="right_aligned datatbl">
		<tr>
			<td><b>Rank</b></td>
			<td style="padding-left: 2px; padding-right: 2px; text-align: center;">:</td>
			<td colspan="2" id="balance_rank" style="text-align: left">
		<% if (user != null && user.getDirection() >0) { %>
			<div id="balance_direction" style="text-align: left"><%=user == null ? "" : user.getRank()%>.<img src="/images/up_small.png" /></div>
		<% } else if(user != null && user.getDirection() <0) { %>
			<div id="balance_direction" style="text-align: left"><%=user == null ? "" : user.getRank()%>.<img src="/images/down_small.png" /></div>
		<% } else { %>
			<div id="balance_direction" style="text-align: left"><%=user == null ? "" : user.getRank()%>.</div><%-- <img src="/images/nochange_small.png" /></div> --%>
		<% }
		%>
			</td>

		</tr>
		<tr>
			<td><b>Cash</b></td>
			<td style="padding-left: 2px; padding-right: 2px; text-align: center;">:</td>
			<td colspan="2" id="cash_value" style="text-align: left">$<%=user == null ? "" : Util.commaSep(user.getCash())%></td>
			<td/>
		</tr>
		<tr>
			<td><b>Portfolio</b></td>
			<td style="padding-left: 2px; padding-right: 2px; text-align: center;">:</td>
			<td colspan="2" id="portfolio_value" style="text-align: left">$<%=user == null ? "" : Util.commaSep(user.getPortfolio())%></td>
			<td/>
		</tr>
		<tr>
			<td><b>Total</b></td>
			<td style="padding-left: 2px; padding-right: 2px; text-align: center;">:</td>
			<td colspan="2" id="total_value" style="text-align: left">$<%=user == null ? "" : Util.commaSep(user.getCash() + user.getPortfolio())%></td>
			<td/>
		</tr>
	</table>
</div>