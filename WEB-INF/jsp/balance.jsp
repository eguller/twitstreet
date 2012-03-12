<%@page import="com.twitstreet.util.GUIUtil"%>
<%@ page import="com.google.inject.Injector"%>
<%@ page import="com.google.inject.Guice"%>
<%@ page import="com.twitstreet.session.UserMgr"%>
<%@ page import="com.twitstreet.db.data.User"%>
<%@ page import="com.twitstreet.localization.LocalizationUtil" %>
<%@ page import="com.twitstreet.util.Util" %>
<%@ page import="com.twitstreet.servlet.TwitStreetServlet" %>

<%
	Injector inj = (Injector) pageContext.getServletContext().getAttribute(Injector.class.getName());
	User user = (User) request.getAttribute(User.USER);
	LocalizationUtil lutil = LocalizationUtil.getInstance();
	String lang = (String) request.getSession().getAttribute(LocalizationUtil.LANGUAGE);
%>

	<div id="balance" class="main-div">
		<input id="cash-hidden" type="hidden" value="<%=user.getCash()%>" />
		<h3><%=lutil.get("balance.header", lang)%></h3>
		<table class="right_aligned datatbl">
			<tr>
				<td><b><%=lutil.get("balance.rank", lang)%></b></td>
				<td style="padding-left: 2px; padding-right: 2px; text-align: center;">:</td>
				<td colspan="2" id="balance_rank" style="text-align: left">
			<%
				if (user != null && user.getDirection() > 0) {
			%>
				<div id="balance_direction" style="text-align: left"><%=user == null ? "" : user.getRank()%>.<img src="/images/up_small.png" /></div>
			<%
				} else if (user != null && user.getDirection() < 0) {
			%>
				<div id="balance_direction" style="text-align: left"><%=user == null ? "" : user.getRank()%>.<img src="/images/down_small.png" /></div>
			<%
				} else {
			%>
				<div id="balance_direction" style="text-align: left"><%=user == null ? "" : user.getRank()%>.</div><%-- <img src="/images/nochange_small.png" /></div> --%>
			<%
				}
			%>
				</td>
	
			</tr>
			<tr>
				<td><b><%=lutil.get("balance.cash", lang)%></b></td>
				<td style="padding-left: 2px; padding-right: 2px; text-align: center;">:</td>
				<td colspan="2" id="cash_value" style="text-align: left">$<%=user == null ? "" : Util.commaSep(user.getCash())%></td>
				<td/>
			</tr>
			<tr>
				<td><b><%=lutil.get("balance.portfolio", lang)%></b></td>
				<td style="padding-left: 2px; padding-right: 2px; text-align: center;">:</td>
				<td colspan="2" id="portfolio_value" style="text-align: left">$<%=user == null ? "" : Util.commaSep(user.getPortfolio())%></td>
				<td/>
			</tr>
			<tr>
				<td><b><%=lutil.get("balance.total", lang)%></b></td>
				<td style="padding-left: 2px; padding-right: 2px; text-align: center;">:</td>
				<td colspan="2" id="total_value" style="text-align: left">$<%=user == null ? "" : Util.commaSep(user.getCash() + user.getPortfolio())%></td>
				<td/>
				<tr>
				<td></td>
				<td style="padding-left: 2px; padding-right: 2px; text-align: center;">&nbsp;</td>
				
				<%
									String speedStr = "";
									if (user != null && user.getProfit() != 0) {

										speedStr = Util.getNumberFormatted(user.getProfit(), true, false, true, true, false, true);
									}
								%>
				<td colspan="2" title="<%=lutil.get("balance.speed.tip", lang)%>" id="total_trend" style="text-align: left"><%=speedStr%></td>
				<td/>
			</tr>
	</table>
	
		<%
						if (user != null) {
					%>
					<div class="field-white" style="overflow: hidden; height:20px" onmouseover="$(this).height(75)" onmouseout="$(this).height(20)">
						<p style="text-align: center">
							<%=lutil.get("topbar.invite", lang, new Object[] { user.getId(), (int) (Math.sqrt(user.getCash() + user.getPortfolio()) * UserMgr.INVITE_MONEY_RATE) })%>
						<%=GUIUtil.getInstance().getTwitterShareButton("?ref="+user.getId(), "twitter.share.main", lang)%></p>
					</div> <%
					 	}
					 %>
				
	</div>
