define(function () {
    $._L('COMMON_PARTY_SEARCH: REQUIRED');

    //bind the components events
    //--------------------------
    $._ADE('click', '#dialog_party_action_search', function () {
    	$._L('PARTY_SEARCH: SEARCH button clicked');
    	partySearch.search();
    });
    
    $._ADE('click', '#dialog_party_action_clear', function () {
    	$._L('PARTY_SEARCH: CLEAR button clicked');
    	partySearch.clear();
    });
    
    $._ADE('click', '#dialog_party_action_cancel', function () {
    	$._L('PARTY_SEARCH: CANCEL button clicked');
    	partySearch.cancel();
    });
    
    $._ADE('click','.select-party', function(){
    	$._L('PARTY_SEARCH.party clicked (popup mode ' + partySearch.isDialogMode + ')');
    	var partyId = $(this).closest('tr').attr('id');
    	var partySearchForm = $("#partySearchForm").serialize();
    	
    	if (partySearch.isDialogMode) {
    		partySearch.selectParty(partyId, partySearchForm);
    	} else {
    		window.location = $._getContextPath() + "/party/" + partyId + "/view.do?" + partySearchForm;
    	}
	});
    
    //bind the search on enter event
    $._ADEE('.action_search_on_enter_popup_party', function () {
    	$._L('PARTY_SEARCH: ENTER key pressed');
    	partySearch.search();
    });

    
    //declare the component object
    //----------------------------
    return partySearch = {
		isDialogMode: false,
    	parentForm: null,
    	controllerPath: null,
    	divToRefresh: null,
    	fnPostCallAfterSelect: null,

    	// partyType parameter can be issuer, sender, receiver
    	openDialog: function(opt) {
	    	$._L('PARTY_SEARCH.openDialog...');

            var parentForm = opt.parentForm;
            var controllerPath = opt.controllerPath;
            var partyType = opt.partyType;
            var fnPostCallAfterSelect = opt.fnPostCallAfterSelect;
            var businessDomainId = opt.businessDomainId;

	    	partySearch.parentForm = parentForm;
	    	partySearch.controllerPath = controllerPath;
	    	partySearch.partyType = partyType;
	    	partySearch.isDialogMode = true;
	    	partySearch.fnPostCallAfterSelect = fnPostCallAfterSelect;

	    	var url = $._getContextPath() + "/party/search/load.do?isSearchDialog=true";
	    	var dialogTitle;

	    	if(partyType) {
                partySearch.divToRefresh = partyType + "Div";
                url += "&partyType=" + partyType;
            } else {
                partySearch.divToRefresh = "partyDiv";
            }

            switch(partyType) {
                case 'first':
                    dialogTitle = $._getData('party.first.dialog.title');
                    break;
                case 'second':
                    dialogTitle = $._getData('party.second.dialog.title');
                    break;
                case 'first3rd': case 'second3rd':
                    dialogTitle = $._getData('party.third.dialog.title');
                    break;
                case 'authorizing':
                    dialogTitle = $._getData('party.authorizing.dialog.title');
                    break;
                case 'delegate':
                    dialogTitle = $._getData('party.delegate.dialog.title');
                    break;
                default:
                    dialogTitle = $._getData('common.popup.title.partySearch');
            }

	    	if (businessDomainId && businessDomainId != '-1') {
	    		url += "&businessDomainId=" + businessDomainId;
	    	}
	    	
	    	$._OAD({
                dialogId: 'popupPartySearchDialog',
                dialogTitle: dialogTitle,
                refreshedFragmentPageUrl: url,
                isOneButtonOnly: true,
                buttonSecondaryFn: function(){
                	$._CD({dialogId: 'popupPartySearchDialog'});
                },
                isShowCloseButton: true,
                dialogWidth: 900,
                fnAfterCreatePostCall: function(){
                    partySearch.setDisabledFields();
                }
            });	   
    	},

    	setDisabledFields: function() {
    	    var controllerPath = partySearch.controllerPath;
    	    if( controllerPath.indexOf('ica') >= 0 || controllerPath.indexOf('partyAgreement') >= 0 ) {
                $('#businessDomainId').prop('disabled', true);
                $('#thirdPartyFlag_search').prop('disabled', true);
            }
    	},

    	create: function(controllerPath) {
    	    var url = $._getContextPath() + '/' + controllerPath  + '/' + 'party.do';

//    	    switch(controllerPath) {
//    	        case 'ica':
//    	            break;
//    	        case 'ica/multi':
//                    break;
//                case ...
//
//                default:
//                    return;
//    	    }

    	    /*
             * This ajax POST call is just to place interchangeAgreement form in session.
             * After returning, navigates to new party creation.
             * TODO Is there any Jscaf simpler way to do it?
             */
            $._ajaxCall({
                pageUrl: url,
                pageForm: 'icaForm',
                fnPostCall: function (r) {
                    $._navigateTo({url: "/party/create.do"});
                }
            });
    	},
    		
        search: function () {
            $._L('PARTY_SEARCH.search (popup mode ' + partySearch.isDialogMode + ')');
            
            if (partySearch.isDialogMode){
            	$._AR({
//        		  		id: "partyResultsListContentDiv",
            		id: "popupPartySearchDialog",
            		pageForm : "partySearchForm",
    		  		pageUrl: $._getContextPath() + "/party/search/results.do?isSearchDialog=" + partySearch.isDialogMode,
                    fnPreCall: function () {//TODO: workaround JSCAF/JQUERY bug _injectDialog()/clone()
                    	var selects1 = $('#popupPartySearchDialog').find("select");
                   		$._injectDialog('popupPartySearchDialog',null,true);
                   		$(selects1).each(function(i) {
                   			var select = this;
                   			$("#popupPartySearchDialog_container").find("select").eq(i).val($(select).val());
                   		});
                    },
                    fnPreSuccess: function () {
                   		$._emptyDialogContainer();
                    },
                    fnPostCall: function () {
                        partySearch.setDisabledFields();
                   		$._centerDialog('popupPartySearchDialog');
                    }
    			});
            } else {
            	$._AR({
            		id: "innerFragment",
    		  		pageUrl: $._getContextPath() + "/party/search/results.do"
    			});
            	
            }
        },
            
        clear: function(){
        	$._L('PARTY_SEARCH.clear (popup mode ' + partySearch.isDialogMode + ')');
        	CIPADMIN.clearElementsOf('#popupPartySearchDialog #partySearchForm #searchCriteriaDiv');
        },
            
        selectParty: function(id, form){
    		$._L('PARTY_SEARCH.select party with id = ' + id + '(popup mode ' + partySearch.isDialogMode + ')');
    		var form = $("#" + partySearch.parentForm).serialize();
    		var url;
    		
            if ($("#pageMode").val()) {
                url = $._getContextPath() + "/" + partySearch.controllerPath + "/add/party/" + id + ".do?" + form;
            } else {
                url = $._getContextPath() + "/" + partySearch.controllerPath + "/search/add/party/" + id + ".do?" + form;
            }

    		if(partySearch.partyType) {
    		    url += "&partyType=" + partySearch.partyType;
    		}
    		
    		$._ajaxRefresh({
    			pageUrl: url,
    			callType: 'GET',
    			id: partySearch.divToRefresh,
    			fnPreSuccess: function () {
    				partySearch.close();
    			},
    			fnPostCall: function () {
    			    if(partySearch.fnPostCallAfterSelect) {
    			        $._execFn(partySearch.fnPostCallAfterSelect,true);
    			    }
                }
    		});    			
    	},
        	
	    close: function () {
            $._L('PARTY_SEARCH close dialog');
            $._CD({dialogId: 'popupPartySearchDialog'});
        },

    	cancel: function(){
    		 $._L('PARTY_SEARCH cancel dialog');
    		 partySearch.close();
    	},

    	clearPartyDiv: function(el, type) {
            var party;

            switch(type) {
                case 'first':
                    party = 'firstParty';
                    $('#action_new_party').removeClass('hidden');
                    $('#firstPartyDelegatesDiv').empty();
                    break;
                case 'second':
                    party = 'secondParty';
                    $('#secondPartyDelegatesDiv').empty();
                    break;
                case 'first3rd':
                    party = 'first3rdParty';
                    break;
                case 'second3rd':
                    party = 'second3rdParty';
                    break;
                case 'authorizing':
                    party = 'authorizingParty';
                    break;
                case 'delegate':
                    party = 'delegateParty';
                    break;
                default:
                    party = 'party';
            }

            $(el).remove();
            $('.tip-black').remove();
            $("#" + party + "Id").val('');
            $("#" + party + "Name").val('');
            $("#display" + party + "Name").text('');
        }
    }

});

