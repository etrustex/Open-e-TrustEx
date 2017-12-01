var p = {
		__init: function() {
			return {
		        pageFormNameOverride:'endpointForm',
		        pageActionUrlOverride:$('#endpointForm').attr('action'),
		        appComponents:{
		        	transactionSearch: true,
		        	icaSearch: true,
		        	partySearch: true
	            }
			};
		},
		
		initDisplay: function(){
			$._L("endpoint." + $("#pageMode").val() + "page.initDisplay");
			
			if ($("#pageMode").val() === 'edit'){
				$('#name').attr("readonly", true);
			}
		},
		
		bindEvents: function() {
			$._ADE('click', '#action_search_party', function () {
				partySearch.openDialog({
                        parentForm: "endpointForm",
                        controllerPath: "endpoint",
                        businessDomainId: $('#businessDomain').val() && $('#businessDomain').val() != -1 ? $('#businessDomain').val() : $('#userBusinessDomainHeader').val()
                });
		    });
			
			$._ADE('click', '#action_search_ica', function () {
				icaSearch.openDialog({
                        parentForm: "endpointForm",
                        controllerPath: "endpoint",
                        businessDomainId: $('#businessDomain').val() && $('#businessDomain').val() != -1 ? $('#businessDomain').val() : $('#userBusinessDomainHeader').val()
				});
		    });
			
			$._ADE('click', '#action_search_transaction', function () {
				transactionSearch.openDialog({
                          parentForm: "endpointForm",
                          controllerPath: "endpoint",
                          businessDomainId: $('#businessDomain').val() && $('#businessDomain').val() != -1 ? $('#businessDomain').val() : $('#userBusinessDomainHeader').val()
                });
		    });
			
			// Credentials
			$._ADE('click', '#action_open_credentials', function () {
				p.openCredentialsDialog(false);
		    });

			$._ADE('click', '#action_open_proxyCredentials', function () {
				p.openCredentialsDialog(true);
		    });

            $._ADE('click', '#action_open_credentials_search', function () {
                p.openCredentialsDialog(false, true);
            });

            $._ADE('click', '#action_open_proxyCredentials_search', function () {
                p.openCredentialsDialog(true, true);
            });

		    $._ADE('click', '#action_reset_credentials', function () {
                p.resetCredentials(false);
                CIPADMIN.clearElementsOf('#authCredentialsDiv');
			});

			$._ADE('click', '#action_reset_proxyCredentials', function () {
				p.resetCredentials(true);
				CIPADMIN.clearElementsOf('#proxyCredentialsDiv');
			});

			$._ADE('click', '#action_credentials_save', function () {
		    	p.saveCredentials();
		    });

		    $._ADE('click', '#action_credentials_search', function () {
                p.searchCredentials();
            });

            //bind the search on enter event
            $._ADEE('.action_search_on_enter_credentials', function () {
                p.searchCredentials();
            });

            $._ADE('click','.select-credentials', function(){
                p.selectCredentials($(this).closest('tr').attr('id'));
            });
			
			$._ADE('click', '#action_credentials_cancel', function () {
				CIPADMIN.clearElementsOf('#popupCredentialsDialog');
				$._CD({dialogId: 'popupCredentialsDialog'});
		    });
			
			
			$._AE('click', '#action_remove_party', function(){
				partySearch.clearPartyDiv(this);
			});
			
			$._AE('click', '#action_remove_ica', function(){
				p.clearIca(this);
			});
			
			$._AE('click', '#action_remove_transaction', function(){
				transactionSearch.clearTransactionDiv(this);
			});
			
			$._AE('click', '#action_save', function() {
				$._L("endpoint: SAVE button clicked");
				p.saveEndpoint();
			});
			
			$._AE('click', '#action_delete', function() {
				$._L("endpoint: DELETE button clicked");
				p.deleteEndpointConfirmation();
			});
			
			$._AE('click', '#action_edit', function() {
				$._L("endpoint: EDIT button clicked");
				p.editEndpoint();
			});
			
			$._AE('click', '#action_cancel', function() {
				$._L("endpoint: CANCEL button clicked");
				p.cancel();
			});
			
			$._AE('click', '#action_clear', function() {
				$._L("endpoint: CLEAR button clicked");
				p.clear();
			});
			
			$._AE('click', '#action_reset', function() {
				$._L("endpoint: CLEAR button clicked");
				location.reload();
			});
			
			$._AE('change', "#businessDomain", function() {
				$._L("CIPADMIN - business domain changed");
				p.loadProfiles();
				partySearch.clearPartyDiv($('#action_remove_party'));
				transactionSearch.clearTransactionDiv($('#action_remove_transaction'));
                p.clearIca($('#action_remove_ica'));
			});
			
			// Port field numbers only
			$._AE('keyup', "#proxyPort", function() {
				this.value = this.value.replace(/[^0-9\.]/g,'');
			});
		},
		
		// function definitions
		saveEndpoint: function(){
			$._L("endpoint.saveEndpoint...");
			
	        if (p.isDataValid() === false) {
	            jQuery.noticeAdd({text: $._getData('common.missing.mandatory.fields'), type: 'error'});
	            return;
	        } else {
	        	$._ajaxCall({
	        		pageUrl: $._getContextPath() + "/endpoint/" + $("#configurationType").val() + "/save.do",
	        		pageForm: "endpointForm",
	        		fnPostCall: function () {
	        			var ajaxResult = CIPADMIN.ajaxResult();
	        			if(ajaxResult.success) {
	        				CIPADMIN.showSuccessMessage(ajaxResult.message, null, null, "/endpoint/" + ajaxResult.id + "/view.do");
	        			} else {
	        				CIPADMIN.showBusinessError(ajaxResult.message);
	        			}
	        		}
	        	});
	        }
	  	
		},
		
		editEndpoint: function() {
			window.location = $._getContextPath() + "/endpoint/" + $("#endpointId").val() + "/edit.do?"+ $("#endpointSearchForm").serialize();
		},

		deleteEndpointConfirmation: function(){
			$._L("endpoint.deleteEndpointConfirmation");
			
			$._msgbox({
				dialogId:'confirmMsg',
                text:$('#msgDeleteConfirmation').text(),
                title: $._getData('endpoint.management.confirmation.message.title'),
                msgboxType:'yes_no',
                alertType:'warning',
                fnCallback:function (r) {
                    if (r === true) {
                    	p.deleteEndpoint();
                    }
                }
            });
		},
		
		deleteEndpoint: function(){
			$._L("endpoint.deleteCertificate...");
			
			$._ajaxCall({
		  		pageUrl: $._getContextPath() + "/endpoint/" + $("#endpointId").val() + "/delete.do?fromView=true",
		  		pageForm: 'endpointForm',
		  		fnPostCall: function () {
		  			var ajaxResult = CIPADMIN.ajaxResult();
		  			var nextUrl;
		  			if ($("#refererPage").val() === 'create') {
		  				nextUrl = "/endpoint/" + $("#configurationType").val() + "create.do";
					} else {
						nextUrl = "/endpoint/search.do?fromView=true&" + $("#endpointSearchForm").serialize();
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
//			bValid = $._isFieldValid($('#name'), true) && bValid;
//	        bValid = $._isFieldValid($('#localName'), true) && bValid;
//	        bValid = $._isFieldValid($('#namespace'), true) && bValid;
//	        bValid = $._isFieldValid($('#documentTypeCode'), true) && bValid;
	        
	        return bValid;
	    },
	    
	    // Credentials
	    isProxyCredentials: false,
	    openCredentialsDialog: function(isProxyCredentials, isSearch) {
	        if(isSearch === undefined) {
	            isSearch = false;
	        }
	    	p.isProxyCredentials = isProxyCredentials;
	    	var url = $._getContextPath() + "/endpoint/credentials/dialog.do?isProxyCredentials=" + isProxyCredentials + "&isSearch=" + isSearch;

	    	if(isProxyCredentials && $("#proxyCredentialId").val()) {
	    		url += "&credentialsId=" + $("#proxyCredentialId").val();
	    	} else if(!isProxyCredentials && $("#credentialsId").val()) {
	    		url += "&credentialsId=" + $("#credentialsId").val();
	    	}
	    	
	    	$._OAD({
                dialogId: 'popupCredentialsDialog',
                dialogTitle: isProxyCredentials ? $._getData('common.popup.title.proxyCredentials') : $._getData('common.popup.title.auth.credentials'),
                refreshedFragmentPageUrl: url,
                isOneButtonOnly: true,
                buttonSecondaryFn: function () {
                	$._CD({dialogId: 'popupCredentialsDialog'});
                },
                isShowCloseButton: true
//                dialogWidth: 425
            });
		},

		saveCredentials: function() {
			if (!p.isCredentialsValid()) {
	            return;
	        }
			
			var form = $("#endpointForm").serialize();
			
			$._AR({
				pageForm : 'credentialsForm',
				pageUrl: $._getContextPath() + "/endpoint/credentials/load.do?isProxyCredentials=" + p.isProxyCredentials+ "&" + form,
				callType: 'GET',
				id: p.isProxyCredentials ? "proxyCredentialsDiv" : "authCredentialsDiv",
				fnPreSuccess: function () {
					$._CD({dialogId: 'popupCredentialsDialog'});
				}
			});
		},

		searchCredentials: function () {
            $._blockUIMessageInfo = $._getData('common.searching.results');
            $._ajaxRefresh({
                pageUrl: $._getContextPath() + "/endpoint/credentials/results.do?isProxyCredentials=" + $('#isProxyCredentials').val() + "&t=" + $('#credentialsType').val(),
                id: "searchResultsDiv",
                pageForm: "credentialsForm"
            });
        },

		selectCredentials: function(id) {
			var form = $("#endpointForm").serialize();

			$._AR({
				pageForm : 'credentialsForm',
				pageUrl: $._getContextPath() + "/endpoint/credentials/load.do?isProxyCredentials=" + p.isProxyCredentials + "&credentialsId=" + id + "&" + form,
				callType: 'GET',
				id: p.isProxyCredentials ? "proxyCredentialsDiv" : "authCredentialsDiv",
				fnPreSuccess: function () {
					$._CD({dialogId: 'popupCredentialsDialog'});
				}
			});
		},

		resetCredentials: function(isProxyCredentials) {
			var form = $("#endpointForm").serialize();

			$._AR({
				pageForm : 'credentialsForm',
				pageUrl: $._getContextPath() + "/endpoint/credentials/reset.do?isProxyCredentials=" + isProxyCredentials+ "&" + form,
				callType: 'GET',
				id: isProxyCredentials ? "proxyCredentialsDiv" : "authCredentialsDiv",
				fnPreSuccess: function () {
					$._CD({dialogId: 'popupCredentialsDialog'});
				}
			});
		},
		
		isCredentialsValid: function() {
			var bValid = true;
			bValid = $._isFieldValid($('#credentialsUsername'), true) && bValid;
//	        bValid = $._isFieldValid($('#credentialsPassword'), true) && bValid;
	        bValid = $._isFieldValid(null, true, null, $('#credentialsPassword'), $('#credentialsPassword2')) && bValid;
			return bValid;
		},
	    
		clearIca: function(el) {
			$(el).remove();
			$('.tip-black').remove();
			$("#icaId").val('');
			$("#displayIcaId").text('');
		},

		loadProfiles: function(){
			$._AR({
    			id: "profilesDiv",
    			pageUrl: $._getContextPath() + "/endpoint/profiles.do?bdId=" + $("#businessDomain").val() + "&pageMode=" + $("#pageMode").val()
    		});
		},
		
		cancel: function() {
			if ($("#pageMode").val() === 'new') {
				CIPADMIN.clearElementsOf('#endpointForm');
//				CIPADMIN.goToHomePage();
				CIPADMIN.goBack();
			} else if ($("#refererPage").val() === 'create') {
				$._navigateTo({url: "/endpoint/" + $("#configurationType").val() + "create.do"});
			} else {
				$._navigateTo({url: "/endpoint/search.do?fromView=true&" + $("#endpointSearchForm").serialize()});
			}
		},
		
		clear: function() {
			location.reload();
		}
};