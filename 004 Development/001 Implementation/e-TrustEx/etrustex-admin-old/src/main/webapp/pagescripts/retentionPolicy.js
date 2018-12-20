var p = {
		__init: function() {
			return {
		        pageFormNameOverride:'retentionPolicyForm',
		        pageActionUrlOverride:$('#retentionPolicyForm').attr('action'),
		        appComponents:{
		        	transactionSearch: true,
					transactionView: true
	            }
			};
		},
		
		initDisplay: function(){
			$._L("retentionPolicy." + $("#pageMode").val() + "page.initDisplay");

			if ($("#pageMode").val() === 'edit'){
				$('#name').attr("readonly", true);
			}
		},
		
		bindEvents: function() {
			$._ADE('click', '#action_search_transaction', function () {
				transactionSearch.openDialog({
                       parentForm: "retentionPolicyForm",
                       controllerPath: "retentionPolicy"
                });
		    });

			$._AE('click', '#action_view_transaction', function () {
				transactionView.openDialog("retentionPolicyForm", "retentionPolicy");
			});
			
			$._AE('click', '#action_remove_transaction', function(){
				transactionSearch.clearTransactionDiv(this);
			});
			
			$._AE('click', '#action_save', function() {
				$._L("retentionPolicy: SAVE button clicked");
				p.save();
			});

            $._AE('click', '#action_edit', function() {
                $._L("retentionPolicy: EDIT button clicked");
                window.location = $._getContextPath() + "/retentionPolicy/" + $("#id").val() + "/edit.do?"+ $("#retentionPolicySearchForm").serialize();
            });

			$._AE('click', '#action_delete', function() {
				$._L("retentionPolicy: DELETE button clicked");
				p.deleteConfirmation();
			});
			
			$._AE('click', '#action_cancel', function() {
				$._L("retentionPolicy: CANCEL button clicked");
				p.cancel();
			});
			
			$._AE('click', '#action_clear', function() {
				$._L("retentionPolicy: CLEAR button clicked");
				p.clear();
			});
			
			$._AE('click', '#action_reset', function() {
				$._L("retentionPolicy: CLEAR button clicked");
				location.reload();
			});

			// Duration field numbers only
            $._AE('keyup', "#duration", function() {
                this.value = this.value.replace(/[^0-9\.]/g,'');
            });
		},
		
		// function definitions
		save: function(){
			$._L("retentionPolicy.saveretentionPolicy...");
			
	        if (p.isDataValid() === false) {
	            jQuery.noticeAdd({text: $._getData('common.missing.mandatory.fields'), type: 'error'});
	            return;
	        } else {
	        	$._ajaxCall({
	        		pageUrl: $._getContextPath() + "/retentionPolicy/save.do",
	        		pageForm: "retentionPolicyForm",
	        		fnPostCall: function () {
	        			var ajaxResult = CIPADMIN.ajaxResult();
	        			if(ajaxResult.success) {
	        				CIPADMIN.showSuccessMessage(ajaxResult.message, null, null, "/retentionPolicy/" + ajaxResult.id + "/view.do");
	        			} else {
	        				CIPADMIN.showBusinessError(ajaxResult.message);
	        			}
	        		}
	        	});
	        }
	  	
		},
		
		deleteConfirmation: function(){
			$._L("retentionPolicy.deleteretentionPolicyConfirmation");
			
			$._msgbox({
				dialogId:'confirmMsg',
                text:$('#msgDeleteConfirmation').text(),
                title: $._getData('retentionPolicy.management.confirmation.message.title'),
                msgboxType:'yes_no',
                alertType:'warning',
                fnCallback:function (r) {
                    if (r === true) {
                    	p.deleteretentionPolicy();
                    }
                }
            });
		},
		
		deleteretentionPolicy: function(){
			$._L("retentionPolicy.deleteCertificate...");
			
			$._ajaxCall({
		  		pageUrl: $._getContextPath() + "/retentionPolicy/" + $("#id").val() + "/delete.do?fromView=true",
		  		pageForm: 'retentionPolicyForm',
		  		fnPostCall: function () {
		  			var ajaxResult = CIPADMIN.ajaxResult();
		  			var nextUrl;
		  			if ($("#refererPage").val() === 'create') {
		  				nextUrl = "/retentionPolicy/create.do";
					} else {
						nextUrl = "/retentionPolicy/search.do?fromView=true&" + $("#retentionPolicySearchForm").serialize();
					}
		  			
		  			if(ajaxResult.success) {
        				CIPADMIN.showSuccessMessage(ajaxResult.message, null, null, nextUrl);
        			} else {
        				CIPADMIN.showBusinessError(ajaxResult.message);
        			}
                }
			});
		},
		
		isDataValid: function() {
			var bValid = true;
			bValid = $._isFieldValid($('#duration'), true) && bValid;
	        bValid = $._isFieldValid($('#transactionId'), true) && bValid;

	        return bValid;
	    },
	
		cancel: function() {
			if ($("#pageMode").val() === 'new') {
				CIPADMIN.clearElementsOf('#retentionPolicyForm');
				CIPADMIN.goBack();
			} else if ($("#refererPage").val() === 'create') {
				$._navigateTo({url: "/retentionPolicy/create.do"});
			} else {
				$._navigateTo({url: "/retentionPolicy/search.do?fromView=true&" + $("#retentionPolicySearchForm").serialize()});
			}
		},
		
		clear: function() {
			location.reload();
		}
};