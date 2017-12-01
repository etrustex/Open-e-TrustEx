var p = {
 	__init: function() {
 		return {
 	    	pageFormNameOverride:'roleForm',
 	        pageActionUrlOverride:'/role.do' 			
 		};
	},		

	initDisplay: function(){
		$._L("ROLE.page.initDisplay");
	},
	
	bindEvents: function(){
		$._L("ROLE.page.bindEvents");
		
		$._addEvent('click','#saveRoleBtn', function(){
			$._L("ROLE.page: SAVE button clicked");
			p.saveRole();
		});
		
		/*$._addEvent('click','#clearRoleBtn', function(){
			$._L("ROLE.page: CLEAR button clicked");
			p.clearRole();
		});*/
		$._addEvent('click','#role_action_cancel', function(){
			$._L("ROLE.page: CANCEL button clicked");
			p.cancel();
		});
		
		$._addEvent('click','.editRoleRow', function(){
			$._L("ROLE.page: EDIT button clicked" );
			p.editRole($(this).closest('tr').attr('id'));
		});
		$._addEvent('click','.deleteRoleRow', function(){
			$._L("ROLE.page: DELETE button clicked" );
			p.deleteRoleConfirmation($(this).closest('tr').attr('id'), $('td:first', $(this).parents('tr')).text());
		});
	},
	
	// function definitions
	saveRole: function(){
		$._L("ROLE.page.saveRole...");
	    
		//validation of mandatory fields client side
		var bValid = true;
        
		bValid = $._isFieldValid($('#roleName'), true) && bValid;
        bValid = $._isFieldValid($('#roleCode'), true) && bValid;

        if (bValid === false) {
            jQuery.noticeAdd({text: $._getData('common.missing.mandatory.fields'), type: 'error'});
            return;
        }
  	
        $._ajaxCall({
        	pageUrl: $._getContextPath() + "/role/save.do",
        	pageForm: "roleForm",
        	fnPostCall: function (r) {
        		var ajaxResult = CIPADMIN.ajaxResult();
				if(ajaxResult.success) {
					CIPADMIN.showSuccessMessage(ajaxResult.message, null, null, "/role.do");
					CIPADMIN.clearElementsOf('#roleForm');
				} else {
					CIPADMIN.showBusinessError(ajaxResult.message);
				}        	
			}
		});
	},
	
	/*clearRole: function(){
		$._log("ROLE.page.clearRole...");
		$('#roleCode').val('');
		$('#roleName').val('');
		$('#roleTechnicalFlag').removeAttr('checked');
	},*/
	cancel: function(){
		$._log("ROLE.page.cancel...");
		$._ajaxRefresh({
	  		pageUrl: $._getContextPath() + "/role/cancel.do",
	  		callType: 'GET',
	  		id: "newRoleDiv"
		});
	},
	
	editRole: function(id){
		$._log("ROLE.page.editRole... id addr " + id);
		
		$._ajaxRefresh({
		  		pageUrl: $._getContextPath() + "/role/" + id + "/edit.do",
		  		callType: 'GET',
		  		id: "newRoleDiv",
		  		fnPostCall: function(){
		  			$('#roleCode').attr("readonly", true);
		  		}
		});
	},
	
	deleteRoleConfirmation: function(id, roleCode){
		$._L("ROLE.page.deleteRoleConfirmation");
		
		$._msgbox({
			dialogId:'confirmMsg',
            text:$('#msgDeleteConfirmation').text() + roleCode + " ?",
            title:$('#msgDeleteConfirmationTitle').text(),
            msgboxType:'yes_no',
            alertType:'warning',
            fnCallback:function (r) {
                if (r === true) {
                	p.deleteRole(id);
                }
            },
            isEnterKeyOnPrimaryActionEnabled:true
        });
	},
	
	deleteRole: function(id){
		$._L("ROLE.deleteRole... id addr "+ id);
		
		$._ajaxCall({
	  		pageUrl: $._getContextPath() + "/role/" + id + "/delete.do",
	  		pageForm: 'roleForm',
	  		fnPostCall: function (r) {
	  			var ajaxResult = CIPADMIN.ajaxResult();
	  			if(ajaxResult.success) {
	  				CIPADMIN.showSuccessMessage(ajaxResult.message, null, null, "/role.do");
    			} else {
    				CIPADMIN.showBusinessError(ajaxResult.message);
    			}
            }
		});
	}	
};
