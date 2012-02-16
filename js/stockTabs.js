
	stockTabsArray = new Array();

	stockTabContentsArray = new Array();
	stockTabsArray.push("#buy-sell-tab");
	stockTabsArray.push("#stock-history-tab");
	stockTabsArray.push("#stock-distribution-tab");
	stockTabsArray.push("#tweets-of-user-tab");
	
	stockTabContentsArray.push("#buy-sell-section");
	stockTabContentsArray.push("#stock-trend-section");
	stockTabContentsArray.push("#stock-share-section");
	stockTabContentsArray.push("#tweets-of-user-section");

function showTab(tabId,tabContentId){
	var i;
	for(i=0; i<stockTabsArray.length; i++){
		
		if(tabId == stockTabsArray[i]){
			
			$(stockTabsArray[i]).addClass("youarehere");
		}
		else{
			
			$(stockTabsArray[i]).removeClass("youarehere");
		}
	}
	
	for(i=0; i<stockTabContentsArray.length; i++){
		
		if(tabContentId == stockTabContentsArray[i]){
			
			$(stockTabContentsArray[i]).show();
		}
		else{
			
			$(stockTabContentsArray[i]).hide();
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
	//runScriptsInElement($("#tweets-of-user-section").html());
}
