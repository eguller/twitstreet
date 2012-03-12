	transactionsTabsArray = new Array();

	transactionsTabContentsArray = new Array();
	transactionsTabsArray.push(".currenttransactions-tab");
	transactionsTabsArray.push(".yourtransactions-tab");

	transactionsTabContentsArray.push("#currenttransactions-content");
	transactionsTabContentsArray.push("#currenttransactions-content");


function showTabTransactions(tabId,tabContentId){
	var i;
	for(i=0; i<transactionsTabsArray.length; i++){
		
		if(tabId == transactionsTabsArray[i]){
			$(transactionsTabsArray[i]).addClass("youarehere");
		}
		else{
			$(transactionsTabsArray[i]).removeClass("youarehere");
		}
	}
	
	for(i=0; i<transactionsTabContentsArray.length; i++){
		if(tabContentId == transactionsTabContentsArray[i]){
			$(transactionsTabContentsArray[i]).fadeTo('fast',1,null);
		}
		else{
			$(transactionsTabContentsArray[i]).hide();
		}
		
	}	
	
}
