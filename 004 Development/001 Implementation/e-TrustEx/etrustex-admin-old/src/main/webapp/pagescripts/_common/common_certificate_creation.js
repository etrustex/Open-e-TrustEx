define(function () {

    $._L('certificate_creation:REQUIRED');

    //bind the components events
    //--------------------------
    $._ADE('click', '#certificate_add_action_ok', function () {
		$._L('PARTY.certificate_add_action_ok.CLICK');
		certificateCreation.importCertificate();
    });
    
    $._ADE('click', '#manage_certificates_action_ok', function () {
    	certificateCreation.certificateDialogOK();
    });
    
    $._ADE('click', '#manage_certificates_action_cancel', function () {
    	$._L('CERTIFICATE.CREATION.POPUP Cancel clicked');
    	certificateCreation.certificateDialogCancel();
    });
    
	$._ADE('click','.editCertificateRow', function(){
		$._L("CERTIFICATE.page: EDIT button clicked" );
		certificateCreation.editCertificate($(this).closest('tr').attr('id'));
	});
    
	$._ADE('click','.deleteCertificateRow', function(){
		$._L("CERTIFICATE.page: DELETE button clicked" );
		certificateCreation.deleteCertificateConfirmation($(this).closest('tr').attr('id'), $(this).closest('tr').find("td.select-certificate").text());
	});
	
	$._ADE('click','#edit_certificate_action_ok', function(){
		$._L("CERTIFICATE.page: EDIT button clicked" );
		certificateCreation.editCertificateOk($("#certificateIndex").val());
	});
	
	$._ADE('click', '#edit_certificate_action_cancel', function () {
    	$._L('CERTIFICATE.CREATION.POPUP Cancel clicked');
    	$._CD({dialogId: 'editCertificatePopup'});
    });
	
	$._ADE('change','#isRevoked_bs_Y', function(event) {
		if($('#isRevoked_bs_Y').val() === "true") {
			certificateCreation.revokeConfirmation('isRevoked');
		}
	});
	
	$._ADE('change','#ceIsRevoked_bs_Y', function(event) {
		if($('#ceIsRevoked_bs_Y').val() === "true") {
			certificateCreation.revokeConfirmation('ceIsRevoked');
		}
	});
    
	
	$._addDocumentEvent('change', '#certificate_file', function() {
		var fileName = $(this).val();
		var valid = $._isFieldValid($(this), true);
		
		//validate the filename
        if (!valid) {
            jQuery.noticeAdd({type:'error',text: $._getData('error.certificate.file.empty')});
            return;
        }
        
        $('#certificate_filename').text(fileName);
    });
	
    //declare the component object
    //----------------------------
    return certificateCreation = {
    		/*
    		 * Opens the "Manage Party Certificates" window 
    		 */
    	    manageCertificates: function () {
    	        $._L('MANAGE_CERTIFICATES');
    	        
    	        $._OAD({
    	        	dialogId: 'manageCertificatesPopup',
    	            dialogTitle: $._getData('common.popup.title.certificates.party.manage'),
    	            dialogWidth: 1000,
    	            dialogHeight: 600,
    	            isShowButtons: false,
    	            refreshedFragmentPageUrl: $._getContextPath() + '/certificate/manage.do?partyId='  + $('#partyId').val(),
    	            refreshedFragmentPageForm: "certificatesForm",
    	            isShowCloseButton: false,
    	            fnDialogClose: function() {certificateCreation.certificateDialogCancel();}
    	        });
    	    },
    	    
    	    importCertificate: function() {
    	    	var bValid = true;
    	    	
    	    	var files = $("#certificate_file")[0].files;
    	    	if(files.length == 0) {
    	    		var bValid = false;
    	    		jQuery.noticeAdd({text: $._getData('error.certificate.file.empty'), type: 'error'});
    	    	} else if(files[0].size > 100000) {
    	    		jQuery.noticeAdd({text: $._getData('error.certificate.file.size'), type: 'error'});
    	    	}
    	    	
    	    	
    	    	/*
    	    	var bValid = certificateCreation.isDataValid();

    	        if (bValid === false) {
    	            jQuery.noticeAdd({text: $._getData('common.missing.mandatory.fields'), type: 'error'});
    	            return;
    	        }

    	    	 */
    	    	
    	    	if(bValid) {
    	    		$('#importCertificatesForm').submit();
    	    		setTimeout(function() {
    	    			$._AR({
    	    				id:'manageCertificatesPopup',
    	    				pageUrl: $._getContextPath() + '/certificate.do',
    	    				pageForm: "certificatesForm",
    	    				fnPostCall: function() {
    	    					var result = $('#manageCertsMsg').val();
    	    					if( result != "success" ) {
    	    						CIPADMIN.showBusinessError(result);
    	    					}
    	    					$._elementUnblock($('.ui-dialog'));
    	    					$._unblockUI();
    	    				}
    	    			});
    	    		}, 1000);
    	    	}
    	    	

    	    	/*
    	    	 * TODO. File upload in jscaf 1.10 uses j:object tag. We can't use _submitForm2 for multipart until we update jscaf tags.  
    	    	 */
    	    	/*
    	    	$._SF({
                    call: 'certificate/import.do',
                    formId: 'importCertificatesForm',
                    fnPostCall:function() {
                		$._AR({
                			id:'manageCertificatesPopup',
                			pageUrl: $._getContextPath() + '/certificate.do',
                			pageForm: "certificatesForm",
                			fnPostCall: function() {
                    				var result = $('#manageCertsMsg').val();
                    				if( result != "success" ) {
                    					CIPADMIN.showBusinessError(result);
                    				}
                    				$._elementUnblock($('.ui-dialog'));
                    				$._unblockUI();
                			}
                		});
                    }
                });
    	    	*/
    	    },
    	    
    	    certificateDialogOK: function() {
    	    	$._L('CERTIFICATE.CREATION.POPUP.OK - save and close');
/*    	    	
    	    	var bValid = certificateCreation.isDataValid();

    	        if (bValid === false) {
    	            jQuery.noticeAdd({text: $._getData('common.missing.mandatory.fields'), type: 'error'});
    	            return;
    	        }
    	        */
    	    	
    	    	$._blockUIMessageInfo = $._getData('blockUI.certificates.adding');
    	    	
    	    	$._ajaxCall({
    	    		pageUrl: $._getContextPath() + "/certificate/add.do",
    	    		pageForm: 'certificatesForm',
    	    		fnPostCall: function (r) {
    	    			var ajaxResult = CIPADMIN.ajaxResult();
    		  			
    		  			if(ajaxResult.success) {
    		  				CIPADMIN.showSuccessMessage(ajaxResult.message, true);
    		  				
    		  				$._CD({dialogId: 'manageCertificatesPopup'});
    		  			} else {
    		  				CIPADMIN.showBusinessError(ajaxResult.message);
    		  			}
    	    		}
    	    	});
    	    },
    	    
    	    certificateDialogCancel: function() {
    	    	$._L('CERTIFICATE.CREATION.POPUP.CANCEL - clear form and close');
    	    	
    	    	$._ajaxCall({
    	    		pageUrl: $._getContextPath() + "/certificate/cancel.do",
    	    		pageForm: 'certificatesForm',
    	    		fnPostCall: function (r) {
    	    			$._CD({dialogId: 'manageCertificatesPopup'});
    	    		}
    	    	});
    	    },
   
    	    editCertificate: function(index) {
    			$._log("CERTIFICATE.page.editCertificate... index " + index);
    			
    			 $._OAD({
     	        	dialogId: 'editCertificatePopup',
     	            dialogTitle: $._getData('common.popup.title.certificateEdit'),
     	            isSecondModalDialog: true,
     	            isDisableFirstDialog: true,
     	            firstDialogId: 'manageCertificatesPopup',
//     	            dialogWidth: 600,
     	            dialogHeight: 400,
     	            refreshedFragmentPageUrl: $._getContextPath() + "/certificate/" + index + "/edit.do",
     	            refreshedFragmentPageForm: "certificateEditForm",
     	            isOneButtonOnly: true,
     	            isShowCloseButton: true
     	        });
    			 
    		},
    		
    		editCertificateOk:  function(certificateIndex) {
    			$._L("CERTIFICATE.editCertificateOk...");
    			
    			$._ajaxCall({
    		  		pageUrl: $._getContextPath() + "/certificate/edit/" + certificateIndex + "/save.do",
    		  		pageForm: 'certificateEditForm',
    		  		fnPostCall: function (r) {
    		  			var ajaxResult = CIPADMIN.ajaxResult();
    		  			if(ajaxResult.success) {
    		  				$._CD({dialogId: 'editCertificatePopup'});
    		  				
    		  				$._AR({
                	    		id:'manageCertificatesPopup',
                	    		pageUrl: $._getContextPath() + '/certificate.do?partyId=' + $('#partyId').val(),
                	    		pageForm: "certificatesForm",
                	    		fnPostCall: function() {
                	    			if( $('#manageCertsMsg').text() == "success" ) {
                	    				$._L('CERTIFICATE.CREATION.POPUP.certificateDialogOK - message SUCCESS');
                		  			} 
                	    		}
                	    	});
    	    			} else {
    	    				CIPADMIN.showBusinessError(ajaxResult.message);
    	    			}
    	            }
    			});
    		},
    		
    		deleteCertificateConfirmation: function(index, serialNumber){
    			$._L("CERTIFICATE.page.deleteCertificateConfirmation");
    			
    			$._msgbox({
    				dialogId: 'confirmMsg',
    	            text: $('#msgDeleteCertificateConfirmation').text() + serialNumber + " ?" + "<br/>" + $('#msgDeleteCertificateConfirmationNote').text(),
    	            title: $('#msgDeleteCertificateConfirmationTitle').text(),
    	            msgboxType: 'yes_no',
    	            alertType: 'warning',
    	            fnCallback: function (r) {
    	                if (r === true) {
    	                	certificateCreation.deleteCertificate(index);
    	                }
    	            },
    	            isEnterKeyOnPrimaryActionEnabled: true
    	        });
    		},
    		
    		deleteCertificate: function(index){
    			$._L("CERTIFICATE.deleteCertificate... index "+ index);
    			
    			$._ajaxCall({
    		  		pageUrl: $._getContextPath() + "/certificate/" + index + "/delete.do",
    		  		pageForm: 'certificatesForm',
    		  		fnPostCall: function (r) {
    		  			var ajaxResult = CIPADMIN.ajaxResult();
    		  			if(ajaxResult.success) {
    		  				$._AR({
                	    		id:'manageCertificatesPopup',
                	    		pageUrl: $._getContextPath() + '/certificate.do',
                	    		pageForm: "certificatesForm",
                	    		fnPostCall: function() {
                	    			if( $('#manageCertsMsg').text() == "success" ) {
                	    				$._L('CERTIFICATE.CREATION.POPUP.certificateDialogOK - message SUCCESS');
                		  			} 
                	    		}
                	    	});
    	    			} else {
    	    				CIPADMIN.showBusinessError(ajaxResult.message);
    	    			}
    	            }
    			});
    		},
    		
    		revokeConfirmation: function(associatedPropertyId) {
    			$._L("CERTIFICATE.page.deleteCertificateConfirmation");
    			var dialogId = 'confirmRevokeCertificate';
    			
    			$._MB({
    				dialogId: dialogId,
    	            text: $._getData('common.popup.text.certificate.revoke.confirmation'),
    	            title: $._getData('common.popup.title.certificate.revoke.confirmation'),
    	            msgboxType: 'yes_no',
    	            alertType: 'warning',
    	            fnCallback: function (r) {
    	                if (r !== true) {
    	                	$('#' + associatedPropertyId + '_bs_N').next('label').trigger("click");
    	                }
    	            },
    	            isEnterKeyOnPrimaryActionEnabled: true
    	        });
    			
    			$('#' + dialogId).parent().css({'z-index':'2000'});
    		},
    	    
    	    isDataValid: function(){
    	    	var bValid = true;

    	    	//TODO: remove comment after implementation of server side validation
    	        /*bValid = $._isFieldValid($('#partyName'), true) && bValid;
    	        if ($('#isThirdParty').val().toUpperCase() === 'TRUE' 
    	        	|| $.trim($('#credentialsUser').val()).length !== 0 
    	        	|| $.trim($('#credentialsPassword1').val()).length !== 0 
    	        	|| $.trim($('#credentialsPassword2').val()).length !== 0){
    	        	bValid = $._isFieldValid($('#credentialsUser'), true) && bValid;
    	        	bValid = $._isFieldValid($('#credentialsPassword1'), true) && bValid;
    	        	bValid = $._isFieldValid($('#credentialsPassword2'), true) && bValid;
    	        }*/
    	        
    	        return bValid;
    	    },
    };
});