	portfolioTabsArray = new Array();

	portfolioTabContentsArray = new Array();
	portfolioTabsArray.push(".portfolio-tab");
	portfolioTabsArray.push(".watchlist-tab");

	portfolioTabContentsArray.push("#portfolio-content");
	portfolioTabContentsArray.push("#watchlist-content");


function showTabPortfolio(tabId,tabContentId){
	var i;
	for(i=0; i<portfolioTabsArray.length; i++){
		
		if(tabId == portfolioTabsArray[i]){
			$(portfolioTabsArray[i]).addClass("youarehere");
		}
		else{
			$(portfolioTabsArray[i]).removeClass("youarehere");
		}
	}
	
	for(i=0; i<portfolioTabContentsArray.length; i++){
		if(tabContentId == portfolioTabContentsArray[i]){
			$(portfolioTabContentsArray[i]).show();
		}
		else{
			$(portfolioTabContentsArray[i]).hide();
		}
		
	}	
	
}
