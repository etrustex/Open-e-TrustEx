define(function () {
		//bind the components events
		$._ADE('click', '#dialog_party_action_cancel', function () {
			$._L('PARTY_VIEW: Close button clicked');
			partyView.close();
		});

		$._ADE('click', '#party_action_view_certificates', function () {
            $._L('PARTY.party_action_manage_certificates.CLICK');
            partyView.viewCertificates();
        });

        $._ADE('click', '#view_certificate_action_cancel', function () {
            $._L('CERTIFICATE.VIEW.POPUP Cancel clicked');
            $._CD({dialogId: 'viewCertificatePopup'});
        });
		    
		// function definitions
		return partyView = {
				parentForm: null,
				controllerPath: null,
				partyType: null,
				openDialog: function(parentForm, controllerPath, partyType) {
					partyView.parentForm = parentForm;
					partyView.controllerPath = controllerPath;
					partyView.partyType = partyType;

					var partyIdSelector = partyType ? "#" + partyType + "Id" : "#partyId";

			    	var url = $._getContextPath() + "/party/" + $(partyIdSelector).val() + "/view/load.do?isViewDialog=true";

			    	$._OAD({
		                dialogId: 'popupPartyViewDialog',
		                dialogTitle: $._getData('party.view.title'),
		                refreshedFragmentPageUrl: url,
		                isOneButtonOnly: true,
		                buttonSecondaryFn: function () {
		                	$._CD({dialogId: 'popupPartyViewDialog'});
		                },
		                isShowCloseButton: true,
		                dialogWidth: 900
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
                         buttonSecondaryFn: function () {
                            $._CD({dialogId: 'viewCertificatePopup'});
                        },
                        isShowCloseButton: true
                    });
                },
		    	
				close: function () {
					$._L('PARTY_VIEW close dialog');
					$._CD({dialogId: 'popupPartyViewDialog'});
				}
		}

});