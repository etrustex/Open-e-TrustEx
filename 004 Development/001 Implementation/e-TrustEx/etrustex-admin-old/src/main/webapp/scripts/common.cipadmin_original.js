var CIPADMIN = {

	/* =========================================================================================================
	   --                                                  APP INIT                                           --
	   ========================================================================================================= */

    __cmpDefs: {
//    	partyCreation: $._getContextPath() + '/pagescripts/party',

    	
    	certificateCreation: contextPath + '/pagescripts/_common/common_certificate_creation',
	    partySearch: contextPath + '/pagescripts/_common/common_party_search',
	    partyView: contextPath + '/pagescripts/_common/common_party_view',
	    documentSearch: contextPath + '/pagescripts/_common/common_document_search',
	    documentView: contextPath + '/pagescripts/_common/common_document_view',
	    icaSearch: contextPath + '/pagescripts/_common/common_ica_search',
	    icaView: contextPath + '/pagescripts/_common/common_ica_view',
	    transactionSearch: contextPath + '/pagescripts/_common/common_transaction_search',
	    transactionView: contextPath + '/pagescripts/_common/common_transaction_view',
	    roles: contextPath + '/pagescripts/_common/common_roles'
    	
//        certificateCreation: $._getContextPath() + '/pagescripts/_common/common_certificate_creation',
//        partySearch: $._getContextPath() + '/pagescripts/_common/common_party_search',
//        documentSearch: $._getContextPath() + '/pagescripts/_common/common_document_search',
//        icaSearch: $._getContextPath() + '/pagescripts/_common/common_ica_search',
//        transactionSearch: $._getContextPath() + '/pagescripts/_common/common_transaction_search'
    },
		
		
	initDisplay : function() {
		$._L('CIPADMIN.initDisplay');
		$("#homeMenuItem").attr('accesskey', $._getData('menu.home.accessKey'));
		$("#partyMenu").attr('accesskey', $._getData('menu.party.accessKey'));

		moment.locale('en', {
          week: { dow: 1 } // Monday is the first day of the week
        });

		$('.input-group.date').datetimepicker({
          format: 'DD/MM/YYYY HH:mm:ss',
          locale: 'en',
//              sideBySide: true,
          showClear: true,
          showClose: true,
          useCurrent: false,
//          debug: true,
//          showTodayButton: true,
          icons: {
              time: "fa fa-clock-o",
              date: "fa fa-calendar",
              up: "fa fa-arrow-up",
              down: "fa fa-arrow-down",
              previous: 'fa fa-arrow-left',
              next: 'fa fa-arrow-right',
              today: 'fa fa-calendar-o fa-stack-2x',
              clear: 'fa fa-trash-o',
              close: 'fa fa-times'
          }
        });

        /*
         * To handle global errors like out of session in IE
         */
        window.onerror = function(msg, url, line, col, error) {
            var errorNumber = error.number & 0xFFFF;
            if( errorNumber == 1002) {
                // IE receiving ECAS page in out of session
                window.location.href = $._getContextPath() + $._settings.ecasRedirectFailedUrl;
            } else if (errorNumber != 0) {
                var extra = !col ? '' : '\ncolumn: ' + col;
                extra += !error ? '' : '\nerror: ' + error;

                // You can view the information in an alert to see things working like this:
                console.log("Error: " + msg + "\nurl: " + url + "\nline: " + line + extra);

                // TODO: save log ?
                CIPADMIN.showBusinessError($._getData('common.system.error'));
            }

            // If you return true, then error alerts (like in older versions of Internet Explorer) will be suppressed.
            return true;
        };
	},

	bindEvents : function() {
		$._L('CIPADMIN.bindEvents');

//		$(".input-group.date").on("dp.change", function(e) {
//            $(this).data("DateTimePicker").date(moment(new Date()).hours(0).minutes(0).seconds(0).milliseconds(0));
//        });
		
		/*
		 * Menu navigation
		 */
		
		$._ADE('click','#action_back_home', function(){
			$._L("id action_back_home clicked");
			CIPADMIN.goToHomePage();
		});
		
		$._ADE('click','#app_action_back_home', function(){
			$._L("id app_action_back_home clicked");
			CIPADMIN.goToHomePage();
		});
		
		$._ADE('click','.action_back_home', function(){
			$._L("class action_back_home clicked");
			CIPADMIN.goToHomePage();
		});
		
		$._ADE('click','.app_action_back_home', function(){
			$._L("class app_action_back_home clicked");
			CIPADMIN.goToHomePage();
		});
		
		$._ADE('change', "#userBusinessDomainHeader", function(){
			$._L("CIPADMIN - business domain changed");
			$._blockUI();
			CIPADMIN.updateBusinessDomainData();
		});
		
		$._ADE('click','#app_action_logout', function(){
			$._L("LOGOUT button clicked");
			$.ajax({
                 url: $._getContextPath() + "/logout.do",
                 type: "POST",
                 dataType: "application/json; charset=utf-8",
                 username: "f",
                 password: "f",
                 processData: false,
                 contentType: "application/json",
                 complete: function (xhr, textStatus) {
                    $._L(textStatus);
                    window.location = $._getContextPath() + "/logoutPage";
                }
             });
		});

		// ETRUSTEX-1381
		$._$document.on('keyup', 'input.table-filter', function () {
            var tableIdToFilter = $(this).attr('tableIdToFilter');
		    var filteredRecords = $('#' + tableIdToFilter + ' tbody tr:visible').length;

		    // Uncheck select all checkbox if unchecked records are visible
            var allChecked = $('#' + tableIdToFilter + ' tbody .checkable:visible:checked').length == $('#' + tableIdToFilter + ' tbody .checkable:visible').length;
            $('#' + tableIdToFilter + ' thead tr th .checkAllFlag').prop("checked", allChecked);

		    // Update table caption with count of visible records
            $('#' + tableIdToFilter + ' caption > span.records-filtered').text( filteredRecords );

            // Apply zebra style
            $('#' + tableIdToFilter + ' > tbody > tr').removeClass('zebra1 zebra2 bgcolor-red');
            $('#' + tableIdToFilter + ' > tbody > tr:visible').filter( ":even" ).addClass('zebra2');
        });

        // ETRUSTEX-1381
            // table with selectable records
            $._$document.on('change', 'table input[type=checkbox]', function () {
                var table = $(this).closest('table');
                var thead = table.children('thead').eq(0);
                var tbody = table.children('tbody').eq(0);

                if($(this).hasClass('checkAllFlag') && $(this).prop("checked")) {
                    // The check all checkbox has been clicked
                    tbody.find('.checkable:visible').prop("checked", true);
                } else {
                    // Uncheck select all checkbox if unchecked records are visible
                    if(tbody.find('.checkable:visible:not(:checked)').length) {
                        $('.checkAllFlag').prop("checked", false);
                    }

                    var selectedRecords = tbody.find('.checkable:visible:checked').length;

                    // Update table caption with count of visible records
                    table.find('caption a.records-selected span').text(selectedRecords);
                }
            });
	},
	
	// common functions
	ajaxResult: function() {
		return JSON.parse($.__AJAX.ajaxXhr.responseText);
	},
	
	showBusinessError: function(message){
		$._L("CIPADMIN.showBusinessError");
		$._msgbox({
			dialogId:'errorMsg',
            text:message,
            alertType:'error'
        });
	},
	
	/*
	 * Shows a success message. If the parent is a dialog window ('isParentDialog'), 
	 * clicking 'Ok' will execute the function specified in the parameters as 'fn'; 
	 * otherwise, it will redirect the user to the page specified in the 'page' parameter.
	 * If no value specified for the page parameter, closing the message window will return 
	 * the user to the parent page.
	 * 
	 */
	showSuccessMessage: function(message, isParentDialog, fn, page){
		$._L("CIPADMIN.showSuccessMessage message=" + message + "; isParentDialog=" + isParentDialog + "; fn=" + fn + "; page=" + page);

		$._msgbox({
			dialogId:'successMsg',
            text:message,
            alertType:'info',
            fnCallback:function (r) {
                if (r === true) {
                	if (isParentDialog !== null && isParentDialog){
//                		$._execFn(fn);
                	} else {
	                	if (page !== null && page !== undefined){
	                		window.location = $._getContextPath() + page;
	                	}
                	}
                }
            }
        });
	},
	
	//TODO: review this (not a nice way to go back to previous page)
	goBack: function(){
		$._L('CIPADMIN.goBack ');
		window.history.back();
	},
	
	existsSelectPlaceholder: function(selectId){
		var exists = false;
		$('#' + selectId + ' option').each(function(){
		    if (this.value == '-1') {
		        exists = true;
		        return false;
		    }
		});
		
		return exists;
	},
	
	goToHomePage: function() {
		$._L('CIPADMIN.goToHomePage ');
//		window.location = $._getContextPath() + "/home.do";
		$._navigateTo({url:'/'});
	},
	
	
	clearElementsOf: function(parent) {
		
		$._L('CIPADMIN.clearFormElements ' + parent);

		$(parent).find('input:visible:enabled').each(function() {
            switch(this.type) {
                case 'password':
                case 'select-multiple':
                case 'text':
                case 'textarea':
                    $(this).val('');
                    break;
                case 'select-one':
                    $(this).val($("#target option:first").val());
                    break;
                case 'checkbox':
                case 'radio':
                    this.checked = false;
            }
        });

        $(parent).find('select:visible:enabled').each(function() {
            $(this).val($(this).find("option:first").val());
        });
    },
    
    executeFnByName: function(functionName, context , args ) {
	  var args = [].slice.call(arguments).splice(2);
	  var namespaces = functionName.split(".");
	  var func = namespaces.pop();
	  for(var i = 0; i < namespaces.length; i++) {
	    context = context[namespaces[i]];
	  }
	  return context[func].apply(this, args);
	},
    
    updateBusinessDomainData: function(){
    	$._L('CIPADMIN.updateBusinessDomainData ');

		$._blockUI();
    	$.post( $._getContextPath() + '/login/authorise/domain/' + $("#userBusinessDomainHeader").val() + '.do', function( messages ) {

        	if (messages.length == 1 && messages[0].indexOf('success') == 0) {
				CIPADMIN.goToHomePage();
			} else if (typeof messages === 'string' && messages.indexOf('DOCTYPE html') != 0) {
			    CIPADMIN.showBusinessError($._getData('common.error.outOfSession.problemdescription'));
			} else {
				var message = '';
				$.each(messages, function(index, value) {
					message = message + value + "<br>";
				});
				CIPADMIN.showBusinessError(message);
			}
        }).fail(function() {
		 	CIPADMIN.showBusinessError($._getData('common.error.outOfSession.problemdescription'));
		}).always(function() {
            $._unblockUI();
        });

    /*	$._ajaxJsonPost({
			pageUrl : $._getContextPath() + '/login/authorise/domain/' + $("#userBusinessDomainHeader").val() + '.do' ,
			fnPostCall : function() {
				var messages = $._ajaxJsonArray;
				if (messages.length == 1 
						&& messages[0].indexOf('success') == 0) {
					$._blockUI();
					CIPADMIN.goToHomePage();
				} else {
					var message = '';
					$.each(messages, function(index, value) {
						message = message + value + "<br>";
					});
					CIPADMIN.showBusinessError(message);
				} 
			}
		});*/

    }/*,
    
	noRecordFoundWarning: function(){
		$._L("CIPADMIN.noRecordFoundWarning");
		
		$._msgbox({
            text:$('#msgNoRecordWarning').text(),
            title:$('#msgNoRecordWarningTitle').text(),
            msgboxType:'ok',
            alertType:'warning',
            fnCallback:function (r) {
                if (r === true) {
                	window.location = $._getContextPath() + "/home.do";
                }
            }
        });
	}*/
};

