var p = {
		__init: function() {
			return {
		        pageFormNameOverride:'partyAgreementSearchForm',
		        pageActionUrlOverride:'/partyAgreement/search.do',
		        appComponents:{
		        	partySearch: true,
		        	transactionSearch: true
	            }				
			};
		},
		
		initDisplay: function(){
			$._L("PARTY.search.page.initDisplay");

			// Run search if coming back from view.
			if($("#searchOnLoad").val() == "true") {
				p.search();
			}
		},
		
		bindEvents: function(){
			$._L("ENDPOINT.search.page.bindEvents");
			 //bind the components events
		    //--------------------------

			$._ADE('click', '.action_search_party', function () {
                var partyType = $(this).attr('id').split("_")[2];
                partySearch.openDialog({
                    parentForm: "partyAgreementSearchForm",
                    controllerPath: "partyAgreement",
                    partyType: partyType,
                    businessDomainId: $('#businessDomain').val()
                });
            });

            $._ADE('click', '#action_search_tx', function () {
                transactionSearch.openDialog({
                    parentForm: "partyAgreementSearchForm",
                    controllerPath: "partyAgreement",
                    businessDomainId: $('#businessDomain').val()
                });
            });

            $._AE('click', '.action_remove_party', function(){
                var partyType = $(this).attr('id').split("_")[2];
                                partySearch.clearPartyDiv(this, partyType);
                partySearch.clearPartyDiv(this);
            });

            $._AE('click', '#action_remove_tx', function(){
                transactionSearch.clearTransactionDiv(this);
            });

		    $._AE('click', '#action_search', function () {
		    	p.search();
		    });
		    
		    $._AE('click', '#action_clear', function () {
		    	location.reload();
		    });

		    $._AE('click','.select-partyAgreement', function(){
                var partyAgreementId = $(this).closest('tr').attr('id');
                var partyAgreementSearchForm = $("#partyAgreementSearchForm").serialize();
                window.location = $._getContextPath() + "/partyAgreement/" + partyAgreementId + "/view.do?" + partyAgreementSearchForm;
            });
		},

        // function definitions
		search: function () {
            $._blockUIMessageInfo = $._getData('common.searching.results');
            $._ajaxRefresh({
                pageUrl: $._getContextPath() + "/partyAgreement/search/results.do",
                id: "innerFragment",
                pageForm: "partyAgreementSearchForm"
            });
        }
};