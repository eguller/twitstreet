
	stockDetailsTabArray = new Array();

	stockDetailsTabContentArray = new Array();
	stockDetailsTabArray.push("#buy-sell-tab");
	stockDetailsTabArray.push("#stock-history-tab");
	stockDetailsTabArray.push("#stock-distribution-tab");
	stockDetailsTabArray.push("#tweets-of-user-tab");
	stockDetailsTabArray.push("#tweets-about-stock-tab");
	
	stockDetailsTabContentArray.push("#buy-sell-section");
	stockDetailsTabContentArray.push("#stock-trend-section");
	stockDetailsTabContentArray.push("#stock-share-section");
	stockDetailsTabContentArray.push("#tweets-of-user-section");
	stockDetailsTabContentArray.push("#tweets-about-stock-section");

function showStockDetailsTab(tabId,tabContentId){
	var i;
	for(i=0; i<stockDetailsTabArray.length; i++){
		
		if(tabId == stockDetailsTabArray[i]){
			
			$(stockDetailsTabArray[i]).addClass("youarehere");
		}
		else{
			
			$(stockDetailsTabArray[i]).removeClass("youarehere");
		}
	}
	
	for(i=0; i<stockDetailsTabContentArray.length; i++){
		
		if(tabContentId == stockDetailsTabContentArray[i]){
			
			$(stockDetailsTabContentArray[i]).show();
		}
		else{
			
			$(stockDetailsTabContentArray[i]).hide();
		}
		
	}	
	
}

function showBuySell(){	
	showStockDetailsTab("#buy-sell-tab", "#buy-sell-section");
}

function showStockDistribution(id){
	showStockDetailsTab("#stock-distribution-tab","#stock-share-section");
	reloadStockDistribution(id);
}

function showStockHistory(){
	showStockDetailsTab("#stock-history-tab","#stock-trend-section");
	drawVisualization('#detail-stock'+$("#hiddenStockDetailsStockId").val());
}
function showTweetsOfUser(){
	showStockDetailsTab("#tweets-of-user-tab","#tweets-of-user-section");
	//runScriptsInElement($("#tweets-of-user-section").html());
}
function showTweetsAboutStock(){
	showStockDetailsTab("#tweets-about-stock-tab","#tweets-about-stock-section");
	//runScriptsInElement($("#tweets-of-user-section").html());
}
