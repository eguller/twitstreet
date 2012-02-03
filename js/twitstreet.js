$(document).ready(function() {
	$("#dashboard-message-field").corner("round 3px");
	$("#buy-links div").corner("round 5px");

	jQuery('#quote').click(function() {
		selectAllText(jQuery(this))
	});

	if ($("#topranks").length > 0) {
		setInterval(toprank, 20000);
	}

	if ($("#currenttransactions").length) {
		setInterval(loadCurrentTransactions, 20000);
	}

	if ($("#balance").length > 0) {
		setInterval(loadBalance, 20000);
	}

	if ($("#portfolio").length > 0) {
		setInterval(loadPortfolio, 20000);
	}
	if ($("#userprofile").length > 0) {
		setInterval(loadUserProfile, 20000);
	}

});

function calculateSold(total, soldPercentage) {
	return total * soldPercentage;
}

function writeBuySellLinks() {
	var cash = parseFloat($("#cash-hidden").val());
	var available = parseFloat($("#available-hidden").val());
	var quote = $("#quote-id").val();
	var min = cash;
	if (cash > available) {
		min = available;
	}

	var buyValues = new Array();
	var sellValues = new Array();
	if (min > 1) {
		buyValues.push(parseInt(min));
	}
	var i = min < 1 ? 0 : parseInt(min).toString().length;
	$(".buy-sell-table").empty();
	for (; i > 0; i--) {
		var amount = Math.pow(10, i - 1);
		buyValues.push(parseInt(amount));
	}

	var sold = parseInt($("#user-stock-val").val());
	i = sold < 1 ? 0 : sold.toString().length;

	if (sold > 0 && sold != Math.pow(10, i - 1)) {
		sellValues.push(sold);
	}

	for (; i > 0; i--) {
		var amount = Math.pow(10, i - 1);
		sellValues.push(parseInt(amount));
	}

	i = 0
	while (true) {
		var tr = $('<tr></tr>');
		var buyTd = $('<td></td>');
		var sellTd = $('<td></td>');
		if (i < buyValues.length) {
			var div = $('<div></div>');
			$(div).html('Buy <br>' + commasep(parseInt(buyValues[i])));
			$(div).attr('class', 'field-green');
			$(div).attr('onclick',
					'buy(' + quote + ', ' + parseInt(buyValues[i]) + ');');
			$(div).corner("round 5px");
			$(buyTd).append($(div));

		}

		if (i < sellValues.length) {
			var div = $('<div></div>');
			$(div).html('Sell <br>' + commasep(parseInt(sellValues[i])));
			$(div).attr('class', 'field-red');
			$(div).attr('onclick',
					'sell(' + quote + ', ' + parseInt(sellValues[i]) + ');');
			$(div).corner("round 5px");
			$(sellTd).append($(div));
		}

		tr.append(buyTd);
		tr.append(sellTd);
		$(".buy-sell-table").append(tr);

		i++;
		if (i > sellValues.length && i > buyValues.length) {
			break;
		}
	}
}

