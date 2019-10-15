var p = {
		__init: function() {
			return {
		        pageFormNameOverride:'metadataForm',
		        pageActionUrlOverride:$('#metadataForm').attr('action'),
		        appComponents:{
		        	documentSearch: true,
		        	transactionSearch: true,
		        	icaSearch: true,
		        	documentView: true,
					transactionView: true,
					icaView: true,
					partyView: true,
					partySearch: true
	            }
			};
		},
		
		initDisplay: function(){
			$._L("METADATA." + $("#pageMode").val() + "page.initDisplay");

			if ($("#pageMode").val() === 'edit'){
				$('#name').attr("readonly", true);
			}
		},
		
		bindEvents: function() {
			$._ADE('click', '#action_search_document', function () {
				documentSearch.openDialog("metadataForm", "metadata");
		    });
			
			$._ADE('click', '#action_search_ica', function () {
				icaSearch.openDialog({
                        parentForm: "metadataForm",
                        controllerPath: "metadata"
                });
		    });

		    $._ADE('click', '#action_search_party', function () {
                partySearch.openDialog({
                        parentForm: "metadataForm",
                        controllerPath: "metadata",
                        partyType: "sender"
                });
            });
			
			$._ADE('click', '#action_search_transaction', function () {
				transactionSearch.openDialog({
                          parentForm: "metadataForm",
                          controllerPath: "metadata"
                });
		    });

		    $._AE('click', '#action_view_document', function () {
				documentView.openDialog("metadataForm", "metadata");
			});

			$._AE('click', '#action_view_ica', function () {
				icaView.openDialog("metadataForm", "metadata");
			});

			$._AE('click', '#action_view_party', function () {
                partyView.openDialog("metadataForm", "metadata");
            });

			$._AE('click', '#action_view_transaction', function () {
				transactionView.openDialog("metadataForm", "metadata");
			});
			
			$._AE('click', '#action_remove_document', function(){
				p.clearDocument(this);
			});
			
			$._AE('click', '#action_remove_ica', function(){
				p.clearIca(this);
			});

			$._AE('click', '#action_remove_party', function(){
                partySearch.clearPartyDiv(this);
            });
			
			$._AE('click', '#action_remove_transaction', function(){
				transactionSearch.clearTransactionDiv(this);
			});
			
			$._AE('click', '#action_save', function() {
				$._L("METADATA: SAVE button clicked");
				p.saveMetadata();
			});
			
			$._AE('click', '#action_delete', function() {
				$._L("METADATA: DELETE button clicked");
				p.deleteMetadataConfirmation();
			});
			
			$._AE('click', '#action_edit', function() {
				$._L("METADATA: EDIT button clicked");
				p.editMetadata();
			});
			
			$._AE('click', '#action_cancel', function() {
				$._L("METADATA: CANCEL button clicked");
				p.cancel();
			});
			
			$._AE('click', '#action_clear', function() {
				$._L("METADATA: CLEAR button clicked");
				p.clear();
			});
			
			$._AE('click', '#action_reset', function() {
				$._L("METADATA: CLEAR button clicked");
				location.reload();
			});
		},
		
		// function definitions
		saveMetadata: function(){
			$._L("METADATA.saveMetadata...");
			
	        if (p.isDataValid() === false) {
	            jQuery.noticeAdd({text: $._getData('common.missing.mandatory.fields'), type: 'error'});
	            return;
	        } else {
	        	$._ajaxCall({
	        		pageUrl: $._getContextPath() + "/metadata/save.do",
	        		pageForm: "metadataForm",
	        		fnPostCall: function () {
	        			var ajaxResult = CIPADMIN.ajaxResult();
	        			if(ajaxResult.success) {
	        				CIPADMIN.showSuccessMessage(ajaxResult.message, null, null, "/metadata/" + ajaxResult.id + "/view.do");
	        			} else {
	        				CIPADMIN.showBusinessError(ajaxResult.message);
	        			}
	        		}
	        	});
	        }
	  	
		},
		
		editMetadata: function() {
			window.location = $._getContextPath() + "/metadata/" + $("#metadataItemId").val() + "/edit.do?"+ $("#metadataSearchForm").serialize();
		},

		deleteMetadataConfirmation: function(){
			$._L("METADATA.deleteMetadataConfirmation");
			
			$._msgbox({
				dialogId:'confirmMsg',
                text:$('#msgDeleteConfirmation').text(),
                title: $._getData('metadata.management.confirmation.message.title'),
                msgboxType:'yes_no',
                alertType:'warning',
                fnCallback:function (r) {
                    if (r === true) {
                    	p.deleteMetadata();
                    }
                }
            });
		},
		
		deleteMetadata: function(){
			$._L("METADATA.deleteCertificate...");
			
			$._ajaxCall({
		  		pageUrl: $._getContextPath() + "/metadata/" + $("#metadataItemId").val() + "/delete.do?fromView=true",
		  		pageForm: 'metadataForm',
		  		fnPostCall: function () {
		  			var ajaxResult = CIPADMIN.ajaxResult();
		  			var nextUrl;
		  			if ($("#refererPage").val() === 'create') {
		  				nextUrl = "/metadata/create.do";
					} else {
						nextUrl = "/metadata/search.do?fromView=true&" + $("#metadataSearchForm").serialize();
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
			bValid = $._isFieldValid($('#name'), true) && bValid;
	        bValid = $._isFieldValid($('#localName'), true) && bValid;
	        bValid = $._isFieldValid($('#namespace'), true) && bValid;
	        bValid = $._isFieldValid($('#documentTypeCode'), true) && bValid;
	        
	        return bValid;
	    },
	
	    clearDocument: function(el) {
	        $(el).remove();
			$("#documentId").val('');
			$("#documentName").val('');
			$("#displayDocumentName").text('');
		},
		
		clearIca: function(el) {
            $(el).remove();
            $('.tip-black').remove();
            $("#icaId").val('');
            $("#displayIcaId").text('');
        },
		
		cancel: function() {
			if ($("#pageMode").val() === 'new') {
				CIPADMIN.clearElementsOf('#metadataForm');
//				CIPADMIN.goToHomePage();
				CIPADMIN.goBack();
			} else if ($("#refererPage").val() === 'create') {
				$._navigateTo({url: "/metadata/create.do"});
			} else {
				$._navigateTo({url: "/metadata/search.do?fromView=true&" + $("#metadataSearchForm").serialize()});
			}
		},
		
		clear: function() {
			location.reload();
		}
};