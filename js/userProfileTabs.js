
	userProfileTabsArray = new Array();

	userProfileTabContentsArray = new Array();
	userProfileTabsArray.push("#user-status-tab");
	userProfileTabsArray.push("#portfolio-tab");
	userProfileTabsArray.push("#user-tweets-tab");

	userProfileTabsArray.push("#user-ranking-history-tab");
	
	userProfileTabContentsArray.push("#userstatus");
	userProfileTabContentsArray.push("#usertweets");
	userProfileTabContentsArray.push("#user-trend-section");
	
function showUserProfileTab(tabId,tabContentId){
	var i;
	for(i=0; i<userProfileTabsArray.length; i++){
		
		if(tabId == userProfileTabsArray[i]){
			
			$(userProfileTabsArray[i]).addClass("youarehere");
		}
		else{
			
			$(userProfileTabsArray[i]).removeClass("youarehere");
		}
	}
	
	for(i=0; i<userProfileTabContentsArray.length; i++){
		
		if(tabContentId == userProfileTabContentsArray[i]){
			
			$(userProfileTabContentsArray[i]).show();
		}
		else{
			
			$(userProfileTabContentsArray[i]).hide();
		}
		
	}	
	
}
