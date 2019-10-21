var p = {
		__init: function() {
			return {
		        pageFormNameOverride:'messageSearchForm',
		        pageActionUrlOverride:'/message/search.do',
		        appComponents:{
		        	transactionSearch: true,
		        	icaSearch: true,
		        	partySearch: true
	            }
			};
		},

		initDisplay: function() {
			$._L("MESSAGE.search.page.initDisplay");
			 // Run search if coming back from view.
			if($("#searchOnLoad") && $("#searchOnLoad").val() == "true") {
				p.search();
			}
		},
		
		bindEvents: function(){
			$._L("MESSAGE.search.page.bindEvents");
			 //bind the components events
		    //--------------------------
			$._ADE('click', '.action_search_party', function () {
			    var divId = $(this).attr('id');

				partySearch.openDialog({
                        parentForm: "messageSearchForm",
                        controllerPath: "message",
                        partyType: divId.substring(divId.lastIndexOf("_") + 1),
                        businessDomainId: $('#businessDomain').val()
                });
		    });
			
			$._ADE('click', '#action_search_ica', function () {
				icaSearch.openDialog({
                        parentForm: "messageSearchForm",
                        controllerPath: "message"
                });
		    });
			
			$._ADE('click', '#action_search_transaction', function () {
				transactionSearch.openDialog({
                          parentForm: "messageSearchForm",
                          controllerPath: "message"
                });
		    });
			

			$._AE('click', '.action_remove_party', function(){
				p.clearParty(this);
			});
			
			$._AE('click', '#action_remove_ica', function(){
				p.clearIca(this);
			});
			
			$._AE('click', '#action_remove_transaction', function(){
				transactionSearch.clearTransactionDiv(this);
			});
			
			
		    $._ADE('click', '#action_search', function () {
		    	$._L('MESSAGE_SEARCH: SEARCH button clicked');
		    	p.search();
		    });
		    
		    $._ADE('click', '#action_clear', function () {
		    	$._L('MESSAGE_SEARCH: CLEAR button clicked');
		    	p.clear();
		    });
		    
		    $._ADE('click', '#action_cancel', function () {
		    	$._L('MESSAGE_SEARCH: CANCEL button clicked');
		    	p.cancel();
		    });
		    
		    $._ADE('click','.select-message', function(){
		    	$._L('MESSAGE_SEARCH: message clicked');
	    		var messageId = $(this).closest('tr').attr('id');
		    	var messageSearchForm = $("#messageSearchForm").serialize();
		    	window.location = $._getContextPath() + "/message/" + messageId + "/view.do?" + messageSearchForm;
			});
		    
		    //bind the search on enter event
		    $._ADEE('.action_search_on_enter', function () {
		    	$._L('MESSAGE_SEARCH: Enter key pressed');
		    	p.search();
		    });
		},
		
		// function definitions
		search: function (page, size) {
            $._L('MESSAGE_SEARCH.search');

            if(page === undefined) {
                page = 1;
            }

            if(size === undefined) {
                size = 50;
            }

            $._blockUIMessageInfo = $._getData('common.searching.results');
            $._ajaxRefresh({
                pageUrl: $._getContextPath() + "/message/search/results.do?page=" + page + "&size=" + size,
                id: "innerFragment",
                pageForm: "messageSearchForm"
            });
        },
        
        clearParty: function(el) {
            var el = $(el);
            el.parent().find('input').val('');
            el.parent().find('span.displayName').text('');
        	el.remove();
        	$('.tip-black').remove();
		},

		clearIca: function(el) {
			$(el).remove();
			$('.tip-black').remove();
			$("#icaId").val('');
			$("#displayIcaId").val('');
		},

        clear: function(){
        	location.reload();
        },
            
	    close: function () {
            $._L('MESSAGE_SEARCH close dialog');
        }

};