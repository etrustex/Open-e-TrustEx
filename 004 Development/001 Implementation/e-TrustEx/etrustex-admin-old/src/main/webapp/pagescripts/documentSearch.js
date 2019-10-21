var p = {
		__init: function() {
			return {
		        pageFormNameOverride:'documentSearchForm',
		        pageActionUrlOverride:'/document/search.do'
			};
		},
		initDisplay: function() {
			$._L("DOCUMENT.search.page.initDisplay");
			 // Run search if coming back from view.
			if($("#searchOnLoad").val() == "true") {
				p.search();
			}
		},
		
		bindEvents: function(){
			$._L("DOCUMENT.search.page.bindEvents");
			 //bind the components events
		    //--------------------------
		    $._ADE('click', '#action_search', function () {
		    	$._L('DOCUMENT_SEARCH: SEARCH button clicked');
		    	p.search();
		    });
		    
		    $._ADE('click', '#action_clear', function () {
		    	$._L('DOCUMENT_SEARCH: CLEAR button clicked');
		    	p.clear();
		    });
		    
		    $._ADE('click', '#action_cancel', function () {
		    	$._L('DOCUMENT_SEARCH: CANCEL button clicked');
		    	p.cancel();
		    });
		    
		    $._ADE('click','.select-document', function(){
		    	$._L('DOCUMENT_SEARCH: document clicked');
	    		var documentId = $(this).closest('tr').attr('id');
		    	var documentSearchForm = $("#documentSearchForm").serialize();
		    	window.location = $._getContextPath() + "/document/" + documentId + "/view.do?" + documentSearchForm;
			});
		    
		    //bind the search on enter event
		    $._ADEE('.action_search_on_enter_document', function () {
		    	$._L('DOCUMENT_SEARCH: Enter key pressed');
		    	p.search();
		    });
		},
		
		// function definitions
		search: function () {
            $._L('DOCUMENT_SEARCH.search');
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
	        		pageUrl: $._getContextPath() + "/document/search/results.do",
	        		id: "innerFragment",
	        		pageForm: "documentSearchForm"
	        	});
	        /*}*/
        },
            
        clear: function(){
        	$._L('DOCUMENT_SEARCH.clear');
        	CIPADMIN.clearElementsOf('#searchCriteriaDiv');
        },
            
	    close: function () {
            $._L('DOCUMENT_SEARCH close dialog');
        }

};