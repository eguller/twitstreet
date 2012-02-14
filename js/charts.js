
function drawVisualization(){
		
	drawStockHistory(drawStockHistoryDivId, drawStockHistoryDateArray, drawStockHistoryValueArray, drawStockHistoryStockName);
	drawStockDistribution(drawStockDistributionDivId,drawStockDistributionNameArray,drawStockDistributionPercentArray,drawStockDistributionStockName);

}





var drawStockHistoryDivId = null;
var drawStockHistoryDateArray = null;
var drawStockHistoryValueArray = null;
var drawStockHistoryStockName = null;

function setStockHistoryData(divId, dateArray, valueArray, stockName){
	

	  drawStockHistoryDivId = divId;
	  drawStockHistoryDateArray = dateArray;
	  drawStockHistoryValueArray = valueArray;
	  drawStockHistoryStockName = stockName;
	  
}	function setStockDistributionData(divId, dateArray, valueArray, stockName){
	

	 drawStockDistributionDivId = divId;
	 drawStockDistributionNameArray = nameArray;
	 drawStockDistributionPercentArray = percentArray;
	 drawStockDistributionStockName = stockName;
	  
}	



function drawStockHistory(divId, dateArray, valueArray, stockName) {

	drawStockHistoryDivId = divId;
	drawStockHistoryDateArray = dateArray;
	drawStockHistoryValueArray = valueArray;
	drawStockHistoryStockName = stockName;

	if (dateArray != null && dateArray.length > 0) {

		var data = new google.visualization.DataTable();
		data.addColumn('datetime', 'Date');
		data.addColumn('number', stockName);

		data.addRows(dateArray.length);

		var i = 0;

		for (i; i < dateArray.length; i++) {

			data.setValue(i, 0, dateArray[i]);
			data.setValue(i, 1, valueArray[i]);

		}
		var div = document.getElementById(divId);

		var annotatedtimeline = new google.visualization.AnnotatedTimeLine(div);
		annotatedtimeline.draw(data, {
			// 'allValuesSuffix': '%', // A suffix that is added to all values
			// 'colors': ['blue', 'red', '#0000bb'], // The colors to be used
			'displayAnnotations' : true,
			'displayExactValues' : true, // Do not truncate values (i.e.
			// using K suffix)
			'displayRangeSelector' : false, // Do not sow the range selector
			'displayZoomButtons' : true, // DO not display the zoom buttons
			'fill' : 30, // Fill the area below the lines with 20% opacity
			'displayLegendDots' : true,
			'legendPosition' : 'newRow', // Can be sameRow
			// 'max': 35000, // Override the automatic default
			// 'min': 30000, // Override the automatic default
			// 'scaleColumns': [0], // Have two scales, by the first and second
			// lines
			'scaleType' : 'allmaximized', // See docs...
			'thickness' : 3, // Make the lines thicker
		// 'zoomStartTime': new Date(2009, 1 ,2), //NOTE: month 1 = Feb
		// (javascript to blame)
		// 'zoomEndTime': new Date(2009, 1 ,5) //NOTE: month 1 = Feb (javascript
		// to blame)
		});

	}

}


var drawStockDistributionDivId = null;
var drawStockDistributionNameArray = null;
var drawStockDistributionPercentArray = null;
var drawStockDistributionStockName = null;


function reloadStockDistribution(stockId) {
	
	
	if ($('#stock-share-section').hasClass('blockUI'))
		return;
	
	$("#stock-share-section").empty();
	$('#stock-share-section').block({
		message : 'Loading'
	});
	    
	$.ajax({
		type: 		"get",
		url: 		"stockdistribution",
		data: 		"stock=" + stockId,
		success:	function(data) {			
			$('#stock-share-section').unblock();
			$("#stock-share-section").html($(data).html());
			 var script = $(data).find("script").prevObject[1].text;
				
				eval(script);

			
		
		}
	});
}
function redrawStockDistribution(){
	
	
	drawStockDistribution(drawStockDistributionDivId, drawStockDistributionNameArray,
			drawStockDistributionPercentArray, drawStockDistributionStockName);
}
function drawStockDistribution(divId,nameArray,percentArray,stockName) {
	 drawStockDistributionDivId = divId;
	  drawStockDistributionNameArray = nameArray;
	  drawStockDistributionPercentArray = percentArray;
	  drawStockDistributionStockName = stockName;
	
	
	var div = document.getElementById(divId);
	
	
	
    // Create the data table.
    var data = new google.visualization.DataTable();
    data.addColumn('string', 'User');
    data.addColumn('number', 'Share');
    var i = 0;
    for(i;i<nameArray.length; i++){
    	
    	data.addRow([nameArray[i], percentArray[i]]);	
    	
    }
    

    // Set chart options
    var options = {
    			 /*'title':stockName,*/
                   'width':500,
                   'height':300,is3D:"true"};
    


    // Instantiate and draw our chart, passing in some options.
    var chart = new google.visualization.PieChart(div);
    chart.draw(data, options);
    
	  
	 
	


}