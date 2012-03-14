
function drawVisualization(divId){
	drawStockHistory(drawStockHistoryDivId[divId], drawStockHistoryDateArray[divId], drawStockHistoryValueArray[divId], drawStockHistoryStockName[divId]);
	drawStockDistribution(drawStockDistributionDivId,drawStockDistributionNameArray,drawStockDistributionPercentArray,drawStockDistributionStockName);

}





var drawStockHistoryDivId = new Array();
var drawStockHistoryDateArray = new Array();
var drawStockHistoryValueArray = new Array();
var drawStockHistoryStockName = new Array();



function drawStockHistory( divId, dateArray, valueArray, stockName, format,color) {
	
	drawStockHistoryDivId[divId] = divId;
	drawStockHistoryDateArray[divId] = dateArray;
	drawStockHistoryValueArray[divId] = valueArray;
	drawStockHistoryStockName[divId] = stockName;
	if($(divId).parent().css('display')!='none'){
		

		if (dateArray != null && dateArray.length > 0) {

			var data = new google.visualization.DataTable();
			data.addColumn('datetime', 'Date');
	//		data.addColumn('number', stockName);
			data.addColumn('number', '');

			data.addRows(dateArray.length);

			var i = 0;

			for (i; i < dateArray.length; i++) {

				data.setValue(i, 0, dateArray[i]);
				data.setValue(i, 1, valueArray[i]);

			}
			var annotatedtimeline = new google.visualization.AnnotatedTimeLine($(divId)[0]);
			
			if(format=='lastHour'){
				
				drawTimeLineLastHour(annotatedtimeline, data,color);
				
			}else if(format=='simple'){
				
				drawTimeLineSimple(annotatedtimeline,data,color);
			}else if(format!=null && format.indexOf('simple,') == 0){
				
				drawTimeLineSimple(annotatedtimeline,data,color,format.split(",")[1]);
			}else {
				
				drawTimeLine(annotatedtimeline,data,color);
			}
			
			

			google.visualization.events.addListener(annotatedtimeline, 'error', errHandler);
			
			
			
		

		}
	}
	

}
function drawTimeLine(annotatedtimeline,data,color){
	
	
	
	annotatedtimeline.draw(data, {
		// 'allValuesSuffix': '%', // A suffix that is added to all values
		'colors': (color)?[color]:undefined, // The colors to be used
		'displayAnnotations' : true,
		'displayExactValues' : true, // Do not truncate values (i.e.
		// using K suffix)
		'displayRangeSelector' : false, // Do not sow the range selector
		'displayZoomButtons' : true, // DO not display the zoom buttons
		'fill' : 30, // Fill the area below the lines with 20% opacity
		'displayLegendDots' : true,
		'legendPosition' : 'sameRow', // Can be sameRow
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

function drawTimeLineLastHour(annotatedtimeline,data,color){
	var ONE_HOUR = 60 * 60 * 1000;
	
	
	annotatedtimeline.draw(data, {
		// 'allValuesSuffix': '%', // A suffix that is added to all values
		 'colors': (color)?[color]:undefined, // The colors to be used
		'displayAnnotations' : true,
		'displayExactValues' : true, // Do not truncate values (i.e.
		// using K suffix)
		'displayRangeSelector' : false, // Do not sow the range selector
		'displayZoomButtons' : false, // DO not display the zoom buttons
		'fill' : 30, // Fill the area below the lines with 20% opacity
		'displayLegendDots' : true,
		'legendPosition' : 'sameRow', // Can be sameRow
		// 'max': 35000, // Override the automatic default
		// 'min': 30000, // Override the automatic default
		// 'scaleColumns': [0], // Have two scales, by the first and second
		// lines
		'scaleType' : 'allmaximized', // See docs...
		'thickness' : 3, // Make the lines thicker
		'zoomStartTime': new Date(new Date().getTime() - ONE_HOUR), //NOTE: month 1 = Feb
	// (javascript to blame)
	// 'zoomEndTime': new Date(2009, 1 ,5) //NOTE: month 1 = Feb (javascript
	// to blame)
	});
	
	
}

function drawTimeLineSimple(annotatedtimeline,data,color,minutes){
	var ONE_MINUTE =  60 * 1000;
	
	var xMinutesAgo =new Date(new Date().getTime() - ONE_MINUTE*minutes);
	annotatedtimeline.draw(data, {
		// 'allValuesSuffix': '%', // A suffix that is added to all values
		// 'colors': ['blue', 'red', '#0000bb'], // The colors to be used
		'colors': (color)?[color]:undefined, // The colors to be used 
		'displayAnnotations' : true,
		'displayExactValues' : true, // Do not truncate values (i.e.
		// using K suffix)
		'displayRangeSelector' : false, // Do not sow the range selector
		'displayZoomButtons' : false, // DO not display the zoom buttons
		'fill' : 30, // Fill the area below the lines with 20% opacity
		'displayLegendDots' : true,
		'legendPosition' : 'sameRow', // Can be sameRow
		// 'max': 35000, // Override the automatic default
		 //'min': 0, // Override the automatic default
		// 'scaleColumns': [0], // Have two scales, by the first and second
		// lines
		'scaleType' : 'allmaximized', // See docs...
		'thickness' : 3, // Make the lines thicker
		'zoomStartTime':(minutes)?xMinutesAgo:undefined,
		//'zoomStartTime': new Date(new Date().getTime() - ONE_HOUR*6), //NOTE: month 1 = Feb
	// (javascript to blame)
	// 'zoomEndTime': new Date(2009, 1 ,5) //NOTE: month 1 = Feb (javascript
	// to blame)
	});
	
	
}


function errHandler(err){
	
	
	alert(err);
}

var drawStockDistributionDivId = null;
var drawStockDistributionNameArray = null;
var drawStockDistributionPercentArray = null;
var drawStockDistributionStockName = null;


function reloadStockDistribution(stockId) {
	
	

	if ($('#stock-details-screen').hasClass('blockUI'))
		return;
	
	
	blockElementWithMsg('#stock-details-screen');
	    
	$.ajax({
		type: 		"get",
		url: 		"stockdistribution",
		data: 		"stock=" + stockId,
		success:	function(data) {		
			
			unblockElement('#stock-details-screen');
			$("#stock-share-section").empty();
			$("#stock-share-section").html($(data).html());
			runScriptsInElement(data);
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
	  

		  if ($(divId).parent().css('display') != 'none') {

		if (nameArray != null && nameArray.length > 0) {

			// Create the data table.
			var data = new google.visualization.DataTable();
			data.addColumn('string', 'User');
			data.addColumn('number', 'Share');
			var i = 0;
			for (i; i < nameArray.length; i++) {

				data.addRow([ nameArray[i], percentArray[i] ]);

			}

			// Set chart options
			var options = {
				/*'title':stockName,*/
				'width' : 500,
				'height' : 300,
				is3D : "true"
			};

			// Instantiate and draw our chart, passing in some options.
			var chart = new google.visualization.PieChart($(divId)[0]);
			chart.draw(data, options);

		}
	}

}




var drawUserValueHistoryDivId = null;
var drawUserValueHistoryValueArray = null;
var drawUserValueHistoryDateArray = null;
var drawUserValueHistoryUserName = null;


var drawUserRankingHistoryDivId = null;
var drawUserRankingHistoryDateArray = null;
var drawUserRankingHistoryRankArray = null;
var drawUserRankingHistoryUserName = null;

function redrawUserRankingHistory() {
	
	//drawUserRankingHistory(drawUserRankingHistoryDivId, drawUserRankingHistoryDateArray, drawUserRankingHistoryRankArray, drawUserRankingHistoryUserName);
	drawUserValueHistory(drawUserValueHistoryDivId, drawUserValueHistoryDateArray, drawUserValueHistoryValueArray, drawUserValueHistoryUserName);
	
	
}
function drawUserValueHistory(divId, dateArray, valueArray, userName){
	
	drawUserValueHistoryDivId = divId;
	drawUserValueHistoryValueArray = valueArray;
	drawUserValueHistoryDateArray = dateArray;
	drawUserValueHistoryUserName = userName;
	if($(divId).parent().css('display')!='none'){
		

		if (dateArray != null && dateArray.length > 0) {

			var data = new google.visualization.DataTable();
			data.addColumn('datetime', 'Date');
			data.addColumn('number', '');

			data.addRows(dateArray.length);

			var i = 0;

			for (i; i < dateArray.length; i++) {

				data.setValue(i, 0, dateArray[i]);
				data.setValue(i, 1, valueArray[i]);

			}
			var annotatedtimeline = new google.visualization.AnnotatedTimeLine($(divId)[0]);
			
			

			google.visualization.events.addListener(annotatedtimeline, 'error', errHandler);
			
			annotatedtimeline.draw(data, {

				// 'allValuesSuffix': '%', // A suffix that is added to all values
				 'colors': ['#F2469C'],//, 'red', '#0000bb'], // The colors to be used
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
			// 'zoomStartTime': new Date(2009, 1 ,2), //NOTE: mo
			});

		}
	}
	
	
}
function drawUserRankingHistory(divId, dateArray, rankArray, userName) {
	drawUserRankingHistoryDivId = divId;
	drawUserRankingHistoryDateArray = dateArray;
	drawUserRankingHistoryUserName = userName;
	drawUserRankingHistoryRankArray = rankArray;
	if($(divId).parent().css('display')!='none'){
		

		if (dateArray != null && dateArray.length > 0) {

			var data = new google.visualization.DataTable();
			data.addColumn('datetime', 'Date');
			data.addColumn('number', '');

			data.addRows(dateArray.length);

			var i = 0;

			for (i; i < dateArray.length; i++) {

				data.setValue(i, 0, dateArray[i]);

				data.setValue(i, 1, rankArray[i]);

			}
			var annotatedtimeline = new google.visualization.AnnotatedTimeLine($(divId)[0]);
			
			

			google.visualization.events.addListener(annotatedtimeline, 'error', errHandler);
			
			annotatedtimeline.draw(data, {
				// 'allValuesSuffix': '%', // A suffix that is added to all values
				 'colors': ['F2D046'], // The colors to be used
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
			// 'zoomStartTime': new Date(2009, 1 ,2), //NOTE: mo
              
			});

		}
	}
	

}