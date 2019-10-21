var p = {
	__init : function() {
		return {
			pageFormNameOverride : 'loginForm',
			pageActionUrlOverride : '/login.do'
		};
	},

	initDisplay : function() {
		$._L("LOGIN.page.initDisplay");
		$('.top-nav').hide();

	
		if( !$.trim($('#businessDomainDiv').html()).length ) {
			$._EF(function () {
				$('#username').focus();
	        }, true);
		} else {
			$._EF(function () {
				$('#login_businessDomainId').focus();
	        }, true);
		}
		
		
		$('#loginDiv').show();
	},

	bindEvents : function() {
		$._log("LOGIN.page.bindEvents");
/*
		$._AE('click', '#action_login', function() {
			$._L("LOGIN.page: LOGIN button clicked");
			p.verifyAccessRights();
		});
		
		$._AEE('#innerFragment input', function() {
			$._L("LOGIN.page: ENTER key pressed on username or password");
			p.verifyAccessRights();
		});
		
		$._AE('click', '#action_access', function() {
			$._L("LOGIN.page: ACCESS button clicked");
			p.accessByDomain();
		});
		
		$._AEE('#businessDomainDiv select', function() {
			$._L("LOGIN.page: ENTER key pressed on business domain");
			p.accessByDomain();
		});
		*/

	},

	// function definitions
	verifyAccessRights : function() { //TODO: needs the UI to be blocked
		$._L("LOGIN.verifyAccessRights...");

		var bValid = true;

		bValid = $._isFieldValid($('#username'), true) && bValid;
		bValid = $._isFieldValid($('#password'), true) && bValid;

		if (bValid === false) {
			jQuery.noticeAdd({
				text : $._getData('common.missing.mandatory.fields'),
				type : 'error'
			});
			return;
		}

		$._ajaxJsonGet({
			pageForm : 'loginForm',
			pageUrl : $._getContextPath() + "/login/authorise.do",
			fnPostCall : function() {
				var messages = $._ajaxJsonArray;
				if (messages.length > 0) {
					var message = '';
					$.each(messages, function(index, value) {
						message = message + value + "<br>";
					});
					CIPADMIN.showBusinessError(message);
				} else {
					$._ajaxRefresh({
						pageUrl : 'login/user/businessDomains.do',
						callType : 'GET',
						id : "businessDomainDiv"
					});
				}
			}
		});
	},

	accessByDomain : function() {
		$._ajaxJsonGet({
			pageForm : 'loginForm',
			pageUrl : $._getContextPath() + '/login/authorise/domain/' + $("#login_businessDomainId").val() + '.do' ,
			fnPostCall : function() {
				var messages = $._ajaxJsonArray;
				if (messages.length == 1 
						&& messages[0].indexOf('success') == 0) {
					$._log('Login successful, loading home page... ');
					window.location = $._getContextPath() + "/home.do";
				} else {
					var message = '';
					$.each(messages, function(index, value) {
						message = message + value + "<br>";
					});
					CIPADMIN.showBusinessError(message);
				} 
			}
		});
	}
};
