var p = {
	__init: function() {
		return {
	        pageFormNameOverride:'homeForm',
	        pageActionUrlOverride:'/home.do'			
		};
	},
	
	initDisplay: function(){
		$._log("HOME.page.initDisplay");
//		$('.user-welcome').show();
		$('.top-nav').show();
	},
	
	bindEvents: function(){
		$._log("Home.page");
	},
};