/*
 * check that party roles are different
 * TODO. In validation.js, only check for 2 equal fields is available by providing JS_comp-equal class on first field,  
 * Suggest Gregory to add check for a JS_comp-not-equal class inside _isFieldValid function
 */ 
(function ($) {
	"use strict";

	$.extend({
		_compareFields: function (o1, o2) {
			 var isValid = false;
			 
			 var isValido1 = $._isFieldValidSingle(o1, true);
             var isValido2 = $._isFieldValidSingle(o2, true);

             var validationContentId = o1.attr('id') + '_' + o2.attr('id') + '_val_content';

             var $validationContent = $('#' + validationContentId);

             //then comparison validation occurs only if both fields passed their own validations
             if (!isValido1 || !isValido2) {

                 $('#' + validationContentId).remove();

                 //in case of one field or the other is invalid, invalidate both
                 o1.parent().find('span.required-toggle').removeClass('required-valid').addClass('required-required');
                 o2.parent().find('span.required-toggle').removeClass('required-valid').addClass('required-required');

             } else {
            	 if (o1.hasClass('JS_comp-not-equal')) {
                     isValid = (o1.val() != o2.val());
                     
                     if (isValid) {
                         o1.parent().find('span.required-toggle').removeClass('required-required').addClass('required-valid');
                         o2.parent().find('span.required-toggle').removeClass('required-required').addClass('required-valid');
                     } else {
                         o1.parent().find('span.required-toggle').removeClass('required-valid').addClass('required-required');
                         o2.parent().find('span.required-toggle').removeClass('required-valid').addClass('required-required');
                         

                         if (o1.hasClass('JS_show_validation_error')) {
                        	 var validationMessage = '';
                             var $validationMessage = $('#' + o1.attr('id') + '_val_message');
                        	 
                             if ($validationMessage.length) {
                                 validationMessage = $.trim($validationMessage.text());
                             } else {
                                 validationMessage = $._getData('jscaf.common_comp.invalid.not_equal');
                             }
                        	 
                        	 //displaying the additional content
                        	 var validationContent = '';

                             if (isValid) {
                                 $('#' + validationContentId).remove();
                             } else {

                                 if (!$('#' + validationContentId).length) {

                                     if (o2.hasClass('JS_error_bottom')) {
                                         validationContent += '<br>';
                                     }

                                     validationContent += '<div id="' + validationContentId + '" class="fl" style="margin-top:0;';

                                     if (o2.hasClass('JS_error_bottom')) {
                                         //finding top label width to left align
                                         var labelWidth = parseInt(o2.parent().find('.field-label').width(), 10);
                                         //adjust padding
                                         labelWidth += 2;
                                         validationContent += 'margin-bottom:10px; margin-left:' + labelWidth + 'px;';
                                     }
                                     
                                     validationContent += '">' +
                                         '   <div class="validation box">' +
                                         '     <p>' + validationMessage + '</p>' +
                                         '   </div>' +
                                         '</div>';

                                     o2.next().after(validationContent);
                                 }
                             }
                         }
                     }
                 } else {
                	 isValid = $._isFieldValid(null, true, null, o1, o2);
                 }
             }
             
             return isValid;
        }
    });

    /*
     * To make date picker today button more intuitive
    */
    $.datepicker._gotoToday = function(id) {
        var target = $(id);
        var inst = this._getInst(target[0]);
        if (this._get(inst, 'gotoCurrent') && inst.currentDay) {
                inst.selectedDay = inst.currentDay;
                inst.drawMonth = inst.selectedMonth = inst.currentMonth;
                inst.drawYear = inst.selectedYear = inst.currentYear;
        }
        else {
                var date = new Date();
                inst.selectedDay = date.getDate();
                inst.drawMonth = inst.selectedMonth = date.getMonth();
                inst.drawYear = inst.selectedYear = date.getFullYear();
                // the below two lines are new
                this._setDateDatepicker(target, date);
                this._selectDate(id, this._getDateDatepicker(target));
        }
        this._notifyChange(inst);
        this._adjustDate(target);
    };

    // ETRUSTEX-1623 unfriendly message when searching for messages
//    $('#creationDateFrom').datepicker({
//    $.datepicker.setDefaults({onSelect : function (dateText, inst) {
//        if($(inst).datepicker("getDate") === '__/__/____') {
//            $(inst).datepicker('setDate', null);
//        }
//    }});

    $.datepicker.setDefaults({
            onClose: function (dateText, inst) {
                if($(this).val() === '__/__/____') {
//                    $(this).datepicker('setDate', null);
                    $(this).val('')
                }
            }
        });

}(jQuery));
