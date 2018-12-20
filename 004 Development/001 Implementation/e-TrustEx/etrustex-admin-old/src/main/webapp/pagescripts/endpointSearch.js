var p = {
		__init: function() {
			return {
		        pageFormNameOverride:'endpointSearchForm',
		        pageActionUrlOverride:'/endpoint/search.do',
		        appComponents:{
		        	transactionSearch: true,
		        	icaSearch: true,
		        	partySearch: true
	            }
			};
		},
		initDisplay: function() {
			$._L("ENDPOINT.search.page.initDisplay");
			 // Run search if coming back from view.
			if($("#searchOnLoad") && $("#searchOnLoad").val() == "true") {
				p.search();
			}
		},
		
		bindEvents: function(){
			$._L("ENDPOINT.search.page.bindEvents");
			 //bind the components events
		    //--------------------------
			$._ADE('click', '#action_search_party', function () {
				partySearch.openDialog({
                        parentForm: "endpointSearchForm",
                        controllerPath: "endpoint",
                        businessDomainId: $('#businessDomain').val()
                });
		    });
			
			$._ADE('click', '#action_search_ica', function () {
				icaSearch.openDialog({
                        parentForm: "endpointSearchForm",
                        controllerPath: "endpoint",
                        businessDomainId: $('#businessDomain').val()
                });
		    });
			
			$._ADE('click', '#action_search_transaction', function () {
				transactionSearch.openDialog({
                          parentForm: "endpointForm",
                          controllerPath: "endpoint",
                          businessDomainId: $('#businessDomain').val()
                });
		    });
			

			$._AE('click', '#action_remove_party', function(){
				partySearch.clearPartyDiv(this);
			});
			
			$._AE('click', '#action_remove_ica', function(){
				p.clearIca(this);
			});
			
			$._AE('click', '#action_remove_transaction', function(){
				transactionSearch.clearTransactionDiv(this);
			});
			
			
		    $._ADE('click', '#action_search', function () {
		    	$._L('ENDPOINT_SEARCH: SEARCH button clicked');
		    	p.search();
		    });
		    
		    $._ADE('click', '#action_clear', function () {
		    	$._L('ENDPOINT_SEARCH: CLEAR button clicked');
		    	p.clear();
		    });
		    
		    $._ADE('click', '#action_cancel', function () {
		    	$._L('ENDPOINT_SEARCH: CANCEL button clicked');
		    	p.cancel();
		    });
		    
		    $._AE('change', "#businessDomain", function() {
				$._L("CIPADMIN - business domain changed");
				p.loadProfiles();
			});
		    
		    $._ADE('click','.select-endpoint', function(){
		    	$._L('ENDPOINT_SEARCH: endpoint clicked');
	    		var endpointId = $(this).closest('tr').attr('id');
		    	var endpointSearchForm = $("#endpointSearchForm").serialize();
		    	window.location = $._getContextPath() + "/endpoint/" + endpointId + "/view.do?" + endpointSearchForm;
			});
		    
		    //bind the search on enter event
		    $._ADEE('.action_search_on_enter_endpoint', function () {
		    	$._L('ENDPOINT_SEARCH: Enter key pressed');
		    	p.search();
		    });
		},
		
		// function definitions
		search: function () {
            $._L('ENDPOINT_SEARCH.search');
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
	        		pageUrl: $._getContextPath() + "/endpoint/search/results.do",
	        		id: "innerFragment",
	        		pageForm: "endpointSearchForm"
	        	});
	        /*}*/
        },
        
		clearIca: function(el) {
			$(el).remove();
			$('.tip-black').remove();
			$("#icaId").val('');
			$("#displayIcaId").text('');
		},

		loadProfiles: function(){
			$._AR({
    			id: "profilesDiv",
    			pageUrl: $._getContextPath() + "/endpoint/profiles.do?bdId=" + $("#businessDomain").val() + "&pageMode=" + $("#pageMode").val()
    		});
		},
		
        clear: function(){
        	location.reload();
        },
            
	    close: function () {
            $._L('ENDPOINT_SEARCH close dialog');
        }

};