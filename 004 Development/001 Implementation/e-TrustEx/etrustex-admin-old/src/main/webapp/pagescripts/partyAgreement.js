var p = {
	__init: function() {
		return {
	        pageFormNameOverride:'partyAgreementForm',
	        pageActionUrlOverride:$('#partyAgreementForm').attr('action'),
            appComponents:{
                partySearch:true,
                transactionSearch: true
            }
		};
	},
	
	initDisplay: function(){
		$._L("PARTYAGREEMENT." + $("#pageMode").val() + "page.initDisplay");
		
		if($("#accessRightsError").val()) { // E.g. no access rights messages
			CIPADMIN.showBusinessError($("#accessRightsError").val());
		}
	},
	
	isDialogMode: false,

	bindEvents: function(){
		$._AE('click', '#action_save', function(){
			p.savePartyAgreement();
		});
		
		$._AE('click', '#action_delete', function(){
			p.deletePartyAgreementConfirmation();
		});
		
		$._AE('click', '#action_edit', function(){
			p.editPartyAgreement();
		});

		$._ADE('click', '.action_search_party', function () {
		    var partyType = $(this).attr('id').split("_")[2];
            partySearch.openDialog({
                parentForm: "partyAgreementForm",
                controllerPath: "partyAgreement",
                partyType: partyType,
                businessDomainId: $('#businessDomain').val()
            });
        });
		
		$._ADE('click', '#action_search_tx', function () {
            transactionSearch.openDialog({
                parentForm: "partyAgreementForm",
                controllerPath: "partyAgreement",
                businessDomainId: $('#businessDomain').val()
            });
        });

	    $._AE('click', '.action_remove_party', function(){
            var partyType = $(this).attr('id').split("_")[2];
            partySearch.clearPartyDiv(this, partyType);
        });

        // this is for the search tx button
        $._AE('click', '#action_remove_tx', function(){
            transactionSearch.clearTransactionDiv(this);
        });

        // this is for the list of transactions
		$._AE('click', '.action_remove_tx', function(){
			p.removeTx($(this).data().txId);
		});
		
		$._AE('click', '#action_cancel', function(){
			p.cancel();
		});
		
		$._AE('click', '#action_clear', function(){
			location.reload();
		});
		
		$._AE('click', '#action_reset', function(){
			location.reload();
		});
		
		$._AE('change', '#allTransactions_bs', function() {
            $("#transactionsTableDiv").toggleClass('hidden');
		});
	},
	
	
	// function definitions
	savePartyAgreement: function() {
    	$._blockUIMessageInfo = $._getData('blockUI.partyAgreement.saving');
    	$._ajaxCall({
    		pageUrl: $._getContextPath() + "/partyAgreement/save.do",
    		pageForm: 'partyAgreementForm',
    		fnPostCall: function (r) {
    			var ajaxResult = CIPADMIN.ajaxResult();

	  			if(ajaxResult.success) {
	  				CIPADMIN.showSuccessMessage(ajaxResult.message, null, null, "/partyAgreement/" + ajaxResult.id + "/view.do");
	  			} else {
	  				CIPADMIN.showBusinessError(ajaxResult.message);
	  			}
    		}
    	});
    },

	editPartyAgreement: function() {
		window.location = $._getContextPath() + "/partyAgreement/" + $("#id").val() + "/edit.do?"+ $("#partyAgreementSearchForm").serialize();
	},
	
	deletePartyAgreementConfirmation: function(){
		$._msgbox({
			dialogId:'confirmMsg',
            text:$('#msgDeleteConfirmation').text(),
            title: $._getData('partyAgreement.management.confirmation.message.title'),
            msgboxType:'yes_no',
            alertType:'warning',
            fnCallback:function (r) {
                if (r === true) {
                	p.deletePartyAgreement();
                }
            }
        });
	},
	
	deletePartyAgreement: function(){
		$._ajaxCall({
	  		pageUrl: $._getContextPath() + "/partyAgreement/" + $("#id").val() + "/delete.do",
	  		pageForm: 'partyAgreementForm',
	  		fnPostCall: function (r) {
	  			var ajaxResult = CIPADMIN.ajaxResult();
	  			var nextUrl;
	  			if ($("#refererPage").val() === 'create') {
	  				nextUrl = "/partyAgreement/create.do";
				} else {
					nextUrl = "/partyAgreement/search.do?fromView=true&" + $("#partyAgreementSearchForm").serialize();
				}
	  			
	  			if(ajaxResult.success) {
    				CIPADMIN.showSuccessMessage(ajaxResult.message, null, null, nextUrl);
    			} else {
    				CIPADMIN.showBusinessError(ajaxResult.message);
    			}		
            }
		});
	},

	removeTx: function(txId){
		$._AR({
			callType: 'POST',
			pageUrl : $._getContextPath() + '/partyAgreement/tx/remove.do?txId=' + txId,
			pageForm : "partyAgreementForm",
			id : "transactionDiv",
			fnPostCall: function () {
			    var txCannotBeRemoved = $('#txCannotBeRemoved').val();
                if(txCannotBeRemoved) {
                    $._msgbox({
                        dialogId:'errorMsg',
                        text:txCannotBeRemoved,
                        alertType:'error',
                        fnCallback: function(){
                            $('#txCannotBeRemoved').val('');
                        }
                    });
                }
			}
		});
	},

	cancel: function() {
		if ($("#pageMode").val() === 'new') {
			CIPADMIN.goBack();
		} else if ($("#refererPage").val() === 'create') {
			$._navigateTo({url: "/partyAgreement/create.do"});
		} else {
			$._navigateTo({url: "/partyAgreement/search.do?fromView=true&" + $("#partyAgreementSearchForm").serialize()});
		}
	}
};