define(function () {
		//bind the components events
		$._ADE('click', '#dialog_ica_action_cancel', function () {
			$._L('ICA_VIEW: CANCEL button clicked');
			icaView.close();
		});
		
		// function definitions
		return icaView = {
				parentForm: null,
				controllerPath: null,
				openDialog: function(parentForm, controllerPath) {
					icaView.parentForm = parentForm;
					icaView.controllerPath = controllerPath;
			    	var url = $._getContextPath() + "/ica/" + $("#icaId").text() + "/view/load.do?isViewDialog=true";
			    	
			    	$._OAD({
		                dialogId: 'popupIcaViewDialog',
		                dialogTitle: $._getData('ica.view.title'),
		                refreshedFragmentPageUrl: url,
		                isOneButtonOnly: true,
		                buttonSecondaryFn: function () {
		                	$._CD({dialogId: 'popupIcaViewDialog'});
		                },
		                isShowCloseButton: true,
		                dialogWidth: 900
		            });	   
		    	},
				
				close: function () {
					$._L('ICA_VIEW close dialog');
					$._CD({dialogId: 'popupIcaViewDialog'});
				}
		}

});