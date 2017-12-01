define(function () {
			 //bind the components events
		    //--------------------------
		    $._ADE('click', '#dialog_document_action_search', function () {
		    	$._L('DOCUMENT_SEARCH: SEARCH button clicked');
		    	documentSearch.search();
		    });
		    
		    $._ADE('click', '#dialog_document_action_clear', function () {
		    	$._L('DOCUMENT_SEARCH: CLEAR button clicked');
		    	documentSearch.clear();
		    });
		    
		    $._ADE('click', '#dialog_document_action_cancel', function () {
		    	$._L('DOCUMENT_SEARCH: CANCEL button clicked');
		    	documentSearch.close();
		    });
		    
		    $._ADE('click','.select-document', function(){
		    	$._L('DOCUMENT_SEARCH: document clicked');
	    		var documentId = $(this).closest('tr').attr('id');
		    	documentSearch.addDoc(documentId);
			});
		    
		    //bind the search on enter event
		    $._ADEE('.action_search_on_enter_document', function () {
		    	$._L('DOCUMENT_SEARCH: Enter key pressed');
		    	documentSearch.search();
		    });
		
		// function definitions
		return documentSearch = {
				parentForm: null,
				controllerPath: null,
				openDialog: function(parentForm, controllerPath) {
					documentSearch.parentForm = parentForm;
					documentSearch.controllerPath = controllerPath;
			    	var url = $._getContextPath() + "/document/search/load.do?isSearchDialog=true";

			    	$._OAD({
		                dialogId: 'popupDocumentSearchDialog',
		                dialogTitle: $._getData('common.popup.title.documentSearch'),
		                refreshedFragmentPageUrl: url,
		                isOneButtonOnly: true,
		                buttonSecondaryFn: function () {
		                	$._CD({dialogId: 'popupDocumentSearchDialog'});
		                },
		                isShowCloseButton: true,
		                dialogWidth: 900
		            });	   
		    	},
		    	
		    	search: function () {
		    		$._L('DOCUMENT_SEARCH.search');
		    		
		    		$._AR({
		    			id: "popupDocumentSearchDialog",
		    			pageForm : "documentSearchForm",
		    			pageUrl: $._getContextPath() + "/document/search/results.do?isSearchDialog=true",
		    			fnPreCall: function () {
		    				var selects1 = $('#popupDocumentSearchDialog').find("select");
		    				$._injectDialog('popupDocumentSearchDialog',null,true);
		    				$(selects1).each(function(i) {
		    					var select = this;
		    					$("#popupDocumentSearchDialog_container").find("select").eq(i).val($(select).val());
		    				});
		    			},
		    			fnPreSuccess: function () {
		    				$._emptyDialogContainer();
		    			},
		    			fnPostCall: function () {
		    				$._centerDialog('popupDocumentSearchDialog');
		    			}
		    		});
		    	},
	    	
		    	addDoc: function(documentId) {
		    		var form = $("#" + documentSearch.parentForm).serialize();
		    		var url;
		    		if ($("#pageMode").val()) {
		    			url = $._getContextPath() + "/" + documentSearch.controllerPath + "/add/document/" + documentId + ".do?" + form;
					} else {
						url = $._getContextPath() + "/" + documentSearch.controllerPath + "/search/add/document/" + documentId + ".do?" + form;		    		
					}
		    		
		    		$._ajaxRefresh({
		    			pageUrl: url,
		    			callType: 'GET',
		    			id: "documentDiv",
		    			fnPreSuccess: function () {
		    				documentSearch.close();
		    			}
		    		});
		    	},
				
				clear: function(){
					$._L('DOCUMENT_SEARCH.clear');
					CIPADMIN.clearElementsOf('#searchCriteriaDiv');
				},
				
				close: function () {
					$._L('DOCUMENT_SEARCH close dialog');
					$._CD({dialogId: 'popupDocumentSearchDialog'});
				}
		}

});