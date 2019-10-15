var p = {
		
		__init: function() {
			return {
		        pageFormNameOverride:'icaMultipleCreationForm',
		        pageActionUrlOverride:'/ica/multi/create.do',
                appComponents:{
                    partySearch: true,
                    roles: true
                }
			};
		},		

		initDisplay: function(){
            $("#secondPartiesDialog").dialog({ autoOpen: false });
            // show "select parties" button
            if( $('#secondPartiesTable > tbody > tr').length) {
                $('#action_select_parties').show();
            } else {
                $('#action_select_parties').hide();
            }
		},

		bindEvents: function(){
			$._L("ICA.multipleCreation.page.bindEvents");

			$._ADE('click', '#action_new_party', function () {
				$._L("ICA: NEW PARTY button clicked");
			    partySearch.create('ica/multi');
		    });

			$._AE('click','#action_save', function(){
				$._L("ICA.multipleCreation: SAVE button clicked");
				p.saveIcasConfirm();
			});
			
			$._AE('click', '#action_cancel', function(){
				$._L('ICA: CANCEL button clicked');
				CIPADMIN.goToHomePage();
			});
			
			$._AE('click','#action_clear', function(){
				$._L("ICA.multipleCreation: CLEAR button clicked");
				location.reload();
			});

			$._AE('change','#firstPartyRoleId', function(){
				$._L("ICA.multipleCreation: first party role changed");
				p.refreshSecondParties();
			});

			$._AE('change','#secondMultiPartyRoleId', function(){
				$._L("ICA.multipleCreation: second party role changed");
				p.refreshSecondParties();
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

            $._AE('click', '.action_remove_party_from_ica', function(){
                p.removeSelectedParty($(this));
            });

			/***** Second parties dialog events ***********/
			$._AE('click','#action_select_parties', function(){
                p.openSelectPartiesDialog();
            });

            $._ADE('click','#second_parties_dialog_cancel', function(){
                $('#secondPartiesDialog').dialog('close');
            });

            $._ADE('click','#second_parties_dialog_ok', function(){
                p.selectParties();
            });

			$._ADE('change','.checkAllFlag', function(){
				p.checkAll(this);
			});


		},


		checkForPartiesInError: function(fn) {
		    var icasInError = $('#selectedSecondParties > tbody > tr.icaInError').length;
		    if (icasInError) {
                $._msgbox({
                    text: '' + icasInError + ' ' + $._getData('ica.multiple.error.leave.warning.text'),
                    msgboxType:'yes_no',
                    alertType:'warning',
                    fnCallback:function (r) {
                        if (r === true) {
                            // remove in error, gray out green records and update table caption
                            $('#selectedSecondParties > tbody > tr.icaInError').remove();
                            $('#selectedSecondParties > tbody > tr.icaCreated').removeClass('icaCreated').find('span.text-color-green').removeClass('text-color-green').addClass('disabled');
                            $('#secondParties > div.content > div.scrollable-table-y > p.icas-created-info').remove();

                            //execute function
                            $._execFn(fn, true);
                        }
                    }
                });
            } else {
                $._execFn(fn, true);
            }
		},

		saveIcasConfirm: function() {
		    p.checkForPartiesInError(p.saveIcas);
		},

		// function definitions
		partyDelegates: function(partyId, partyType) {
            $._ajaxRefresh({
                pageUrl: $._getContextPath() + '/ica/partyDelegates/' + partyId + '?partyType=' + partyType,
                callType: 'GET',
                id: partyType + 'PartyDelegatesDiv'
            });
        },

		saveIcas: function(){
			if (!p.isDataValid()) {
	            jQuery.noticeAdd({text: $._getData('common.missing.mandatory.fields'), type: 'error'});
	            return;
	        }
			
		  	$._blockUIMessageInfo = $._getData('blockUI.ica.multiple.saving');

            // add selected parties to model attributes creating hidden attributes
            var selectedParties = $('#selectedSecondParties tbody tr');
            selectedParties.each(function(idx, tr){
		  	    $('<input />', {
		  	        type: 'hidden',
		  	        name: 'partyListItems[' + idx + '].id',
		  	        value: $(tr).data('partyId'),
		  	    }).appendTo('#icaForm');
            });

			$._AR({
				callType: 'POST',
				pageForm : "icaForm",
				pageUrl: $._getContextPath() + "/ica/multi/save.do" ,
		  		id: "innerFragment",
		  		fnPreCall: function() {
		  		    $("#secondPartiesDialog").dialog('destroy').remove();
		  		},
		  		fnPostCall: function (r) {
		  		    // remove previously generated hidden inputs for selected parties
		  		    $("[name^=partyListItems]").remove();

		  		    // recreate second parties dialog
		  		    $("#secondPartiesDialog").dialog({
                        autoOpen: false,
                        modal: true,
                        width: 'auto',
                        title: $._getData('ica.multi.select.parties.dialog.title')
                    });

		  			var errorMsg = $("#error_msg").val();
		  			if (errorMsg) {
		  				CIPADMIN.showBusinessError(errorMsg);
		  			}
                }
			});
		  	
		},

		refreshSecondParties: function(){
			var profileId = $("#interchangeAgreement_profileId").val();
			var firstPartyId = $("#firstPartyId").val();
			var firstPartyRoleId = $("#firstPartyRoleId").val();
			var secondPartyRoleId = $("#secondMultiPartyRoleId").val();

			if (profileId && firstPartyId && firstPartyRoleId && firstPartyRoleId != -1 && secondPartyRoleId && secondPartyRoleId != -1) {
                $._blockUIMessageInfo = $._getData('common.reloading.parties');

                $._AR({
                    callType: 'POST',
    //					pageForm : "icaForm",
                    pageUrl: $._getContextPath() + "/ica/multi/secondParties.do?profileId=" + profileId
                                                        + "&firstPartyId=" + firstPartyId
                                                        + "&firstPartyRoleId=" + firstPartyRoleId
                                                        + "&secondPartyRoleId=" + secondPartyRoleId ,
                    id: "secondParties",
                    fnPreCall: function() {
                        $("#secondPartiesDialog").dialog('destroy').remove();
                    },
                    fnPostCall: function () {
                        // create select parties dialog
                        $("#secondPartiesDialog").dialog({
                            autoOpen: false,
                            modal: true,
                            width: 'auto',
                            title: $._getData('ica.multi.select.parties.dialog.title')
                        });

                        // show/hide "select parties" button
                        if( $('#secondPartiesTable > tbody > tr').length) {
                            $('#action_select_parties').show();
                        } else {
                            $('#action_select_parties').hide();
                        }
                    }
                });
			}
		},

		openSelectPartiesDialog: function() {
            p.checkForPartiesInError(function() {$('#secondPartiesDialog').dialog('open');});
		},

        /***** Second parties dialog functions ***********/
		selectParties: function() {
		    var table = $('#secondPartiesTable');
            var thead = table.children('thead').eq(0);
            var tbody = table.children('tbody').eq(0);
		    var selectedRecords = tbody.find('.checkable:visible:checked').closest('tr');

		    /* move selected records to existingSecondParties table */
		    selectedRecords.each( function(i, el){
		        // remove tr from dialog table
		        var tr = $(el).remove().clone();

		        // Cell containing the remove button
                var tdDeleteAction = $('<td></td>', {class: 'action'})
                            .append($('<a/>', {title: $._getData('tooltip.button.delete'), class: 'action_remove_party_from_ica'})
                                        .append($('<span/>',{class: 'icon icon-reject'})));

		        // append td at the end (actions) and remove first td (checkbox)
		        tr.append(tdDeleteAction).find("td:first").remove();

		        $('#selectedSecondParties').prepend(tr);
		    });

		    p.applyZebraStyle();

            // Update table caption
            var newParties = $('.action_remove_party_from_ica').length;
            $('#newPartiesCaption').text('' + newParties + ' ' + $._getData('ica.multiple.new.parties.caption'));
		    $('#secondPartiesDialog').dialog('close');
		},

		removeSelectedParty: function(el) {
		    /* move deleted record to secondParties table */
		    var tr = el.closest('tr').remove().clone();

		    var tdCheckbox = $('<td/>', {align: 'center'});
		    $('<input />', { type: 'checkbox', value: tr.data('partyId'), class: 'checkable' }).appendTo(tdCheckbox);

		    // append td at the beginning (checkbox) and remove last td (actions)
            tr.prepend(tdCheckbox).find("td:last").remove();

            // append table row in the right index
            var rowIndex = tr.data('idx'); // original row index
            var previousRows = $('#secondPartiesTable > tbody > tr').filter(function(){
                return $(this).data('idx') < rowIndex;
            });

            if(previousRows.length) {
                previousRows.last().after(tr);
            } else {
                var nextRows = $('#secondPartiesTable > tbody > tr').filter(function(){
                   return $(this).data('idx') > rowIndex;
                });
                if(nextRows.length) {
                    nextRows.first().before(tr);
                } else {
                    $('#secondPartiesTable').append(tr);
                }
            }

            p.applyZebraStyle();
		},

		applyZebraStyle: function(){
		    $('table.tablesorter-table > tbody > tr').removeClass('zebra1 zebra2 bgcolor-red');
		    $("table.tablesorter-table > tbody > tr:visible").filter( ":even" ).addClass("zebra2");
		},

		checkAll: function(el){
			$._log("ICA.multipleCreation.checkAll...");
			var allChecked = $(el).prop("checked");
			if(allChecked) {
			    $('#secondPartiesTable tbody .checkable:visible').prop("checked", true);
			    $('#secondPartiesTable tbody .checkable:visible').trigger("change");
			} else {
			     $('#secondPartiesTable tbody .checkable:visible').prop("checked", false);
                 $('#secondPartiesTable tbody .checkable:visible').trigger("change");
			}
		},

		isDataValid: function(){
			var bValid = true;

	        bValid = $._isFieldValid($('#interchangeAgreement_profileId'), true) && bValid;
	        bValid = $._isFieldValid($('#firstPartyId'), true) && bValid;
	        bValid = $._isFieldValid($('#firstPartyRoleId'), true) && bValid;
	        bValid = $._isFieldValid($('#secondMultiPartyRoleId'), true) && bValid;

	        return bValid;
	    }
};