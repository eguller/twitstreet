//TODO: Move these into dashboard prototype
function post(action, _data, f) {
	$.ajax({
		  type: 'POST',
		  url: action,
		  data: _data,
		  success: f,
		  dataType: 'json'
		});
}

function getstock() {
	// see dashboard.jsp
	var stock = $('#stock').val();
	$("current_stock").val(stock);
	$.post('/a/getstock', {
		stock : stock
	}, function(data) {
        $('#s_total').html(data.total);
        $('#s_available').html(data.total - data.sold);
        $('#s_sold').html(data.sold);
	});
}

function buy(){
	var currentStock = $("current_stock").val();
	var amount = $("buy_amount").val();
	$.post('/a/buy', {
		stock : currentStock,
		amount : amount
	}, function(data){
		alert('hello data');
	});
}

function sell(){
	alert('hello sell');
}

function setup(){
	var dbIp = $("#dbIp").val();
	var dbPort = $("#dbPort").val();
	var dbAdmin =$("#dbAdmin").val();
	var dbAdminPassword = $("#dbAdminPassword").val();
	var dbName = $("#dbName").val();
	var admin = $("#admin").val();
	var adminPassword = $("#adminPassword").val();
	var consumerKey = $("#consumerKey").val();
	var consumerSecret = $("#consumerSecret").val();

	$.post('/setup', {
		dbIp : dbIp,
		dbPort : dbPort,
		dbAdmin : dbAdmin,
		dbAdminPassword : dbAdminPassword,
		dbName : dbName,
		admin : admin,
		adminPassword : adminPassword,
		consumerKey : consumerKey,
		consumerSecret : consumerSecret
	}, function(data) {
		
	});
}

