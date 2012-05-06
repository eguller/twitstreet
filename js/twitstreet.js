
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

function loadSuggestedStocks(page) {
	if(page==null){
		page = 1;
	}
	showTabMain('.stocks-tab');
	showSuggestedStocks();
	ajaxLoad("/suggestedstocks", "page=" + page, "#suggested-stocks-content", "#column_center");
	

}

function loadTopGrossingStocks() {
	showTabMain('.stocks-tab');
	showTopGrossingStocks();
	ajaxLoad("/topgrossingstocks", null, "#top-grossing-stocks-content", "#column_center");
	

}
function loadTopGrossingUsers() {

	showTabMain('.users-tab');
	showTopGrossingUsersContent();
	ajaxLoad("/trendyusers", null, "#top-grossing-users-content","#column_center");
}
function loadNewUsers(){

	showTabMain('.users-tab');

	showNewUsersContent();
	ajaxLoad("/newusers", null, "#new-users-content","#column_center");
}

function getGroup(group) {
	if (group.length > 0) {
		ajaxLoad("/getgroup", "getgroup=" + group, "#groups-container",
				"#column_center");
		showTabMain('.groups-tab');
	}
}

var groupOnScreen = "#hiddenGroupDetailsGroupId";
function reloadGroup() {
	loadGroup($(groupOnScreen).val());
}


var createGroupName = "";
function createGroup(name) {
	if (name != null && name.length>0) {
		createGroupName = name;
		ajaxLoad("/creategroup", "name=" + name, null, "#column_center",false,createGroupCallback);
	}
}
function createGroupCallback(createdGroupId) {
	
	showTabMain('.groups-tab');
	showGroupDetailsContent();
	loadGroup(createdGroupId);

}
function loadGroup(id, reload,page) {
	showTabMain('.groups-tab');
	if (id != null) {
		ajaxLoad("/groupdetails", "group=" + id+"&page="+page, "#groups-container",
				"#column_center", reload);
	}
}
function loadGroupUsers(page) {
	 loadGroup($(groupOnScreen).val(), false,page); 
}

function blockUserForGroup(userId, groupId) {
	if (userId != null && groupId != null) {
		
		ajaxLoad("/group", "group=" + groupId+"&op=blockuser&user="+userId, "#groups-container",
				"#column_center",false,groupOperationCallback);
	}
}
function unblockUserForGroup(userId, groupId) {
	if (userId != null && groupId != null) {
		
		ajaxLoad("/group", "group=" + groupId+"&op=unblockuser&user="+userId, "#groups-container",
				"#column_center",false,groupOperationCallback);
	}
}
function removeUserFromGroup(userId, groupId) {
	if (userId != null && groupId != null) {
		
		ajaxLoad("/group", "group=" + groupId+"&op=removeuser&user="+userId, "#groups-container",
				"#column_center",false,groupOperationCallback);
	}
}
function deleteGroup(id,confirmMsg) {
	
	if(confirmMsg==null||confirmMsg.length<1|| id == null) return;
	
	if(confirm(confirmMsg)){

		ajaxLoad("/group", "group=" + id+"&op=delete", "#groups-container",
					"#column_center",false,groupOperationCallback);
	}
	
}

function joinGroup(id) {
	if (id != null) {
		ajaxLoad("/group", "group=" + id+"&op=join", "#groups-container",
				"#column_center",false,groupOperationCallback);
	}
}
function joinGroupByReference(id) {
	if (id != null) {
		ajaxLoad("/group", "group=" + id+"&op=joinByReference", "#groups-container",
				"#column_center",false,groupOperationCallback);
	}
}
function leaveGroup(id) {
	if (id != null) {
		
		ajaxLoad("/group", "group=" + id+"&op=leave", "#groups-container",
				"#column_center",false,groupOperationCallback);
	}
}
function enableEntranceForGroup(id) {
	if (id != null) {
		ajaxLoad("/group", "group=" + id+"&op=enableentrance", "#groups-container",
				"#column_center",false,groupOperationCallback);
	}
}

function disableEntranceForGroup(id) {
	if (id != null) {
		ajaxLoad("/group", "group=" + id+"&op=disableentrance", "#groups-container",
				"#column_center",false,groupOperationCallback);
	}
}

function groupOperationCallback(){
	if(tabSelected(".group-list-tab")){
		reloadGroupList();
	}else if(tabSelected(".my-groups-tab")){
		reloadMyGroups();
	}
	if(tabSelected(".group-details-tab")){
		reloadGroup();
	}
	
	
}
var groupListPage = 1;
function loadGroupList(page) {
	if(page!=null){
		groupListPage = page;
	}
	showTabMain('.groups-tab');
	showGroupListContent();
	ajaxLoad("/grouplist", "page="+ groupListPage, "#groups-screen","#column_center");
}
function reloadGroupList() {
	loadGroupList(groupListPage);
}
var myGroupsPage = 1;
function loadMyGroups(page) {
	if(page!=null){
		myGroupsPage = page;
	}
	showTabMain('.groups-tab');
	showMyGroupsContent();
	ajaxLoad("/grouplist", "page="+ myGroupsPage+"&type=my", "#groups-screen","#column_center");
}
function reloadMyGroups() {
	loadMyGroups(myGroupsPage);
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
function loadUserHistory(userId, seasonId) {
	if (userId != null && seasonId!=null) {
		ajaxLoad("/userhistory", "user=" + userId+"&season="+seasonId, "#userRankingHistoryId",
				"#column_center");
	}
}
function loadSeasonResults(seasonId) {
	if (seasonId!=null) {
		ajaxLoad("/season", "season=" + seasonId , "#oldseasons-container",
				"#column_center");
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

function sellAll(stockId){
	
	
	sell(stockId,"all");
}

function sell(stock, amount) {

	var responseNeeded = "";

	var containerDiv = '#buy-sell-container';


	ajaxLoad("a/sell", "stock=" + stock + "&amount=" + amount + responseNeeded,
			containerDiv, containerDiv, false, buySellCallback, stock);
	showTabPortfolio('.portfolio-tab', '#portfolio-content');
}

function buySellCallback(stock) {

	if (stock == $(stockOnScreen).val()) {
		showBuySell();
	}else{
		if(tabSelected(".stock-details-tab")){
			reloadStock();
		}
	
	}
	loadPortfolio(false, true);
	loadBalance(false, true);
	loadUserTransactions(false, true);
	loadCurrentTransactions(false);
	reloadToprank();

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
	toprank(null,null, true);

}

function toprank(page, type, reload,group) {
	var pageParam = page;

	if (pageParam == null) {
		pageParam = $('.toprankSelect:first').val();

	}
	if (type == null) {
		type = $('.topRankSelectSeason:first').val();

	}
	if (group == null) {
		group = $('.topRankSelectGroup:first').val();

	}

	
	if(tabSelected(".user-ranking-tab")){

		ajaxLoad("toprank", "page=" + pageParam+ "&type=" + type+ "&group=" + group,"#topranks-screen",
				"#topranks-screen", reload);
	}else{

		ajaxLoad("toprankgroup", "page=" + pageParam+ "&type=" + type, "#topranks-screen",
				"#topranks-screen", reload);
	}
	

}
function toprankGroup(page, type, reload) {
	var pageParam = page;

	if (pageParam == null) {
		pageParam = $('.toprankSelect:first').val();

	}
	if (type == null) {
		type = $('.topRankSelectSeason:first').val();

	}

		ajaxLoad("toprankgroup", "page=" + pageParam+ "&type=" + type, "#topranks-screen",
				'#topranks-screen', reload);

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