function loadPortfolio() {
	$.post('/portfolio', {

	}, function(data) {
		var stockInPortfolioList = data.stockInPortfolioList;
		$("#portfolio-table").empty();
		var i;
		for ( i = 0; i < stockInPortfolioList.length;) {
			var tr = $('<tr></tr>');
			for ( var j = 0; j < 1; j++) {
				var stockInPortfolio = null;

				if (i < stockInPortfolioList.length) {
					stockInPortfolio = stockInPortfolioList[i];
				}

				var td = $('<td></td>');
				var table = $('<table></table');
				var tableTr = $('<tr></tr>');

				var tableTd1 = $('<td></td>');
				if (stockInPortfolio != null) {
					var img = $('<img />').attr('class', 'twuser').attr('src',
							stockInPortfolio.pictureUrl);
					$(tableTd1).append(img);
				}

				var tableTd2 = $('<td></td>');
				if (stockInPortfolio != null) {
					var tdA = $('<a>' + stockInPortfolio.stockName + '</a>');
					tdA.attr('href', '/?stock='+stockInPortfolio.stockId);
					tdA.attr('title', 'Loads ' + stockInPortfolio.stockName + '\'s stock details.');
					
					var balance = stockInPortfolio.amount-stockInPortfolio.capital;
					var balanceStr = '<br><span>$'+commasep( balance.toFixed(2) )+'</span>';
					if(balance > 0){
						balanceStr = '<br><span class=\'green-light\'>$'+commasep( balance.toFixed(2) )  + '&nbsp; &#9650; </span>';
					}
					else if(balance < 0){
						balanceStr = '<br><span  class=\'red-light\'>$'+commasep( balance.toFixed(2) )  + '&nbsp; &#9660;</span>';
					}
					
					balance = (balance>0)?'+'+balance:balance;
					tableTd2.append(tdA).append(
							'<br>$' + commasep( stockInPortfolio.amount.toFixed(2) ) 
							).append(balanceStr);
				}
				$(tableTr).append(tableTd1);
				$(tableTr).append(tableTd2);
				$(table).append(tableTr);
				$(td).append(table);
				$(tr).append(td);
				i++;
			}
			$("#portfolio-table").append(tr);
		}
		if(i==0){
			var tr = $('<tr></tr>');
			var td = $('<td></td>');
			$("#portfolio-table").append(tr).append(tr).html(getNoRecordsFound());
			
		}
	});
}
function loadUserProfile() {
	var userId = $('#userProfileUserId').val();
	$.post('/user', {
		isAjaxRequest: true,
		user:userId
	}, function(data) {
		var user = data;
		var directionImage;
		$("#userProfileRank").html(user.rank+".");
		$("#userProfileCash").html('$'+commasep(user.cash.toFixed(2)));
		$("#userProfilePortfolio").html('$'+commasep(user.portfolio.toFixed(2)));
		$("#userProfileTotal").html('$'+commasep((user.cash+user.portfolio).toFixed(2)));
		
		if (user.direction > 0) {
			$("#userProfileDirection").html("<img src=\"/images/up_small.png\" />");
		} else if (user.direction < 0){
			$("#userProfileDirection").html("<img src=\"/images/down_small.png\" />");
		}else{
			//$("#userProfileDirection").html("<img src=\"/images/nochange_small.png\" />");
		}	
		
	});
}



function loadCurrentTransactions() {
	$
			.post(
					'/transaction',
					{
							
					},
					function(data) {
						if (data != null) {
							$("#current-transactions-table").empty();
							for ( var i = 0; i < data.length; i++) {
								var transactionRecord = data[i];
								var tr = $("<tr></tr>");
								if (i % 2 == 0) {
									$(tr).attr('class', 'odd');
								}
								var td = $('<td></td>');

								if (transactionRecord.operation == 1) {
									$(td)
											.html(
													"<a href=\"/user?user="
															+ transactionRecord.userId
															+ "\" title=\""+transactionRecord.userName+"&#39;s profile page.\">"
															+ transactionRecord.userName
															+ "</a> <span class=\"green\">bought</span> "
															+ commasep(transactionRecord.amount)
															+ " <a href=\"/?stock="+transactionRecord.stockId+"\" title=\""+transactionRecord.stockName+"&#39;s stock details page.\">"
															+ transactionRecord.stockName
															+ "</a>");
								} else {
									$(td)
											.html(
													"<a href=\"/user?user="
															+ transactionRecord.userId
															+ "\" title=\""+transactionRecord.userName+"&#39;s profile page.\">"
															+ transactionRecord.userName
															+ "</a> <span class=\"red\">sold</span> "
															+ commasep(transactionRecord.amount)
															+ " <a href=\"/?stock="+transactionRecord.stockId+"\" title=\""+transactionRecord.stockName+"&#39;s stock details page.\">"
															+ transactionRecord.stockName
															+ "</a>");
								}
								$(tr).append(td);
								$("#current-transactions-table").append(tr);
							}
						}
					});
}

