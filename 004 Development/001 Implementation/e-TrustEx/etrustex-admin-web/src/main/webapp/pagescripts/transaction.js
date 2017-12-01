var p = {
		__init: function() {
			return {
		        pageFormNameOverride:'transactionForm',
		        pageActionUrlOverride:$('#transactionForm').attr('action'),
		        appComponents:{
		        	documentSearch: true
	            }
			};
		},
		
		initDisplay: function(){
			$._L("TRANSACTION." + $("#pageMode").val() + "page.initDisplay");
			
			var updateNotAuthorizedMsg = $("#updateNotAuthorizedMsg").val();
			var pageMode = $("#pageMode").val();
			
			if (!$("#profiles").val()) {
				$("#profiles").val("-1")
			}
			
			if(updateNotAuthorizedMsg) {
				CIPADMIN.showBusinessError(updateNotAuthorizedMsg);
			}
		},
		
		bindEvents: function(){
			$._ADE('click', '#action_search_document', function () {
				documentSearch.openDialog("transactionForm", "transaction");
		    });
			
			
			$._AE('click', '#action_save', function() {
				$._L("TRANSACTION: SAVE button clicked");
				p.saveTransaction();
			});
			
			$._AEE('.action_save_on_enter_transaction', function() {
				$._L("TRANSACTION: ENTER key pressed");
				p.saveTransaction();
			});
			
			$._AE('click', '#action_delete', function() {
				$._L("TRANSACTION: DELETE button clicked");
				p.deleteTransactionConfirmation();
			});
			
			$._AE('click', '#action_edit', function() {
				$._L("TRANSACTION: EDIT button clicked");
				p.editTransaction();
			});
			
			$._AE('click', '#action_cancel', function() {
				$._L("TRANSACTION: CANCEL button clicked");
				p.cancel();
			});
			
			$._AE('click', '#action_clear', function() {
				$._L("TRANSACTION: CLEAR button clicked");
				p.clear();
			});
			
			$._AE('click', '#action_reset', function() {
				$._L("TRANSACTION: CLEAR button clicked");
				location.reload();
			});

			$._AE('click', '#action_remove_document', function(){
				p.clearDocument(this);
			});
			
			$._AE('blur', '#name', function() {
				var name = $("#name").val();
				if (name) {
					if (!$("#requestLocalName").val()) {
						$("#requestLocalName").val(name + "Request");
					}
					if (!$("#responseLocalName").val()) {
						$("#responseLocalName").val(name + "Response");
					}
				}
			});
			
			/*
			 * UC70_BR04	Default option for the associated profiles is None. 
			 * If profiles have been already selected and user chooses None option, the previously selected profiles will be removed from the selection.
			 * If None option is selected, and user selects any profile in the list, None will be removed from the selection. 
			 */
			var profileNoneSelected;
			
			$._AE('focus', '#profiles', function() {
				profileNoneSelected = $('#profiles option[value="-1"]').prop('selected');
			});
			
			$._AE('change', '#profiles', function() {
				if(profileNoneSelected) {
					$("#profiles option:selected").each(function() {
						if(this.value != "-1") {
							$('#profiles option[value="-1"]').prop('selected', false);
							profileNoneSelected = false;
							return false;
						}
					});
				} else {
					$("#profiles option:selected").each(function() {
						if(this.value == "-1") {
							$("#profiles option:selected").prop("selected", false)
							$('#profiles option[value="-1"]').prop('selected', true);
							profileNoneSelected = true;
							return false;
						}
					});
				}
			});
		},
		
		// function definitions
		saveTransaction: function(){
			$._L("TRANSACTION.saveTransaction...");
			
	        if (p.isDataValid() === false) {
	            jQuery.noticeAdd({text: $._getData('common.missing.mandatory.fields'), type: 'error'});
	            return;
	        } else {
	        	$._ajaxCall({
	        		pageUrl: $._getContextPath() + "/transaction/save.do",
	        		pageForm: "transactionForm",
	        		fnPostCall: function () {
	        			var ajaxResult = CIPADMIN.ajaxResult();
	        			if(ajaxResult.success) {
	        				CIPADMIN.showSuccessMessage(ajaxResult.message, null, null, "/transaction/" + ajaxResult.id + "/view.do");
	        			} else {
	        				CIPADMIN.showBusinessError(ajaxResult.message);
	        			}
	        		}
	        	});
	        }
	  	
		},
		
		editTransaction: function() {
			window.location = $._getContextPath() + "/transaction/" + $("#transactionId").val() + "/edit.do?"+ $("#transactionSearchForm").serialize();
		},

		deleteTransactionConfirmation: function(){
			$._L("TRANSACTION.deleteTransactionConfirmation");
			
			$._msgbox({
				dialogId:'confirmMsg',
                text:$('#msgDeleteConfirmation').text(),
                title: $._getData('transaction.management.confirmation.message.title'),
                msgboxType:'yes_no',
                alertType:'warning',
                fnCallback:function (r) {
                    if (r === true) {
                    	p.deleteTransaction();
                    }
                }
            });
		},
		
		deleteTransaction: function(){
			$._L("TRANSACTION.deleteTransaction...");
			
			$._ajaxCall({
		  		pageUrl: $._getContextPath() + "/transaction/" + $("#transactionId").val() + "/delete.do?fromView=true",
		  		pageForm: 'transactionForm',
		  		fnPostCall: function () {
		  			var ajaxResult = CIPADMIN.ajaxResult();
		  			var nextUrl;
		  			if ($("#refererPage").val() === 'create') {
		  				nextUrl = "/transaction/create.do";
					} else {
						nextUrl = "/transaction/search.do?fromView=true&" + $("#transactionSearchForm").serialize();
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
			bValid = $._isFieldValid($('#name'), true) && bValid;
	        bValid = $._isFieldValid($('#localName'), true) && bValid;
	        bValid = $._isFieldValid($('#namespace'), true) && bValid;
	        bValid = $._isFieldValid($('#transactionTypeCode'), true) && bValid;
	        
	        return bValid;
	    },

	    clearDocument: function() {
			$("#documentId").val('');
			$("#documentName").val('');
			$("#displayDocumentName").text('');
		},
		
		cancel: function() {
			if ($("#pageMode").val() === 'new') {
				CIPADMIN.clearElementsOf('#transactionForm');
//				CIPADMIN.goToHomePage();
				CIPADMIN.goBack();
			} else if ($("#refererPage").val() === 'create') {
				$._navigateTo({url: "/transaction/create.do"});
			} else {
				$._navigateTo({url: "/transaction/search.do?fromView=true&" + $("#transactionSearchForm").serialize()});
			}
		},
		
		clear: function() {
			CIPADMIN.clearElementsOf('#transactionForm');
		}
};