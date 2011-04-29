//Move to dashboard.js if we need to reuse.
function _dashboard(dashdiv) {
	return {
		reload: function(user) {
			//TODO ajax
			var userid = user.data('screen_name');
			dashdiv.children('.sectionHeader').text('Dashboard ('+userid+')');
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
					$('#tcp').append(profileImageTag + ' <a href="#" id=\"signout-link\">' + screenName + ', Sign out &gt;&gt;</a>');
					
					$("#signout-link").bind("click", function () {
						twttr.anywhere.signOut();
						window.location = '/logout'
					});
				} else {
					T('#tcp').connectButton({
						authComplete: function(user) {
							dashboard.reload(user);
						},
						signOut: function() {
						}
					});
				}
			}
			twttr.anywhere(ta);
		});
