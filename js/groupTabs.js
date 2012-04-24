
	groupTabArray = new Array();

	groupTabContentArray = new Array();
	groupTabArray.push(".group-list-tab");
	groupTabArray.push(".my-groups-tab");
	groupTabArray.push(".group-details-tab");
	
	groupTabContentArray.push("#group-list-content");
	groupTabContentArray.push("#group-details-content");

function showGroupTab(tabId,tabContentId){
	var i;
	for(i=0; i<groupTabArray.length; i++){
		
		if(tabId == groupTabArray[i]){
			
			$(groupTabArray[i]).addClass("youarehere");
		}
		else{
			
			$(groupTabArray[i]).removeClass("youarehere");
		}
	}
	
	for(i=0; i<groupTabContentArray.length; i++){
		
		if(tabContentId == groupTabContentArray[i]){
			
			$(groupTabContentArray[i]).show();
		}
		else{
			
			$(groupTabContentArray[i]).hide();
		}
		
	}	
	
}

function showGroupDetailsContent(){
	showGroupTab(".group-details-tab","#group-details-content");
	
}

function showMyGroupsContent(){
	showGroupTab(".my-groups-tab","#my-groups-content");
	
}

function showGroupListContent(){
	showGroupTab(".group-list-tab","#group-list-content");
	
}