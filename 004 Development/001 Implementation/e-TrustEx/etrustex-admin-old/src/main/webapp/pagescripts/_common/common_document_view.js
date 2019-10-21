define(function () {
		//bind the components events
		$._ADE('click', '#dialog_document_action_cancel', function () {
			$._L('DOCUMENT_VIEW: Close button clicked');
			documentView.close();
		});
		    
		// function definitions
		return documentView = {
				parentForm: null,
				controllerPath: null,
				openDialog: function(parentForm, controllerPath) {
					documentView.parentForm = parentForm;
					documentView.controllerPath = controllerPath;
			    	var url = $._getContextPath() + "/document/" + $("#documentId").val() + "/view/load.do?isViewDialog=true";

			    	$._OAD({
		                dialogId: 'popupDocumentViewDialog',
		                dialogTitle: $._getData('document.view.title'),
		                refreshedFragmentPageUrl: url,
		                isOneButtonOnly: true,
		                buttonSecondaryFn: function () {
		                	$._CD({dialogId: 'popupDocumentViewDialog'});
		                },
		                isShowCloseButton: true,
		                dialogWidth: 900
		            });	   
		    	},
		    	
				close: function () {
					$._L('DOCUMENT_VIEW close dialog');
					$._CD({dialogId: 'popupDocumentViewDialog'});
				}
		}

});