

var reloadInterval = 60 * 1000;



$(document).ready(function() {
	$("#dashboard-message-field").corner("round 3px");

	jQuery('#getQuoteTextboxId').click(function() {
		selectAllText(jQuery(this))
	});
	jQuery('#getUserTextboxId').click(function() {
		selectAllText(jQuery(this))
	});
	

	if ($("#topranks").length > 0) {
		setInterval(reloadToprank, reloadInterval);
	}

	if ($("#currenttransactions").length) {
		setInterval(reloadCurrentTransactions, reloadInterval);
	}

	if ($("#balance").length > 0) {
		setInterval(reloadBalance, reloadInterval);
	}

	if ($("#portfolio").length > 0) {
		setInterval(reloadPortfolio, reloadInterval);
	}
	if ($("#user-watch-list").length > 0) {
		setInterval(reloadWatchList, reloadInterval);
	}
	

});

function calculateSold(total, soldPercentage) {
	return total * soldPercentage;
}


var portfolioLastUpdate = new Date();


function reloadPortfolio() {
		
	if (timeToReload(portfolioLastUpdate)) {

		loadPortfolio(true);
	}	

	
}
function loadPortfolio(reload) {
	
	
	if(!reload){
		blockElementWithMsg('#portfolio-container');	
	}
	$.ajax({
		type : "get",
		url : "portfolio",
		success : function(data) {
			
			portfolioLastUpdate = (new Date()).getTime();
			
			unblockElement('#portfolio-container');	
			if (objectExists(data)) {
				$("#portfolio-container").empty();
				$("#portfolio-container").append($(data));
				
			}

		}
	});
}
var watchListLastUpdate = new Date();


function reloadWatchList() {
		
	if (timeToReload(watchListLastUpdate)) {

		loadWatchList(true);
	}	
}
function loadWatchList(reload) {
	if(!reload){
		blockElementWithMsg('#user-watch-list');	
	}
	$.ajax({
		type : "get",
		url : "watchlist",
		success : function(data) {
			watchListLastUpdate = (new Date()).getTime();
			unblockElement("#user-watch-list");
			if (objectExists(data)) {				
				$("#user-watch-list").empty();
				$("#user-watch-list").html($(data).html());
			}

		}
	});
}
function addToWatchList(stockid) {

	blockElementWithMsg("#user-watch-list");
	$.ajax({
		type : "get",
		url : "watchlist",
		data : "stock="+stockid+"&operation=add",
		success : function(data) {
			unblockElement("#user-watch-list");
			
			$("#user-watch-list").empty();
			$("#user-watch-list").html($(data).html());
			$(".add-to-watch-list-link-"+stockid).hide();
			$(".remove-from-watch-list-link-"+stockid).show();

		}
	});
}
function removeFromWatchList(stockid) {

	blockElementWithMsg("#user-watch-list");
	$.ajax({
		type : "get",
		url : "watchlist",
		data : "stock="+stockid+"&operation=remove",
		success : function(data) {
			unblockElement("#user-watch-list");
			
			$("#user-watch-list").empty();
			$("#user-watch-list").html($(data).html());
			$(".remove-from-watch-list-link-"+stockid).hide();
			$(".add-to-watch-list-link-"+stockid).show();
		}
	});
	
}




function getQuote(quote) {
	if (quote.length > 0) {
		
		blockElementWithMsg('#column_center', 'Loading');
		showTabMain('.stocks-tab','#stocks-container');
		
		$.ajax({
			type : "get",
			url : "/getquote",
			data : "quote=" + quote,
			success : function(data) {
				unblockElement("#column_center");
				
				$("#stocks-container").empty();
				$("#stocks-container").append($(data));

			}
		});
	}
}



function loadTrendyStocks() {
	
	blockElementWithMsg('#column_center', 'Loading');
	
	$.ajax({
		type : "get",
		url : "/trendystocks",
		success : function(data) {
			unblockElement("#column_center");
			
			$("#stockdetails").empty();
			$("#stockdetails").append($(data).html());

		}
	});

}
function getUser(user) {
	if (user.length > 0) {
		blockElementWithMsg('#column_center', 'Loading');
		showTabMain('.users-tab','#users-container');
		
		
		$.ajax({
			type : "get",
			url : "/getuser",
			data : "getuser=" + user,
			success : function(data) {
				unblockElement("#column_center");
				
				$("#users-container").empty();
				$("#users-container").append($(data));

			}
		});
	}
}


function loadUserProfile(userId,reload) {
	if (userId != null) {
		
		if(!reload){
			blockElementWithMsg('#column_center', 'Loading');
			showTabMain('.users-tab','#users-container');
			
		}
		$.ajax({
			type : "get",
			url : "/user",
			data : "user=" + userId,
			success : function(data) {
				unblockElement("#column_center");
				if (objectExists(data)) {				
					$("#users-container").empty();
					$("#users-container").append($(data));
				}
			}
		});
	}
}

var currentTransactionsLastUpdate = new Date();


function reloadCurrentTransactions() {
		
	if (timeToReload(currentTransactionsLastUpdate)) {

		loadCurrentTransactions(true);
	}	
}

function loadCurrentTransactions(reload) {
	if(!reload){
		blockElementWithMsg('#currenttransactions-container');
		
	}
	$.ajax({
		type : "get",
		url : "/transaction",
		success : function(data) {
			
			currentTransactionsLastUpdate = (new Date()).getTime();
			unblockElement("#currenttransactions-container");
			if (objectExists(data)) {
				$("#currenttransactions-container").empty();
				$("#currenttransactions-container").append($(data));
			}

		}
	});
}


var userTransactionsLastUpdate = new Date();


function reloadUserTransactions() {
		
	if (timeToReload(userTransactionsLastUpdate)) {

		loadUserTransactions(true);
	}	
}

