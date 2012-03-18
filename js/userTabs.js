
	userTabArray = new Array();

	userTabContentArray = new Array();
	userTabArray.push(".top-grossing-users-tab");
	userTabArray.push(".user-details-tab");
	
	userTabContentArray.push("#top-grossing-users-content");
	userTabContentArray.push("#user-details-content");

function showUserTab(tabId,tabContentId){
	var i;
	for(i=0; i<userTabArray.length; i++){
		
		if(tabId == userTabArray[i]){
			
			$(userTabArray[i]).addClass("youarehere");
		}
		else{
			
			$(userTabArray[i]).removeClass("youarehere");
		}
	}
	
	for(i=0; i<userTabContentArray.length; i++){
		
		if(tabContentId == userTabContentArray[i]){
			
			$(userTabContentArray[i]).show();
		}
		else{
			
			$(userTabContentArray[i]).hide();
		}
		
	}	
	
}

function showUserDetailsContent(){
	showUserTab(".user-details-tab","#user-details-content");
	
}

function showTopGrossingUsersContent(){
	showUserTab(".top-grossing-users-tab","#top-grossing-users-content");
	
}