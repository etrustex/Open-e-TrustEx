var p = {
		__init: function() {
			return {
		        pageFormNameOverride:'partySearchForm',
		        pageActionUrlOverride:'/party/search.do',
		        appComponents:{
		        	partySearch: true
	            }				
			};
		},
		
		initDisplay: function(){
			$._L("PARTY.search.page.initDisplay");

			// Run search if coming back from view.
			if($("#searchOnLoad").val() == "true") {
				partySearch.search();
			}
		},
		
		bindEvents: function(){
			$._L("ENDPOINT.search.page.bindEvents");
			 //bind the components events
		    //--------------------------
			
		    $._ADE('click', '#action_search', function () {
		    	partySearch.search();
		    });
		    
		    $._ADE('click', '#action_clear', function () {
		    	CIPADMIN.clearElementsOf('#searchCriteriaDiv');
		    });
		    
		    //bind the search on enter event
		    $._ADEE('.action_search_on_enter_endpoint', function () {
		    	partySearch.search();
		    });
		}
};