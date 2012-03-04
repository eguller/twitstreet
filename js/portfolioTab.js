
	portfolioTabsArray = new Array();

	portfolioTabContentsArray = new Array();
	portfolioTabsArray.push(".portfolio-tab");
	portfolioTabsArray.push(".watchlist-tab");

	portfolioTabContentsArray.push("#portfolio-container");
	portfolioTabContentsArray.push("#watchlist-container");


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
			$(portfolioTabContentsArray[i]).fadeTo('fast',1,null);
		}
		else{
			$(portfolioTabContentsArray[i]).hide();
		}
		
	}	
	
}
