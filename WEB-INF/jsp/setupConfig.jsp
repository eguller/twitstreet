<div id="dashboard">
	<h3>Twitstreet Setup</h3>
	<div id="setupConfigHolder">
		<table>
			<tr>
				<td>Database Ip</td>
				<td><input type="text" class="textbox" value="localhost" id="dbIp">
				</td>
			</tr>
			<tr>
				<td>Database Port</td>
				<td><input type="text" class="textbox" value="3306" id="dbPort"></td>
			</tr>
			<tr>
				<td>Database Name</td>
				<td><input type="text" class="textbox" value="twitstreet" id="dbName">
				</td>
			</tr>
			<tr>
				<td>Database Admin</td>
				<td><input type="text" class="textbox" value="root" id="dbAdmin"></td>
			</tr>
			<tr>
				<td>Database Admin Password</td>
				<td><input type="password" class="textbox" id="dbAdminPassword"></td>
			</tr>
						<tr>
				<td>Twitstreet Admin</td>
				<td><input type="text" class="textbox" value="admin" id="admin"></td>
			</tr>
			<tr>
				<td>Twitstreet Admin Password</td>
				<td><input type="password" class="textbox" id="adminPassword"></td>
			</tr>
			<tr>
				<td>Consumer Key</td>
				<td><input type="text" class="textbox" value="" id="consumerKey"></td>
			</tr>
			<tr>
				<td>Consumer Secret</td>
				<td><input type="text" class="textbox" value="" id="consumerSecret"></td>
			</tr>

			<tr>
				<td colspan="2" align="right"><button class="button" onclick = "setup();">Setup</button>
				</td>
			</tr>
		</table>


	</div>
</div>