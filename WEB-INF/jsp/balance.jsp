<%@page import="com.twitstreet.util.GUIUtil"%>
<%@ page import="com.google.inject.Injector"%>
<%@ page import="com.google.inject.Guice"%>
<%@ page import="com.twitstreet.session.UserMgr"%>
<%@ page import="com.twitstreet.db.data.User"%>
<%@ page import="com.twitstreet.localization.LocalizationUtil"%>
<%@ page import="com.twitstreet.util.Util"%>
<%@ page import="com.twitstreet.servlet.TwitStreetServlet"%>

<%
	Injector inj = (Injector) pageContext.getServletContext()
			.getAttribute(Injector.class.getName());
	User user = (User) request.getAttribute(User.USER);
	LocalizationUtil lutil = LocalizationUtil.getInstance();
	String lang = (String) request.getSession().getAttribute(
			LocalizationUtil.LANGUAGE);
	
	String speedStr ="";
%>
	<div id="balance" class="main-div">
		<input id="cash-hidden" type="hidden" value="<%=user.getCash()%>" />
		<h3><%=lutil.get("balance.header", lang)%></h3>
		<table class="right_aligned datatbl">
			<tr>
				<td colspan="3" align="center" style="text-align: center"><b><%=lutil.get("season.thisseason", lang)%></b></td>
			</tr>
		
			<tr>
				<td width="48%"><b><%=lutil.get("balance.rank", lang)%></b></td>
				<td width="4%" style="padding-left: 2px; padding-right: 2px; text-align: center;">:</td>
				<td width="48%" colspan="2" id="balance_rank" style="text-align: left">
					<%=user == null ? "" : user.getRank()%>
				
	
			</tr>
		
			<tr>
				<td><b><%=lutil.get("balance.cash", lang)%></b></td>
				<td style="padding-left: 2px; padding-right: 2px; text-align: center;">:</td>
				<td colspan="2" id="cash_value" style="text-align: left">$<%=user == null ? "" : Util.commaSep(user.getCash())%></td>
			
			</tr>
			<tr>
				<td><b><%=lutil.get("balance.portfolio", lang)%></b></td>
				<td style="padding-left: 2px; padding-right: 2px; text-align: center;">:</td>
				<td colspan="2" id="portfolio_value" style="text-align: left">$<%=user == null ? "" : Util.commaSep(user.getPortfolio())%></td>
				
			</tr>
			<tr>
				<td><b><%=lutil.get("balance.total", lang)%></b></td>
				<td style="padding-left: 2px; padding-right: 2px; text-align: center;">:</td>
				<td colspan="2" id="total_value" style="text-align: left">$<%=user == null ? "" : Util.commaSep(user.getCash() + user.getPortfolio())%></td>
			</tr>
			<tr>
				<td></td>
				<td style="padding-left: 2px; padding-right: 2px; text-align: center;">&nbsp;</td>
				
				<%
									speedStr = "";
									if (user != null && user.getProfit() != 0) {

										speedStr = Util.getNumberFormatted(user.getProfit(), true, false, true, true, false, true);
									}
								%>
				<td colspan="2" title="<%=lutil.get("balance.speed.tip", lang)%>" style="text-align: left"><%=speedStr%></td>
			
			</tr>		
							
			<tr>
				<td colspan="3">
				</td>
			</tr>
		
			<tr>
				<td colspan="3" align="center" style="text-align: center"><b><%=lutil.get("season.alltime", lang)%></b></td>
			
	
			</tr>
			
			<tr>
				<td><b><%=lutil.get("balance.rank", lang)%></b></td>
				<td style="padding-left: 2px; padding-right: 2px; text-align: center;">:</td>
				<td colspan="2" id="balance_rank" style="text-align: left">
					<%=user == null ? "" : user.getRankCumulative()%>
				</td>
	
			</tr>
			<tr>
				<td><b><%=lutil.get("balance.total", lang)%></b></td>
				<td style="padding-left: 2px; padding-right: 2px; text-align: center;">:</td>
				<td colspan="2" id="total_value" style="text-align: left">$<%=user == null ? "" : Util.commaSep(user.getValueCumulative())%></td>
		
			</tr>

			
		
		
	</table>
	<%
		if (user != null && user.isInviteActive()) {
	%>
	<div class="field-white">
		<p style="text-align: center">
			<%=lutil.get(
						"topbar.invite",
						lang,
						new Object[] {
								user.getId(),
								(int) (Math.sqrt(user.getCash()
										+ user.getPortfolio()) * UserMgr.INVITE_MONEY_RATE) })%>
			<br>
			<%=GUIUtil.getInstance().getTwitterShareButton(
						"?ref=" + user.getId(), "twitter.share.main", lang)%></p>
	</div>
	<%
		}
	%>

</div>
