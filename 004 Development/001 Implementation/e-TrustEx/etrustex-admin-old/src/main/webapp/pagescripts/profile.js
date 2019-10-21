var p = {
		__init: function() {
			return {
		        pageFormNameOverride:'profileForm',
		        pageActionUrlOverride:$('#profileForm').attr('action')
			};
		},
		
		initDisplay: function(){
			$._L("PROFILE." + $("#pageMode").val() + "page.initDisplay");
			
			if ($("#pageMode").val() === 'edit'){
				$('#name').attr("readonly", true);
			}  else {
				$("#businessDomains").val("-1")
			}
		},
		
		bindEvents: function(){
			$._AE('click', '#action_save', function() {
				$._L("PROFILE: SAVE button clicked");
				p.saveProfile();
			});
			
			$._AEE('.action_save_on_enter_profile', function() {
				$._L("PROFILE: ENTER key pressed");
				p.saveProfile();
			});
			
			$._AE('click', '#action_delete', function() {
				$._L("PROFILE: DELETE button clicked");
				p.deleteProfileConfirmation();
			});
			
			$._AE('click', '#action_edit', function() {
				$._L("PROFILE: EDIT button clicked");
				p.editProfile();
			});
			
			$._AE('click', '#action_cancel', function() {
				$._L("PROFILE: CANCEL button clicked");
				p.cancel();
			});
			
			$._AE('click', '#action_clear', function() {
				$._L("PROFILE: CLEAR button clicked");
				p.clear();
			});
			
			$._AE('click', '#action_reset', function() {
				$._L("PROFILE: CLEAR button clicked");
				location.reload();
			});
			
			var bdNoneSelected;
			
			$._AE('focus', '#businessDomains', function() {
				bdNoneSelected = $('#businessDomains option[value="-1"]').prop('selected');
			});
			
			$._AE('change', '#businessDomains', function() {
				if(bdNoneSelected) {
					$("#businessDomains option:selected").each(function() {
						if(this.value != "-1") {
							$('#businessDomains option[value="-1"]').prop('selected', false);
							bdNoneSelected = false;
							return false;
						}
					});
				} else {
					$("#businessDomains option:selected").each(function() {
						if(this.value == "-1") {
							$("#businessDomains option:selected").prop("selected", false)
							$('#businessDomains option[value="-1"]').prop('selected', true);
							bdNoneSelected = true;
							return false;
						}
					});
				}
			});
		},
		
		// function definitions
		saveProfile: function(){
			$._L("PROFILE.saveProfile...");
			
	        if (p.isDataValid() === false) {
	            jQuery.noticeAdd({text: $._getData('common.missing.mandatory.fields'), type: 'error'});
	            return;
	        } else {
	        	$._ajaxCall({
	        		pageUrl: $._getContextPath() + "/profile/save.do",
	        		pageForm: "profileForm",
	        		fnPostCall: function () {
	        			var ajaxResult = CIPADMIN.ajaxResult();
	        			if(ajaxResult.success) {
	        				CIPADMIN.showSuccessMessage(ajaxResult.message, null, null, "/profile/" + ajaxResult.id + "/view.do");
	        			} else {
	        				CIPADMIN.showBusinessError(ajaxResult.message);
	        			}
	        		}
	        	});
	        }
	  	
		},
		
		editProfile: function() {
			window.location = $._getContextPath() + "/profile/" + $("#profileId").val() + "/edit.do?"+ $("#profileSearchForm").serialize();
		},

		deleteProfileConfirmation: function(){
			$._L("PROFILE.deleteProfileConfirmation");
			
			$._msgbox({
				dialogId:'confirmMsg',
                text:$('#msgDeleteConfirmation').text(),
                title: $._getData('profile.management.confirmation.message.title'),
                msgboxType:'yes_no',
                alertType:'warning',
                fnCallback:function (r) {
                    if (r === true) {
                    	p.deleteProfile();
                    }
                }
            });
		},
		
		deleteProfile: function(){
			$._L("PROFILE.deleteProfile...");
			
			$._ajaxCall({
		  		pageUrl: $._getContextPath() + "/profile/" + $("#profileId").val() + "/delete.do?fromView=true",
		  		pageForm: 'profileForm',
		  		fnPostCall: function () {
		  			var ajaxResult = CIPADMIN.ajaxResult();
		  			var nextUrl;
		  			if ($("#refererPage").val() === 'create') {
		  				nextUrl = "/profile/create.do";
					} else {
						nextUrl = "/profile/search.do?fromView=true&" + $("#profileSearchForm").serialize();
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
	        bValid = $._isFieldValid($('#profileTypeCode'), true) && bValid;
	        
	        return bValid;
	    },
		
		cancel: function() {
			if ($("#pageMode").val() === 'new') {
				CIPADMIN.clearElementsOf('#profileForm');
//				CIPADMIN.goToHomePage();
				CIPADMIN.goBack();
			} else if ($("#refererPage").val() === 'create') {
				$._navigateTo({url: "/profile/create.do"});
			} else {
				$._navigateTo({url: "/profile/search.do?fromView=true&" + $("#profileSearchForm").serialize()});
			}
		},
		
		clear: function() {
			CIPADMIN.clearElementsOf('#profileForm');
		}
};