var p = {
    __init: function() {
        return {}
    },
    
    initDisplay: function() {
    	$._L('PAGE.initDisplay');
    	
    },
    
    bindEvents: function() {
    	$._L('PAGE.bindEvents');
    	
    }
};




// OR USE ALTERNATE REVEALING MODULE PATTERN DECLARATION TO MAKE PRIVATE ACCESS TO YOUR INNER FUNCTIONS
/*
var p = function(){

    function __init() {
        return {}
    }

    function initDisplay() {
		 $._L('PAGE.initDisplay');

    }

    function bindEvents() {
		 $._L('PAGE.bindEvents');
    
    }


	//only function handlers declared here will be accessible from outside
	return {
       initDisplay: initDisplay,
	   bindEvents: bindEvents
    };
}();
*/