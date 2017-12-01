var p = {
		
		__init: function() {
			return {
		        pageFormNameOverride:'icaMultipleCreationForm',
		        pageActionUrlOverride:'/ica//multi/create.do',
                appComponents:{
                    partySearch: true,
                    roles: true
                }
			};
		},		
		/*
		initDisplay: function(){
			$._L("ICA.multipleCreation.page.initDisplay");
			$("#validFrom").datepicker("setDate", new Date());
		},
		*/
		bindEvents: function(){
			$._L("ICA.multipleCreation.page.bindEvents");

			$._ADE('click', '#action_new_party', function () {
				$._L("ICA: NEW PARTY button clicked");
			    partySearch.create('ica/multi');
		    });

			$._AE('click','#action_save', function(){
				$._L("ICA.multipleCreation: SAVE button clicked");
				p.saveIcas();
			});
			
			$._AE('click', '#action_cancel', function(){
				$._L('ICA: CANCEL button clicked');
				CIPADMIN.goToHomePage();
			});
			
			$._AE('click','#action_clear', function(){
				$._L("ICA.multipleCreation: CLEAR button clicked");
				location.reload();
			});
			
//			$._AE('change','#interchangeAgreement_profileId', p.refreshSecondParties);
			$._AE('change','#firstPartyRoleId', function(){
				$._L("ICA.multipleCreation: first party role changed");
				p.refreshSecondParties();
			});

			$._AE('change','#secondMultiPartyRoleId', function(){
				$._L("ICA.multipleCreation: second party role changed");
				p.refreshSecondParties();
			});
			
			$._AE('change','#checkAllFlagId', function(){
				$._L("ICA.multipleCreation: checkAllFlag clicked");
				p.checkAll();
			});
			
			$._AE('change','#interchangeAgreement_profileId', function(){
				$._L('ICA.multipleCreation: Profile changed: ' + $('#interchangeAgreement_profileId').val());
				roles.reloadRoles($('#interchangeAgreement_profileId').val());
				setTimeout(function(){ p.refreshSecondParties(); }, 700);
			});

			$._ADE('click', '.action_search_party', function () {
			    var partyType = $(this).attr('id').split("_")[2];

                partySearch.openDialog({
                        parentForm: "interchangeAgreement",
                        controllerPath: "ica/multi",
                        partyType: partyType,
                        fnPostCallAfterSelect: function() {
                          if(partyType == 'first' || partyType == 'second') {
                            var partyId = partyType == 'first' ? $('#firstPartyId').val() : $('#secondPartyId').val();
                            p.partyDelegates(partyId, partyType);
                          }

                          p.refreshSecondParties();
                        },
                        businessDomainId: $('#businessDomain').val()
                });
            });

            $._AE('click', '.action_remove_party', function(){
                var partyType = $(this).attr('id').split("_")[2];
                partySearch.clearPartyDiv(this, partyType);
                p.refreshSecondParties();
            });
		},
		
		// function definitions
		saveIcas: function(){
			$._L("ICA.multipleCreation.saving...");
			
			if (!p.isDataValid()) {
	            jQuery.noticeAdd({text: $._getData('common.missing.mandatory.fields'), type: 'error'});
	            return;
	        }
			
		  	$._blockUIMessageInfo = $._getData('blockUI.ica.multiple.saving');

            // add selected parties to model attributes creating hidden attributes
            var selectedParties = $('#partyListTableDiv tbody .checkable:checked');
            selectedParties.each(function(idx){
		  	    $('<input />').attr('type', 'hidden')
                      .attr('name', 'partyListItems[' + idx + '].id')
                      .attr('value', $(this).val())
                      .appendTo('#icaForm');
            });

			$._AR({
				callType: 'POST',
				pageForm : "icaForm",
				pageUrl: $._getContextPath() + "/ica/multi/save.do" ,
		  		id: "innerFragment",
		  		fnPostCall: function (r) {
		  		    // remove previously generated hidden inputs for selected parties
		  		    $("[name^=partyListItems]").remove();

		  			var successMsg = $("#success_msg").val();
		  			
		  			if (successMsg.indexOf('success') == 0) {
		  				$._msgbox({
		  					text: successMsg.substr(7),
		  					msgboxType: 'ok',
		  					alertType: 'info'
		  				});
		  			} else if(successMsg.indexOf('error') == 0) {
		  				CIPADMIN.showBusinessError(successMsg.substr(5));
		  				jQuery.noticeAdd({text: $._getData('common.missing.mandatory.fields'), type: 'error'});
		  			} else {
		  				$._msgbox({
		  					text: successMsg.substr(7),
		  					msgboxType: 'ok',
		  					alertType: 'warning'
		  				});
		  			}
                }
			});
		  	
		},
		
		refreshSecondParties: function(){
			$._L("ICA.multipleCreation.refreshSecondParties...");
			
			var profileId = $("#interchangeAgreement_profileId").val();
			var firstPartyId = $("#firstPartyId").val();
			var firstPartyRoleId = $("#firstPartyRoleId").val();
			var secondPartyRoleId = $("#secondMultiPartyRoleId").val();
			
            $._L("ICA.multipleCreation.refreshSecondParties - call ajax refresh");
            $._blockUIMessageInfo = $._getData('common.reloading.parties');

            $._AR({
                callType: 'POST',
//					pageForm : "icaForm",
                pageUrl: $._getContextPath() + "/ica/multi/refreshSecondParties.do?profileId=" + profileId
                                                    + "&firstPartyId=" + firstPartyId
                                                    + "&firstPartyRoleId=" + firstPartyRoleId
                                                    + "&secondPartyRoleId=" + secondPartyRoleId ,
                id: "secondParties"
            });
		},
	
		isDataValid: function(){
			var bValid = true;

	        bValid = $._isFieldValid($('#interchangeAgreement_profileId'), true) && bValid;
	        bValid = $._isFieldValid($('#firstPartyId'), true) && bValid;
	        bValid = $._isFieldValid($('#firstPartyRoleId'), true) && bValid;
	        bValid = $._isFieldValid($('#secondMultiPartyRoleId'), true) && bValid;

	        return bValid;
	    },
	    
//		clearIcas: function(){
//			$._log("ICA.multipleCreation.clearing fields...");
//			$('#icaForm').each (function(){this.reset();});
//			$('#interchangeAgreement_profileId').val(-1);
//			$('#firstPartyRoleId option:not(:first)').remove();
//			$('#secondMultiPartyRoleId option:not(:first)').remove();
//			$('#icaForm input[type="checkbox"]').attr('disabled', false).attr('checked', false);
//		},

		checkAll: function(){
			$._log("ICA.multipleCreation.checkAll...");
			var allChecked = $('#checkAllFlagId').prop("checked");
			if(allChecked) {
			    $('#icaResultsListTable tbody .checkable:visible').prop("checked", true);
			    $('#icaResultsListTable tbody .checkable:visible').trigger("change");
			} else {
			     $('#icaResultsListTable tbody .checkable:visible').prop("checked", false);
                 $('#icaResultsListTable tbody .checkable:visible').trigger("change");
			}
		}
		

};