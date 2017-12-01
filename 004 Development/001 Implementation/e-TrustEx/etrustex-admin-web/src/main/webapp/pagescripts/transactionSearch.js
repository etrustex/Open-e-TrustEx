var p = {
		__init: function() {
			return {
		        pageFormNameOverride:'transactionSearchForm',
		        pageActionUrlOverride:'/transaction/search.do'
			};
		},
		initDisplay: function() {
			$._L("TRANSACTION.search.page.initDisplay");
			 // Run search if coming back from view.
			if($("#searchOnLoad").val() == "true") {
				p.search();
			}
			
			p.profileNoneSelected = $('#profiles option[value="-1"]').prop('selected');
			p.profileAnySelected = $('#profiles option[value="-2"]').prop('selected');
		},
		
		profileNoneSelected: false,
		profileAnySelected: false,
		
		bindEvents: function(){
			$._L("TRANSACTION.search.page.bindEvents");
			 //bind the components events
		    //--------------------------
		    $._ADE('click', '#action_search', function () {
		    	$._L('TRANSACTION_SEARCH: SEARCH button clicked');
		    	p.search();
		    });
		    
		    $._ADE('click', '#action_clear', function () {
		    	$._L('TRANSACTION_SEARCH: CLEAR button clicked');
		    	p.clear();
		    });
		    
		    $._ADE('click', '#action_cancel', function () {
		    	$._L('TRANSACTION_SEARCH: CANCEL button clicked');
		    	p.cancel();
		    });
		    
		    $._ADE('click','.select-tx', function(){
		    	$._L('TRANSACTION_SEARCH: transaction clicked');
	    		var transactionId = $(this).closest('tr').attr('id');
		    	var transactionSearchForm = $("#transactionSearchForm").serialize();
		    	window.location = $._getContextPath() + "/transaction/" + transactionId + "/view.do?" + transactionSearchForm;
			});
		    
		    //bind the search on enter event
		    $._ADEE('.action_search_on_enter_tx', function () {
		    	$._L('TRANSACTION_SEARCH: Enter key pressed');
		    	p.search();
		    });
		    
		    /*
			 * UC70_BR04	Default option for the associated profiles is None. 
			 * If profiles have been already selected and user chooses None option, the previously selected profiles will be removed from the selection.
			 * If None option is selected, and user selects any profile in the list, None will be removed from the selection. 
			 */
			$._AE('change', '#profiles', function() {
				// if "None" was previously selected and "Any" is now selected, unselect the rest
		    	if(p.profileNoneSelected && $('#profiles option[value="-2"]').prop('selected')) {
		    		$("#profiles option:selected").prop("selected", false)
		    		$('#profiles option[value="-2"]').prop('selected', true);
		    		p.profileAnySelected = true;
		    		p.profileNoneSelected = false;
		    		return;
		    	}
		    	
		    	// if "Any" was previously selected and "None" is now selected, unselect the rest 
		    	if(p.profileAnySelected && $('#profiles option[value="-1"]').prop('selected')) {
		    		$("#profiles option:selected").prop("selected", false)
		    		$('#profiles option[value="-1"]').prop('selected', true);
		    		p.profileNoneSelected = true;
		    		p.profileAnySelected = false;
		    		return;
		    	}

		    	// If either "Any" or "None" where previously selected and another option is now selected
				if(p.profileNoneSelected || p.profileAnySelected) {
					$('#profiles option[value="-1"]').prop('selected', false);
					$('#profiles option[value="-2"]').prop('selected', false);
					p.profileNoneSelected = false;
					p.profileAnySelected = false;
					return;
				} else {
					$("#profiles option:selected").each(function() {
						if(this.value == "-1" || this.value == "-2") {
							$("#profiles option:selected").prop("selected", false)
							$('#profiles option[value="' + this.value + '"]').prop('selected', true);
							p.profileNoneSelected = this.value == "-1";
							p.profileAnySelected = this.value == "-2";
							return false;
						}
					});
				}
			});
		},
		
		// function definitions
		search: function () {
            $._L('TRANSACTION_SEARCH.search');
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
	        	$._AR({
	        		pageUrl: $._getContextPath() + "/transaction/search/results.do",
	        		id: "innerFragment",
	        		pageForm: "transactionSearchForm",
	        		isBlockerActive: false,
	        		fnPostCall: function (r) {
	        			 $._L(r);
//	        			var ajaxResult = CIPADMIN.ajaxResult();
	        			$._unblockUI();
	    			}
	        	});
	        /*}*/
        },
            
        clear: function(){
        	$._L('TRANSACTION_SEARCH.clear');
        	CIPADMIN.clearElementsOf('#searchCriteriaDiv');
        	$('#senderRole').val('');
        	$('#receiverRole').val('');
        	$('#profiles').val('-1');
        },
            
	    close: function () {
            $._L('TRANSACTION_SEARCH close dialog');
        }

};