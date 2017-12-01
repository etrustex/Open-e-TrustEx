var p = {
	__init: function() {
		return {
	        pageFormNameOverride:'partyForm',
	        pageActionUrlOverride:$('#partyForm').attr('action'),
            appComponents:{
                certificateCreation:true
            }
		};
	},
	
	initDisplay: function(){
		$._L("PARTY." + $("#pageMode").val() + "page.initDisplay");
		
		$("#passwordFlagDiv").addClass('hidden');
		
		if($("#accessRightsError").val()) { // E.g. no access rights messages
			CIPADMIN.showBusinessError($("#accessRightsError").val());
		}
		
		if ($("#pageMode").val() === 'edit' && $.trim($('#credentialsUser').val()).length !== 0){
			$("#party_action_change_password").removeClass('hidden');
			$("#passwordDiv").addClass('hidden');
		}
	},
	
	isDialogMode: false,
	isIcaCreation: false,
	isIcaMultiCreation: false,
	
	bindEvents: function(){
		/*$._ADE('click', "#action_view_certificate", function () {
			$._L('PARTY.action_view_certificate.CLICK');
			certificateCreation.openDialogFrom('party', false, null, $("#partyCertificateId").val(), $("#pageMode").val());
	    });*/
		
		$._AE('click', '#action_save', function(){
			$._L('PARTY: SAVE button clicked');
			var refererController = $("#refererController").val();
			if(refererController == 'IcaController') {
				p.isIcaCreation = true;
			} else if (refererController == 'IcaMultiController') {
				p.isIcaMultiCreation = true;
			} 
			
			p.saveParty();
		});
		
		$._AE('click', '#action_delete', function(){
			$._L('PARTY: DELETE button clicked');
			p.deletePartyConfirmation();
		});
		
		$._AE('click', '#action_edit', function(){
			$._L('PARTY: EDIT button clicked');
			p.editParty();
		});
		
		$._AE('click', '#party_action_new_identifier', function(){
			$._L('PARTY: NEW IDENTIFIER button clicked');
			p.addNewIdentifierRow();
		});
		
		$._AE('click', '.party_action_delete_identifier', function(){
			$._L('PARTY: DELETE IDENTIFIER button clicked');
			p.deleteIdentifierRow($(this).data().identifierIndex);
		});
		
		$._AE('click', '#party_action_manage_certificates', function () {
			$._L('PARTY.party_action_manage_certificates.CLICK');
			certificateCreation.manageCertificates();
	    });
		
		$._AE('click', '#party_action_view_certificates', function () {
			$._L('PARTY.party_action_manage_certificates.CLICK');
			p.viewCertificates();
	    });
		
		
		$._AE('click', "#party_action_change_password", function() {
			$._L('PARTY: CHANGE PASSWORD button clicked');
			p.changePassword();
		});
		
		$._AE('click', '#action_cancel', function(){
			$._L('PARTY: CANCEL button clicked');
			p.cancel();
		});
		
		$._AE('click', '#action_clear', function(){
			$._L('PARTY.action_clear.CLICK');
			p.clear();
		});
		
		$._AE('click', '#action_reset', function(){
			$._L('PARTY: RESET button clicked');
			location.reload();
		});
		
		
		$._AE('change', '#isThirdParty_bs', function() {
			$._L('PARTY: THIRD PARTY FLAG changed => refreshing Credentials');
			$._execFn(function(){
				p.refreshCredentials();
			},true);
		});
		
		$._ADE('click', '#view_certificate_action_cancel', function () {
	    	$._L('CERTIFICATE.VIEW.POPUP Cancel clicked');
	    	$._CD({dialogId: 'viewCertificatePopup'});
	    });
		
	},
	
	
	// function definitions
	saveParty : function() {
    	$._L('PARTY.Saving (from dialog = ' + p.isDialogMode + ')...');
    	$("#partyBusinessDomainId").change();

    	if (!$._isFieldValid($('#partyName'), true)) {
            jQuery.noticeAdd({text: $._getData('common.missing.mandatory.fields'), type: 'error'});
            return;
        }

        if(!p.validatePwd()) {
            return;
        }
        
    	$._blockUIMessageInfo = $._getData('blockUI.party.saving');
    	$._ajaxCall({
    		pageUrl: $._getContextPath() + "/party/save.do",
    		pageForm: 'partyForm',
    		fnPreCall: function() {
    			if (p.isDialogMode === true){
        			$._injectDialog('partyNewDialog');
        			$('#' + $._getPageForm() + '.partyNewPopup_container input').each(function(){
	  				    $(this).attr('name', 'partyForm.' + $(this).attr('name'));
	  				});
    			}
    		},
    		fnPostCall: function (r) {
    			var ajaxResult = CIPADMIN.ajaxResult();
    			var nextUrl = null;
	  			
	  			if (p.isIcaCreation){
	  				nextUrl = "/ica/create.do?partyId=" + ajaxResult.id;
	  			} else if (p.isIcaMultiCreation){
	  				nextUrl = "/ica/multi/create.do?partyId=" + ajaxResult.id;
	  			} else {
	  				nextUrl = "/party/" + ajaxResult.id + "/view.do";
	  			}
	  			
	  			if(ajaxResult.success) {
	  				CIPADMIN.showSuccessMessage(ajaxResult.message, null, null, nextUrl);
	  			} else {
	  				CIPADMIN.showBusinessError(ajaxResult.message);
	  			}
    		}
    	});
    },

	editParty: function() {
		window.location = $._getContextPath() + "/party/" + $("#partyId").val() + "/edit.do?"+ $("#partySearchForm").serialize();
	},
	
	deletePartyConfirmation: function(){
		$._L("PARTY.deletePartyConfirmation");
		
		$._msgbox({
			dialogId:'confirmMsg',
            text:$('#msgDeleteConfirmation').text(),
            title: $._getData('party.management.confirmation.message.title'),
            msgboxType:'yes_no',
            alertType:'warning',
            fnCallback:function (r) {
                if (r === true) {
                	p.deleteParty();
                }
            }
        });
	},
	
	deleteParty: function(){
		$._L("PARTY.deleteParty...");
		$._ajaxCall({
	  		pageUrl: $._getContextPath() + "/party/" + $("#partyId").val() + "/delete.do",
	  		pageForm: 'partyForm',
	  		fnPostCall: function (r) {
	  			var ajaxResult = CIPADMIN.ajaxResult();
	  			var nextUrl;
	  			if ($("#refererPage").val() === 'create') {
	  				nextUrl = "/party/create.do";
				} else {
					nextUrl = "/party/search.do?fromView=true&" + $("#partySearchForm").serialize();
				}
	  			
	  			if(ajaxResult.success) {
    				CIPADMIN.showSuccessMessage(ajaxResult.message, null, null, nextUrl);
    			} else {
    				CIPADMIN.showBusinessError(ajaxResult.message);
    			}		
            }
		});
	},
    
    validatePwd: function() {
    	if ($('#isThirdParty').val() == "true"
    		|| $.trim($('#credentialsUser').val()).length !== 0
    		|| $.trim($('#credentialsPassword1').val()).length !== 0
    		|| $.trim($('#credentialsPassword2').val()).length !== 0) {

    		if(!$._isFieldValid($('#credentialsUser'), true)) {
    		    jQuery.noticeAdd({text: $._getData('common.missing.mandatory.fields'), type: 'error'});
    		    return false;
    		}
    	}

    	if ($("#credentialsPassword1").val() && !$._isFieldValid(null, true, null, $('#credentialsPassword1'), $('#credentialsPassword2'))) {
    		jQuery.noticeAdd({text: $._getData('error.party.credentials.password.mismatch'), type: 'error'});
    		return false;
    	}

    	return true;
    },


	refreshCredentials: function(){
		$._L("PARTTY.CREATION.POPUP.refreshCredentials");
	  	$._AR({
	  		pageUrl: $._getContextPath() + '/party/credentials.do',
    		fnPreCall: function() {
    			if (p.isDialogMode === true){
        			$._injectDialog('partyNewDialog');
        			$('#' + $._getPageForm() + '.partyNewPopup_container input').each(function(){
	  				    $(this).attr('name', 'partyForm.' + $(this).attr('name'));
	  				});
    			}
    		},
	  		callType: 'GET',
	  		id: "partyCredentialsDiv"
		});
	},
	
	addNewIdentifierRow: function(){
		$._L("PARTTY.CREATION.POPUP.addNewIdentifierRow");
		$._AR({
			callType: 'POST',
			pageForm : "partyForm",
			pageUrl: $._getContextPath() + "/party/addIdentifier.do" ,
	  		id: "identifiers-div",
	  		fnPostCall: function (r) {
	  			var validationMsg = $("#validation_msg").val();
	  			
	  			if (validationMsg) {
	  				CIPADMIN.showBusinessError(validationMsg);
	  			} 
            }
		});
	},
	
	deleteIdentifierRow: function(identifierIndex){
		$._L("PARTTY.CREATION.POPUP.deleteIdentifierRow");
		var identifierId = $("#identifierId" + identifierIndex).val();
		var identifierType = $("#identifierType" + identifierIndex).val();
		var identifierValue = $("#identifierValue" + identifierIndex).val();
		
		$("#identifierMarkedForDeletion" + identifierIndex).prop('checked', true);
		
		$._AR({
			callType: 'POST',
			pageUrl : $._getContextPath() + '/party/identifier/delete.do?identifierId=' + identifierId 
	  			+ "&identifierType=" + identifierType + "&identifierValue=" + identifierValue,
			pageForm : "partyForm",
			id : "identifiers-div"
		});
	},
	
	changePassword: function() {
		buttons = [
           {
               text: $._getData('button.cancel'),
               click: function() { 
            	   CIPADMIN.clearElementsOf('#passwordDiv'); 
            	   p.closeChangePwdDialog(); 
               }
           },
           {
               text: $._getData('button.ok'),
               click: function() { 
            	   if(p.validatePwd()) {
            		   p.closeChangePwdDialog(); 
            		   $("#changePasswordFlag").attr("checked","checked");
            	   } else {
            		   jQuery.noticeAdd({text: $._getData('common.popup.password.mismatch'), type: 'error'});
            	   }
               }
           }
        ];
		
		$._openDialog({
            dialogId: 'passwordDiv',
            dialogTitle: $._getData('common.popup.title.password.change'),
            dialogWidth: 425,
            isDialogOverflowAllowed: false,
            isBodyOverflowHiddenOnCreate: false,
//            fnDialogOpen: function () {},
            fnDialogClose: function() { $("#passwordDiv").addClass('hidden');},
            dialogButtons: buttons,
            isDialogButtonsShowIcons: true,
            isKeepCurrentDialogId: false,
            isCloseOnEscape: true
        });
	},
	
	closeChangePwdDialog: function () {
        $._L('CHANGE_PWD_DIALOG.close');
        $._CD({
        	dialogId: 'passwordDiv', 
        	isEmptyOnClose: false
        });
    },
    

	viewCertificates: function () {
        $._L('VIEW_CERTIFICATES');
        
        $._OAD({
        	dialogId: 'viewCertificatePopup',
            dialogTitle: $._getData('common.popup.title.certificates.party.view'),
            dialogWidth: 1000,
//            dialogHeight: 600,
            refreshedFragmentPageUrl: $._getContextPath() + '/party/' + $("#partyId").val() + '/certificates.do',
            refreshedFragmentPageForm: "partyFormForm",
            isOneButtonOnly: true,
            isShowCloseButton: true
        });
    },
    
	cancel: function() {
		CIPADMIN.clearElementsOf('#partyForm');
		if ($("#pageMode").val() === 'new') {
//			CIPADMIN.goToHomePage();
			CIPADMIN.goBack();
		} else if ($("#refererPage").val() === 'create') {
			$._navigateTo({url: "/party/create.do"});
		} else {
			$._navigateTo({url: "/party/search.do?fromView=true&" + $("#partySearchForm").serialize()});
		}
	},
	
	clear: function() {
		CIPADMIN.clearElementsOf('#partyForm');
	}
};