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
<!-- <script>
 	$(function() {
 		$( "#dialog" ).dialog();
	});
 	</script> 
 <div id="dialog" title="Basic dialog"> 
 	<p>This is the default dialog which is useful for displaying information. The dialog window can be moved, resized and closed with the 'x' icon.</p> 
</div> -->

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
				<td colspan="2" id="cash_value" style="text-align: left"><%=user == null ? "" : Util.getNumberFormatted(user.getCash(), true, false, false, false, false, false)%></td>
			
			</tr>
			<tr>
				<td><b><%=lutil.get("balance.portfolio", lang)%></b></td>
				<td style="padding-left: 2px; padding-right: 2px; text-align: center;">:</td>
				<td colspan="2" id="portfolio_value" style="text-align: left"><%=user == null ? "" : Util.getNumberFormatted(user.getPortfolio(), true, false, false, false, false, false)%></td>
				
			</tr>
			<tr>
				<td><b><%=lutil.get("balance.loan.debt", lang)%></b></td>
				<td style="padding-left: 2px; padding-right: 2px; text-align: center;">:</td>
				<td colspan="2" id="debt_value" style="text-align: left;"><%=user == null ? "" : Util.getNumberFormatted(user.getLoan(), true, false, false, false, false, false)%></td>
				
			</tr>
			<tr>
				<td><b><%=lutil.get("balance.total", lang)%></b></td>
				<td style="padding-left: 2px; padding-right: 2px; text-align: center;">:</td>
				<td colspan="2" id="total_value" style="text-align: left"><%=user == null ? "" : Util.getNumberFormatted(user.getTotal(), true, false, false, false, false, false)%></td>
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
				<td colspan="2" id="total_value" style="text-align: left"><%=user == null ? "" : Util.getNumberFormatted(user.getValueCumulative(), true, false, false, false, false, false)%></td>
		
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
								(int) (Math.sqrt(user.getTotal()) * UserMgr.INVITE_MONEY_RATE) })%>
			<br>
			<%=GUIUtil.getInstance().getTwitterShareButton(
						"?ref=" + user.getId(), "twitter.share.main", lang)%></p>
	</div>
	<%
		}
	%>
	
	<div style="margin-top: 10px;">
		<span><b><%= lutil.get("balance.loan", lang) %></b></span>
		<hr>
		
		<table class="datatbl" id="loan-loading-div">
			<tr>
				<td align="center"><%= lutil.get("balance.loan.receive", lang) %></td>
				<td align="center"><%= lutil.get("balance.loan.payback", lang) %></td>
			</tr>
			<tr>
				<td>
				<% if (user.getLoan() < UserMgr.MAX_LOAN) { %>
				<button class ="buy-button" onclick="receiveLoan(10000); return true;">
				$<%=Util.commaSep((int)(UserMgr.MAX_LOAN - user.getLoan())) %>
				</button>
				<% } %>
				</td>
				<td>
				<% if(user.getLoan() > 0 && user.getCash() > 1){ %>
				<button class ="sell-button" onclick="payAllLoanBack(); return true;">$<%=Util.commaSep((int)(user.getLoan() > user.getCash() ? user.getCash() : user.getLoan())) %></button>
				<% } %>
				</td>
			</tr>
			<tr>
				<td>
				<% if(user.getLoan()  < UserMgr.MAX_LOAN && UserMgr.MAX_LOAN - user.getLoan() > 1000){ %>
					<button class ="buy-button" onclick="receiveLoan(1000);return true;">$1,000</button>
				<% } %>
				</td>
				<td>
				<% if(user.getLoan() > 1000 && user.getCash() > 1000){ %>
				<button class ="sell-button" onclick="payLoanBack(1000);return true;">$1,000</button>
				<% } %>
				</td>
			</tr>
		</table>
		<ul>
			<li style="font-size: 10px; color: #777777;"><%= lutil.get("balance.loan.1",lang) %></li>
			<li style="font-size: 10px; color: #777777;"><%=lutil.get("balance.loan.2",lang) %></li>
		</ul>
		<div align="right">
			<% if(user.getTotal()<0){ %>
				<button class ="buy-button" onclick="bankrupt('<%= lutil.get("balance.bankrupt.confirm",lang) %>');return true;"><%= lutil.get("balance.bankrupt.declare",lang) %></button>
			<% } %>
		</div>
	</div>

</div>