var p = {
		
		__init: function() {
			return {
		        pageFormNameOverride:'icaForm',
		        pageActionUrlOverride:'/ica/search.do'				
			};
		},		
		
		initDisplay: function(){
			$._L("ICA.search.page.initDisplay");
			// Run search if coming back from view.
			if($("#searchOnLoad").val() == "true") {
				p.searchInterchangeAgreement();
			}
		},
		
		bindEvents: function(){
			$._L("ICA.search.page.bindEvents");
			
			$._AE('click','#action_search', function(){
				$._L("ICA.search: SEARCH button clicked");
				p.searchInterchangeAgreement();
			});
			
			$._AEE('.action_search_on_enter_ica', function(){
				$._L("ICA.search: ENTER key pressed");
				p.searchInterchangeAgreement();
			});
			
			$._AE('click','#action_clear', function(){
				$._L("ICA.search: CLEAR button clicked");
				p.clearSearchCriteria();
			});
			
			$._AE('click','#icaResultsListTable .clickable', function(){
				$._L("ICA.search: Interchange Agreement clicked in grid");
				p.viewInterchangeAgreement($(this).attr('id'));
			});
			
			$._AE('click', '#action_export', function(){				
				p.exportResults();
			});
		},
		
		// function definitions
		searchInterchangeAgreement: function(){
			$._L("ICA.search.searchInterchangeAgreement...");
		  	$._ajaxRefresh({
		  		pageUrl: "search/results.do",
		  		pageForm: "interchangeAgreementSearchForm",
		  		id: "innerFragment"
			});
		},
		
		exportResults: function(){
			$._L("ICA.search.exportResults...");
			var interchangeAgreementSearchForm = $("#interchangeAgreementSearchForm").serialize();
			window.location = $._getContextPath() + "/ica/search/result/xls?" + interchangeAgreementSearchForm;
		},
		
		clearSearchCriteria: function(){
			$._log("ICA.search.clearSearchCriteria...");
			CIPADMIN.clearElementsOf('#searchCriteriaDiv');
		},
		
		viewInterchangeAgreement: function(id){
			//window.location = $._getContextPath() + "/ica/" + $(this).attr('id') + "/view.do";
			var interchangeAgreementSearchForm = $("#interchangeAgreementSearchForm").serialize();
			window.location = $._getContextPath() + "/ica/" + id + "/view.do?" + interchangeAgreementSearchForm;
		}
};
