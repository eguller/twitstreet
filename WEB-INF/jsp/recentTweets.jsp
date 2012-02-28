<div id="recent-tweets" style="margin-top: 5px;">
<h3>
Tweets about us
</h3>
	<script charset="utf-8" src="http://widgets.twimg.com/j/2/widget.js"></script>
	<script>
		new TWTR.Widget({
			version : 2,
			type : 'search',
			search: 'twitstreet OR twitstreet_game OR #twitstreet OR from:twitstreet_game',
			rpp : 5,
			interval : 6000,
			width : 260,
			height : 300,
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
			    scrollbar: false,
			    loop: true,
			    live: true,
			    hashtags: true,
			    timestamp: true,
			    avatars: true,
			    toptweets: true,
				behavior : 'all'
			}
		}).render().start();
	</script>
</div>