function loadUserTransactions() {
	$.post('/transaction', {
		type : 'user'
	}, function(data) {
		if (data != null) {
			$("#your-transactions-table").empty();
			for ( var i = 0; i < data.length; i++) {
				var transactionRecord = data[i];
				var tr = $("<tr></tr>");
				if (i % 2 == 0) {
					$(tr).attr('class', 'odd');
				}
				var td = $('<td></td>');

				if (transactionRecord.operation == 1) {
					$(td).html(
							"You <span class=\"green\">bought</span> "
									+ commasep(transactionRecord.amount)
									+ " <a href=\"/?stock="+transactionRecord.stockId+"\" title=\""+transactionRecord.stockName+"&#39;s stock details page.\">"
									+ transactionRecord.stockName + "</a>");
				} else {
					$(td).html(
							"You <span class=\"red\">sold</span> "
									+ commasep(transactionRecord.amount)
									+ " <a href=\"/?stock="+transactionRecord.stockId+"\" title=\""+transactionRecord.stockName+"&#39;s stock details page.\">"
									+ transactionRecord.stockName + "</a>");
				}
				$(tr).append(td);
				$("#your-transactions-table").append(tr);
			}
		}
	});
}

function showQuotePanel(panel) {
	var panels = new Array("userfound", "searchresult", "searchnoresult",
			"searchfailed");
	for ( var i = 0, len = panels.length; i < len; ++i) {
		if (panels[i] == panel) {
			$("#" + panels[i]).show();
		} else {
			$("#" + panels[i]).hide();
		}
	}
}

function buy(stock, amount) {
	// if already clicked do nothing
	if ($('#buy-sell-div').hasClass('blockUI'))
		return;
	// block element
	$('#buy-sell-div').block({
		message : 'Processing'
	});

	$.post('/a/buy', {
		stock : stock,
		amount : amount
	}, function(data) {
		// unblock when data arrives
		$('#buy-sell-div').unblock();

		$("#total").html(commasep(parseInt(data.stockTotal)));
		$("#total-hidden").val(parseInt(data.stockTotal));

		var sold = parseInt(data.stockTotal * data.stockSold);
		$("#sold").html(commasep(sold));
		$("#sold-hidden").val(sold);

		$("#available").html(commasep(parseInt(data.stockTotal - sold)));
		$("#available-hidden").val(parseInt(data.stockTotal - sold));

		$("#cash_value").html("$" + commasep(data.userCash.toFixed(2)));
		$("#cash-hidden").val(data.userCash);
		$("#portfolio_value").html(
				"$" + commasep(data.userPortfolio.toFixed(2)));
		$("#total_value")
				.html(
						commasep("$"
								+ (data.userCash + data.userPortfolio)
										.toFixed(2)));
		$("#user-stock-val").val(
				parseInt(data.userStock)
						);
		
		$("#user-stock").html(
				"You have <b>" + commasep(parseInt(data.userStock))
						+ "</b> of " + data.stockName);
		
		writeBuySellLinks();

		loadPortfolio();
		loadUserTransactions();
	});
}

function sell(stock, amount) {
	// if already clicked do nothing
	if ($('#buy-sell-div').hasClass('blockUI'))
		return;
	// block element
	$('#buy-sell-div').block({
		message : 'Processing'
	});

	$.post('/a/sell', {
		stock : stock,
		amount : amount
	}, function(data) {
		// unblock when data arrives
		$('#buy-sell-div').unblock();

		$("#total").html(parseInt(data.stockTotal));
		$("#total-hidden").val(parseInt(data.stockTotal));

		var sold = data.stockTotal * data.stockSold;
		$("#sold").html(parseInt(sold));
		$("#sold-hidden").val(parseInt(sold));

		$("#available").html(parseInt((data.stockTotal - sold)));
		$("#available-hidden").val( parseInt(data.stockTotal - sold));

		$("#cash_value").html("$" + commasep(data.userCash.toFixed(2)));
		$("#cash-hidden").val(data.userCash);
		$("#portfolio_value").html(
				"$" + commasep(data.userPortfolio.toFixed(2)));
		$("#total_value")
				.html(
						"$"
								+ commasep((data.userCash + data.userPortfolio)
										.toFixed(2)));
		
		$("#user-stock-val").val(
				parseInt(data.userStock)
						);
		
		$("#user-stock").html(
				"You have <b>" + commasep(parseInt(data.userStock)) + "</b> of "
						+ data.stockName);
		
		writeBuySellLinks();

		loadPortfolio();
		loadUserTransactions();

		// update cash on top rank list
		$('#userOnTopRank').text($("#total_value").text());
	});
}

