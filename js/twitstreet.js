$(document).ready(function() {
  showQuotePanel("hide-all");
});

function post(action, _data, f) {
	$.ajax({
		  type: 'POST',
		  url: action,
		  data: _data,
		  success: f,
		  dataType: 'json'
		});
}

function getquote(quote){
	$("#quote").val(quote);
	getquote();
}

function getquote() {
	// see dashboard.jsp
	var quote = $('#quote').val();
	$("#quote-hidden").val(quote);
	$.post('/getquote', {
		quote : quote
	}, function(data) {
        if(data.result){
        	if(data.resultCode == 'user-notfound'){
        		
        	}
        	else{
        		$("#total").html(data.respObj.stock.total);
        		$("#quote-id").val(data.respObj.stock.id);
        		$("#user-stock-val").val(parseInt(data.respObj.stock.total * data.respObj.percentage));
        		var sold = calculateSold(data.respObj.stock.total, data.respObj.stock.sold);
        		$("#sold").html(sold);
        		$("#available").html(data.respObj.stock.total - sold);
        		writeBuyLinks();
        		writeSellLinks();
        		if(data.respObj.percentage == 0){
        			$("#user-stock").html("You do not have any " + quote);
        		}
        		else{
        			$("#user-stock").html("You have " + parseInt(data.respObj.stock.tota * data.respObj.percentage) + " of " + quote);
        		}
        		showQuotePanel("userfound");
        	}
        }
        else{
        	
        }
	});
}

function calculateSold(total, soldPercentage){
	return parseInt(total * soldPercentage);
}

function writeBuyLinks(){
	var buyLink = "";
	var cash = parseInt($("#cash").val());
	var available = parseInt($("#available").val());
	var quote = $("#quote-hidden").val();
	var min = cash;
	if(cash > available){
		min = available;
	}	
	var length = min.toString().length;
	for(var i = length; i > 0; i --){
		var amount = Math.pow(10,i - 1);
		var link = $("<a>"+amount+"</a>").attr('onclick', "buy(\'"+quote+"\',"+amount+");").addClass("buy");
		$("#buy-links").append(link);
		if( i > 1){
			$("#buy-links").append(" | ");
		}
		
	}
}

function writeSellLinks(){
	var length = parseInt($("#user-stock-val").val()) == 0 ? 0 : $("#user-stock-val").val().length;
	for(var i = length; i > 0; i --){
		var amount = Math.pow(10,i - 1);
		var link = $("<a>"+amount+"</a>").attr('onclick', "sell(\'"+quote+"\',"+amount+");").addClass("sell");
		$("#sell-links").append(link);
		if( i > 1){
			$("#sell-links").append(" | ");
		}
		
	}
}

function showQuotePanel(panel){
	var panels = new Array("userfound", "searchresult", "searchnoresult", "searchfailed");
	for ( var i=0, len=panels.length; i<len; ++i ){
		if(panels[i] == panel){
			$("#"+panels[i]).show();
		}
		else{
			$("#"+panels[i]).hide();
		}
	}	
}

function buy(stock, amount){
	$.post('/a/buy', {
		stock : stock,
		amount : amount
	}, function(data){
		alert('hello data');
	});
}

function sell(){
	alert('hello sell');
}

function setup(){
	var dbHost = $("#dbHost").val();
	var dbPort = $("#dbPort").val();
	var dbAdmin =$("#dbAdmin").val();
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
		if(data.result){
			window.location = '/';
		}
		else{
			$(".error").empty();
			$(".error").append(document.createElement("ul"));
			for(var i=0,len=data.reasons.length; value=data.reasons[i], i<len; i++) {
				var li = document.createElement("li");
				$(li).text(value);
				$(".error ul").append(li)
			}
		}
	});
}

