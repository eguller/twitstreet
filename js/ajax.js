

var reloadInterval = 60 * 1000;

var lastUpdateMap = new Object();
function ajaxLoad(url, queryString, containerDiv, loadingDiv, isReload, callbackFunction,callbackParameters, doNotBlock){
	
	if(!isReload && !doNotBlock){
		blockElementWithMsg(loadingDiv);		
	}
	else if(isReload && !timeToReload(lastUpdateMap[containerDiv])){
		return;
	}
	$.ajax({
		type : "get",
		url : url,
		data : queryString,
		success : function(data) {
			
			
			lastUpdateMap[containerDiv] = new Date();
			unblockElement(loadingDiv);
			
			
			
			if($(data).length>0){
				$(containerDiv).empty();
				$(containerDiv).append($(data))	
			}
			
			
			if(typeof callbackFunction == 'function'){
				if(callbackParameters!=null){
					callbackFunction(callbackParameters);
				}
				else{					
					callbackFunction(data);	
				}
			};
		}
	});
	
	
}

function timeToReload(lastUpdate){
	if(lastUpdate==null) return true;
	return (new Date()).getTime() - lastUpdate.getTime() > reloadInterval;
	
}