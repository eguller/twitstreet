
	stockTabArray = new Array();

	stockTabContentArray = new Array();
	stockTabArray.push(".suggested-stocks-tab");
	stockTabArray.push(".stock-details-tab");
	stockTabArray.push(".top-grossing-stocks-tab");
	
	stockTabContentArray.push("#suggested-stocks-content");
	stockTabContentArray.push("#top-grossing-stocks-content");
	stockTabContentArray.push("#stock-details-content");

function showStockTab(tabId,tabContentId){
	var i;
	for(i=0; i<stockTabArray.length; i++){
		
		if(tabId == stockTabArray[i]){
			
			$(stockTabArray[i]).addClass("youarehere");
		}
		else{
			
			$(stockTabArray[i]).removeClass("youarehere");
		}
	}
	
	for(i=0; i<stockTabContentArray.length; i++){
		
		if(tabContentId == stockTabContentArray[i]){
			
			$(stockTabContentArray[i]).show();
		}
		else{
			
			$(stockTabContentArray[i]).hide();
		}
		
	}	
	
}

function showStockDetails(){
	showStockTab(".stock-details-tab","#stock-details-content");
	
}

function showSuggestedStocks(){
	showStockTab(".suggested-stocks-tab","#suggested-stocks-content");
	
}
function showTopGrossingStocks(){
	showStockTab(".top-grossing-stocks-tab","#top-grossing-stocks-content");
	
}