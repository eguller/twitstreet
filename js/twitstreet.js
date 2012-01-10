$(document).ready(function() {
	$("#quote").keyup(function(event){
	    if(event.keyCode == 13){
	        $("#getquotebutton").click();
	    }
	});
	
	$("#dashboard-message-field").corner("round 3px");
	$("#buy-links div").corner("round 5px");
});



setInterval(toprank, 5000);

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
        		$("#total").html(commasep(data.respObj.stock.total));
        		$("#total-hidden").value(data.respObj.stock.total);
        		
        		$("#quote-id").val(data.respObj.stock.id);
        		$("#user-stock-val").val(parseInt(data.respObj.stock.total));
        		
        		var sold = calculateSold(data.respObj.stock.total, data.respObj.stock.sold);
        		$("#sold").html(commasep(sold));
        		$("#sold-hidden").value(sold);
        		
        		$("#available").html(commasep(data.respObj.stock.total - sold));
        		$("#available-hidden").value(data.respObj.stock.total - sold);
        		$("#dashboard-stock-follower-status").html(data.respObj.stock.name+"\'s follower status");
        		$("#dashboard-picture").attr("src",data.respObj.stock.pictureUrl);
        		if(data.resultCode != 'min-follower-count'){
        			$("#buy-links-row").show();
        			$("#sell-links-row").show();
        			writeBuySellLinks();
	        		if(data.respObj.percentage == 0){
	        			$("#user-stock").html("You do not have any " + quote);
	        		}
	        		else{
	        			$("#user-stock").html("You have " + parseInt(data.respObj.stock.total * data.respObj.percentage) + " of " + quote);
	        		}
        		}
        		else{
        			$("#buy-links-row").hide();
        			$("#sell-links-row").hide();
        			$("#user-stock").html(data.respObj.stock.name + " has <b>" + data.respObj.stock.total + "</b> follower. <br>You cannot buy followers if total is less than <b>" + data.respObj.minFollowerCount +"</b>");
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

function writeBuySellLinks(){
	var cash = parseInt($("#cash").val());
	var available = parseInt($("#available-hidden").val());
	var quote = $("#quote-id").val();
	var min = cash;
	if(cash > available){
		min = available;
	}	
	
	var buyValues = new Array();
	var sellValues = new Array();
	if(min > 0){
		buyValues.push(min);
	}
	var i = min == 0 ? 0 : min.toString().length;
	$(".buy-sell-table").empty();
	for(; i > 0; i --){
		var amount = Math.pow(10,i - 1);
		buyValues.push(amount);
	}
	
	var sold = parseInt($("#sold-hidden").val());
	i = sold == 0 ? 0 : sold.toString().length;
	
	if(sold > 0 && sold != Math.pow(10,i - 1)){
		sellValues.push(sold);
	}	
	
	
	for(; i > 0; i --){
		var amount = Math.pow(10,i - 1);
		sellValues.push(amount);
	}
	
	i = 0
	while(true){
		var tr = $('<tr></tr>');
		var buyTd = $('<td></td>');
		var sellTd = $('<td></td>');
		if(i < buyValues.length){
			var div = $('<div></div>');
			$(div).html('Buy <br>' + commasep(buyValues[i]));
			$(div).attr('class', 'field-green');
			$(div).attr('onclick','buy('+quote+', '+buyValues[i]+');');
			$(div).corner("round 5px");
			$(buyTd).append($(div));
			
		}
		
		if(i < sellValues.length){
			var div = $('<div></div>');
			$(div).html( 'Sell <br>' + commasep(sellValues[i]));
			$(div).attr('class', 'field-red');
			$(div).attr('onclick','sell('+quote+', '+sellValues[i]+');');
			$(div).corner("round 5px");
			$(sellTd).append($(div));
		}
		
		tr.append(buyTd);
		tr.append(sellTd);
		$(".buy-sell-table").append(tr);
		
		i++;
		if(i > sellValues.length && i > buyValues.length){
			break;
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
		$("#total").html(data.stockTotal);
		$("#total-hidden").val(data.stockTotal);
		
		var sold =  parseInt( data.stockTotal * data.stockSold);
		$("#sold").html( commasep(sold) );
		$("#sold-hidden").val(sold);
		
		$("#available").html(data.stockTotal - sold);
		$("#available-hidden").val(data.stockTotal - sold);
		
		$("#cash_value").html(data.userCash + "$");
		$("#cash").val(data.userCash);
		$("#portfolio_value").html(data.userPortfolio);
		$("#total_value").html(data.userCash + data.userPortfolio);
		writeBuySellLinks();
	});
}

function sell(stock, amount){
	$.post('/a/sell', {
		stock : stock,
		amount : amount
	}, function(data){
		$("#total").html(commasep(data.stockTotal));
		$("#total-hidden").val(data.stockTotal);
		
		var sold =  parseInt( data.stockTotal * data.stockSold);
		$("#sold").html( commasep(sold) );
		$("#sold-hidden").val(sold);
		
		$("#available").html(commasep(data.stockTotal - sold));
		$("#available-hidden").val(data.stockTotal - sold);
		
		
		$("#cash_value").html(data.userCash + "$");
		$("#cash").val(data.userCash);
		$("#portfolio_value").html(data.userPortfolio + "$");
		$("#total_value").html(data.userCash + data.userPortfolio + "$");
		writeBuySellLinks();
	});
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

function toprank(){
	$("topranktable").empty();
	$.getJSON('/toprank', function(data) {
		  for(var i = 0, length = data.length; i < length; i ++){
			  var rank = data[i];
			  var tr = $("<tr></tr>");
			  tr.append($("<td>" + rank.rank + "</td>"));
			  tr.append($("<td>" + rank.userName +"</td>"));
			  tr.append($("<td>" + commasep(rank.cash + rank.portfolio)) + "</td>");
			  if(data.direction == 1){
				  tr.append($("td><img src=\"../images/up.png\" /></td>"));
			  }
			  else{
				  tr.append($("td><img src=\"../images/down.png\" /></td>"));
			  }			  
			  $("topranktable").append(tr);
		  }
		});
}

function commasep(number){
	var numberStr = number + '';
	var newNumber = '';
	var remaining = numberStr.length % 3 == 0 ? 3 : numberStr.length % 3;
    newNumber = numberStr.substr(0,remaining);
	
	for(var i = remaining; i < numberStr.length; i = i + 3){
		newNumber = newNumber + "," + numberStr.substr(i,3);
		
	}
	return newNumber;
}

