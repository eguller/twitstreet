$(document).ready(
		function() {
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
					T('#tcp').connectButton();
				}
			}

			twttr.anywhere(ta);
		});