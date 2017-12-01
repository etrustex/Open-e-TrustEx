define(function () {
			 //bind the components events
		    //--------------------------
		    $._ADE('click', '#dialog_ica_action_search', function () {
		    	$._L('ICA_SEARCH: SEARCH button clicked');
		    	icaSearch.search();
		    });
		    
		    $._ADE('click', '#dialog_ica_action_clear', function () {
		    	$._L('ICA_SEARCH: CLEAR button clicked');
		    	icaSearch.clear();
		    });
		    
		    $._ADE('click', '#dialog_ica_action_cancel', function () {
		    	$._L('ICA_SEARCH: CANCEL button clicked');
		    	icaSearch.close();
		    });
		    
		    $._ADE('click','.select-ica', function(){
		    	$._L('ICA_SEARCH: ica clicked');
	    		var icaId = $(this).closest('tr').attr('id');
		    	icaSearch.addIcaToMetadata(icaId);
			});
		    
		    //bind the search on enter event
		    $._ADEE('.action_search_on_enter_ica', function () {
		    	$._L('ICA_SEARCH: Enter key pressed');
		    	icaSearch.search();
		    });
		
		// function definitions
		return icaSearch = {
				parentForm: null,
				controllerPath: null,

				openDialog: function(opt) {
				    var parentForm = opt.parentForm;
				    var controllerPath = opt.controllerPath;
				    var businessDomainId = opt.businessDomainId;

					icaSearch.parentForm = parentForm;
					icaSearch.controllerPath = controllerPath;

			    	var url = $._getContextPath() + "/ica/search/load.do?isSearchDialog=true";

			    	if (businessDomainId && businessDomainId != '-1') {
			    	    url += "&businessDomainId=" + businessDomainId;
			    	}
			    	
			    	$._OAD({
		                dialogId: 'popupIcaSearchDialog',
		                dialogTitle: $._getData('common.popup.title.icaSearch'),
		                refreshedFragmentPageUrl: url,
		                isOneButtonOnly: true,
		                buttonSecondaryFn: function () {
		                	$._CD({dialogId: 'popupIcaSearchDialog'});
		                },
		                isShowCloseButton: true,
		                dialogWidth: 900
		            });	   
		    	},
		    	
		    	search: function () {
		    		$._L('ICA_SEARCH.search');
		    		
		    		$._AR({
		    			id: "popupIcaSearchDialog",
		    			pageForm : "icaSearchForm",
		    			pageUrl: $._getContextPath() + "/ica/search/results.do?isSearchDialog=true",
		    			fnPreCall: function () {
		    				var selects1 = $('#popupIcaSearchDialog').find("select");
		    				$._injectDialog('popupIcaSearchDialog',null,true);
		    				$(selects1).each(function(i) {
		    					var select = this;
		    					$("#popupIcaSearchDialog_container").find("select").eq(i).val($(select).val());
		    				});
		    			},
		    			fnPreSuccess: function () {
		    				$._emptyDialogContainer();
		    			},
		    			fnPostCall: function () {
		    				$._centerDialog('popupIcaSearchDialog');
		    			}
		    		});
		    	},
	    	
		    	addIcaToMetadata: function(icaId) {
		    		var form = $("#" + icaSearch.parentForm).serialize();
		    		var url;
		    		if ($("#pageMode").val()) {
		    			url = $._getContextPath() + "/" + icaSearch.controllerPath + "/add/ica/" + icaId + ".do?" + form;
					} else {
						url = $._getContextPath() + "/" + icaSearch.controllerPath + "/search/add/ica/" + icaId + ".do?" + form;		    		
					}
		    		
		    		$._ajaxRefresh({
		    			pageUrl: url,
		    			callType: 'GET',
		    			id: "icaDiv",
		    			fnPreSuccess: function () {
		    				icaSearch.close();
		    			}
		    		});
		    	},
				
				clear: function(){
					$._L('ICA_SEARCH.clear');
					CIPADMIN.clearElementsOf('#popupIcaSearchDialog #icaSearchForm #searchCriteriaDiv');
					$('#popupIcaSearchDialog #icaSearchForm #searchCriteriaDiv #interchangeAgreement_partyRoleId').val('-1');
					$('#popupIcaSearchDialog #icaSearchForm #searchCriteriaDiv #interchangeAgreement_profileId').val('-1');
				},
				
				close: function () {
					$._L('ICA_SEARCH close dialog');
					$._CD({dialogId: 'popupIcaSearchDialog'});
				}
		}

});