<div id="topranks">
	<h3>Ranking</h3>
	<table class="datatbl">
		<script language="javascript" type="text/javascript">
			for ( var i = 0; i < 100; i++) {
				if (i % 2 == 1){
					document.write('<tr>');
				}
				else{
					document.write('<tr class=\'odd\'>');
				}
				document.write('<td>');
				document.write(i + 1 + '.');
				document.write('</td>');
				document.write('<td>');
				document.write(randomString());
				document.write('</td>');
				document.write('<td>');
				document.write(randomNumber() + '$');
				document.write('</td>');
				document.write('<td>');
				if(Math.random() < 0.5){
					document.write('<img src=\'../images/down.gif\'');
				}
				else{
					document.write('<img src=\'../images/up.gif\'');
				}
				document.write('</td>');
				document.write('</tr>');
			}
		</script>
	</table>
</div>