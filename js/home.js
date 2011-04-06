$(document).ready(
		function() {
			function ta(T) {
				var currentUser, screenName, profileImage, profileImageTag;

				if (T.isConnected()) {
					currentUser = T.currentUser;
					screenName = currentUser.data('screen_name');
					profileImage = currentUser.data('profile_image_url');
					profileImageTag = '<img src="' + profileImage + '"/>';
					$('#tcp').append(
							'Logged in as ' + profileImageTag + ' '
									+ screenName);
				} else {
					T('#tcp').connectButton();
				}
			}

			twttr.anywhere(ta);
		});