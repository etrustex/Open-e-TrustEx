define(function () {
		 //bind the components events
		$._ADE('click', '#dialog_transaction_action_cancel', function () {
			$._L('TRANSACTION_VIEW: Close button clicked');
			transactionView.close();
		});
		    
		// function definitions
		return transactionView = {
				parentForm: null,
				controllerPath: null,
				openDialog: function(parentForm, controllerPath) {
					transactionView.parentForm = parentForm;
					transactionView.controllerPath = controllerPath;
			    	var url = $._getContextPath() + "/transaction/" + $("#transactionId").val() + "/view/load.do?isViewDialog=true";

			    	$._OAD({
		                dialogId: 'popupTransactionViewDialog',
		                dialogTitle: $._getData('transaction.view.title'),
		                refreshedFragmentPageUrl: url,
		                isOneButtonOnly: true,
		                buttonSecondaryFn: function () {
		                	$._CD({dialogId: 'popupTransactionViewDialog'});
		                },
		                isShowCloseButton: true,
		                dialogWidth: 900
		            });	   
		    	},
		    	
				close: function () {
					$._L('TRANSACTION_VIEW close dialog');
					$._CD({dialogId: 'popupTransactionViewDialog'});
				}
		}

});