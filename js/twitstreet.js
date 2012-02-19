$(document).ready(function() {
	$("#dashboard-message-field").corner("round 3px");
	$("#buy-links div").corner("round 5px");

	jQuery('#getQuoteTextboxId').click(function() {
		selectAllText(jQuery(this))
	});
	jQuery('#getUserTextboxId').click(function() {
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
		setInterval(reloadUserProfile, 20000);
	}

});

function calculateSold(total, soldPercentage) {
	return total * soldPercentage;
}
function loadPortfolio() {

	$.ajax({
		type : "get",
		url : "portfolio",
		success : function(data) {
			$("#portfolio").empty();
			$("#portfolio").html($(data).html());

		}
	});
}

// function goToUsers(){
//	
//	
// if(user.length>0){
// blockElementWithMsg('#column_center', 'Loading');
// $.ajax({
// type : "get",
// url : "/user",
// data : "quote=" + quote,
// success : function(data) {
// $("#column_center").unblock();
// $("#column_center").empty();
// $("#column_center").append($(data));
//
// }
// });
// }
//	
// }

function getQuote(quote) {
	if (quote.length > 0) {
		
		blockElementWithMsg('#column_center', 'Loading');
		showTabMain('#stocks-tab','#stocks-container');
		
		$.ajax({
			type : "get",
			url : "/getquote",
			data : "quote=" + quote,
			success : function(data) {
				$("#column_center").unblock();
				$("#stocks-container").empty();
				$("#stocks-container").append($(data).html());

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
			$("#column_center").unblock();
			$("#stockdetails").empty();
			$("#stockdetails").append($(data).html());

		}
	});

}
function getUser(user) {
	if (user.length > 0) {
		blockElementWithMsg('#column_center', 'Loading');
		showTabMain('#users-tab','#users-container');
		
		
		$.ajax({
			type : "get",
			url : "/getuser",
			data : "getuser=" + user,
			success : function(data) {
				$("#column_center").unblock();
				$("#users-container").empty();
				$("#users-container").append($(data));

			}
		});
	}
}
function reloadUserProfile() {
	var userId = $('#userProfileUserId').val();
	loadUserProfile(userId);
}
function loadUserProfile(userId,reload) {
	if (userId != null) {
		
		if(!reload){
			blockElementWithMsg('#column_center', 'Loading');
			showTabMain('#users-tab','#users-container');
			
		}
		$.ajax({
			type : "get",
			url : "/user",
			data : "user=" + userId,
			success : function(data) {
				$("#column_center").unblock();
				$("#users-container").empty();
				$("#users-container").append($(data));
			}
		});
	}
}

function loadCurrentTransactions() {
	$.ajax({
		type : "get",
		url : "/transaction",
		success : function(data) {
			$("#currenttransactions").empty();
			$("#currenttransactions").html($(data).html());

		}
	});
}

function loadUserTransactions() {
	$.ajax({
		type : "get",
		url : "/transaction",
		data : "type=user",
		success : function(data) {
			$("#yourtransactions").empty();
			$("#yourtransactions").html($(data).html());

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
	$.ajax({
		type : "get",
		url : "a/buy",
		data : "stock=" + stock + "&amount=" + amount,
		success : function(data) {
			$("#buy-sell-section").empty();
			$("#buy-sell-section").html($(data).html());

			loadPortfolio();
			loadUserTransactions();
		}
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
	$.ajax({
		type : "get",
		url : "a/sell",
		data : "stock=" + stock + "&amount=" + amount,
		success : function(data) {
			$("#buy-sell-section").empty();
			$("#buy-sell-section").html($(data).html());

			loadPortfolio();
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

function reloadStock(id){
	
	
	loadStock(id, true);
}
function loadStock(id, reload) {

	if(!reload){
		blockElementWithMsg('#column_center', 'Loading');
		showTabMain('#stocks-tab','#stocks-container');
		
	}
	
	$.ajax({
		type : "get",
		url : "stock",
		data : "stock=" + id,
		success : function(data) {

			$("#column_center").unblock();
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
function toprank(page) {
	var pageParam = page;
	
	if(pageParam==null){
		pageParam = $('.topRankSelect:first').val();	
		
	}
	
	$.ajax({
		type : "get",
		url : "toprank",
		data : "page=" + pageParam,
		success : function(data) {

			$("#topranks-loading-div").unblock();
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

function loadBalance() {

	$.ajax({
		type : "get",
		url : "balance",
		success : function(data) {

			$("#balance").unblock();
			$("#balance").empty();
			$("#balance").html($(data).html());
		}
	});
}

function retrievePage(pageElement, page) {
	// is this clicked one ?
	if (pageElement.attr("class") != 'active_tnt_link') {
		// make previous page number clickable
		var clicked = $('.active_tnt_link');
		clicked.removeClass();
		// and add href to it
		clicked.attr("href", "javascript:void(0)");

		// then add make new link disabled
		pageElement.attr('class', 'active_tnt_link');
		// remove href
		pageElement.removeAttr("href");

		blockElementWithMsg('#topranks-loading-div', 'Loading')

		$('.toprank-current-page').val(page);
		// finally load data
		toprank();
	}
}
function blockElementWithMsg(elementId, msg) {
	if ($(elementId).hasClass('blockUI'))
		return;
	// block element
	$(elementId).block({
		message : msg
	});

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
