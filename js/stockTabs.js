
var stockTabsArray = new Array();

var stockTabContentsArray = new Array();


function initStockTabs(){

	stockTabsArray.push($("#buy-sell-tab"));
	stockTabsArray.push($("#stock-history-tab"));
	stockTabsArray.push($("#stock-distribution-tab"));
	stockTabsArray.push($("#tweets-of-user-tab"));
	
	stockTabContentsArray.push($("#buy-sell-section"));
	stockTabContentsArray.push($("#stock-trend-section"));
	stockTabContentsArray.push($("#stock-share-section"));
	stockTabContentsArray.push($("#tweets-of-user-section"));
	
	
}

function showTab(tabId,tabContentId){
	var i;
	for(i=0; i<stockTabsArray.length; i++){
		var element = stockTabsArray[i];
		if(element.attr('id') == $(tabId).attr('id')){
			
			element.addClass("youarehere");
		}
		else{
			
			element.removeClass("youarehere");
		}
	}
	
	for(i=0; i<stockTabContentsArray.length; i++){
		var element = stockTabContentsArray[i];
		if(element.attr('id') == $(tabContentId).attr('id')){
			
			element.show();
		}
		else{
			
			element.hide();
		}
		
	}
	
	
}

function showBuySell(){	
	showTab("#buy-sell-tab", "#buy-sell-section");
}

function showStockDistribution(id){
	showTab("#stock-distribution-tab","#stock-share-section");
	
	reloadStockDistribution(id);
}

function showStockHistory(){
	showTab("#stock-history-tab","#stock-trend-section");
	drawVisualization();
}
function showTweetsOfUser(){
	showTab("#tweets-of-user-tab","#tweets-of-user-section");
}
