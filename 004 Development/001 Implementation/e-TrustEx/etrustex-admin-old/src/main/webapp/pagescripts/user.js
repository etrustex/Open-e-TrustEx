var p = {
		__init: function() {
			return {
		        pageFormNameOverride:'userForm',
		        pageActionUrlOverride:$('#userForm').attr('action'),
		        appComponents:{
		        	partySearch: true
	            }
			};
		},
		
		initDisplay: function(){
			var pageMode = $("#pageMode").val();
			$._L("USER." + pageMode + "page.initDisplay");
			
			var validationMsg = $("#validationMsg").val();
			if (validationMsg) {
				CIPADMIN.showBusinessError(validationMsg);
			}
			
			$("#passwordFlagDiv").addClass('hidden');
			$("#user_password_div").addClass('hidden');
			
	    	if($('#changePasswordFlag').is(':checked')){
	    		$("#user_password_div").removeClass('hidden');
	    	} 
	    	
			if (pageMode === 'new'){
				$("#user_configuration_action_clear").removeClass('hidden');
				$("#user_configuration_action_cancel").removeClass('hidden');
				
				$("#user_password_div").removeClass('hidden');
			}
			

			if (pageMode === 'edit'){
				$("#user_action_changePassword").removeClass('hidden');
				$("#pwdReccommendations").addClass('hidden');
			}

		},
		
		bindEvents: function(){
			$._L("USER." + $("#pageMode").val() + "page.bindEvents");
			
			$._ADE('click', '#action_search_party', function () {
				partySearch.openDialog({
                        parentForm: "userForm",
                        controllerPath: "user",
                        businessDomainId: $('#businessDomain').val()
                });
		    });
			
			$._AE('click', "#user_action_changePassword", function(){
				$._L("USER page: Change password button clicked");
				$("#user_password_div").removeClass('hidden');
				$("#pwdReccommendations").removeClass('hidden');
				$("#changePasswordFlag").attr("checked","checked");
				$("#user_action_changePassword").addClass('hidden');
			});
			
			$._AE('click', '#action_remove_party', function(){
				$._L("USER page: REMOVE party button clicked");
				partySearch.clearPartyDiv(this);
			});
			
			$._AE('click', '#user_action_add_configuration',  function () {
				$._L("USER page: ADD CONFIGURATION button clicked");
				p.addUserConfiguration();
			});
			
			$._AE('click', '#action_save',  function () {
				$._L("USER page: CONFIGURE button clicked");
				p.saveUser();
			});
			
			$._AE('click', '#action_delete', function () {
				$._L("USER page: DELETE button clicked");
				p.deleteUserConfirmation();
			});
			
			$._AE('click', '#action_edit', function () {
				$._L("USER page: EDIT button clicked");
				p.editUser();
			});
			
			$._AE('click','.editUserAccessRightsRow', function(){
				$._L("USER.page: EDIT user access rights button clicked" );

				$("#user_configuration_action_clear").addClass('hidden');
				$("#user_configuration_action_cancel").removeClass('hidden');
				
				p.executeUserConfigurationAction($(this).closest('tr'), "edit");
			});
			
			$._AE('click','.deleteUserAccessRightsRow', function(){
				$._L("USER.page: DELETE user access rights button clicked" );
				p.executeUserConfigurationAction($(this).closest('tr'), "delete");
			});
			
			$._AE('click', '#action_cancel', function(){
				$._L('USER: CANCEL button clicked');
				p.cancel();
			});
			
			$._AE('click', '#action_clear', function() {
				$._L("DOCUMENT: CLEAR button clicked");
				p.clear();
			});
			
			$._AE('click', '#action_reset', function(){
				$._L('USER: RESET button clicked');
				location.reload();
			});

			$._AE('click', '#action_change_pwd', function(){
                p.changePwd();
            });
			
			$._AE('click', '#user_configuration_action_clear', function(){
				$._L('USER: CLEAR(configuration) button clicked');
				p.clearCurrentConfiguration();
			});

			$._AE('click', '#user_configuration_action_cancel', function(){
				$._L('USER: CANCEL(configuration) button clicked');
				p.refreshConfigurations();
			});
			
		},
		
		//only for new configurations (the id of a configuration which is modified is not cleared)
		clearCurrentConfiguration: function(){ 
			$("#userRoleId").val(-1);
			$("#businessDomain").val(-1);

			partySearch.clearPartyDiv($('#action_remove_party'));
		},
		
		/*-------------------------------------------------------------------------*/
		/*----------------------- User access rights operations -------------------*/
		/*-------------------------------------------------------------------------*/
		addUserConfiguration: function(){
			$._L("USER." + $("#pageMode").val() + "page.addUserConfiguration");
			
			$._ajaxRefresh({
		  		pageUrl: $._getContextPath() + "/user/addUserConfiguration.do",
		  		pageForm: "userForm",
		  		id: "configurationDiv"
			});
			
		},
		
		/*
		 * Triggered when a user cancels a configuration edit (middle section of the screen).
		 * Initialises current configuration sections, and puts back in the list the original record. 
		 */
		refreshConfigurations: function(){
			$._L("USER." + $("#pageMode").val() + "page.refreshConfigurations ");
			
			$._ajaxRefresh({
				callType: 'POST',
				pageUrl : $._getContextPath() + "/user/refreshConfigurations.do",
				pageForm : "userForm",
				id : "configurationDiv"
			});
		},
		
		executeUserConfigurationAction: function($this_row, actionName){
			$._L("USER." + $("#pageMode").val() + "page.executeUserConfigurationAction " + actionName);
			
			if (actionName == "delete"){
			    var roleTD = $('td:eq(0)', $this_row);
            	var businessDomainTD = $('td:eq(1)', $this_row);
            	var partyTD = $('td:eq(2)', $this_row);
				var role = roleTD.text();
				var businessDomain = businessDomainTD.text();
				var party = partyTD.text();
				
				$._msgbox({
					dialogId:'confirmMsg',
					text: $._getData('user.rights.remove.confirmation.message') + " (" + role + ", " + businessDomain + ", " + party + ") for user " + $('#username').val() +"?",
					title: $._getData('user.rights.remove.confirmation.title'),
					msgboxType:'yes_no',
					alertType:'warning',
					fnCallback:function (r) {
						if (r === true) {
							$._ajaxRefresh({
								callType: 'POST',
								pageUrl : $._getContextPath() + "/user/removeUserConfiguration.do?userAccessRightsIdx=" + $this_row.data( "index" ),
								pageForm : "userForm",
								id : "configurationDiv"
							});
						}
					}
				});
			} else {
				$._ajaxRefresh({
					callType: 'POST',
					pageUrl : $._getContextPath() + "/user/editUserConfiguration.do?userAccessRightsIdx=" + $this_row.data( "index" ),
					pageForm : "userForm",
					id : "configurationDiv"
				});
			}
		},
		
		/*-------------------------------------------------------------------------*/
		/*------------------------------ User operations --------------------------*/
		/*-------------------------------------------------------------------------*/
		saveUser: function(){
			$._L("USER." + $("#pageMode").val() + "page.saveUser");
			
			if (p.isDataValid() === false) {
	            jQuery.noticeAdd({text: $._getData('common.missing.mandatory.fields'), type: 'error'});
	            return;
			}
			
			$._ajaxCall({
				pageUrl: $._getContextPath() + "/user/save.do",
				pageForm: "userForm",
				fnPostCall: function () {
					var ajaxResult = CIPADMIN.ajaxResult();
					if(ajaxResult.success) {
						CIPADMIN.showSuccessMessage(ajaxResult.message, null, null, "/user/" + ajaxResult.id + "/view.do");
					} else {
						CIPADMIN.showBusinessError(ajaxResult.message);
					}
				}
			});
		},

		changePwd: function(){
		    if (p.isDataValid() === false) {
                jQuery.noticeAdd({text: $._getData('common.missing.mandatory.fields'), type: 'error'});
                return;
            }

            $._ajaxCall({
                pageUrl: $._getContextPath() + "/user/changePwd.do",
                pageForm: "userForm",
                fnPostCall: function () {
                    var ajaxResult = CIPADMIN.ajaxResult();
                    if(ajaxResult.success) {
                        CIPADMIN.showSuccessMessage(ajaxResult.message, null, null, "/user/" + ajaxResult.id + "/view.do");
                    } else {
                        CIPADMIN.showBusinessError(ajaxResult.message);
                    }
                }
            });
        },
		
		editUser: function(){
			$._L("USER." + $("#pageMode").val() + "page.editUser");
			window.location = $._getContextPath() + "/user/" + $("#userId").val() + "/edit.do?"+ $("#userSearchForm").serialize();
		},
		
		
		deleteUserConfirmation: function(){
			$._L("USER." + $("#pageMode").val() + "page.deleteUserConfirmation");
			
			$._msgbox({
				dialogId:'confirmMsg',
                text:$('#msgDeleteConfirmation').text(),
                title:$('#msgDeleteConfirmationTitle').text(),
                msgboxType:'yes_no',
                alertType:'warning',
                fnCallback:function (r) {
                    if (r === true) {
                    	p.deleteUser();
                    }
                }
            });
		},
		
		deleteUser: function(){
			$._L("USER." + $("#pageMode").val() + "page.deleteUser");
			
			$._ajaxCall({
		  		pageUrl: $._getContextPath() + "/user/" + $("#userId").val() + "/delete.do?fromView=true",
		  		pageForm: 'userForm',
		  		fnPostCall: function (r) {
		  			var ajaxResult = CIPADMIN.ajaxResult();
		  			var nextUrl;
		  			if ($("#refererPage").val() === 'create') {
		  				nextUrl = "/user/create.do";
					} else {
						nextUrl = "/user/search.do?fromView=true&" + $("#userSearchForm").serialize();
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
			var pageMode = $("#pageMode").val();
			var bValid = true;

			if(pageMode == "new") {
				bValid = $._isFieldValid($('#username'), true) && bValid;
			}
			
			if(pageMode == "changePwd") {
				bValid = $._isFieldValid($('#oldPassword'), true) && bValid;
				bValid = $._isFieldValid($('#userPassword1'), true) && bValid;
				bValid = $._isFieldValid($('#userPassword2'), true) && bValid;
			}
			
//			if($.trim($('#userPassword1').val()).length !== 0 || $.trim($('#userPassword2').val()).length !== 0) {
//				bValid = $._isFieldValid(null, true, null, $('#userPassword1'), $('#userPassword2')) && bValid;
//			}
	        
	        return bValid;
	    },
	    
		cancel: function() {
	    	var pageMode = $("#pageMode").val(); 
			if (pageMode === 'new' || pageMode === 'changePwd') {
				CIPADMIN.clearElementsOf('#userForm');
				CIPADMIN.goBack();
			} else if ($("#refererPage").val() === 'create') {
				$._navigateTo({url: "/user/create.do"});
			} else {
				$._navigateTo({url: "/user/search.do?fromView=true&" + $("#userSearchForm").serialize()});
			}
		},
		
		clear: function() {
			location.reload();
		}
};