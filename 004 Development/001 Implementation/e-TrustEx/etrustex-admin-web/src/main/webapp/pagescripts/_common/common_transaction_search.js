define(function () {
			 //bind the components events
		    //--------------------------
		    $._ADE('click', '#dialog_transaction_action_search', function () {
		    	$._L('TRANSACTION_SEARCH: SEARCH button clicked');
		    	transactionSearch.search();
		    });
		    
		    $._ADE('click', '#dialog_transaction_action_clear', function () {
		    	$._L('TRANSACTION_SEARCH: CLEAR button clicked');
		    	transactionSearch.clear();
		    });
		    
		    $._ADE('click', '#dialog_transaction_action_cancel', function () {
		    	$._L('TRANSACTION_SEARCH: CANCEL button clicked');
		    	transactionSearch.close();
		    });
		    
		    $._ADE('click','.select-tx', function(){
		    	$._L('TRANSACTION_SEARCH: transaction clicked');
	    		var transactionId = $(this).closest('tr').attr('id');
		    	transactionSearch.selectTx(transactionId);
			});
		    
		    //bind the search on enter event
		    $._ADEE('.action_search_on_enter_tx', function () {
		    	$._L('TRANSACTION_SEARCH: Enter key pressed');
		    	transactionSearch.search();
		    });
		    
		    var profileNoneSelected = $('#profiles option[value="-1"]').prop('selected');
			var profileAnySelected = $('#profiles option[value="-2"]').prop('selected');
			
			$._ADE('change', '#profiles', function() {
				// if "None" was previously selected and "Any" is now selected, unselect the rest
		    	if(profileNoneSelected && $('#profiles option[value="-2"]').prop('selected')) {
		    		$("#profiles option:selected").prop("selected", false)
		    		$('#profiles option[value="-2"]').prop('selected', true);
		    		profileAnySelected = true;
		    		profileNoneSelected = false;
		    		return;
		    	}
		    	
		    	// if "Any" was previously selected and "None" is now selected, unselect the rest 
		    	if(profileAnySelected && $('#profiles option[value="-1"]').prop('selected')) {
		    		$("#profiles option:selected").prop("selected", false)
		    		$('#profiles option[value="-1"]').prop('selected', true);
		    		profileNoneSelected = true;
		    		profileAnySelected = false;
		    		return;
		    	}

		    	// If either "Any" or "None" where previously selected and another option is now selected
				if(profileNoneSelected || profileAnySelected) {
					$('#profiles option[value="-1"]').prop('selected', false);
					$('#profiles option[value="-2"]').prop('selected', false);
					profileNoneSelected = false;
					profileAnySelected = false;
					return;
				} else {
					$("#profiles option:selected").each(function() {
						if(this.value == "-1" || this.value == "-2") {
							$("#profiles option:selected").prop("selected", false)
							$('#profiles option[value="' + this.value + '"]').prop('selected', true);
							profileNoneSelected = this.value == "-1";
							profileAnySelected = this.value == "-2";
							return false;
						}
					});
				}
			});
		
		// function definitions
		return transactionSearch = {
				parentForm: null,
				controllerPath: null,
				fnPostCallAfterSelect: null,

				openDialog: function(opt) {
				    var parentForm = opt.parentForm;
                    var controllerPath = opt.controllerPath;
                    var businessDomainId = opt.businessDomainId;
                    var fnPostCallAfterSelect = opt.fnPostCallAfterSelect;

					transactionSearch.parentForm = parentForm;
					transactionSearch.controllerPath = controllerPath;
                    transactionSearch.fnPostCallAfterSelect = fnPostCallAfterSelect;

			    	var url = $._getContextPath() + "/transaction/search/load.do?isSearchDialog=true";

			    	if (businessDomainId && businessDomainId != '-1') {
                        url += "&businessDomainId=" + businessDomainId;
                    }

			    	$._OAD({
		                dialogId: 'popupTransactionSearchDialog',
		                dialogTitle: $._getData('common.popup.title.transactionSearch'),
		                refreshedFragmentPageUrl: url,
		                isOneButtonOnly: true,
		                buttonSecondaryFn: function () {
		                	$._CD({dialogId: 'popupTransactionSearchDialog'});
		                },
		                isShowCloseButton: true,
		                dialogWidth: 900
		            });	   
		    	},
		    	
		    	search: function () {
		    		$._L('TRANSACTION_SEARCH.search');
		    		
		    		$._AR({
		    			id: "popupTransactionSearchDialog",
		    			pageForm : "transactionSearchForm",
		    			pageUrl: $._getContextPath() + "/transaction/search/results.do?isSearchDialog=true",
		    			fnPreCall: function () {
		    				var selects1 = $('#popupTransactionSearchDialog').find("select");
		    				$._injectDialog('popupTransactionSearchDialog',null,true);
		    				$(selects1).each(function(i) {
		    					var select = this;
		    					$("#popupTransactionSearchDialog_container").find("select").eq(i).val($(select).val());
		    				});
		    			},
		    			fnPreSuccess: function () {
		    				$._emptyDialogContainer();
		    			},
		    			fnPostCall: function () {
		    				$._centerDialog('popupTransactionSearchDialog');
		    			}
		    		});
		    	},
	    	
		    	selectTx: function(transactionId) {
		    		var form = $("#" + transactionSearch.parentForm).serialize();
		    		var url;
		    		if ($("#pageMode").val()) {
		    			url = $._getContextPath() + "/" + transactionSearch.controllerPath + "/add/transaction/" + transactionId + ".do?" + form;
					} else {
						url = $._getContextPath() + "/" + transactionSearch.controllerPath + "/search/add/transaction/" + transactionId + ".do?" + form;		    		
					}
		    		
		    		$._ajaxRefresh({
		    			pageUrl: url,
		    			callType: 'GET',
		    			id: "transactionDiv",
		    			fnPreSuccess: function () {
		    				transactionSearch.close();
		    			},
                        fnPostCall: function () {
                            if(transactionSearch.fnPostCallAfterSelect) {
                                $._execFn(transactionSearch.fnPostCallAfterSelect,true);
                            }
                         }
		    		});
		    	},

		    	clearTransactionDiv: function(el) {
                    $(el).remove();
                    $('.tip-black').remove();
                    $("#transactionId").val('');
                    $("#transactionName").val('');
                    $("#transactionVersion").val('');
                    $("#displayTansactionName").text('');
                },
				
				clear: function(){
					$._L('TRANSACTION_SEARCH.clear');
					CIPADMIN.clearElementsOf('#popupTransactionSearchDialog #transactionSearchForm #searchCriteriaDiv');
					$('#popupTransactionSearchDialog #transactionSearchForm #searchCriteriaDiv #senderRole').val('');
		        	$('#popupTransactionSearchDialog #transactionSearchForm #searchCriteriaDiv #receiverRole').val('');
		        	$('#popupTransactionSearchDialog #transactionSearchForm #searchCriteriaDiv #profiles').val('-2');
				},
				
				close: function () {
					$._L('TRANSACTION_SEARCH close dialog');
					$._CD({dialogId: 'popupTransactionSearchDialog'});
				}
		}

});