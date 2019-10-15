var p = {
		__init: function() {
			return {
		        pageFormNameOverride:'metadataSearchForm',
		        pageActionUrlOverride:'/metadata/search.do',
		        appComponents:{
		        	documentSearch: true,
		        	transactionSearch: true,
		        	icaSearch: true,
		        	partySearch: true
	            }
			};
		},
		initDisplay: function() {
			$._L("METADATA.search.page.initDisplay");
			 // Run search if coming back from view.
			if($("#searchOnLoad") && $("#searchOnLoad").val() == "true") {
				p.search();
			}
		},
		
		bindEvents: function(){
			$._L("METADATA.search.page.bindEvents");
			 //bind the components events
		    //--------------------------
			$._ADE('click', '#action_search_document', function () {
				documentSearch.openDialog("metadataSearchForm", "metadata");
		    });
			
			$._ADE('click', '#action_search_ica', function () {
				icaSearch.openDialog({
                        parentForm: "metadataSearchForm",
                        controllerPath: "metadata"
                });
		    });

		    $._ADE('click', '#action_search_party', function () {
                partySearch.openDialog({
                        parentForm: "metadataSearchForm",
                        controllerPath: "metadata",
                        partyType: "sender"
                });
            });
			
			$._ADE('click', '#action_search_transaction', function () {
				transactionSearch.openDialog({
                          parentForm: "metadataSearchForm",
                          controllerPath: "metadata"
                });
		    });
			

			$._AE('click', '#action_remove_document', function(){
				p.clearDocument();
			});
			
			$._AE('click', '#action_remove_ica', function(){
				p.clearIca();
			});

			$._AE('click', '#action_remove_party', function(){
                partySearch.clearPartyDiv(this);
            });
			
			$._AE('click', '#action_remove_transaction', function(){
				transactionSearch.clearTransactionDiv(this);
			});
			
			
		    $._ADE('click', '#action_search', function () {
		    	$._L('METADATA_SEARCH: SEARCH button clicked');
		    	p.search();
		    });
		    
		    $._ADE('click', '#action_clear', function () {
		    	$._L('METADATA_SEARCH: CLEAR button clicked');
		    	p.clear();
		    });
		    
		    $._ADE('click', '#action_cancel', function () {
		    	$._L('METADATA_SEARCH: CANCEL button clicked');
		    	p.cancel();
		    });
		    
		    $._ADE('click','.select-metadata', function(){
		    	$._L('METADATA_SEARCH: metadata clicked');
	    		var metadataId = $(this).closest('tr').attr('id');
		    	var metadataSearchForm = $("#metadataSearchForm").serialize();
		    	window.location = $._getContextPath() + "/metadata/" + metadataId + "/view.do?" + metadataSearchForm;
			});
		    
		    //bind the search on enter event
		    $._ADEE('.action_search_on_enter_metadata', function () {
		    	$._L('METADATA_SEARCH: Enter key pressed');
		    	p.search();
		    });
		},
		
		// function definitions
		search: function () {
            $._L('METADATA_SEARCH.search');
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
	        		pageUrl: $._getContextPath() + "/metadata/search/results.do",
	        		id: "innerFragment",
	        		pageForm: "metadataSearchForm"
	        	});
	        /*}*/
        },
        
	    clearDocument: function() {
			$("#documentId").val('');
			$("#documentName").val('');
			$("#displayDocumentName").text('');
			
		},
		
		clearIca: function() {
			$("#icaId").val('');
			$("#displayIcaId").val('');
		},
		
        clear: function(){
        	$._L('METADATA_SEARCH.clear');
        	location.reload();
        },
            
	    close: function () {
            $._L('METADATA_SEARCH close dialog');
        }

};