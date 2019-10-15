var p = {
		
		__init: function() {
			return {
		        pageFormNameOverride:'icaForm',
		        pageActionUrlOverride:$('#icaForm').attr('action'),
		        appComponents:{
                    partySearch: true,
                    roles: true
                }
			};
		},		
		
		initDisplay: function(){
			$._L("ICA.creation.page.initDisplay");

			if($("#accessRightsError").val()) { // E.g. no access rights messages
				CIPADMIN.showBusinessError($("#accessRightsError").val());
			}
			
			if ($("#pageMode").val() === 'edit'){
				$('#interchangeAgreement_profileId option:not(:selected)').attr('disabled', true);
				$('#firstPartyId option:not(:selected)').attr('disabled', true);
				$('.roleSelect option:not(:selected)').attr('disabled', true);
				$('#secondParty_id option:not(:selected)').attr('disabled', true);
				$('#secondPartyRoleId option:not(:selected)').attr('disabled', true);
			}

			if($("#businessDomainErrorMessage").val()) {
				$._msgbox({
					dialogId:'confirmMsg',
	                text: $("#businessDomainErrorMessage").val(),
	                title:$('#msgNoRecordWarningTitle').text(),
	                msgboxType:'ok',
	                alertType:'error'
	            });
			}
		},
		
		bindEvents: function(){
			$._L("ICA.creation.page.bindEvents");
			
			$._ADE('click', '#action_new_party', function () {
				$._L("ICA: NEW PARTY button clicked");
				partySearch.create('ica');
		    });
			
			$._AE('click', '#action_save', function () {
				$._L("ICA: SAVE button clicked");
				p.saveInterchangeAgreement();
		    });

			$._AE('click', '#action_clear', function(){
				$._L('ICA: CLEAR button clicked');
				location.reload();
			});
			
			$._AE('click', '#action_reset', function(){
				$._L('ICA: RESET button clicked');
				location.reload();
			});

			$._AE('click', '#action_edit', function(){
				$._L('ICA: EDIT button clicked');
				p.editInterchangeAgreement();
			});
			
			$._AE('click', '#action_delete', function(){
				$._L('ICA: DELETE button clicked');
				p.deleteInterchangeAgreementConfirmation();
			});
			
			$._AE('click', '#action_cancel', function(){
				p.cancel();
			});

			$._ADE('click', '.action_search_party', function () {
			    var partyType = $(this).attr('id').split("_")[2];

                partySearch.openDialog({
                        parentForm: "interchangeAgreement",
                        controllerPath: "ica",
                        partyType: partyType,
                        fnPostCallAfterSelect: function() {
                          if(partyType == 'first' || partyType == 'second') {
                            var partyId = partyType == 'first' ? $('#firstPartyId').val() : $('#secondPartyId').val();
                            p.partyDelegates(partyId, partyType);
                          }
                        },
                        businessDomainId: $('#businessDomain').val()
                });

            });

            $._AE('click', '.action_remove_party', function(){
                var partyType = $(this).attr('id').split("_")[2];
                partySearch.clearPartyDiv(this, partyType);
            });
			
			$._AE('change','#interchangeAgreement_profileId', function(){
				$._log('Profile changed: ' + $('#interchangeAgreement_profileId').val());
				roles.reloadRoles($('#interchangeAgreement_profileId').val());
			});
		},
		
		// function definitions
		saveInterchangeAgreement: function(){
			$._L("ICA.creation.saving...");
			
			if (p.isDataValid() === false) {
	            jQuery.noticeAdd({text: $._getData('common.missing.mandatory.fields'), type: 'error'});
	            return;
	        }
			
        	$._blockUIMessageInfo = $._getData('blockUI.ica.saving');
        	$._ajaxCall({
        		pageUrl: $._getContextPath() + "/ica/save.do",
        		pageForm: 'icaForm',
        		fnPostCall: function (r) {
        			var ajaxResult = CIPADMIN.ajaxResult();
        			if(ajaxResult.success) {
        				CIPADMIN.showSuccessMessage(ajaxResult.message, null, null, "/ica/" + ajaxResult.id + "/view.do?"+ $("#interchangeAgreementSearchForm").serialize());
        			} else {
        				CIPADMIN.showBusinessError(ajaxResult.message);
        			}
        		}
        	});
		},
		
		editInterchangeAgreement: function(){
			window.location = $._getContextPath() + "/ica/" + $("#interchangeAgreement_id").val() + "/edit.do?"+ $("#interchangeAgreementSearchForm").serialize();
		},
		
		deleteInterchangeAgreementConfirmation: function(){
			$._L("ICA.deleteInterchangeAgreementConfirmation");
			
			$._msgbox({
				dialogId:'confirmMsg',
                text:$('#msgDeleteConfirmation').text(),
                title:$('#msgDeleteConfirmationTitle').text(),
                msgboxType:'yes_no',
                alertType:'warning',
                fnCallback:function (r) {
                    if (r === true) {
                    	p.deleteInterchangeAgreement();
                    }
                }
            });
		},
		
		deleteInterchangeAgreement: function(){
			$._L("ICA.deleteParty...");
			$._ajaxCall({
		  		pageUrl: $._getContextPath() + "/ica/" + $("#interchangeAgreement_id").val() + "/delete.do",
		  		pageForm: 'icaForm',
		  		fnPostCall: function () {
		  			var ajaxResult = CIPADMIN.ajaxResult();
		  			var nextUrl;
		  			if ($("#refererPage").val() === 'create') {
		  				nextUrl = "/ica/create.do";
					} else {
						nextUrl = "/ica/search.do?fromView=true&" + $("#interchangeAgreementSearchForm").serialize();
					}
		  			
		  			if(ajaxResult.success) {
        				CIPADMIN.showSuccessMessage(ajaxResult.message, null, null, nextUrl);
        			} else {
        				CIPADMIN.showBusinessError(ajaxResult.message);
        			}
                }
			});
		},
		
		isDataValid: function(){
			var bValid = true;

	        bValid = $._isFieldValid($('#interchangeAgreement_profileId'), true) && bValid;
	        bValid = $._isFieldValid($('#firstPartyId'), true) && bValid;
	        bValid = $._isFieldValid($('.roleSelect'), true) && bValid;
	        bValid = $._isFieldValid($('#secondParty_id'), true) && bValid;
	        bValid = $._isFieldValid($('#secondPartyRoleId'), true) && bValid;

	        return bValid;
	    },

		partyDelegates: function(partyId, partyType) {
		    $._ajaxRefresh({
                pageUrl: $._getContextPath() + '/ica/partyDelegates/' + partyId + '?partyType=' + partyType,
                callType: 'GET',
                id: partyType + 'PartyDelegatesDiv'
            });
		},
		
		cancel: function() {
			if ($("#pageMode").val() === 'new') {
				CIPADMIN.clearElementsOf('#icaForm');
//				CIPADMIN.goToHomePage();
				CIPADMIN.goBack();
			} else if ($("#refererPage").val() === 'create') {
				$._navigateTo({url: "/ica/create.do"});
			} else {
				$._navigateTo({url: "/ica/search.do?fromView=true&" + $("#interchangeAgreementSearchForm").serialize()});
			}
		}
};