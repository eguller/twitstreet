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

function signup(){
	var userName = $("#signup_username").val();
	var email = $("#signup_email").val();
	var password ="#signup_password").val();
	var valid = true;
	if(userName == null || userName.length() < 4 || userName.length() > 9){
		valid = false;
	}
	if(password == null || password.length() == 0 || password.length() > 16){
		valid = false;
	}
	if(!validateemail(email)){
		valid = false;
	}

	$.post('/signup', {
		userName : userName,
		email : email,
		password : password
	}, function(data) {
		
	});
	
	
	
}
function validateemail(address) {
	var reg = /^([A-Za-z0-9_\-\.])+\@([A-Za-z0-9_\-\.])+\.([A-Za-z]{2,4})$/;
	if(reg.test(address) == false) {
		return false;
		}
		return true;
	}
}
