var p = {
		__init: function() {
			return {
		        pageFormNameOverride:'logsSearchForm',
		        pageActionUrlOverride: $('#logsSearchForm').attr('action')
			};
		},
		initDisplay: function() {
			$._L("LOGS.search.page.initDisplay");

            var dpFrom = $('#creationDateFrom').parent();
            var dpTo = $('#creationDateTo').parent();
            /*
                Set hours, mins and seconds to default values when the picker is opened and input is empty
            */
            dpFrom.on("dp.show", function(e) {
                if(!$('#creationDateFrom').val()) {
                    $(this).data("DateTimePicker").date(moment(new Date()).hours(0).minutes(0).seconds(0).milliseconds(0));
                }
            });
            dpTo.on("dp.show", function(e) {
                if(!$('#creationDateTo').val()) {
                    $(this).data("DateTimePicker").date(moment(new Date()).hours(23).minutes(59).seconds(59).milliseconds(999));
                }
            });

            /*
                Set hours, mins and seconds to default values when another day is selected
            */
            dpFrom.on("dp.change", function (e) {
                var date =  moment(e.date, "DD/MM/YYYY");
                var oldDate =  moment(e.oldDate, "DD/MM/YYYY");
                if(date.date() != oldDate.date()) {
                    $(this).data("DateTimePicker").date(moment(date).hours(0).minutes(0).seconds(0).milliseconds(0));
                }
            });
            dpTo.on("dp.change", function (e) {
                var date =  moment(e.date, "DD/MM/YYYY");
                var oldDate =  moment(e.oldDate, "DD/MM/YYYY");
                if(date.date() != oldDate.date()) {
                    $(this).data("DateTimePicker").date(moment(date).hours(23).minutes(59).seconds(59).milliseconds(999));
                }
            });



		},
		
		bindEvents: function(){
			$._L("LOGS.search.page.bindEvents");
			 //bind the components events
		    //--------------------------

		    $._ADE('click', '#action_search', function () {
		    	$._L('LOGS_SEARCH: SEARCH button clicked');
		    	p.search();
		    });
		    
		    $._ADE('click', '#action_clear', function () {
		    	$._L('LOGS_SEARCH: CLEAR button clicked');
		    	p.clear();
		    });
		    
		    $._ADE('click', '#action_cancel', function () {
		    	$._L('LOGS_SEARCH: CANCEL button clicked');
		    	p.close();
		    });
		    
		    $._ADE('click','.select-log', function(){
		    	$._L('LOGS_SEARCH: log clicked');
		    	var logId = $(this).closest('tr').attr('id');
		    	p.openDialog(logId);
			});
		    
		    //bind the search on enter event
		    $._ADEE('.action_search_on_enter', function () {
		    	$._L('LOGS_SEARCH: Enter key pressed');
		    	p.search();
		    });
		},
		
		// function definitions
		search: function (page, size) {
            $._L('LOGS_SEARCH.search');
            
            if(page === undefined) {
            	page = 1;
            }
            
            if(size === undefined) {
            	size = 50;
            }
            
            /*var bValid = false;
	        if( $._isFieldValid($('#name_search'), true)
            		|| $._isFieldValid($('#localName_search'), true)
            		|| $._isFieldValid($('#typeCode_search'), true) ) {
            	bValid = true;
            }
	        
	        if (bValid === false) {
	            jQuery.noticeAdd({text: $._getData('error.search.criteria.needed'), type: 'error'});
	            return;
	        } else {*/
	        	$._blockUIMessageInfo = $._getData('common.searching.results');
	        	$._ajaxRefresh({
	        		pageUrl: $._getContextPath() + "/logs/search/results.do?page=" + page + "&size=" + size,
	        		id: "searchResultsDiv",
	        		pageForm: "logsSearchForm"
	        	});
	        /*}*/
        },
        
        openDialog: function(logId) {
	    	$._L('LOGS.openDialog...');
	    	
	    	$._OAD({
                dialogId: 'logDetailsDialog',
                dialogTitle: $._getData('common.popup.title.log'),
                refreshedFragmentPageUrl: $._getContextPath() + "/logs/" + logId + "/view/load.do?module=" + $("#module_search").val(),
//                isOneButtonOnly: true,
                isShowCloseButton: true,
                dialogWidth: 900
            });	   
    	},
            
        clear: function(){
        	$._L('LOGS_SEARCH.clear');
        	CIPADMIN.clearElementsOf('#searchCriteriaDiv');
        	$('#logType_search').val('');
        	$('#operation_search').val('');
        	if($('#entity_search').length) {
        		$('#entity_search').val('');
        	}
        },
            
	    close: function () {
            $._L('LOGS_SEARCH close dialog');
            $._CD({dialogId: 'logDetailsDialog'});
        }

};