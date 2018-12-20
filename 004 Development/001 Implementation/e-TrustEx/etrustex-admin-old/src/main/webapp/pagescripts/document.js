var p = {
		__init: function() {
			return {
		        pageFormNameOverride:'documentForm',
		        pageActionUrlOverride:$('#documentForm').attr('action')
			};
		},
		
		initDisplay: function(){
			$._L("DOCUMENT." + $("#pageMode").val() + "page.initDisplay");
			
			if ($("#pageMode").val() === 'edit'){
				$('#name').attr("readonly", true);
			}
		},
		
		bindEvents: function(){
			$._AE('click', '#action_save', function() {
				$._L("DOCUMENT: SAVE button clicked");
				p.saveDocument();
			});
			
			$._AEE('.action_save_on_enter_document', function() {
				$._L("DOCUMENT: ENTER key pressed");
				p.saveDocument();
			});
			
			$._AE('click', '#action_delete', function() {
				$._L("DOCUMENT: DELETE button clicked");
				p.deleteDocumentConfirmation();
			});
			
			$._AE('click', '#action_edit', function() {
				$._L("DOCUMENT: EDIT button clicked");
				p.editDocument();
			});
			
			$._AE('click', '#action_cancel', function() {
				$._L("DOCUMENT: CANCEL button clicked");
				p.cancel();
			});
			
			$._AE('click', '#action_clear', function() {
				$._L("DOCUMENT: CLEAR button clicked");
				p.clear();
			});
			
			$._AE('click', '#action_reset', function() {
				$._L("DOCUMENT: CLEAR button clicked");
				location.reload();
			});
			
			$._AE('blur', '#name', function(){
				$._L("DOCUMENT - name blur (name = '" + $("#name").val() + "', localName = '" + $("#localName").val() + "').");
				if (!$("#localName").val() && $("#name").val()){
					$("#localName").val($("#name").val());
				}
			});
		},
		
		// function definitions
		saveDocument: function(){
			$._L("DOCUMENT.saveDocument...");
			
	        if (p.isDataValid() === false) {
	            jQuery.noticeAdd({text: $._getData('common.missing.mandatory.fields'), type: 'error'});
	            return;
	        } else {
	        	$._ajaxCall({
	        		pageUrl: $._getContextPath() + "/document/save.do",
	        		pageForm: "documentForm",
	        		fnPostCall: function () {
	        			var ajaxResult = CIPADMIN.ajaxResult();
	        			if(ajaxResult.success) {
	        				CIPADMIN.showSuccessMessage(ajaxResult.message, null, null, "/document/" + ajaxResult.id + "/view.do");
	        			} else {
	        				CIPADMIN.showBusinessError(ajaxResult.message);
	        			}
	        		}
	        	});
	        }
	  	
		},
		
		editDocument: function() {
			window.location = $._getContextPath() + "/document/" + $("#documentId").val() + "/edit.do?"+ $("#documentSearchForm").serialize();
		},

		deleteDocumentConfirmation: function(){
			$._L("DOCUMENT.deleteDocumentConfirmation");
			
			$._msgbox({
				dialogId:'confirmMsg',
                text:$('#msgDeleteConfirmation').text(),
                title: $._getData('document.management.confirmation.message.title'),
                msgboxType:'yes_no',
                alertType:'warning',
                fnCallback:function (r) {
                    if (r === true) {
                    	p.deleteDocument();
                    }
                }
            });
		},
		
		deleteDocument: function(){
			$._L("DOCUMENT.deleteDocument...");
			
			$._ajaxCall({
		  		pageUrl: $._getContextPath() + "/document/" + $("#documentId").val() + "/delete.do?fromView=true",
		  		pageForm: 'documentForm',
		  		fnPostCall: function () {
		  			var ajaxResult = CIPADMIN.ajaxResult();
		  			var nextUrl;
		  			if ($("#refererPage").val() === 'create') {
		  				nextUrl = "/document/create.do";
					} else {
						nextUrl = "/document/search.do?fromView=true&" + $("#documentSearchForm").serialize();
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
	        bValid = $._isFieldValid($('#documentTypeCode'), true) && bValid;
	        
	        return bValid;
	    },
		
		cancel: function() {
			if ($("#pageMode").val() === 'new') {
				CIPADMIN.clearElementsOf('#documentForm');
//				CIPADMIN.goToHomePage();
				CIPADMIN.goBack();
			} else if ($("#refererPage").val() === 'create') {
				$._navigateTo({url: "/document/create.do"});
			} else {
				$._navigateTo({url: "/document/search.do?fromView=true&" + $("#documentSearchForm").serialize()});
			}
		},
		
		clear: function() {
			CIPADMIN.clearElementsOf('#documentForm');
		}
};