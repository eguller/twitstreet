$('body').bind('ajaxSuccess', function(event, request, settings) {
	if (request.getResponseHeader('REQUIRES_AUTH') === '1') {
		window.location = '/';
	}
	;
});

function post(action, _data, f) {
	$.post(action, {
		data : JSON.stringify(_data)
	}, f, "json");
}

function gettwituser() {
	// see dashboard.jsp
	var twituser = $("#twituser").val();
	post("/a/gettwituser", {
		twituser : twituser
	}, function(data) {
		alert(data);
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

$(document).ready(
		function() {
			var dashboard = _dashboard($("#dashboard"));
			function ta(T) {
				var currentUser, screenName, profileImage, profileImageTag;

				if (T.isConnected()) {
					currentUser = T.currentUser;
					screenName = currentUser.data('screen_name');
					profileImage = currentUser.data('profile_image_url');
					profileImageTag = '<img src="' + profileImage + '"/>';
					$('#tcp').append(
							profileImageTag
									+ ' <a href="#" id=\"signout-link\">'
									+ screenName + ', Sign out &gt;&gt;</a>');

					$("#signout-link").bind("click", function() {
						twttr.anywhere.signOut();
						window.location = '/logout'
					});
				} else {
					T('#tcp').connectButton({
						authComplete : function(user, bridge_code) {
							setCookie("ts_bridge_code", bridge_code, null);
							dashboard.reload(user);
						},
						signOut : function() {
						}
					});
				}
			}
			twttr.anywhere.config({ callbackURL: "http://twitstreet.com/callback" });
			twttr.anywhere(ta);
		});

function setCookie(c_name, value, expiredays) {
	var exdate = new Date();
	exdate.setDate(exdate.getDate() + expiredays);
	document.cookie = c_name + "=" + escape(value)
			+ ((expiredays == null) ? "" : ";expires=" + exdate.toGMTString());
}
