var p = {
		__init: function() {
			return {
		        pageFormNameOverride:'userSearchForm',
		        pageActionUrlOverride:'/user/search.do'				
			};
		},
		
		initDisplay: function(){
			$._L("USER.search.page.initDisplay");
			
			 // Run search if coming back from user view.
			if($("#searchOnLoad").val() == "true") {
				p.searchUser();
			}
		},
		
		bindEvents: function(){
			$._L("USER.search.page.bindEvents");
			
			$._AE('change','.action_refresh_parties', function(){
				$._L("USER.search: business domain changed");
				p.refreshParties();
			});
			
			$._AE('click','#action_search', function(){
				$._L("USER.search: SEARCH button clicked");
				p.searchUser();
			});
			
			$._AEE('.action_search_on_enter', function(){
				$._L("USER.search: ENTER key pressed");
				p.searchUser();
			});
			
			$._AE('click','#action_clear', function(){
				$._L("USER.search: CLEAR button clicked");
				p.clearSearchCriteria();
			});
			
		    $._AE('click','.select-user', function(){
		    	$._L('USER.search: user clicked');
		    	var userId = $(this).closest('tr').attr('id');
		    	var userSearchForm = $("#userSearchForm").serialize();
		    	$._L('USER.search: criteria: ' + userSearchForm);
		    	window.location = $._getContextPath() + "/user/" + userId + "/view.do?" + userSearchForm;
			});
		},
		
		// function definitions
		searchUser: function(){
			$._L("USER.search.searchUser...");
            /*var bValid = false;
            if( $._isFieldValid($('#usernameSearch'), true)
            		|| $._isFieldValid($('#userRoleIdSearch'), true)
            		|| $._isFieldValid($('#businessDomainIdSearch'), true) 
            		|| $._isFieldValid($('#partyIdSearch'), true) ) {
            	bValid = true;
            }
	        
	        if (bValid === false) {
	            jQuery.noticeAdd({text: $._getData('error.search.criteria.needed'), type: 'error'});
	            return;
	        } else {*/
	        	$._blockUIMessageInfo = $._getData('common.searching.results');
	        	$._ajaxRefresh({
	        		pageUrl: "search/results.do",
	        		id: "innerFragment",
	        		pageForm: "userSearchForm",
	        	});
	        /*}*/
		},
		
		refreshParties: function(){
			$._L("USER.search.refreshParties...");
			$._blockUIMessageInfo = $._getData('common.reloading.parties');
		  	$._ajaxRefresh({
		  		pageUrl: "search/refreshParties.do",
		  		pageForm: "userSearchForm",
		  		id: "innerFragment"
			});
		},
		
		clearSearchCriteria: function(){
			$._log("USER.search.clearSearchCriteria...");
			CIPADMIN.clearElementsOf('#searchCriteriaDiv');
		}
		
};