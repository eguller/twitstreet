
$(document).ready(function() {
	$("#dashboard-message-field").corner("round 3px");

	if ($("#topranks").length > 0) {
		setInterval(reloadToprank, reloadInterval);
	}

	if ($("#transactions").length) {
		setInterval(reloadCurrentTransactions, reloadInterval);
	}

	if ($("#balance").length > 0) {
		setInterval(reloadBalance, reloadInterval);
	}

	if ($("#portfolio").length > 0) {
		setInterval(reloadPortfolio, reloadInterval);
	}
	if ($("#portfolio").length > 0) {
		setInterval(reloadWatchList, reloadInterval);
	}
	
	showTabMain(".stocks-tab");

});

function reloadBalance() {

	loadBalance(true);
}

function loadBalance(reload, doNotBlock) {
	ajaxLoad("balance", null, '#balance-container', '#balance-container',
			reload, null, null, doNotBlock);
}

function reloadPortfolio() {
	if ($(".portfolio-tab").hasClass("youarehere")) {
		loadPortfolio(true);
	}
}
function loadPortfolio(reload, doNotBlock) {
	ajaxLoad("/portfolio", null, "#portfolio-content", "#portfolio-content",
			reload, null, null, doNotBlock);
}

function reloadWatchList() {
	if ($(".watchlist-tab").hasClass("youarehere")) {
		loadWatchList(true);
	}
}
function loadWatchList(reload) {
	ajaxLoad("/watchlist", null, "#portfolio-content", "#portfolio-content",
			reload);
}

function addToWatchList(stockid) {
	ajaxLoad("/watchlist", "stock=" + stockid + "&operation=add",
			"#portfolio-content", "#portfolio-content", false,
			addToWatchListCallback, stockid);
	showTabPortfolio('.watchlist-tab', '#watchlist-content');
}
function addToWatchListCallback(stockid) {
	$(".add-to-watch-list-link-" + stockid).hide();
	$(".remove-from-watch-list-link-" + stockid).show();
}
function removeFromWatchList(stockid) {
	ajaxLoad("/watchlist", "stock=" + stockid + "&operation=remove",
			"#portfolio-content", "#portfolio-content", false,
			removeFromWatchListCallback, stockid);
	showTabPortfolio('.watchlist-tab', '#watchlist-content');
}

function removeFromWatchListCallback(stockid) {

	$(".remove-from-watch-list-link-" + stockid).hide();
	$(".add-to-watch-list-link-" + stockid).show();

}

function getQuote(quote) {
	if (quote.length > 0) {
		ajaxLoad("/getquote", "quote=" + quote, "#stocks-container",
				'#column_center');
		showTabMain('.stocks-tab');
	}
}


var stockOnScreen = "#hiddenStockDetailsStockId";
function reloadStock() {
	loadStock($(stockOnScreen).val());

}
function loadStock(id, reload) {

	showTabMain('.stocks-tab');
	showStockDetails();
	if(id!=null){
		ajaxLoad("stock", "stock=" + id, '#stocks-container', '#column_center');	
	}
	

}

function loadSuggestedStocks() {
	showTabMain('.stocks-tab');
	showSuggestedStocks();
	ajaxLoad("/suggestedstocks", null, "#suggested-stocks-content", "#column_center");
	

}

function loadTopGrossingStocks() {
	showTabMain('.stocks-tab');
	showTopGrossingStocks();
	ajaxLoad("/topgrossingstocks", null, "#top-grossing-stocks-content", "#column_center");
	

}
function loadTopGrossingUsers() {

	showTabMain('.users-tab');
	showTopGrossingUsersContent();
	ajaxLoad("/trendyusers", null, "#users-screen", "#users-screen");
}


function getUser(user) {
	if (user.length > 0) {
		ajaxLoad("/getuser", "getuser=" + user, "#users-container",
				"#column_center");
		showTabMain('.users-tab');
	}
}

var userOnScreen = "#hiddenUserDetailsUserId";
function reloadUserProfile() {
	loadUserProfile($(userOnScreen).val());
}
function loadUserProfile(userId, reload) {
	showTabMain('.users-tab');
	if (userId != null) {
		ajaxLoad("/user", "user=" + userId, "#users-container",
				"#column_center", reload);
	}
}

function reloadCurrentTransactions() {
	if ($(".currenttransactions-tab").hasClass("youarehere")) {
		loadCurrentTransactions(true);
	}
}

function loadCurrentTransactions(reload) {
	ajaxLoad("transaction", null, "#transactions-content",
			"#transactions-content", reload);

}

function reloadUserTransactions() {

	loadUserTransactions(true);
}

function loadUserTransactions(reload, doNotBlock) {
	if ($(".yourtransactions-tab").hasClass("youarehere")) {
		ajaxLoad("transaction", "type=user", "#transactions-content",
				"#transactions-content", reload, null, null, doNotBlock);
	}
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

	var responseNeeded = "";

	var containerDiv = '#buy-sell-container';
	if (stock != $(stockOnScreen).val()) {
		responseNeeded = "&response=n";
		containerDiv = '';
	}

	ajaxLoad("a/buy", "stock=" + stock + "&amount=" + amount + responseNeeded,
			containerDiv, containerDiv, false, buySellCallback, stock);
	showTabPortfolio('.portfolio-tab', '#portfolio-content');
}
function sell(stock, amount) {

	var responseNeeded = "";

	var containerDiv = '#buy-sell-container';
	if (stock != $(stockOnScreen).val()) {
		responseNeeded = "&response=n";
		containerDiv = '';
	}

	ajaxLoad("a/sell", "stock=" + stock + "&amount=" + amount + responseNeeded,
			containerDiv, containerDiv, false, buySellCallback, stock);
	showTabPortfolio('.portfolio-tab', '#portfolio-content');
}

function buySellCallback(stock) {

	if (stock == $(stockOnScreen).val()) {
		showBuySell();
	}
	loadPortfolio(false, true);
	loadBalance(false, true);
	loadUserTransactions(false, true);
	loadCurrentTransactions(false);

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

function toprankPrevPage() {

	var page = parseInt($(".topRankSelect:first").val());
	toprank(page - 1);

}
function toprankNextPage() {

	var page = parseInt($(".topRankSelect:first").val());
	toprank(page + 1);

}

function reloadToprank() {
	toprank(null, true);

}

function toprank(page, reload) {
	var pageParam = page;

	if (pageParam == null) {
		pageParam = $('.topRankSelect:first').val();

	}

	ajaxLoad("toprank", "page=" + pageParam, "#topranks-container",
			'#topranks-loading-div', reload);
}

function signout() {
	deletecookie("id");
	deletecookie("oauthtoken");
	document.location = "/signout";
}

function deletecookie(cookieName) {
	document.cookie = cookieName + "=;expires=Thu, 01-Jan-1970 00:00:01 GMT;";
}
function getCookie(c_name) {
	if (document.cookie.length > 0) {
		c_start = document.cookie.indexOf(c_name + "=");
		if (c_start != -1) {
			c_start = c_start + c_name.length + 1;
			c_end = document.cookie.indexOf(";", c_start);
			if (c_end == -1)
				c_end = document.cookie.length;
			return unescape(document.cookie.substring(c_start, c_end));
		}
	}
	return "";
}