function loadUserTransactions(reload) {
	if(!reload){
		blockElementWithMsg('#yourtransactions-container');
		
	}
	$.ajax({
		type : "get",
		url : "/transaction",
		data : "type=user",
		success : function(data) {
			
			userTransactionsLastUpdate = (new Date()).getTime();
			unblockElement("#yourtransactions-container");

						
			if (objectExists(data)) {

				$("#yourtransactions-container").empty();
				$("#yourtransactions-container").append($(data));
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
	blockElementWithMsg('#buy-sell-div', 'Processing');
	
	$.ajax({
		type : "get",
		url : "a/buy",
		data : "stock=" + stock + "&amount=" + amount,
		success : function(data) {
			$("#buy-sell-section").empty();
			$("#buy-sell-section").html($(data).html());

			loadPortfolio();
			loadBalance();
			loadUserTransactions();
		}
	});
}

function sell(stock, amount) {
	// if already clicked do nothing
	if ($('#buy-sell-div').hasClass('blockUI'))
		return;
	
	blockElementWithMsg('#buy-sell-div', 'Processing');
	
	$.ajax({
		type : "get",
		url : "a/sell",
		data : "stock=" + stock + "&amount=" + amount,
		success : function(data) {
			$("#buy-sell-section").empty();
			$("#buy-sell-section").html($(data).html());

			loadPortfolio();
			loadBalance();
			loadUserTransactions();
		}
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

function loadStock(id, reload) {

	if(!reload){
		blockElementWithMsg('#column_center', 'Loading');
		showTabMain('.stocks-tab','#stocks-container');
		
	}
	
	$.ajax({
		type : "get",
		url : "stock",
		data : "stock=" + id,
		success : function(data) {

			unblockElement("#column_center");
			
			$("#stocks-container").empty();
			$("#stocks-container").append($(data));
		

			
		}
	});

}
function toprankPrevPage(){
	
	var page = parseInt($(".topRankSelect:first").val());
	toprank(page-1);
	
}
function toprankNextPage(){
	
	var page = parseInt($(".topRankSelect:first").val());
	toprank(page+1);
	
}


var toprankLastUpdate = new Date();


function reloadToprank() {
		
	if (timeToReload(toprankLastUpdate)) {

		toprank(null, true);
	}	
}

function toprank(page,reload) {
	var pageParam = page;
	
	if(pageParam==null){
		pageParam = $('.topRankSelect:first').val();	
		
	}
	if(!reload){
		blockElementWithMsg('#topranks-loading-div', 'Loading');		
	}

	$.ajax({
		type : "get",
		url : "toprank",
		data : "page=" + pageParam,
		success : function(data) {
			
			toprankLastUpdate = (new Date()).getTime();
			unblockElement("#topranks-loading-div");
			
			$("#topranks").empty();
			$("#topranks").html($(data).html());
		}
	});
}

function runScriptsInElement(responseData) {
	var scriptArray = $(responseData).find("script").prevObject;

	for ( var i = 0; i < scriptArray.length; i++) {
		if (scriptArray[i] != null && scriptArray[i].text != null) {
			eval(scriptArray[i].text);

		}
	}

}


var balanceLastUpdate = new Date();


function reloadBalance() {
		
	if (timeToReload(balanceLastUpdate)) {

		loadBalance(true);
	}	
}

function loadBalance(reload) {
	if(!reload){
		blockElementWithMsg('#balance-container');	
	}
	$.ajax({
		type : "get",
		url : "balance",
		success : function(data) {
			
			balanceLastUpdate = (new Date()).getTime();
			unblockElement('#balance-container');	
			if (objectExists(data)) {
				$("#balance-container").empty();
				$("#balance-container").append($(data));
			}


		}
	});
}
function blockElementWithMsg(elementId, msg) {
	if(msg==null){
		msg = 'Loading';
		
	}
	if ($(elementId).hasClass('blockUI'))
		return;
	
	$(elementId).fadeTo('slow', 0.5, null);
	
	// block element
	$(elementId).block({
		message : '<img src="images/activity_indicator_32.gif" /><br>'+msg
	});

}
function unblockElement(elementId) {
	
	$(elementId).fadeTo('fast', 1,null);
	
	// block element
	$(elementId).unblock();

}
function commasep(nStr) {
	nStr = parseFloat(nStr).toFixed(2);
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

function roundedInteger(number) {
	if (parseInt(number) != number) {

		if (number < 0) {
			return parseInt(number - 1);
		} else if (number > 0) {
			return parseInt(number + 1);
		}
	}
	return number;
}

function getDouble(dbl, minval) {
	var pctgStr = dbl.toFixed(2);

	if (pctgStr == '0.00') {
		if (dbl < 0) {
			pctgStr = pctgStr.replace('0.00', '-' + minval.toFixed(2));
		} else if (dbl > 0) {

			pctgStr = pctgStr.replace('0.00', minval.toFixed(2));
		}
	}
	return pctgStr;
}

function objectExists(id) {
	return $(id).length > 0;
}

function showTweetsOfUserInDiv(username, elementId) {

	new TWTR.Widget({
		version : 2,
		type : 'profile',
		rpp : 20,
		interval : 30000,
		width : 500,
		height : 300,
		id : elementId,
		theme : {
			shell : {
				background : '#f2f2f2',
				color : '#000000'
			},
			tweets : {
				background : '#ffffff',
				color : '#000000',
				links : '#4183c4'
			}
		},
		features : {
			scrollbar : false,
			loop : false,
			live : false,
			behavior : 'all'
		}
	}).render().setUser(username).start();
}

function timeToReload(lastUpdate){
	
	return (new Date()).getTime() - lastUpdate > reloadInterval;
	
}