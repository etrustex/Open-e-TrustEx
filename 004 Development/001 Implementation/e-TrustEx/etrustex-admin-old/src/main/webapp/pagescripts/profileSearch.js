var p = {
		__init: function() {
			return {
		        pageFormNameOverride:'profileSearchForm',
		        pageActionUrlOverride:'/profile/search.do'
			};
		},
		initDisplay: function() {
			$._L("PROFILE.search.page.initDisplay");
			 // Run search if coming back from view.
			if($("#searchOnLoad").val() == "true") {
				p.search();
			}
		},
		
		bindEvents: function(){
			$._L("PROFILE.search.page.bindEvents");
			 //bind the components events
		    //--------------------------
		    $._ADE('click', '#action_search', function () {
		    	$._L('PROFILE_SEARCH: SEARCH button clicked');
		    	p.search();
		    });
		    
		    $._ADE('click', '#action_clear', function () {
		    	$._L('PROFILE_SEARCH: CLEAR button clicked');
		    	p.clear();
		    });
		    
		    $._ADE('click', '#action_cancel', function () {
		    	$._L('PROFILE_SEARCH: CANCEL button clicked');
		    	p.cancel();
		    });
		    
		    $._ADE('click','.select-profile', function(){
		    	$._L('PROFILE_SEARCH: profile clicked');
	    		var profileId = $(this).closest('tr').attr('id');
		    	var profileSearchForm = $("#profileSearchForm").serialize();
		    	window.location = $._getContextPath() + "/profile/" + profileId + "/view.do?" + profileSearchForm;
			});
		    
		    //bind the search on enter event
		    $._ADEE('.action_search_on_enter_profile', function () {
		    	$._L('PROFILE_SEARCH: Enter key pressed');
		    	p.search();
		    });
		},
		
		// function definitions
		search: function () {
            $._L('PROFILE_SEARCH.search');
            /*var bValid = false;
	        if( $._isFieldValid($('#name_search'), true)
            		|| $._isFieldValid($('#localName_search'), true)
            		|| $._isFieldValid($('#typeCode_search'), true) ) {
            	bValid = true;
            }
	        
	        if (bValid === false) {
	            jQuery.noticeAdd({text: $._getData('error.search.criteria.needed'), type: 'error'});
	            return;
	        } else {*/
	        	$._blockUIMessageInfo = $._getData('common.searching.results');
	        	$._ajaxRefresh({
	        		pageUrl: $._getContextPath() + "/profile/search/results.do",
	        		id: "innerFragment",
	        		pageForm: "profileSearchForm"
	        	});
	        /*}*/
        },
            
        clear: function(){
        	$._L('PROFILE_SEARCH.clear');
        	CIPADMIN.clearElementsOf('#searchCriteriaDiv');
        },
            
	    close: function () {
            $._L('PROFILE_SEARCH close dialog');
        }

};