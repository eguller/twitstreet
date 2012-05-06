
	rankingTabArray = new Array();

	rankingTabContentArray = new Array();
	rankingTabArray.push(".user-ranking-tab");
	rankingTabArray.push(".group-ranking-tab");
	
	rankingTabContentArray.push("#user-ranking-content");
	rankingTabContentArray.push("#group-ranking-content");

function showRankingTab(tabId,tabContentId){
	var i;
	for(i=0; i<rankingTabArray.length; i++){
		
		if(tabId == rankingTabArray[i]){
			
			$(rankingTabArray[i]).addClass("youarehere");
		}
		else{
			
			$(rankingTabArray[i]).removeClass("youarehere");
		}
	}
	
	for(i=0; i<rankingTabContentArray.length; i++){
		
		if(tabContentId == rankingTabContentArray[i]){
			
			$(rankingTabContentArray[i]).show();
		}
		else{
			
			$(rankingTabContentArray[i]).hide();
		}
		
	}	
	
}
