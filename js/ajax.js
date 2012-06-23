

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
			if(data.length>0){
				if($(data).size()==1 && $(data)[0].nodeName == "SCRIPT" ){
					 eval($(data).text());
				}else{
					$(containerDiv).empty();
					$(containerDiv).append($(data));	
				}
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
		},
		error : function(xhrError) {
			unblockElement(loadingDiv);
			if(xhrError.status==450 && !isReload){
			
				alert(xhrError.responseText);
			}else{
				
				
				
			}
		}
	});
	
	
}

function timeToReload(lastUpdate){
	if(lastUpdate==null) return true;
	return (new Date()).getTime() - lastUpdate.getTime() > reloadInterval;
	
}

var defaultStocksCommand = "suggestedstocks";
var defaultUsersCommand = "topgrossingusers";
var defaultGroupsCommand = "grouplist";
var defaultOldSeasonsCommand = "seasonresults";
var stocksCommand = "";
var usersCommand = "";
var groupsCommand = "";
var oldSeasonsCommand = "";

function checkTabCommand(tabId){
	if(tabId=='.stocks-tab'){
		if(stocksCommand!=''){
			location.hash="!"+stocksCommand;
		}
	}else if(tabId=='.users-tab'){
		if(usersCommand==''){

			location.hash="!"+defaultUsersCommand;
		}
		else{
			location.hash="!"+usersCommand;
		}
	}else if(tabId=='.groups-tab'){
		if(groupsCommand==''){

			location.hash="!"+defaultGroupsCommand;
		}
		else{
			location.hash="!"+groupsCommand;
		}
	}else if(tabId=='.oldseasons-tab'){
		if(oldSeasonsCommand==''){
			location.hash="!"+defaultOldSeasonsCommand;
		}
		else{
			location.hash="!"+oldSeasonsCommand;
		}
	}
	
}

var lastCommand = null;
$(function(){
	  // Bind an event to window.onhashchange that, when the hash changes, gets the
	  // hash and adds the class "selected" to any matching nav link.
	  $(window).hashchange( function(){
	    var hash = location.hash;
	    // Set the page title based on the hash.
	    var command =  hash.replace( /^#!/, '' );    
//	    if(command==""){
//	    	command = defaultCommand;
//	    	location.hash = "#!"+defaultCommand;
//	    	return;
//	    }
	    
	    
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
    	stocksCommand = command;
    	 var id = command.split('=')[1];
    	loadStock(id);
    }else if(itemType == 'user'){
    	usersCommand = command;
    	 var id = command.split('=')[1];
    	loadUserProfile(id);
    }
	else if(itemType == 'suggestedstocks'){
    	stocksCommand = command;
    	var page = command.split('=')[1];
    	loadSuggestedStocks(page);
    	
    }
	else if(itemType == 'topgrossingstocks'){
    	stocksCommand = command;
    	loadTopGrossingStocks();
    }else if(itemType == 'topgrossingusers'){
    	usersCommand = command;
    	loadTopGrossingUsers();
    }else if(itemType == 'newusers'){
    	usersCommand = command;
    	loadNewUsers();
    }else if(itemType == 'searchstock'){
    	stocksCommand = command;
    	var searchString = command.split('=')[1];
    	getQuote(searchString);
    }
    else if(itemType == 'searchuser'){
    	usersCommand = command;
    	var searchString = command.split('=')[1];
    	getUser(searchString);
    }
    else if(itemType == 'searchgroup'){
    	groupsCommand = command;
    	var searchString = command.split('=')[1];
    	getGroup(searchString);
    }
    else if(itemType == 'group'){
    	groupsCommand = command;
    	var id = command.split('=')[1];
    	loadGroup(id);
    }
    else if(itemType == 'joingroup'){
    	groupsCommand = command;
    	var id = command.split('=')[1];
    	joinGroupByReference(id);
    }
    else if(itemType == 'grouplist'){
    	groupsCommand = command;
    	loadGroupList(1);
    }
    else if(itemType == 'mygroups'){
    	groupsCommand = command;
    	loadMyGroups(1);
    }
    else if(itemType == 'seasonresults'){
    	oldSeasonsCommand = command;
    	var id = command.split('=')[1];
    	loadSeasonResults(id);
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
	if(isLocationEqualTo(hash)){
		performLastOperation();
	}else{

		window.location.hash=hash;
	}
}


function isLocationEqualTo(url){
	return window.location.hash==url;
}

function widgetLoad(){
	if(twttr.widgets){
		twttr.widgets.load();
	}
}