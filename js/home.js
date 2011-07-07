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

function gettwituser() {
	// see dashboard.jsp
	var twituser = $('#twituser').val();
	$.post('/a/gettwituser', {
		twituser : twituser
	}, function(data) {
        $('#s_total').html(data.total);
        $('#s_available').html(data.total - data.sold);
        $('#s_sold').html(data.sold);
	});
}

// Move to dashboard.js if we need to reuse.
function _dashboard(dashdiv) {
	return {
		reload : function(user) {
			// TODO ajax: get DashboardData for user in json
			var userid = user.data('id');
			$('.sectionHeader', dashdiv).text('Dashboard (' + userid + ')');
		}
	};
}

var callback;

function _login(logindiv) {
	var nli = $('div.Notloggedin',logindiv);
	var li = $('div.Loggedin',logindiv);
	var un = $('span.UserName',li);
	
	$('input.BtnLogin',nli).click( function(event) {
		var x,y;
		if ($.browser.msie) {//hacked together for IE browsers
			y = window.screenTop - 120;
			x = window.screenLeft + ((document.body.offsetWidth + 20)/2) - 800;
		}else{
			y = window.screenY;
			x = window.screenX + window.outerWidth - 800;
		}

		window.open('/login','twitstreet_login',
				'height=10,width=800,left='+x+',top='+y+
				',toolbar=0,scrollbars=0,status=0,resizable=0,location=0,menuBar=0'
				).focus();
		});

	return {
		callback : function(callbackData) {
			un.text(callbackData.screenName);
			nli.hide();
			li.show();
		}
	}
}

$(document).ready(
		function() {
			$('body').bind('ajaxSuccess', function(event, request, settings) {
				if (request.getResponseHeader('REQUIRES_AUTH') === '1') {
					window.location = '/';
				}
			});
			
			var dashboard = _dashboard($('#dashboard'));
			var login = _login($('#login'));
			callback = login.callback;
		});
