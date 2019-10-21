var p = {
		__init: function() {
			return {
		        pageFormNameOverride:'retentionPolicySearchForm',
		        pageActionUrlOverride:'/retentionPolicy/search.do'
			};
		},
		initDisplay: function() {
			$._L("retentionPolicy.search.page.initDisplay");
			 // Run search if coming back from view.
			if($("#searchOnLoad") && $("#searchOnLoad").val() == "true") {
				p.search();
			}
		},
		
		bindEvents: function(){
			$._L("retentionPolicy.search.page.bindEvents");

		    //--------------------------
		    $._ADE('click', '#action_search', function () {
		    	$._L('retentionPolicy_SEARCH: SEARCH button clicked');
		    	p.search();
		    });
		    
		    $._ADE('click', '#action_clear', function () {
		    	$._L('retentionPolicy_SEARCH: CLEAR button clicked');
		    	p.clear();
		    });
		    
		    $._ADE('click', '#action_cancel', function () {
		    	$._L('retentionPolicy_SEARCH: CANCEL button clicked');
		    	p.cancel();
		    });

			$._ADE('click','.select-retentionPolicy', function(){
                $._L('retentionPolicy_SEARCH: retentionPolicy clicked');
                window.location = $._getContextPath() + "/retentionPolicy/" + $(this).closest('tr').attr('id') + "/view.do?"+ $("#retentionPolicySearchForm").serialize();
            });
		    
		    //bind the search on enter event
		    $._ADEE('.action_search_on_enter_retentionPolicy', function () {
		    	$._L('retentionPolicy_SEARCH: Enter key pressed');
		    	p.search();
		    });

		    // Duration field numbers only
            $._AE('keyup', "#minDuration", function() {
                this.value = this.value.replace(/[^0-9\.]/g,'');
            });

            $._AE('keyup', "#maxDuration", function() {
                this.value = this.value.replace(/[^0-9\.]/g,'');
            });
		},
		
		// function definitions
		search: function () {
            $._L('retentionPolicy_SEARCH.search');
            $._blockUIMessageInfo = $._getData('common.searching.results');
            $._ajaxRefresh({
                pageUrl: $._getContextPath() + "/retentionPolicy/search/results.do",
                id: "innerFragment",
                pageForm: "retentionPolicySearchForm"
            });
        },

        clear: function(){
        	$._L('retentionPolicy_SEARCH.clear');
        	CIPADMIN.clearElementsOf('#searchCriteriaDiv');
        },
            
	    close: function () {
            $._L('retentionPolicy_SEARCH close dialog');
        }
};