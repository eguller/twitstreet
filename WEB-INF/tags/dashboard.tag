<%@ tag language="java" pageEncoding="ISO-8859-1"%>
<%@ attribute name="data" required="true" type="com.twitstreet.data.DashboardData" %>
<div id="dashboard">
	<div class="sectionHeader">Dashboard (<%=data.userid%>)</div>
	<table width="100%">
		<tr>
			<td colspan="2" id="balance-rank">Cash: ***$ - Portfolio: ***$ - Total: ***$
				- Rank: ***.</td>
		</tr>
		<tr>
			<td colspan="2">
				<div>
					<input type="text" title="Type a twitter's user name"
						value="twit_street" id="stock"  /> <input type="button" value="Get User!" onclick="getstock();"/>
						<input type="hidden" value="twit_street" id="current_stock" />
				</div>
			</td>
		</tr>
		<tr>
			<td>
				<div id="follower-status">
					<table>
						<tr>
							<td colspan="2"><a href="ebertchicago" class="user">ebertchicago</a>'s
								followers</td>
						</tr>
						<tr>
							<td>Available</td>
							<td id="s_available"></td>
						</tr>
						<tr>
							<td>Sold</td>
							<td id="s_sold"></td>
						</tr>
						<tr>
							<td>Total</td>
							<td id="s_total"></td>
						</tr>
					</table>
				</div>
			</td>
			<td>
				<div id="user-follower-status">
					<table>
						<tr>
							<td colspan="2">You have <b>15</b> <a href="ebertchicago"
								class="user">ebertchicago </a>
							</td>
						</tr>
						<tr>
							<td><input type="text" />
							</td>
							<td><input type="button" value="Buy!" id="buy_amount" onclick="buy();" />
							</td>
						</tr>
						<tr>
							<td><input type="text" />
							</td>
							<td><input type="button" value="Sell" id="sell_amount" onclick="sell();"/>
							</td>
						</tr>
					</table>
				</div>
			</td>
		</tr>
		<tr>
			<td colspan="2">
				<div id="user-graph">
					<img
						src="http://chart.apis.google.com/chart?chxl=0:|followers&chxp=0,30&chxr=0,0,46&chxt=y&chs=300x225&cht=lc&chco=5599BB&chds=0,400&chd=t:172.08,76.632,80.991,219.803,294.186,400&chg=14.3,-1,1,1&chls=2,4,0&chm=B,DDEEFF,0,0,0&chtt=followers"
						width="300" height="225" alt="followers" />
				</div>
			</td>
		</tr>
	</table>
</div>