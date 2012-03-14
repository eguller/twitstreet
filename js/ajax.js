

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
				$(containerDiv).append($(data));
			}
			
			
			if(typeof callbackFunction == 'function'){
				if(callbackParameters!=null){
					callbackFunction(callbackParameters);
				}
				else{					
					callbackFunction(data);	
				}
			};
			
			
			widgetLoad();
		}
	});
	
	
}

function timeToReload(lastUpdate){
	if(lastUpdate==null) return true;
	return (new Date()).getTime() - lastUpdate.getTime() > reloadInterval;
	
}


var lastCommand = null;
$(function(){
	  
	  // Bind an event to window.onhashchange that, when the hash changes, gets the
	  // hash and adds the class "selected" to any matching nav link.
	  $(window).hashchange( function(){
	    var hash = location.hash;
	    
	    // Set the page title based on the hash.
	    var command =  hash.replace( /^#!/, '' );
	    

	    lastCommand = command;
	    
	    performOperation(command);
	    
	 
	  })
	  
	  // Since the event is only triggered when the hash changes, we need to trigger
	  // the event now, to handle the hash the page may have loaded with.
	  $(window).hashchange();
	  
});

function performLastOperation(){
	performOperation(lastCommand);
}
function performOperation(command){
	var itemType = command.split('=')[0];
		    
    if(itemType == 'stock'){
    	 var id = command.split('=')[1];
    	loadStock(id);
    	
    }else if(itemType == 'user'){
    	 var id = command.split('=')[1];
    	loadUserProfile(id);
    }
	else if(itemType == 'suggestedstocks'){
    	
    	loadTrendyStocks();
    }else if(itemType == 'topgrossingusers'){
    	
    	loadTrendyUsers();
    }else if(itemType == 'searchstock'){

    	var searchString = command.split('-')[1];
    	getQuote(searchString);
    }
    else if(itemType == 'searchuser'){

    	var searchString = command.split('-')[1];
    	getUser(searchString);
    }
}
function loadLanguage(lang){
	
	ajaxLoad("/lang", 'lang='+lang, null, "body", false, reloadPage);
	
}
function reloadPage(data){
	
	location.reload(true);
	if(data!=null && data.length>0){
		

		blockElementWithMsg("body", data);
	}
}

function reloadIfHashIsMyHref(element){
	
	if(isLocationEqualTo($(element).attr('href'))) performLastOperation();
	
	
}

function reloadIfHashIs(hash){
	
	if(isLocationEqualTo(hash)) performLastOperation();
	
	
}

function isLocationEqualTo(url){
	
	
	return window.location.hash==url;
	
}


function widgetLoad(){
	if(twttr.widgets){
		twttr.widgets.load();
	}
	
	
	
}