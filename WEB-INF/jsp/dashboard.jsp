<div id="dashboard">
	<h3>Dasboard</h3>
	<div id="quoteholder">
		<input type="text" class="textbox" id="quote">
		<button class="button" onclick="getquote();">Get quote</button>
		<input type="hidden" id="quote-hidden" />
		<input type="hidden" id="quote-id" />
	</div>
	<div id="userfound">
		<p style="width: 100%; text-align: center"><span id="user-stock"></span></p>
		<input type="hidden" id="user-stock-val"/>
		<table>

			<tr>
				<td style="text-align: right">Total</td>
				<td>:</td>
				<td id="total"></td>
			</tr>
			<tr>
				<td style="text-align: right">Sold</td>
				<td>:</td>
				<td id="sold"></td>
			</tr>
			<tr>
				<td style="text-align: right">Available</td>
				<td>:</td>
				<td id="available"></td>
			</tr>
			<tr id="buy-links-row">
				<td style="text-align: right">Buy</td>
				<td>:</td>
				<td id="buy-links"></td>
			</tr>
			<tr id="sell-links-row">
				<td style="text-align: right">Sell</td>
				<td>:</td>
				<td id="sell-links"></td>
			</tr>
		</table>
	</div>
	<div id="searchresult">
		Has search result
	</div>
	<div id="searchnoresult">
		Search does not have result
	</div>
	<div id="searchfailed">
		Search failed.
	</div>
</div>

