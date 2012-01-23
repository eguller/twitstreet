<div id="recent-tweets" style="margin-top: 5px;">
	<script charset="utf-8" src="http://widgets.twimg.com/j/2/widget.js"></script>
	<script>
		new TWTR.Widget({
			version : 2,
			type : 'profile',
			rpp : 5,
			interval : 30000,
			width : 240,
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
				scrollbar : false,
				loop : false,
				live : false,
				behavior : 'all'
			}
		}).render().setUser('twitstreet_game').start();
	</script>
</div>