function setup() {
	var dbHost = $("#dbHost").val();
	var dbPort = $("#dbPort").val();
	var dbAdmin = $("#dbAdmin").val();
	var dbAdminPassword = $("#dbAdminPassword").val();
	var dbName = $("#dbName").val();
	var admin = $("#admin").val();
	var adminPassword = $("#adminPassword").val();
	var consumerKey = $("#consumerKey").val();
	var consumerSecret = $("#consumerSecret").val();

	$.post('/setup', {
		dbHost : dbHost,
		dbPort : dbPort,
		dbAdmin : dbAdmin,
		dbAdminPassword : dbAdminPassword,
		dbName : dbName,
		admin : admin,
		adminPassword : adminPassword,
		consumerKey : consumerKey,
		consumerSecret : consumerSecret
	}, function(data) {
		if (data.result) {
			window.location = '/';
		} else {
			$(".error").empty();
			$(".error").append(document.createElement("ul"));
			for ( var i = 0, len = data.reasons.length;
					value = data.reasons[i], i < len; i++) {
				var li = document.createElement("li");
				$(li).text(value);
				$(".error ul").append(li)
			}
		}
	});
}

function toprank() {
	$("topranktable").empty();
	var pageParam = $('.active_tnt_link').text();
	$.getJSON('/toprank?page=' + pageParam , function(data) {
		$("#topranktable").empty();
		
		for ( var i = 0, length = data.length; i < length; i++) {
			var user = data[i];
			var tr = $("<tr></tr>");
			if (i % 2 == 0) {
				tr.attr('class', 'odd');
			}
			$(tr).append(
					$("<td class=\'rank-number\'>" + user.rank + ". </td>"));
			$(tr).append(
					$("<td><img class=\'twuser\' src=\'" + user.pictureUrl
							+ "\'/></td>"));
			$(tr).append(
					$("<td><a href=\"/user?user=" + user.id + "\" title=\""+user.userName+"&#39;s profile page.\">" + user.userName
							+ "</a> <br>$"
							+ commasep((user.cash + user.portfolio).toFixed(2))
							+ '</td>'));
			if (user.direction > 0) {
				$(tr).append($("<td><img src=\"/images/up.png\" /></td>"));
			} else if (user.direction < 0){
				$(tr).append($("<td><img src=\"/images/down.png\" /></td>"));
			}else{
				//$(tr).append($("<td><img src=\"/images/nochange.png\" /></td>"));
			}
			$("#topranktable").append(tr);
		}
	});

}

function loadBalance() {
	$.post('/balance', {

	}, function(data) {
		if (data != null) {
			//$("#balance_rank").html(data.rank + ".");
			if (data.direction > 0) {
				$("#balance_direction").html(
						data.rank + "."+"<img src=\"/images/up_small.png\" />");
			} else if (data.direction < 0){
				$("#balance_direction").html(
						data.rank + "."+"<img src=\"/images/down_small.png\" />");
			}else {
				$("#balance_direction").html(data.rank + ".");//"<img src=\"/images/nochange_small.png\" />"
			}

			$("#cash_value").html("$" + commasep(data.cash.toFixed(2)));
			$("#portfolio_value").html(
					"$" + commasep(data.portfolio.toFixed(2)));
			$("#total_value").html("$" + commasep(data.total.toFixed(2)));
		}
	});
}

function retrievePage(pageElement) {
	// is this clicked one ?
	if (pageElement.attr("class") != 'active_tnt_link') {
		// make previous page number clickable
		var clicked = $('.active_tnt_link');
		clicked.removeClass();
		// and add href to it
		clicked.attr("href", "javascript:void(0)");
		
		// then add make new link disabled
		pageElement.attr('class','active_tnt_link');
		// remove href
		pageElement.removeAttr("href");
		
		// finally load data
		toprank();
	}
}

function commasep(nStr) {
	
	nStr += '';
	x = nStr.split('.');
	x1 = x[0];
	x2 = x.length > 1 ? '.' + x[1] : '';
	var rgx = /(\d+)(\d{3})/;
	while (rgx.test(x1)) {
		x1 = x1.replace(rgx, '$1' + ',' + '$2');
	}
	return x1 + x2;
}


function getNoRecordsFound() {
	
	return '<p>No records found.</p>';
}

function selectAllText(textbox) {
	textbox.focus();
	textbox.select();
}
